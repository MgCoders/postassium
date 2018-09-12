package coop.magnesium.potassium.api;

import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.NotificacionDestinatarioDao;
import coop.magnesium.potassium.db.entities.NotificacionDestinatario;
import coop.magnesium.potassium.db.entities.Role;
import coop.magnesium.potassium.utils.Logged;
import coop.magnesium.potassium.utils.ex.MagnesiumBdAlredyExistsException;
import coop.magnesium.potassium.utils.ex.MagnesiumBdNotFoundException;
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

@Path("/notificaciones")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Notificaciones service", tags = "notificaciones")
public class NotificacionService {

    @Inject
    private Logger logger;

    @EJB
    NotificacionDestinatarioDao notificacionDestinatarioDao;

    @POST
    @Logged
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @ApiOperation(value = "Create NotificacionDestinatario", response = NotificacionDestinatario.class)
    public Response create(@Valid NotificacionDestinatario notificacionDestinatario) {
        try {
            if (notificacionDestinatario.getId() != null) throw new MagnesiumException("Ya existe el registro");

            NotificacionDestinatario notificacionDestinatario1 = notificacionDestinatarioDao.findByTipoAndUsuario(
                    notificacionDestinatario.getTipo(), notificacionDestinatario.getUsuario());
            if (notificacionDestinatario1 != null)
                throw new MagnesiumBdAlredyExistsException("El usuario ya está asociado a este tipo de notificación");

            notificacionDestinatario = notificacionDestinatarioDao.save(notificacionDestinatario);
            return Response.status(Response.Status.CREATED).entity(notificacionDestinatario).build();
        } catch (MagnesiumException | MagnesiumBdAlredyExistsException e) {
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
    @ApiOperation(value = "Get notificacioens destinatarios", response = NotificacionDestinatario.class, responseContainer = "List")
    public Response findAll() {
        return Response.ok(notificacionDestinatarioDao.findAll()).build();
    }


    @PUT
    @Logged
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @ApiOperation(value = "Edit NotificacionDestinatario", response = NotificacionDestinatario.class)
    @ApiResponses(value = {
            @ApiResponse(code = 304, message = "Error: objeto no modificado")})
    public Response edit(@PathParam("id") Long id, @Valid NotificacionDestinatario notificacionDestinatario) {
        try {
            NotificacionDestinatario notificacionVieja = notificacionDestinatarioDao.findById(id);
            if (notificacionVieja == null) throw new MagnesiumNotFoundException("No existe notificacion destinario");

            NotificacionDestinatario notificacionDestinatario1 = notificacionDestinatarioDao.findByTipoAndUsuario(
                    notificacionDestinatario.getTipo(), notificacionDestinatario.getUsuario());
            if (notificacionDestinatario1 != null && !notificacionDestinatario1.getId().equals(id))
                throw new MagnesiumBdAlredyExistsException("Ya existe el tipo de notificación para el usuario: " +
                        notificacionDestinatario.getUsuario().getEmail());

            notificacionDestinatario.setId(id);

            notificacionDestinatario = notificacionDestinatarioDao.save(notificacionDestinatario);
            return Response.ok(notificacionDestinatario).build();
        } catch (MagnesiumNotFoundException | MagnesiumBdAlredyExistsException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Logged
    @Path("{id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.USER, Role.ADMIN, Role.SUPER_ADMIN})
    @ApiOperation(value = "Delete NotificacionDestinatario", response = NotificacionDestinatario.class)
    public Response delete(@PathParam("id") Long id) {
        try {
            NotificacionDestinatario notificacionVieja = notificacionDestinatarioDao.findById(id);
            if (notificacionVieja == null) throw new MagnesiumBdNotFoundException("No existe notificacion destinario");

            notificacionDestinatarioDao.delete(id);

            return Response.ok(notificacionVieja).build();
        } catch (MagnesiumBdNotFoundException e) {
            logger.warning(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
