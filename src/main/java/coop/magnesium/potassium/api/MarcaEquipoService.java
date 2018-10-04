package coop.magnesium.potassium.api;

import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.MarcaEquipoDao;
import coop.magnesium.potassium.db.entities.MarcaEquipo;
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
 * Created by msteglich on 8/24/18.
 */
@Path("/marcas")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Equipos service", tags = "equipos")

public class MarcaEquipoService {
    @Inject
    private Logger logger;
    @EJB
    private MarcaEquipoDao marcaEquipoDao;

    @POST
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @ApiOperation(value = "Create Marca", response = MarcaEquipo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 409, message = "Código o Id ya existe"),
            @ApiResponse(code = 500, message = "Error interno")})
    public Response create(@Valid MarcaEquipo marcaEquipo) {
        try {
            MarcaEquipo marcaEquipoExists = marcaEquipo.getId() != null ? marcaEquipoDao.findById(marcaEquipo.getId()) : null;
            if (marcaEquipoExists != null) throw new MagnesiumBdAlredyExistsException("Id ya existe");



            marcaEquipo = marcaEquipoDao.save(marcaEquipo);
            return Response.status(Response.Status.CREATED).entity(marcaEquipo).build();
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
    @RoleNeeded({Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @ApiOperation(value = "Get marcas", response = MarcaEquipo.class, responseContainer = "List")
    public Response findAll() {
        List<MarcaEquipo> equipoList = marcaEquipoDao.findAll();
        return Response.ok(equipoList).build();
    }

    @GET
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @ApiOperation(value = "Get Equipo", response = MarcaEquipo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Id no encontrado")})
    public Response find(@PathParam("id") Long id) {
        MarcaEquipo marcaEquipo = marcaEquipoDao.findById(id);
        if (marcaEquipo == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(marcaEquipo).build();
    }


    @PUT
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @ApiOperation(value = "Edit Marca", response = MarcaEquipo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Error: objeto no modificado")})
    public Response edit(@PathParam("id") Long id, @Valid MarcaEquipo marcaEquipo) {
        try {
            if (marcaEquipoDao.findById(id) == null) throw new MagnesiumNotFoundException("Marca no encontrada");
            marcaEquipo.setId(id);
            marcaEquipo = marcaEquipoDao.save(marcaEquipo);
            return Response.ok(marcaEquipo).build();
        } catch (Exception e) {
            return Response.notModified().entity(e.getMessage()).build();
        }
    }

    @GET
    @Logged
    @Path("autocomplete/{query}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @ApiOperation(value = "Get Marca", response = MarcaEquipo.class, responseContainer = "List")
    public Response findByPattern(@PathParam("query") String query) {
        return Response.ok(marcaEquipoDao.findByPattern(query)).build();
    }
}
