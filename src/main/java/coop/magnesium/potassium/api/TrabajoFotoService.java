package coop.magnesium.potassium.api;

import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.TrabajoDao;
import coop.magnesium.potassium.db.dao.TrabajoFotoDao;
import coop.magnesium.potassium.db.entities.Role;
import coop.magnesium.potassium.db.entities.Tarea;
import coop.magnesium.potassium.db.entities.Trabajo;
import coop.magnesium.potassium.db.entities.TrabajoFoto;
import coop.magnesium.potassium.utils.Logged;
import coop.magnesium.potassium.utils.ex.MagnesiumBdNotFoundException;
import coop.magnesium.potassium.utils.ex.MagnesiumException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/trabajofotos")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "TrabajoFoto service", tags = "trabajofotos")
public class TrabajoFotoService {


    @Inject
    private Logger logger;

    @EJB
    private TrabajoDao trabajoDao;

    @EJB
    private TrabajoFotoDao trabajoFotoDao;

    @POST
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Create TrabajoFoto", response = Tarea.class)
    public Response create(@Valid TrabajoFoto trabajoFoto) {
        try {
            if (trabajoFoto.getId() != null) throw new MagnesiumException("Ya existe el TrabajoFoto");

            Trabajo trabajo = trabajoDao.findById(trabajoFoto.getTrabajo().getId());
            if (trabajo == null) throw new MagnesiumBdNotFoundException("No existe el trabajo");
            trabajoFoto.setTrabajo(trabajo);

            trabajoFoto = trabajoFotoDao.save(trabajoFoto);

            return Response.status(Response.Status.CREATED).entity(trabajoFoto).build();
        } catch (MagnesiumException | MagnesiumBdNotFoundException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }


    @GET
    @Logged
    @Path("trabajo/{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get tareas", response = TrabajoFoto.class, responseContainer = "List")
    public Response findAllByTrabajo(@PathParam("id") Long id) {
        List<TrabajoFoto> fotos = trabajoFotoDao.findAllByTrabajo(id);
        return Response.ok(fotos).build();
    }

}
