package coop.magnesium.potassium.api;


import coop.magnesium.potassium.api.dto.EstimacionProyectoTipoTareaXCargo;
import coop.magnesium.potassium.api.dto.HorasProyectoTipoTareaCargoXColaborador;
import coop.magnesium.potassium.api.dto.HorasProyectoTipoTareaXCargo;
import coop.magnesium.potassium.api.dto.HorasProyectoXCargo;
import coop.magnesium.potassium.api.utils.JWTTokenNeeded;
import coop.magnesium.potassium.api.utils.RoleNeeded;
import coop.magnesium.potassium.db.dao.*;
import coop.magnesium.potassium.db.entities.*;
import coop.magnesium.potassium.utils.ex.MagnesiumNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by rsperoni on 05/05/17.
 */
@Path("/reportes")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@Transactional
@Api(description = "Reportes service", tags = "reportes")
public class ReportesService {

    @Inject
    private Logger logger;
    @EJB
    private EstimacionDao estimacionDao;
    @EJB
    private HoraDao horaDao;
    @EJB
    private ProyectoDao proyectoDao;
    @EJB
    private TipoTareaDao tipoTareaDao;
    @EJB
    private CargoDao cargoDao;
    @EJB
    private UsuarioDao colaboradorDao;


    @GET
    @JWTTokenNeeded
    @RoleNeeded({Role.ADMIN})
    @ApiOperation(value = "Get estimaciones", response = Estimacion.class, responseContainer = "List")
    public Response findAll() {
        List<Estimacion> estimacionList = estimacionDao.findAll();
        return Response.ok(estimacionList).build();
    }

    @GET
    @Path("horas/proyecto/{proyecto_id}/tarea/{tarea_id}/cargo/{cargo_id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.ADMIN})
    @ApiOperation(value = "Horas de Proyecto, TipoTarea y Cargo agrupadas por Usuario", response = HorasProyectoTipoTareaCargoXColaborador.class, responseContainer = "List")
    public Response findHorasProyectoTipoTareaCargoXColaborador(@PathParam("proyecto_id") Long proyecto_id, @PathParam("tarea_id") Long tarea_id, @PathParam("cargo_id")Long cargo_id) {
        try {
            Proyecto proyecto = proyectoDao.findById(proyecto_id);
            if (proyecto == null)
                throw new MagnesiumNotFoundException("Proyecto no encontrado");
            TipoTarea tipoTarea = tipoTareaDao.findById(tarea_id);
            if (tipoTarea == null)
                throw new MagnesiumNotFoundException("Tarea no encontrada");
            Cargo cargo = cargoDao.findById(cargo_id);
            if (cargo == null)
                throw new MagnesiumNotFoundException("Cargo no encontrado");
            return Response.ok(horaDao.findHorasProyectoTipoTareaCargoXColaborador(proyecto, tipoTarea, cargo)).build();
        } catch (MagnesiumNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("horas/proyecto/{proyecto_id}/tarea/{tarea_id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.ADMIN})
    @ApiOperation(value = "Horas de Proyecto y TipoTarea agrupadas por Cargo", response = HorasProyectoTipoTareaXCargo.class, responseContainer = "List")
    public Response findHorasProyectoTipoTareaXCargo(@PathParam("proyecto_id") Long proyecto_id, @PathParam("tarea_id") Long tarea_id) {
        try {
            Proyecto proyecto = proyectoDao.findById(proyecto_id);
            if (proyecto == null)
                throw new MagnesiumNotFoundException("Proyecto no encontrado");
            TipoTarea tipoTarea = tipoTareaDao.findById(tarea_id);
            if (tipoTarea == null)
                throw new MagnesiumNotFoundException("Tarea no encontrada");
            return Response.ok(horaDao.findHorasProyectoTipoTareaXCargo(proyecto, tipoTarea)).build();
        } catch (MagnesiumNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("estimaciones/proyecto/{proyecto_id}/tarea/{tarea_id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.ADMIN})
    @ApiOperation(value = "Estimaciones de Proyecto y TipoTarea agrupadas por Cargo", response = EstimacionProyectoTipoTareaXCargo.class, responseContainer = "List")
    public Response findEstimacionesProyectoTipoTareaXCargo(@PathParam("proyecto_id") Long proyecto_id, @PathParam("tarea_id") Long tarea_id) {
        try {
            Proyecto proyecto = proyectoDao.findById(proyecto_id);
            if (proyecto == null)
                throw new MagnesiumNotFoundException("Proyecto no encontrado");
            TipoTarea tipoTarea = tipoTareaDao.findById(tarea_id);
            if (tipoTarea == null)
                throw new MagnesiumNotFoundException("Tarea no encontrada");
            return Response.ok(estimacionDao.findEstimacionProyectoTipoTareaXCargo(proyecto, tipoTarea)).build();
        } catch (MagnesiumNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("horas/proyecto/{proyecto_id}")
    @JWTTokenNeeded
    @RoleNeeded({Role.ADMIN})
    @ApiOperation(value = "Horas de Proyecto agrupadas por Cargo", response = HorasProyectoXCargo.class, responseContainer = "List")
    public Response findHorasProyectoXCargo(@PathParam("proyecto_id") Long proyecto_id) {
        try {
            Proyecto proyecto = proyectoDao.findById(proyecto_id);
            if (proyecto == null)
                throw new MagnesiumNotFoundException("Proyecto no encontrado");
            return Response.ok(horaDao.findHorasProyectoXCargo(proyecto)).build();
        } catch (MagnesiumNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }


}
