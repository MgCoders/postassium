package coop.magnesium.potassium.api;

import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.NucleoMaterialDao;
import coop.magnesium.potassium.db.entities.NucleoMaterial;
import coop.magnesium.potassium.db.entities.Role;
import coop.magnesium.potassium.utils.Logged;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/nucleomaterial")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Nucleo Material service", tags = "nucleomaterial")
public class NucleoMaterialService {

    @Inject
    private Logger logger;

    @EJB
    private NucleoMaterialDao nucleoMaterialDao;

    @GET
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @ApiOperation(value = "Get nucleomaterial", response = NucleoMaterial.class, responseContainer = "List")
    public Response findAll() {
        return Response.ok(nucleoMaterialDao.findAll()).build();
    }


    @GET
    @Logged
    @Path("tipomaterial/{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @ApiOperation(value = "Get nucleomaterial", response = NucleoMaterial.class, responseContainer = "List")
    public Response findByTipoMaterial(@PathParam("id") Long id) {
        return Response.ok(nucleoMaterialDao.findAllByTipoMaterial(id)).build();
    }

}
