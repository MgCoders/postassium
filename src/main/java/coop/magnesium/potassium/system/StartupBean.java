package coop.magnesium.potassium.system;

import coop.magnesium.potassium.db.dao.*;
import coop.magnesium.potassium.db.entities.*;
import coop.magnesium.potassium.utils.DataRecuperacionPassword;
import coop.magnesium.potassium.utils.PasswordUtils;
import coop.magnesium.potassium.utils.ex.MagnesiumBdMultipleResultsException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Created by rsperoni on 18/11/17.
 */
@Singleton
@Startup
public class StartupBean {

    @EJB
    UsuarioDao usuarioDao;
    @Inject
    Logger logger;
    @Resource
    TimerService timerService;

    @EJB
    TareaDao tareaDao;

    @EJB
    ClienteDao clienteDao;

    @EJB
    TrabajoDao trabajoDao;

    @EJB
    PuntoControlDao puntoControlDao;

    @EJB
    TipoEquipoDao tipoEquipoDao;

    @EJB
    EquipoDao equipoDao;

    @EJB
    RubroDao rubroDao;

    private ConcurrentHashMap recuperacionPassword = null;

    @PostConstruct
    public void init() {
        this.recuperacionPassword = new ConcurrentHashMap();
        try {
            if (usuarioDao.findByEmail("root@magnesium.coop") == null) {
                usuarioDao.save(new Usuario("root@magnesium.coop", "root", PasswordUtils.digestPassword(System.getenv("ROOT_PASSWORD") != null ? System.getenv("ROOT_PASSWORD") : "bu"), "ADMIN", true));
                Cliente cliente = clienteDao.save(new Cliente("Empresa 1","100 321 6546", "099 111 111", "C1", "1", "empre2@asd.ewe", "w"));
                Cliente cliente2 = clienteDao.save(new Cliente("Empresa 2","218 987 352", "097 666 666", "C2", "1", "empre1.qwe@asdas.col", "w"));

                TipoEquipo camion = tipoEquipoDao.save(new TipoEquipo("Camion","Camion"));
                TipoEquipo remolque = tipoEquipoDao.save(new TipoEquipo("Remolque","Remolque"));
                TipoEquipo barco = tipoEquipoDao.save(new TipoEquipo("Barco","Barco"));

                Equipo equipo1 = equipoDao.save( new Equipo(cliente, "Scania", "48 ruedas", "sdasd", "WER2343F44", "Rojo", camion));
                Equipo equipo2 = equipoDao.save( new Equipo(cliente, "Volkswagen", "52 ruedas", "asdasd", "1234564AD3", "Azul", remolque));
                Equipo equipo3 = equipoDao.save( new Equipo(cliente2, "Mercedes Benz", "67 ruedas", "asdasd","A23D43F44", "Blanco", barco));

                Trabajo trabajo = new Trabajo();
                trabajo.setCliente(cliente);
                trabajo.setMotivoVisita("NUEVO");
                trabajo.setFechaRecepcion(LocalDateTime.now());
                trabajo.setFechaProvistaEntrega(LocalDate.now());
                trabajo.setEstado("FINALIZADO");
                trabajo.setEquipo(equipo1);
                trabajo = trabajoDao.save(trabajo);

                Trabajo trabajo2 = new Trabajo();
                trabajo2.setCliente(cliente);
                trabajo2.setMotivoVisita("REPARACION");
                trabajo2.setFechaRecepcion(LocalDateTime.now());
                trabajo2.setFechaProvistaEntrega(LocalDate.now());
                trabajo2.setEstado("CREADO");
                trabajo2.setEquipo(equipo2);
                trabajo2 = trabajoDao.save(trabajo2);


                Trabajo trabajo3 = new Trabajo();
                trabajo3.setCliente(cliente2);
                trabajo3.setMotivoVisita("NUEVO");
                trabajo3.setFechaRecepcion(LocalDateTime.now());
                trabajo3.setFechaProvistaEntrega(LocalDate.now());
                trabajo3.setEstado("EN_PROCESO");
                trabajo3.setEquipo(equipo3);
                trabajo3 = trabajoDao.save(trabajo3);



                PuntoControl puntoControl = puntoControlDao.save(new PuntoControl("n1", trabajo, 1));
                PuntoControl puntoControl2 = puntoControlDao.save(new PuntoControl("n2", trabajo, 1));
                tareaDao.save(new Tarea("T1","D1", 120, 0, puntoControl));
                tareaDao.save(new Tarea("T2","D2", 120, 0, puntoControl));
                tareaDao.save(new Tarea("T2","D2", 120, 0, puntoControl2));

                rubroDao.save(new Rubro("Soldador","El que suelda"));
                rubroDao.save(new Rubro("Pintor","El que pinta"));
            }



        } catch (MagnesiumBdMultipleResultsException e) {
            logger.warning(e.getMessage());
        }
    }

    @Timeout
    public void timeout(Timer timer) {
        logger.info("Timeout: " + timer.toString());
        recuperacionPassword.remove(timer.getInfo());
    }

    public void putRecuperacionPassword(DataRecuperacionPassword dataRecuperacionPassword) {
        Instant instant = dataRecuperacionPassword.getExpirationDate().toInstant(ZoneOffset.UTC);
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setInfo(dataRecuperacionPassword.getToken());
        timerService.createSingleActionTimer(Date.from(instant), timerConfig);
        recuperacionPassword.put(dataRecuperacionPassword.getToken(), dataRecuperacionPassword);
    }

    public DataRecuperacionPassword getRecuperacionInfo(String token) {
        DataRecuperacionPassword dataRecuperacionPassword = (DataRecuperacionPassword) recuperacionPassword.get(token);
        if (dataRecuperacionPassword != null && dataRecuperacionPassword.getExpirationDate().isAfter(LocalDateTime.now())) {
            return (DataRecuperacionPassword) recuperacionPassword.get(token);
        } else {
            recuperacionPassword.remove(token);
            return null;
        }
    }


}
