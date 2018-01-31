package coop.magnesium.potassium.api;

import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.ClienteDao;
import coop.magnesium.potassium.db.dao.EquipoDao;
import coop.magnesium.potassium.db.entities.Cliente;
import coop.magnesium.potassium.db.entities.Equipo;
import coop.magnesium.potassium.db.entities.Role;
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
 * Created by msteglich on 1/28/18.
 */
@Path("/equipos")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Equipos service", tags = "equipos")
public class EquipoService {

    @Inject
    private Logger logger;
    @EJB
    private EquipoDao equipoDao;
    @EJB
    private ClienteDao clienteDao;

    @POST
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Create Equipo", response = Equipo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 409, message = "Código o Id ya existe"),
            @ApiResponse(code = 500, message = "Error interno")})
    public Response create(@Valid Equipo equipo) {
        try {
            Equipo equipoExists = equipo.getIdEquipo() != null ? equipoDao.findById(equipo.getIdEquipo()) : null;
            if (equipoExists != null) throw new MagnesiumBdAlredyExistsException("Id ya existe");



            equipo = equipoDao.save(equipo);
            return Response.status(Response.Status.CREATED).entity(equipo).build();
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
    @ApiOperation(value = "Get equipos", response = Equipo.class, responseContainer = "List")
    public Response findAll() {
        List<Equipo> equipoList = equipoDao.findAll();
        return Response.ok(equipoList).build();
    }

    @GET
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Equipo", response = Equipo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Id no encontrado")})
    public Response find(@PathParam("id") Long id) {
        Equipo equipo = equipoDao.findById(id);
        if (equipo == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(equipo).build();
    }

    @GET
    @Path("matricula/{matricula}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Equipo", response = Equipo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Id no encontrado")})
    public Response find(@PathParam("matricula") String matricula) {
        Equipo equipo = equipoDao.findByMatricula(matricula);
        if (equipo == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(equipo).build();
    }

    @GET
    @Path("cliente/{cliente}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Equipos", response = Equipo.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Cliente no encontrado")})
    public Response findByCliente(@PathParam("cliente") Long idCliente) {
        Cliente cliente = clienteDao.findById(idCliente);
        if (cliente == null) return Response.status(Response.Status.NOT_FOUND).build();

        List<Equipo> equipoList = equipoDao.findAllByCliente(cliente);
        return Response.ok(equipoList).build();
    }

    @PUT
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Edit equipo", response = Equipo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Error: objeto no modificado")})
    public Response edit(@PathParam("id") Long id, @Valid Equipo equipo) {
        try {
            if (equipoDao.findById(id) == null) throw new MagnesiumNotFoundException("Equipo no encontrado");
            equipo.setIdEquipo(id);
            equipo = equipoDao.save(equipo);
            return Response.ok(equipo).build();
        } catch (Exception e) {
            return Response.notModified().entity(e.getMessage()).build();
        }
    }
}
