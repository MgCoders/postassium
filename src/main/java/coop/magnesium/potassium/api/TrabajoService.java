package coop.magnesium.potassium.api;

import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.TrabajoDao;
import coop.magnesium.potassium.db.entities.Role;
import coop.magnesium.potassium.db.entities.Trabajo;
import coop.magnesium.potassium.utils.Logged;
import coop.magnesium.potassium.utils.ex.MagnesiumBdAlredyExistsException;
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
 * Created by msteglich on 1/23/18.
 */
@Path("/trabajos")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Trabajos service", tags = "trabajos")
public class TrabajoService {

    @Inject
    private Logger logger;
    @EJB
    private TrabajoDao trabajoDao;

    @POST
    @Logged
    //@JWTTokenNeeded
    //@RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Create Trabajo", response = Trabajo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 409, message = "Código o Id ya existe"),
            @ApiResponse(code = 500, message = "Error interno")})
    public Response create(@Valid Trabajo trabajo) {
        try {
            Trabajo trabajoExists = trabajo.getIdTrabajo() != null ? trabajoDao.findById(trabajo.getIdTrabajo()) : null;
            if (trabajoExists != null) throw new MagnesiumBdAlredyExistsException("Id ya existe");



            trabajo = trabajoDao.save(trabajo);
            return Response.status(Response.Status.CREATED).entity(trabajo).build();
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
    @ApiOperation(value = "Get proyectos", response = Trabajo.class, responseContainer = "List")
    public Response findAll() {
        List<Trabajo> trabajoList = trabajoDao.findAll();
        return Response.ok(trabajoList).build();
    }

    @GET
    @Path("{id}")
    //@JWTTokenNeeded
    //@RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Trabajo", response = Trabajo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Id no encontrado")})
    public Response find(@PathParam("id") Long id) {
        Trabajo trabajo = trabajoDao.findById(id);
        if (trabajo == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(trabajo).build();
    }

    @GET
    @Path("{status}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Trabajo", response = Trabajo.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Id no encontrado")})
    public Response find(@PathParam("status") String status) {
        List<Trabajo> trabajoList = trabajoDao.findByEstado(status);
        return Response.ok(trabajoList).build();
    }
}
