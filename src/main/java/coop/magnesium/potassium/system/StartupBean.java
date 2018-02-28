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


    private ConcurrentHashMap recuperacionPassword = null;

    @PostConstruct
    public void init() {
        this.recuperacionPassword = new ConcurrentHashMap();
        try {
            if (usuarioDao.findByEmail("root@magnesium.coop") == null) {
                usuarioDao.save(new Usuario("root@magnesium.coop", "root", PasswordUtils.digestPassword(System.getenv("ROOT_PASSWORD") != null ? System.getenv("ROOT_PASSWORD") : "bu"), "ADMIN"));
                Cliente cliente = clienteDao.save(new Cliente("Empresa 1","100 321 6546", "099 111 111", "C1", "1", "p@asd.ewe", "w"));
                Cliente cliente2 = clienteDao.save(new Cliente("Eempresa 2","218 987 352", "097 666 666", "C2", "1", "kk.qwe@asdas", "w"));
                Trabajo trabajo = new Trabajo();
                trabajo.setCliente(cliente);
                trabajo.setMotivoVisita("m1");
                trabajo.setFechaRecepcion(LocalDateTime.now());
                trabajo.setFechaProvistaEntrega(LocalDate.now());
                trabajo = trabajoDao.save(trabajo);
                PuntoControl puntoControl = puntoControlDao.save(new PuntoControl("n1", trabajo, 1));
                PuntoControl puntoControl2 = puntoControlDao.save(new PuntoControl("n2", trabajo, 1));
                tareaDao.save(new Tarea("T1","D1", 120, 0, puntoControl));
                tareaDao.save(new Tarea("T2","D2", 120, 0, puntoControl));
                tareaDao.save(new Tarea("T2","D2", 120, 0, puntoControl2));

                TipoEquipo camion = tipoEquipoDao.save(new TipoEquipo("Camion","Camion"));
                TipoEquipo remolque = tipoEquipoDao.save(new TipoEquipo("Remolque","Remolque"));
                TipoEquipo barco = tipoEquipoDao.save(new TipoEquipo("Barco","Barco"));

                equipoDao.save( new Equipo(cliente, "Scania", "48 ruedas", "sdasd", "Rojo", camion));
                equipoDao.save( new Equipo(cliente, "Volkswagen", "52 ruedas", "asdasd", "Azul", remolque));
                equipoDao.save( new Equipo(cliente2, "Mercedes Benz", "67 ruedas", "asdasd", "Blanco", barco));
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
