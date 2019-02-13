package coop.magnesium.potassium.api;

import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.*;
import coop.magnesium.potassium.db.entities.*;
import coop.magnesium.potassium.utils.Logged;
import coop.magnesium.potassium.utils.ex.MagnesiumBdAlredyExistsException;
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
@Path("/materiales")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Materiales service", tags = "materiales")
public class MaterialService {

    @Inject
    private Logger logger;

    @EJB
    private MaterialDao materialDao;

    @POST
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @ApiOperation(value = "Create Material", response = Material.class)
    public Response create(@Valid Material material) {
        try {
            if (material.getId() != null) throw new MagnesiumException("Ya existe el material");

            material = materialDao.save(material);

            return Response.status(Response.Status.CREATED).entity(material).build();
        } catch (MagnesiumNotFoundException e) {
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
    @RoleNeeded({Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @ApiOperation(value = "Get Material", response = Material.class, responseContainer = "List")
    public Response findAll(@QueryParam("limit") Integer limit,
                            @QueryParam("offset") Integer offset,
                            @QueryParam("filter") String filter) {
        if (limit == null || offset == null) {
            return Response.ok(materialDao.findPage(10, 0)).build();
        } else if (filter == null || filter.isEmpty()) {
            return Response.ok(materialDao.findPage(limit, offset)).build();
        } else {
            return Response.ok(materialDao.findPageFilter(limit, offset, filter)).build();
        }
    }

    @GET
    @Logged
    @Path("count")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @ApiOperation(value = "Get Cantidad Material", response = Long.class)
    public Response count() {
        return Response.ok(materialDao.countAll()).build();
    }

    @GET
    @Logged
    @Path("autocomplete/{query}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @ApiOperation(value = "Get Material", response = Material.class, responseContainer = "List")
    public Response findByPattern(@PathParam("query") String query) {
        return Response.ok(materialDao.findByPattern(query)).build();
    }

    @GET
    @Logged
    @Path("tipomaterial/{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @ApiOperation(value = "Get Material", response = Material.class, responseContainer = "List")
    public Response findByTipoMaterial(@PathParam("id") Long idTipoMaterial) {
        return Response.ok(materialDao.findByTipoMaterial(idTipoMaterial)).build();
    }

    @PUT
    @Logged
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @ApiOperation(value = "Edit Material", response = Material.class)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Error: objeto no modificado")})
    public Response edit(@PathParam("id") Long id, @Valid Material material) {
        try {
            if (materialDao.findById(id) == null) throw new MagnesiumNotFoundException("No existe el material");

            material.setId(id);

            material = materialDao.save(material);
            return Response.ok(material).build();
        } catch (MagnesiumNotFoundException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
