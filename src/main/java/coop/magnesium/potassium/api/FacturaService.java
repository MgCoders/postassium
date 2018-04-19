package coop.magnesium.potassium.api;

import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.FacturaDao;
import coop.magnesium.potassium.db.dao.TrabajoDao;
import coop.magnesium.potassium.db.entities.Factura;
import coop.magnesium.potassium.db.entities.Role;
import coop.magnesium.potassium.db.entities.Trabajo;
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

import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by msteglich on 4/13/18.
 */
@Path("/facturas")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Factura service", tags = "facturas")
public class FacturaService {
    @Inject
    private Logger logger;

    @EJB
    private FacturaDao facturaDao;

    @EJB
    private TrabajoDao trabajoDao;

    @POST
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Create Factura", response = Factura.class)
    public Response create(@Valid Factura factura) {
        try {
            if (factura.getId() != null) throw new MagnesiumException("Ya existe la factura");

            if (facturaDao.findByTrabajo(factura.getTrabajo()) != null) throw new MagnesiumException("Ya existe factura para el trabajo");

            factura = facturaDao.save(factura);

            return Response.status(Response.Status.CREATED).entity(factura).build();
        } catch (MagnesiumNotFoundException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Factura", response = Factura.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Id no encontrado")})
    public Response find(@PathParam("id") Long id) {
        Factura factura = facturaDao.findById(id);
        if (factura == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(factura).build();
    }


    @GET
    @Path("trabajo/{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Trabajo", response = Factura.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Id no encontrado")})
    public Response findByTrabajo(@PathParam("id") Long id) {
        Factura factura = facturaDao.findByTrabajo(trabajoDao.findById(id));
        if (factura == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(factura).build();
    }
}
