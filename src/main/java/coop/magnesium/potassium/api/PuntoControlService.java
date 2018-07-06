package coop.magnesium.potassium.api;

import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.PuntoControlDao;
import coop.magnesium.potassium.db.dao.TrabajoDao;
import coop.magnesium.potassium.db.entities.PuntoControl;
import coop.magnesium.potassium.db.entities.Role;
import coop.magnesium.potassium.db.entities.Trabajo;
import coop.magnesium.potassium.utils.Logged;
import coop.magnesium.potassium.utils.ex.MagnesiumBdAlredyExistsException;
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
 * Created by msteglich on 1/31/18.
 */
@Path("/puntoscontrol")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Punto Control service", tags = "puntoscontrol")
public class PuntoControlService {

    @Inject
    private Logger logger;
    @EJB
    private PuntoControlDao puntoControlDao;
    @EJB
    private TrabajoDao trabajoDao;


    @POST
    @Path("{id}")
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Create Punto Control", response = PuntoControl.class)
    @ApiResponses(value = {
            @ApiResponse(code = 409, message = "Código o Id ya existe"),
            @ApiResponse(code = 500, message = "Error interno")})
    public Response create(@PathParam("id") Long id, @Valid PuntoControl puntoControl) {
        try {
            Trabajo trabajo = trabajoDao.findById(puntoControl.getTrabajo().getId());
            PuntoControl puntoControlExists  = puntoControl.getId() != null ? puntoControlDao.findById(puntoControl.getId()) : null;
            if (puntoControlExists != null) throw new MagnesiumBdAlredyExistsException("Id ya existe");


            puntoControl.setTrabajo(trabajo);
            puntoControl = puntoControlDao.save(puntoControl);
            return Response.status(Response.Status.CREATED).entity(puntoControl).build();
        } catch (MagnesiumBdAlredyExistsException exists) {
            logger.warning(exists.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(exists.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }


    @GET
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Puntos Control", response = PuntoControl.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Id no encontrado")})
    public Response find(@PathParam("id") Long id) {
        Trabajo trabajo = trabajoDao.findById(id);

        List<PuntoControl> puntoControlList = puntoControlDao.findAllByTrabajo(trabajo);
        return Response.ok(puntoControlList).build();
    }


    @PUT
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Edit punto control", response = PuntoControl.class)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Error: objeto no modificado")})
    public Response edit(@PathParam("id") Long id, @Valid PuntoControl puntoControl) {
        try {
            if (puntoControlDao.findById(id) == null) throw new MagnesiumNotFoundException("Punto Control no encontrado");
            puntoControl.setId(id);
            puntoControl = puntoControlDao.save(puntoControl);
            return Response.ok(puntoControl).build();
        } catch (Exception e) {
            return Response.notModified().entity(e.getMessage()).build();
        }
    }
}
