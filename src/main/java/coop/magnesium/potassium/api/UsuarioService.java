package coop.magnesium.potassium.api;


import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.RubroDao;
import coop.magnesium.potassium.db.dao.UsuarioDao;
import coop.magnesium.potassium.db.dao.UsuarioRubroDao;
import coop.magnesium.potassium.db.entities.Role;
import coop.magnesium.potassium.db.entities.Rubro;
import coop.magnesium.potassium.db.entities.Usuario;
import coop.magnesium.potassium.db.entities.UsuarioRubro;
import coop.magnesium.potassium.utils.Logged;
import coop.magnesium.potassium.utils.PasswordUtils;
import coop.magnesium.potassium.utils.ex.MagnesiumBdAlredyExistsException;
import coop.magnesium.potassium.utils.ex.MagnesiumBdNotFoundException;
import coop.magnesium.potassium.utils.ex.MagnesiumNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by rsperoni on 05/05/17.
 */
@Path("/usuarios")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Usuarios service", tags = "usuarios")
public class UsuarioService {

    @Inject
    private Logger logger;
    @EJB
    private UsuarioDao usuarioDao;

    @EJB
    private RubroDao rubroDao;

    @EJB
    private UsuarioRubroDao usuarioRubroDao;

    @POST
    @Logged
    @ApiOperation(value = "Create usuario", response = Usuario.class)
    public Response create(@Valid Usuario usuario) {
        try {
            Usuario usuarioExists = usuarioDao.findByEmail(usuario.getEmail());
            if (usuarioExists != null) throw new MagnesiumBdAlredyExistsException("Email ya existe");

            if (usuarioDao.findByEmail(usuario.getEmail()) != null) throw new MagnesiumBdAlredyExistsException("Email ya existe");

            for (UsuarioRubro ur : usuario.getUsuarioRubros()) {
                Rubro rubro = rubroDao.findById(ur.getRubro().getId());
                if (rubro == null) throw new MagnesiumBdNotFoundException("Rubro no encontrado");
                ur.setRubro(rubro);
                ur.setUsuario(usuario);
                UsuarioRubro urAux = usuarioRubroDao.findByUsuarioAndRubro(usuario, rubro);
                if (urAux != null) throw new MagnesiumBdAlredyExistsException("UsuarioRubro ya existe");
            }

            usuario.setPassword(PasswordUtils.digestPassword(UUID.randomUUID().toString()));
            usuario = usuarioDao.save(usuario);
            return Response.status(Response.Status.CREATED).entity(usuario).build();
        } catch (MagnesiumBdAlredyExistsException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }


    @GET
    @JWTTokenNeeded
    @RoleNeeded({ Role.SUPER_ADMIN, Role.ADMIN})
    @ApiOperation(value = "Get usuarios", response = Usuario.class, responseContainer = "List")
    public Response findAll() {
        List<Usuario> allSulfurUsers = usuarioDao.findAll();
        return Response.ok(allSulfurUsers).build();
    }

    @GET
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({ Role.SUPER_ADMIN})
    @ApiOperation(value = "Get usuario", response = Usuario.class)
    public Response find(@PathParam("id") Long id) {
        Usuario usuario = usuarioDao.findById(id);
        if (usuario == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(usuario).build();
    }

    @PUT
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({ Role.SUPER_ADMIN})
    @ApiOperation(value = "Edit usuario", response = Usuario.class)
    public Response edit(@PathParam("id") Long id, @Valid Usuario usuario) {
        try {
            Usuario usuarioViejo = usuarioDao.findById(id);
            if (usuarioViejo == null) throw new MagnesiumNotFoundException("Usuario no encontrado");

            Usuario usuarioExists = usuarioDao.findByEmail(usuario.getEmail());
            if (usuarioExists != null && !usuarioExists.getId().equals(id)) throw new MagnesiumBdAlredyExistsException("Email ya existe");

            usuario.setId(id);
            usuario.setPassword(usuarioExists.getPassword());

            for (UsuarioRubro ur : usuario.getUsuarioRubros()) {
                Rubro rubro = rubroDao.findById(ur.getRubro().getId());
                if (rubro == null) throw new MagnesiumBdNotFoundException("Rubro no encontrado");
                ur.setRubro(rubro);
                ur.setUsuario(usuario);
                UsuarioRubro urAux = usuarioRubroDao.findByUsuarioAndRubro(usuario, rubro);
                if (urAux != null) ur.setId(urAux.getId());
                ur.setUsuario(usuario);
                ur.setRubro(rubro);
            }

            usuario = usuarioDao.save(usuario);
            return Response.ok(usuario).build();
        } catch (Exception e) {
            return Response.notModified().entity(e.getMessage()).build();
        }
    }


    private void usuarioRubro(Usuario usuario, List<UsuarioRubro> usuarioRubroList) {

    }
}
