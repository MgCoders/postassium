package coop.magnesium.potassium.api;


import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.TipoEquipoDao;
import coop.magnesium.potassium.db.entities.Role;
import coop.magnesium.potassium.db.entities.TipoEquipo;
import coop.magnesium.potassium.utils.Logged;
import coop.magnesium.potassium.utils.ex.MagnesiumBdNotFoundException;
import coop.magnesium.potassium.utils.ex.MagnesiumException;
import coop.magnesium.potassium.utils.ex.MagnesiumNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
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


    @POST
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Create Tipo Equipo", response = TipoEquipo.class)
    public Response create(@Valid TipoEquipo tipoEquipo) {
        try {
            if (tipoEquipo.getId() != null) throw new MagnesiumException("Ya existe el tipo equipo");

            tipoEquipo = tipoEquipoDao.save(tipoEquipo);

            return Response.status(Response.Status.CREATED).entity(tipoEquipo).build();
        } catch (MagnesiumException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @PUT
    @Logged
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Edit Tipo Equipo", response = TipoEquipo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Error: objeto no modificado")})
    public Response edit(@PathParam("id") Long id, @Valid TipoEquipo tipoEquipo) {
        try {
            TipoEquipo tipoEquipoViejo = tipoEquipoDao.findById(id);
            if (tipoEquipoViejo == null) throw new MagnesiumNotFoundException("No existe el tipo equipo");


            tipoEquipo.setId(id);
            tipoEquipo = tipoEquipoDao.save(tipoEquipo);
            return Response.ok(tipoEquipo).build();
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
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get Tipo Equipo", response = TipoEquipo.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Id no encontrado")})
    public Response find(@PathParam("id") Long id) {
        TipoEquipo tipoEquipo = tipoEquipoDao.findById(id);
        if (tipoEquipo == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(tipoEquipo).build();
    }

    @GET
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN})
    @ApiOperation(value = "Get tipoequipo", response = TipoEquipo.class, responseContainer = "List")
    public Response findAll() {
        return Response.ok(tipoEquipoDao.findAll()).build();
    }

}
