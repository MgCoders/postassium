package coop.magnesium.potassium.api;

import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.MaterialDao;
import coop.magnesium.potassium.db.dao.PuntoControlDao;
import coop.magnesium.potassium.db.dao.TareaDao;
import coop.magnesium.potassium.db.dao.TareaMaterialDao;
import coop.magnesium.potassium.db.entities.*;
import coop.magnesium.potassium.utils.Logged;
import coop.magnesium.potassium.utils.ex.MagnesiumBdNotFoundException;
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

    @EJB
    private PuntoControlDao puntoControlDao;

    @EJB
    private TareaMaterialDao tareaMaterialDao;

    @EJB
    private MaterialDao materialDao;


    @POST
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Create Tarea", response = Tarea.class)
    public Response create(@Valid Tarea tarea) {
        try {
            if (tarea.getId() != null) throw new MagnesiumException("Ya existe la tarea");

            PuntoControl puntoControl = puntoControlDao.findById(tarea.getPuntoControl().getId());
            if (puntoControl == null) throw new MagnesiumBdNotFoundException("No existe el punto de control");
            tarea.setPuntoControl(puntoControl);

            tarea = tareaDao.save(tarea);

            return Response.status(Response.Status.CREATED).entity(tarea).build();
        } catch (MagnesiumException | MagnesiumBdNotFoundException e) {
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
    @ApiOperation(value = "Get tareas", response = Tarea.class, responseContainer = "List")
    public Response findAll() {
        List<Tarea> tareas = tareaDao.findAll();
        return Response.ok(tareas).build();
    }

    @GET
    @Logged
    @Path("trabajo/{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get tareas", response = Tarea.class, responseContainer = "List")
    public Response findAllByTrabajo(@PathParam("id") Long id) {
        List<Tarea> tareas = tareaDao.findAllByTrabajo(id);
        return Response.ok(tareas).build();
    }

    @GET
    @Logged
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
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
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Edit Tarea", response = Tarea.class)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Error: objeto no modificado")})
    public Response edit(@PathParam("id") Long id, @Valid Tarea tarea) {
        try {
            Tarea tareaVieja = tareaDao.findById(id);
            if (tareaVieja == null) throw new MagnesiumNotFoundException("No existe la tarea");

            PuntoControl puntoControl = puntoControlDao.findById(tarea.getPuntoControl().getId());
            if (puntoControl == null) throw new MagnesiumBdNotFoundException("No existe el punto de control");
            tarea.setPuntoControl(puntoControl);

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

    /****
     *
     * MATERIALES DE LA TAREA
     *
     */

    @GET
    @Path("{id}/materiales")
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get TareaMaterial", response = TareaMaterial.class, responseContainer = "List")
    public Response findAllByTarea(@PathParam("id") Long id) {
        try {
            Tarea tarea = tareaDao.findById(id);
            if (tarea == null) throw new MagnesiumNotFoundException("Tarea no encontrada");

            List<TareaMaterial> tareaMaterialList = tareaMaterialDao.findAllByTarea(tarea);

            return Response.ok(tareaMaterialList).build();
        } catch (MagnesiumNotFoundException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("{id}/materiales")
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Create TareaMaterial", response = TareaMaterial.class)
    public Response addMaterial(@PathParam("id") Long id, @Valid TareaMaterial tareaMaterial) {
        try {
            Tarea tarea = tareaDao.findById(id);
            if (tarea == null) throw new MagnesiumNotFoundException("Tarea no encontrada");

            Material material = materialDao.findById(tareaMaterial.getMaterial().getId());
            if (material == null) throw new MagnesiumNotFoundException("Material no encontrado");

            tareaMaterial.setMaterial(material);
            tareaMaterial.setTarea(tarea);

            tareaMaterial = tareaMaterialDao.save(tareaMaterial);

            return Response.ok(tareaMaterial).build();
        } catch (MagnesiumNotFoundException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
