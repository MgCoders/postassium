package coop.magnesium.potassium.api;

import coop.magnesium.potassium.db.dao.TareaDao;
import coop.magnesium.potassium.db.entities.Tarea;
import coop.magnesium.potassium.utils.Logged;
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

            tareaDao.save(tarea);

            return Response.status(Response.Status.CREATED).entity(tarea).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
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

}
