package coop.magnesium.potassium.api;

import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.UnidadMedidaDao;
import coop.magnesium.potassium.db.entities.Role;
import coop.magnesium.potassium.db.entities.TipoMaterial;
import coop.magnesium.potassium.db.entities.UnidadMedida;
import coop.magnesium.potassium.utils.Logged;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/unidadmedida")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Unidad Medida service", tags = "unidadmedida")
public class UnidadMedidaService {

    @Inject
    private Logger logger;

    @EJB
    private UnidadMedidaDao unidadMedidaDao;

    @GET
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @ApiOperation(value = "Get unidadmedida", response = UnidadMedida.class, responseContainer = "List")
    public Response findAll() {
        return Response.ok(unidadMedidaDao.findAll()).build();
    }

}
