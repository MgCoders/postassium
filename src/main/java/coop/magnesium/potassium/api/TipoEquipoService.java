package coop.magnesium.potassium.api;


import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.TipoEquipoDao;
import coop.magnesium.potassium.db.entities.Role;
import coop.magnesium.potassium.db.entities.TipoEquipo;
import coop.magnesium.potassium.utils.Logged;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/tiposequipos")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "TipoEquipo service", tags = "tiposequipos")
public class TipoEquipoService {


    @Inject
    private Logger logger;

    @Inject
    private TipoEquipoDao tipoEquipoDao;


    @GET
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get tipoequipo", response = TipoEquipo.class, responseContainer = "List")
    public Response findAll() {
        return Response.ok(tipoEquipoDao.findAll()).build();
    }

}
