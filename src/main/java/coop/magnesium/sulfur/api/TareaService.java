package coop.magnesium.sulfur.api;


import coop.magnesium.sulfur.db.dao.TipoTareaDao;
import coop.magnesium.sulfur.db.entities.TipoTarea;
import coop.magnesium.sulfur.utils.Logged;
import coop.magnesium.sulfur.utils.ex.MagnesiumBdAlredyExistsException;
import coop.magnesium.sulfur.utils.ex.MagnesiumNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
 * Created by rsperoni on 05/05/17.
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
    private TipoTareaDao tipoTareaDao;

    @POST
    @Logged
    @ApiOperation(value = "Create Tipo Tarea", response = TipoTarea.class)
    public Response create(@Valid TipoTarea tipoTarea) {
        try {
            TipoTarea tipoTareaExists = tipoTareaDao.findById(tipoTarea.getId());
            if (tipoTareaExists != null) throw new MagnesiumBdAlredyExistsException("Tipo Tarea ya existe");
            tipoTarea = tipoTareaDao.save(tipoTarea);
            return Response.status(Response.Status.CREATED).entity(tipoTarea).build();
        } catch (MagnesiumBdAlredyExistsException exists) {
            logger.warning(exists.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(exists.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }


    @GET
    //@JWTTokenNeeded
    //@RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get tipos tarea", response = TipoTarea.class, responseContainer = "List")
    public Response findAll() {
        List<TipoTarea> tipoTareaList = tipoTareaDao.findAll();
        return Response.ok(tipoTareaList).build();
    }

    @GET
    @Path("{id}")
    //@JWTTokenNeeded
    //@RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get tipo Tarea", response = TipoTarea.class)
    public Response find(@PathParam("id") Long id) {
        TipoTarea tipoTarea = tipoTareaDao.findById(id);
        if (tipoTarea == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(tipoTarea).build();
    }

    @PUT
    @Path("{id}")
    //@JWTTokenNeeded
    //@RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Edit tipo area", response = TipoTarea.class)
    public Response edit(@PathParam("id") Long id, @Valid TipoTarea tipoTarea) {
        try {
            if (tipoTareaDao.findById(id) == null) throw new MagnesiumNotFoundException("Tipo de tarea no encontrado");
            tipoTarea.setId(id);
            tipoTarea = tipoTareaDao.save(tipoTarea);
            return Response.ok(tipoTarea).build();
        } catch (Exception e) {
            return Response.notModified().entity(e.getMessage()).build();
        }
    }


}
