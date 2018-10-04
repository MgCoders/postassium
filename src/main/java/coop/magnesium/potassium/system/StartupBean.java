package coop.magnesium.potassium.system;

import coop.magnesium.potassium.db.dao.*;
import coop.magnesium.potassium.db.entities.*;
import coop.magnesium.potassium.utils.PasswordUtils;
import coop.magnesium.potassium.utils.ex.MagnesiumBdMultipleResultsException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
    @Inject
    Event<Notificacion> notificacionEvent;
    @EJB
    NotificacionDao notificacionDao;
    @Inject
    Event<MailEvent> mailEvent;
    @EJB
    NotificacionDestinatarioDao notificacionDestinatarioDao;

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
    MarcaEquipoDao marcaEquipoDao;

    @EJB
    RubroDao rubroDao;

    @PostConstruct
    public void init() {
        System.setProperty("user.timezone", "America/Montevideo");
        logger.warning("FECHA HORA DE JVM: " + LocalDateTime.now());

        setMyselfAsNodoMaster();

        // TODO borrar todo esto para subir al server...
        try {
            if (usuarioDao.findByEmail("root@magnesium.coop") == null) {
                usuarioDao.save(new Usuario("root@magnesium.coop", "root", PasswordUtils.digestPassword(System.getenv("ROOT_PASSWORD") != null ? System.getenv("ROOT_PASSWORD") : "bu"), "ADMIN", true));
                marcaEquipoDao.save(new MarcaEquipo("Otros"));
            }



        } catch (MagnesiumBdMultipleResultsException e) {
            logger.warning(e.getMessage());
        }
    }

    public void setMyselfAsNodoMaster() {
        configuracionDao.setNodoMaster(jbossNodeName);
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
        if (configuracionDao.getMailHost() == null) {
            configuracionDao.setMailHost("ip-172-31-6-242");
        }
        if (configuracionDao.getProjectName() == null) {
            configuracionDao.setProjectName("SIGPO");
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

    @Schedule(hour = "*/72", info = "limpiarNotificacionesAntiguas", persistent = false)
    public void limpiarNotificacionesAntiguas(){
        //Solo si soy master
        if (configuracionDao.getNodoMaster().equals(jbossNodeName)) {
            logger.info("Master limpiando notificaciones antiguas");
            notificacionDao.findAll(LocalDateTime.now().minusDays(100), LocalDateTime.now().minusDays(30)).forEach(notificacion -> notificacionDao.delete(notificacion));
        }
    }

    @Schedule(dayOfWeek = "Mon", hour = "3", info = "notificacionTrabajosDeadline", persistent = false)
    public void notificacionTrabajosDeadline() {
        //Solo si soy master
        if (configuracionDao.getNodoMaster().equals(jbossNodeName)) {
            logger.info("Master generando notificacionTrabajosDeadline");
            List<Trabajo> trabajos = trabajoDao.findAllNearDeadline();
            fireNotificacionesTrabajo(trabajos, TipoNotificacion.DEADLINE);
        }
    }

    @Schedule(dayOfWeek = "Mon", hour = "6", info = "emailTrabajosDeadline", persistent = false)
    public void emailTrabajosDeadline() {
        //Solo si soy master
        if (configuracionDao.getNodoMaster().equals(jbossNodeName)) {
            logger.info("Master generando emailTrabajosDeadline");
            StringBuilder stringBuilder = new StringBuilder();
            notificacionDao.findAllNoEnviadas().stream()
                    .filter(notificacion -> notificacion.getTipo().equals(TipoNotificacion.DEADLINE))
                    .forEach(notificacion -> {
                        stringBuilder.append(notificacion.getTexto()).append("\n").append("\n");
                        notificacion.setEnviado(true);
                    });

            String projectName = configuracionDao.getProjectName();
            List<String> emails = notificacionDestinatarioDao.findAllMailByTipo(TipoNotificacion.DEADLINE);
            if (!stringBuilder.toString().isEmpty()) {
                mailEvent.fire(new MailEvent(emails,
                        stringBuilder.toString(), projectName + ": Trabajos cercanos a entregar"));
            }
        }
    }

    @Schedule(dayOfWeek = "Mon", hour = "3", minute = "30", info = "notificacionTrabajosAtrasados", persistent = false)
    public void notificacionTrabajosAtrasados() {
        //Solo si soy master
        if (configuracionDao.getNodoMaster().equals(jbossNodeName)) {
            logger.info("Master generando notificacionTrabajosAtrasados");
            List<Trabajo> trabajos = trabajoDao.findAllDelayedWorks();
            fireNotificacionesTrabajo(trabajos, TipoNotificacion.TRABAJO_ATRASADO);
        }
    }

    @Schedule(dayOfWeek = "Mon", hour = "6", minute = "30", info = "emailTrabajosAtrasados", persistent = false)
    public void emailTrabajosAtrasados() {
        //Solo si soy master
        if (configuracionDao.getNodoMaster().equals(jbossNodeName)) {
            logger.info("Master generando emailTrabajosAtrasados");
            StringBuilder stringBuilder = new StringBuilder();
            notificacionDao.findAllNoEnviadas().stream()
                    .filter(notificacion -> notificacion.getTipo().equals(TipoNotificacion.TRABAJO_ATRASADO))
                    .forEach(notificacion -> {
                        stringBuilder.append(notificacion.getTexto()).append("\n").append("\n");
                        notificacion.setEnviado(true);
                    });

            String projectName = configuracionDao.getProjectName();
            List<String> emails = notificacionDestinatarioDao.findAllMailByTipo(TipoNotificacion.TRABAJO_ATRASADO);
            if (!stringBuilder.toString().isEmpty()) {
                mailEvent.fire(new MailEvent(emails,
                        stringBuilder.toString(), projectName + ": Trabajos atrasados"));
            }
        }
    }

    private void fireNotificacionesTrabajo(List<Trabajo> trabajos, TipoNotificacion tipo) {
        for (Trabajo trabajo : trabajos) {
            Notificacion notificacion = new Notificacion(tipo, trabajo.toNotificacion());
            notificacionEvent.fire(notificacion);
        }
    }
}
