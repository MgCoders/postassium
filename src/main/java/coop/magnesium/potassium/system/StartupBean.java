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
    @Inject
    ConfiguracionDao configuracionDao;
    @Inject
    String jbossNodeName;
    @Inject
    RecuperacionPasswordDao recuperacionPasswordDao;

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

    @PostConstruct
    public void init() {
        System.setProperty("user.timezone", "America/Montevideo");
        logger.warning("FECHA HORA DE JVM: " + LocalDateTime.now());

        try {
            if (usuarioDao.findByEmail("root@magnesium.coop") == null) {
                usuarioDao.save(new Usuario("root@magnesium.coop", "root", PasswordUtils.digestPassword(System.getenv("ROOT_PASSWORD") != null ? System.getenv("ROOT_PASSWORD") : "bu"), "ADMIN", true));
                Cliente cliente = clienteDao.save(new Cliente("Empresa 1","100 321 6546", "099 111 111", "C1", "1", "empre2@asd.ewe", "w"));
                Cliente cliente2 = clienteDao.save(new Cliente("Empresa 2","218 987 352", "097 666 666", "C2", "1", "empre1.qwe@asdas.col", "w"));

                TipoEquipo camion = tipoEquipoDao.save(new TipoEquipo("Camion","Camion"));
                TipoEquipo remolque = tipoEquipoDao.save(new TipoEquipo("Remolque","Remolque"));
                TipoEquipo barco = tipoEquipoDao.save(new TipoEquipo("Barco","Barco"));

                Equipo equipo1 = equipoDao.save( new Equipo(cliente, "Scania", "48 ruedas", "sdasd", "WER2343F44", "Rojo", "descr", camion));
                Equipo equipo2 = equipoDao.save( new Equipo(cliente, "Volkswagen", "52 ruedas", "asdasd", "1234564AD3", "Azul", "descr", remolque));
                Equipo equipo3 = equipoDao.save( new Equipo(cliente2, "Mercedes Benz", "67 ruedas", "asdasd","A23D43F44", "Blanco", "descr", barco));

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
                trabajo2.setEstado("EN_PROCESO");
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

    public void configuraciones() {
        if (!configuracionDao.isEmailOn()) {
            configuracionDao.setMailOn(false);
        }
        if (configuracionDao.getPeriodicidadNotificaciones().equals(0L)) {
            configuracionDao.setPeriodicidadNotificaciones(48L);
        }
        if (configuracionDao.getDestinatariosNotificacionesAdmins().isEmpty()) {
            configuracionDao.addDestinatarioNotificacionesAdmins("info@magnesium.coop");
        }
        if (configuracionDao.getMailFrom() == null) {
            configuracionDao.setMailFrom("no-reply@mm.com");
        }
        if (configuracionDao.getMailHost() == null) {
            configuracionDao.setMailPort("1025");
        }
        if (configuracionDao.getMailPort() == null) {
            configuracionDao.setMailHost("ip-172-31-6-242");
        }
        if (configuracionDao.getProjectName() == null) {
            configuracionDao.setProjectName("MMMM");
        }
        if (configuracionDao.getProjectLogo() == null) {
            configuracionDao.setProjectLogo("https://fffff.com");
        }
        configuracionDao.ifNullSetStringProperty(TipoConfiguracion.FRONTEND_HOST, "https://fffff.com");
        configuracionDao.ifNullSetStringProperty(TipoConfiguracion.FRONTEND_PATH, "/#/extra/new-password?token=");
        configuracionDao.ifNullSetStringProperty(TipoConfiguracion.REST_BASE_PATH, "api");
    }

    public void putRecuperacionPassword(RecuperacionPassword recuperacionPassword) {
        recuperacionPasswordDao.save(recuperacionPassword);
    }

    public RecuperacionPassword getRecuperacionInfo(String token) {
        RecuperacionPassword recuperacionPassword = recuperacionPasswordDao.findById(token);
        if (recuperacionPassword != null && recuperacionPassword.getExpirationDate().isAfter(LocalDateTime.now())) {
            return recuperacionPassword;
        } else {
            recuperacionPasswordDao.delete(token);
            return null;
        }
    }

    @Schedule(hour = "*/24", info = "cleanRecuperacionContrasena", persistent = false)
    public void cleanRecuperacionContrasena() {
        //Solo si soy master
        if (configuracionDao.getNodoMaster().equals(jbossNodeName)) {
            logger.info("Master cleaning Recuperacion Contraseña");
            recuperacionPasswordDao.findAll().forEach(recuperacionPassword -> {
                if (recuperacionPassword.getExpirationDate().isBefore(LocalDateTime.now())) {
                    recuperacionPasswordDao.delete(recuperacionPassword);
                }
            });
        }
    }

}
