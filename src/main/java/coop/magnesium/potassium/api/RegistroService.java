package coop.magnesium.potassium.api;

import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.RegistroDao;
import coop.magnesium.potassium.db.dao.RubroDao;
import coop.magnesium.potassium.db.dao.TareaDao;
import coop.magnesium.potassium.db.dao.UsuarioDao;
import coop.magnesium.potassium.db.entities.*;
import coop.magnesium.potassium.utils.Logged;
import coop.magnesium.potassium.utils.ex.MagnesiumException;
import coop.magnesium.potassium.utils.ex.MagnesiumNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by pablo on 25/01/18.
 */
@Path("/registros")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Registros service", tags = "registros")
public class RegistroService {


    @Inject
    private Logger logger;

    @EJB
    private TareaDao tareaDao;

    @EJB
    private RegistroDao registroDao;

    @EJB
    private UsuarioDao usuarioDao;

    @EJB
    private RubroDao rubroDao;

    @POST
    @Logged
//    @JWTTokenNeeded
//    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Create Registro", response = Registro.class)
    public Response create(@Valid Registro registro) {
        try {
            if (registro.getTarea() == null) throw new MagnesiumNotFoundException("El registro debe tener una tarea");

            Tarea tarea = tareaDao.findById(registro.getTarea().getId());
            if (tarea == null) throw new MagnesiumNotFoundException("Tarea no existe");
            registro.setTarea(tarea);

            validate(registro);

            registro = registroDao.save(registro);
            return Response.status(Response.Status.CREATED).entity(registro).build();
        } catch (MagnesiumNotFoundException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
//    @JWTTokenNeeded
//    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get registros", response = Registro.class, responseContainer = "List")
    public Response findAll() {
        List<Registro> registros = registroDao.findAll();
        return Response.ok(registros).build();
    }

    @GET
    @Path("{id}")
//    @JWTTokenNeeded
//    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Registro", response = Registro.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Id no encontrado")})
    public Response find(@PathParam("id") Long id) {
        Registro registro = registroDao.findById(id);
        if (registro == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(registro).build();
    }


    @PUT
    @Path("{id}")
//    @JWTTokenNeeded
//    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Edit Registro", response = Registro.class)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Error: objeto no modificado")})
    public Response edit(@PathParam("id") Long id, @Valid Registro registro) {
        try {

            Registro registroViejo = registroDao.findById(registro.getId());
            if (registroViejo == null) throw new MagnesiumNotFoundException("No existe el registro");

            if (registro.getTarea() == null) throw new MagnesiumNotFoundException("El registro debe tener una tarea");

            Tarea tarea = tareaDao.findById(registro.getTarea().getId());
            if (tarea == null) throw new MagnesiumNotFoundException("Tarea no existe");
            if (!tarea.getId().equals(registroViejo.getTarea().getId()))
                throw new MagnesiumException("No se permite cambiar la tarea del registro");
            registro.setTarea(tarea);

            validate(registro);

            registro = registroDao.save(registro);
            return Response.ok(registro).build();
        } catch (MagnesiumNotFoundException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }


    /***************************************************
     *
     * AUXILIARY METHODS
     *
     ***************************************************/

    private void validate(Registro registro) throws MagnesiumNotFoundException {

        if (registro.getRubro() != null) {
            Rubro rubro = rubroDao.findById(registro.getRubro().getId());
            if (rubro == null) throw new MagnesiumNotFoundException("Rubro no existe");
            registro.setRubro(rubro);
        }

        if (registro.getUsuario() != null) {
            Usuario usuario = usuarioDao.findById(registro.getUsuario().getId());
            if (usuario == null) throw new MagnesiumNotFoundException("Usuario no existe");
            registro.setUsuario(usuario);
        }

    }
}
