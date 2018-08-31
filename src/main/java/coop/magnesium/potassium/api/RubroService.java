package coop.magnesium.potassium.api;

import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.RubroDao;
import coop.magnesium.potassium.db.entities.*;
import coop.magnesium.potassium.utils.Logged;
import coop.magnesium.potassium.utils.ex.MagnesiumBdAlredyExistsException;
import coop.magnesium.potassium.utils.ex.MagnesiumBdNotFoundException;
import coop.magnesium.potassium.utils.ex.MagnesiumException;
import coop.magnesium.potassium.utils.ex.MagnesiumNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ejb.EJB;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by pablo on 21/01/18.
 */
@Path("/rubros")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Rubros service", tags = "rubros")
public class RubroService {


    @Inject
    private Logger logger;
    @EJB
    private RubroDao rubroDao;
    @Inject
    Event<Notificacion> notificacionEvent;


    @POST
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Create Rubro", response = Rubro.class)
    public Response create(@Valid Rubro rubro) {
        try {
            if (rubro.getId() != null) throw new MagnesiumException("Ya existe el registro");

            Rubro rubro1 = rubroDao.findByName(rubro.getNombre());
            if (rubro1 != null) throw new MagnesiumBdAlredyExistsException("Ya existe el rubro para el nombre: " + rubro.getNombre());
//
//                            Notificacion notificacion = new Notificacion(TipoNotificacion.GENERAR_REMITO, "prueba" + rubro.getNombre());
//                notificacionEvent.fire(notificacion);

            rubro = rubroDao.save(rubro);
            return Response.status(Response.Status.CREATED).entity(rubro).build();
        } catch (MagnesiumException | MagnesiumBdAlredyExistsException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }


    @GET
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get rubros", response = Rubro.class, responseContainer = "List")
    public Response findAll() {
        return Response.ok(rubroDao.findAll()).build();
    }

    @GET
    @Logged
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Rubro", response = Rubro.class)
    public Response find(@PathParam("id") Long id) {
        try {
            Rubro rubro = rubroDao.findById(id);
            if (rubro == null) throw new MagnesiumNotFoundException("No existe el rubro");

            return Response.ok(rubro).build();
        } catch (MagnesiumNotFoundException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PUT
    @Logged
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Edit Rubro", response = Rubro.class)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Error: objeto no modificado")})
    public Response edit(@PathParam("id") Long id, @Valid Rubro rubro) {
        try {
            Rubro rubroViejo = rubroDao.findById(id);
            if (rubroViejo == null) throw new MagnesiumNotFoundException("No existe el rubro");

            Rubro rubro1 = rubroDao.findByName(rubro.getNombre());
            if (rubro1 != null && !rubro1.getId().equals(id))
                throw new MagnesiumBdAlredyExistsException("Ya existe el rubro para el nombre: " + rubro.getNombre());

            rubro.setId(id);

            rubro = rubroDao.save(rubro);
            return Response.ok(rubro).build();
        } catch (MagnesiumNotFoundException | MagnesiumBdAlredyExistsException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Logged
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Delete Rubro", response = Rubro.class)
    public Response delete(@PathParam("id") Long id) {
        try {
            Rubro rubro = rubroDao.findById(id);
            if (rubro == null) throw new MagnesiumBdNotFoundException("No existe el rubro");

            rubro.setBorrado(1);
            rubroDao.save(rubro);

            return Response.ok(rubro).build();
        } catch (MagnesiumBdNotFoundException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
