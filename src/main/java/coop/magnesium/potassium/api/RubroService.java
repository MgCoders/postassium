package coop.magnesium.potassium.api;

import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.RubroDao;
import coop.magnesium.potassium.db.entities.Role;
import coop.magnesium.potassium.db.entities.Rubro;
import coop.magnesium.potassium.utils.Logged;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by pablo on 21/01/18.
 */
@Path("/rubros")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Rubros service", tags = "rubros")
public class RubroService {


    @Inject
    private Logger logger;
    @EJB
    private RubroDao rubroDao;

    @POST
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Create Rubro", response = Rubro.class)
    public Response create(@Valid Rubro rubro) {

        return Response.status(Response.Status.CREATED).entity(rubro).build();
    }

}
