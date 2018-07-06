package coop.magnesium.potassium.api;


import coop.magnesium.potassium.db.dao.ConfiguracionDao;
import coop.magnesium.potassium.db.dao.UsuarioDao;
import coop.magnesium.potassium.db.entities.RecuperacionPassword;
import coop.magnesium.potassium.db.entities.TipoConfiguracion;
import coop.magnesium.potassium.db.entities.Usuario;
import coop.magnesium.potassium.system.MailEvent;
import coop.magnesium.potassium.system.MailService;
import coop.magnesium.potassium.system.StartupBean;
import coop.magnesium.potassium.utils.*;
import coop.magnesium.potassium.utils.ex.MagnesiumBdMultipleResultsException;
import coop.magnesium.potassium.utils.ex.MagnesiumBdNotFoundException;
import coop.magnesium.potassium.utils.ex.MagnesiumSecurityException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ejb.EJB;
import javax.ejb.ObjectNotFoundException;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

/**
 * Created by rsperoni on 05/05/17.
 */
@Path("/auth")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Application auth service", tags = "auth")
public class AuthService {

    @Inject
    Event<MailEvent> mailEvent;
    @Inject
    @PropertiesFromFile
    Properties endpointsProperties;
    @Inject
    private KeyGenerator keyGenerator;
    @Context
    private UriInfo uriInfo;
    @Inject
    private Logger logger;
    @EJB
    private UsuarioDao usuarioDao;
    @EJB
    private StartupBean startupBean;
    @Inject
    ConfiguracionDao configuracionDao;

    @POST
    @Path("/login")
    @Consumes(APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "Authenticate user", response = Usuario.class)
    @Logged
    public Response authenticateUser(@FormParam("email") String email,
                                     @FormParam("password") String password) {
        try {
            // Authenticate the sulfurUser using the credentials provided
            Usuario usuario = authenticate(email, password);
            //Info que quiero guardar en token
            Map<String, Object> map = new HashMap<>();
            map.put("role", usuario.getRole());
            map.put("id", String.valueOf(usuario.getId()));
            // Issue a token for the sulfurUser
            String token = issueToken(email, map);
            usuario.setToken(token);
            return Response.ok(usuario).build();
        } catch (MagnesiumSecurityException | MagnesiumBdMultipleResultsException | MagnesiumBdNotFoundException e) {
            logger.warning(e.getMessage());
            return Response.status(UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().build();
        }
    }

    /**
     * Paso uno de la recuperacion, genero el token y guardo en cache con vencimiento.
     *
     * @param email
     * @return
     */
    @POST
    @Path("/recuperar/{email}")
    @ApiOperation(value = "Recuperar password", response = Response.class)
    @Logged
    public Response recuperarPassword(@PathParam("email") String email) {
        try {
            Usuario usuario = usuarioDao.findByEmail(email);
            if (usuario == null) throw new ObjectNotFoundException("no existe el usuario");
            if (!usuario.isLogin()) throw new MagnesiumBdNotFoundException("El usuario no tiene login");

            RecuperacionPassword recuperacionPassword = new RecuperacionPassword(email,
                    UUID.randomUUID().toString(), LocalDateTime.now().plusHours(1));

            startupBean.putRecuperacionPassword(recuperacionPassword);

            String projectName = configuracionDao.getStringProperty(TipoConfiguracion.PROJECT_NAME);
            String frontendHost = configuracionDao.getStringProperty(TipoConfiguracion.FRONTEND_HOST);
            String frontendPath = configuracionDao.getStringProperty(TipoConfiguracion.FRONTEND_PATH);

            mailEvent.fire(new MailEvent(Arrays.asList(email),
                    MailService.generarEmailRecuperacionClave(recuperacionPassword.getToken(),
                            frontendHost,
                            frontendPath),
                    projectName + ": Recuperación de Contraseña"));

            logger.info(recuperacionPassword.getToken());
            return Response.ok().build();
        } catch (ObjectNotFoundException e) {
            logger.warning(e.getMessage());
            return Response.status(UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().build();
        }
    }

    /**
     * Paso dos, me dan el token y doy el mail.
     *
     * @param token
     * @return
     */
    @GET
    @Path("/recuperar/{token}")
    @ApiOperation(value = "Recuperar email", response = Response.class)
    @Logged
    public Response recuperarEmail(@PathParam("token") String token) {
        try {
            RecuperacionPassword recuperacionPassword = startupBean.getRecuperacionInfo(token);
            if (recuperacionPassword == null) throw new ObjectNotFoundException("no existe recuperación");
            return Response.ok(recuperacionPassword).build();
        } catch (ObjectNotFoundException e) {
            logger.warning(e.getMessage());
            return Response.status(UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().build();
        }
    }

    @PUT
    @Path("/recuperar")
    @Consumes(APPLICATION_FORM_URLENCODED)
    @ApiOperation(value = "Cambiar password", response = Usuario.class)
    @Logged
    public Response cambiarPassword(@FormParam("token") String token,
                                    @FormParam("password") String password) {
        try {
            RecuperacionPassword recuperacionPassword = startupBean.getRecuperacionInfo(token);
            if (recuperacionPassword == null) throw new MagnesiumBdNotFoundException("no existe recuperación");

            Usuario usuario = usuarioDao.findByEmail(recuperacionPassword.getEmail());
            if (usuario == null) throw new MagnesiumBdNotFoundException("no existe usuario");
            if (!usuario.isLogin()) throw new MagnesiumBdNotFoundException("El usuario no tiene login");

            usuario.setPassword(PasswordUtils.digestPassword(password));
            return Response.ok().build();
        } catch (MagnesiumBdNotFoundException e) {
            logger.warning(e.getMessage());
            return Response.status(UNAUTHORIZED).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().build();
        }
    }


    private Usuario authenticate(String email, String password)
            throws MagnesiumBdNotFoundException, MagnesiumBdMultipleResultsException, MagnesiumSecurityException {

        Usuario usuario = usuarioDao.findByEmail(email);

        if (usuario == null) {
            logger.info("Usuario no encontrado");
            throw new MagnesiumSecurityException("Invalid user/password");
        }
        if (!usuario.isLogin()) {
            logger.info("Usuario no tiene login");
            throw new MagnesiumSecurityException("Invalid user/password");
        }
        if (!PasswordUtils.digestPassword(password).equals(usuario.getPassword())) {
            logger.info("Password no es correcta");
            throw new MagnesiumSecurityException("Invalid user/password");
        }

        return usuario;
    }

    private String issueToken(String login, Map<String, Object> claims) {
        Key key = keyGenerator.generateKey();
        String jwtToken = Jwts.builder()
                .setSubject(login)
                .setClaims(claims)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        return jwtToken;
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
