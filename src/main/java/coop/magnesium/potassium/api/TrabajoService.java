package coop.magnesium.potassium.api;

import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.ClienteDao;
import coop.magnesium.potassium.db.dao.EquipoDao;
import coop.magnesium.potassium.db.dao.PuntoControlDao;
import coop.magnesium.potassium.db.dao.TrabajoDao;
import coop.magnesium.potassium.db.entities.*;
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
    @EJB
    private EquipoDao equipoDao;
    @EJB
    private ClienteDao clienteDao;
    @EJB
    private PuntoControlDao puntoControlDao;

    @POST
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Create Trabajo", response = Trabajo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 409, message = "Código o Id ya existe"),
            @ApiResponse(code = 500, message = "Error interno")})
    public Response create(@Valid Trabajo trabajo) {
        try {
            Trabajo trabajoExists = trabajo.getId() != null ? trabajoDao.findById(trabajo.getId()) : null;
            if (trabajoExists != null) throw new MagnesiumBdAlredyExistsException("Id ya existe");



            trabajo = trabajoDao.save(trabajo);
            PuntoControl puntoControl  = new PuntoControl("Final", trabajo, 0);
            puntoControl = puntoControlDao.save(puntoControl);
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
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get trabajos", response = Trabajo.class, responseContainer = "List")
    public Response findAll() {
        List<Trabajo> trabajoList = trabajoDao.findAll();
        return Response.ok(trabajoList).build();
    }

    @GET
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Trabajo", response = Trabajo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Id no encontrado")})
    public Response find(@PathParam("id") Long id) {
        Trabajo trabajo = trabajoDao.findById(id);
        if (trabajo == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(trabajo).build();
    }

    @GET
    @Path("estado/{status}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Trabajos", response = Trabajo.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Estado no encontrado")})
    public Response find(@PathParam("status") String status) {

        List<Trabajo> trabajoList = trabajoDao.findByEstado(status);
        return Response.ok(trabajoList).build();
    }

    @GET
    @Path("cliente/{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Trabajos", response = Trabajo.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Cliente no encontrado")})
    public Response findByCliente(@PathParam("id") Long idCliente) {
        Cliente cliente = clienteDao.findById(idCliente);
        if (cliente == null) return Response.status(Response.Status.NOT_FOUND).build();

        List<Trabajo> trabajoList = trabajoDao.findAllByCliente(cliente);
        return Response.ok(trabajoList).build();
    }

    @GET
    @Path("equipo/{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Trabajos", response = Trabajo.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Equipo no encontrado")})
    public Response findByEquipo(@PathParam("id") Long idEquipo) {
        Equipo equipo = equipoDao.findById(idEquipo);
        if (equipo == null) return Response.status(Response.Status.NOT_FOUND).build();

        List<Trabajo> trabajoList = trabajoDao.findAllByEquipo(equipo);
        return Response.ok(trabajoList).build();
    }



    @PUT
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Edit trabajo", response = Trabajo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Error: objeto no modificado")})
    public Response edit(@PathParam("id") Long id, @Valid Trabajo trabajo) {
        try {
            if (trabajoDao.findById(id) == null) throw new MagnesiumNotFoundException("Trabajo no encontrado");
            trabajo.setId(id);
            trabajo = trabajoDao.save(trabajo);
            return Response.ok(trabajo).build();
        } catch (Exception e) {
            return Response.notModified().entity(e.getMessage()).build();
        }
    }
}
