package coop.magnesium.potassium.api;

import coop.magnesium.potassium.db.dao.TareaDao;
import coop.magnesium.potassium.db.entities.Registro;
import coop.magnesium.potassium.db.entities.Tarea;
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
 * Created by pablo on 21/01/18.
 */
@Path("/tareas")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Tareas service", tags = "tareas")
public class TareaService {


    @Inject
    private Logger logger;

    @EJB
    private TareaDao tareaDao;


    @POST
    @Logged
//    @JWTTokenNeeded
//    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Create Tarea", response = Tarea.class)
    public Response create(@Valid Tarea tarea) {
        try {
            if (tarea.getId() != null) throw new MagnesiumException("Ya existe la tarea");

            tarea = tareaDao.save(tarea);

            return Response.status(Response.Status.CREATED).entity(tarea).build();
        } catch (MagnesiumException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();        }
        catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Logged
//    @JWTTokenNeeded
//    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get tareas", response = Tarea.class, responseContainer = "List")
    public Response findAll() {
        List<Tarea> tareas = tareaDao.findAll();
        return Response.ok(tareas).build();
    }


    @GET
    @Logged
    @Path("{id}")
//    @JWTTokenNeeded
//    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Tarea", response = Tarea.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Id no encontrado")})
    public Response find(@PathParam("id") Long id) {
        Tarea tarea = tareaDao.findById(id);
        if (tarea == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(tarea).build();
    }

    @PUT
    @Logged
    @Path("{id}")
    //    @JWTTokenNeeded
//    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Edit Tarea", response = Tarea.class)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Error: objeto no modificado")})
    public Response edit(@PathParam("id") Long id, @Valid Tarea tarea) {
        try {
            Tarea tareaVieja = tareaDao.findById(id);
            if (tareaVieja == null) throw new MagnesiumNotFoundException("No existe la tarea");

            tarea.setId(id);
            tarea = tareaDao.save(tarea);
            return Response.ok(tarea).build();
        } catch (MagnesiumNotFoundException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
