package coop.magnesium.potassium.api;

import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.TipoMaterialDao;
import coop.magnesium.potassium.db.entities.Material;
import coop.magnesium.potassium.db.entities.Role;
import coop.magnesium.potassium.db.entities.TipoMaterial;
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
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/tiposmateriales")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Tipos Materiales service", tags = "tiposmateriales")
public class TipoMaterialService {

    @Inject
    private Logger logger;

    @EJB
    private TipoMaterialDao tipoMaterialDao;

    @POST
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Create Material", response = Material.class)
    public Response create(@Valid TipoMaterial tipoMaterial) {
        try {
            if (tipoMaterial.getId() != null) throw new MagnesiumException("Ya existe el tipomaterial");

            if (tipoMaterialDao.findByCodigo(tipoMaterial.getCodigo()) != null) {
                throw new MagnesiumBdAlredyExistsException("Ya existe el tipomaterial para el codigo: " + tipoMaterial.getCodigo());
            }

            tipoMaterial = tipoMaterialDao.save(tipoMaterial);

            return Response.status(Response.Status.CREATED).entity(tipoMaterial).build();
        } catch (MagnesiumNotFoundException | MagnesiumBdAlredyExistsException e) {
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
    @ApiOperation(value = "Get tipomaterial", response = TipoMaterial.class, responseContainer = "List")
    public Response findAll() {
        return Response.ok(tipoMaterialDao.findAll()).build();
    }


    @PUT
    @Logged
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Edit TipoMaterial", response = TipoMaterial.class)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Error: objeto no modificado")})
    public Response edit(@PathParam("id") Long id, @Valid TipoMaterial tipoMaterial) {
        try {
            if (tipoMaterialDao.findById(id) == null) throw new MagnesiumNotFoundException("No existe el tipomaterial");

            TipoMaterial tipoMaterialAux = tipoMaterialDao.findByCodigo(tipoMaterial.getCodigo());
            if (tipoMaterialAux != null && !tipoMaterialAux.getId().equals(id)) {
                throw new MagnesiumBdAlredyExistsException("Ya existe el tipomaterial para el codigo: " + tipoMaterial.getCodigo());
            }

            tipoMaterial.setId(id);

            tipoMaterial = tipoMaterialDao.save(tipoMaterial);
            return Response.ok(tipoMaterial).build();
        } catch (MagnesiumNotFoundException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
