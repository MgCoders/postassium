package coop.magnesium.potassium.api;

import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.ClienteDao;
import coop.magnesium.potassium.db.entities.Cliente;
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
 * Created by msteglich on 1/27/18.
 */
@Path("/clientes")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Clientes service", tags = "clientes")
public class ClienteService {
    @Inject
    private Logger logger;

    @EJB
    private ClienteDao clienteDao;

    @POST
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Create Cliente", response = Cliente.class)
    @ApiResponses(value = {
            @ApiResponse(code = 409, message = "Código o Id ya existe"),
            @ApiResponse(code = 500, message = "Error interno")})
    public Response create(@Valid Cliente cliente) {
        try {
            Cliente clienteExists = cliente.getId() != null ? clienteDao.findById(cliente.getId()) : null;
            if (clienteExists != null) throw new MagnesiumBdAlredyExistsException("Id ya existe");



            cliente = clienteDao.save(cliente);
            return Response.status(Response.Status.CREATED).entity(cliente).build();
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
    @ApiOperation(value = "Get clientes", response = Cliente.class, responseContainer = "List")
    public Response findAll() {
        List<Cliente> clienteList = clienteDao.findAll();
        return Response.ok(clienteList).build();
    }

    @GET
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get cliente", response = Cliente.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Id no encontrado")})
    public Response find(@PathParam("id") Long id) {
        Cliente cliente = clienteDao.findById(id);
        if (cliente == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(cliente).build();
    }

    @GET
    @Path("nombreEmpresa/{name}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get cliente", response = Cliente.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Nombre no encontrado")})
    public Response findByNombreEmpresa(@PathParam("name") String nombreEmpresa) {
        Cliente cliente = clienteDao.findByNombreEmpresa(nombreEmpresa);
        if (cliente == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(cliente).build();
    }

    @GET
    @Path("rut/{rut}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get cliente", response = Cliente.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Nombre no encontrado")})
    public Response find(@PathParam("rut") String rut) {
        Cliente cliente = clienteDao.findByRUT(rut);
        if (cliente == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(cliente).build();
    }

    @PUT
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Edit cliente", response = Cliente.class)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Error: objeto no modificado")})
    public Response edit(@PathParam("id") Long id, @Valid Cliente cliente) {
        try {
            if (clienteDao.findById(id) == null) throw new MagnesiumNotFoundException("Cliente no encontrado");
            cliente.setId(id);
            cliente = clienteDao.save(cliente);
            return Response.ok(cliente).build();
        } catch (Exception e) {
            return Response.notModified().entity(e.getMessage()).build();
        }
    }
}
