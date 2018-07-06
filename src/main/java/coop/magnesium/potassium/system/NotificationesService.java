package coop.magnesium.potassium.system;

import coop.magnesium.potassium.db.dao.ConfiguracionDao;
import coop.magnesium.potassium.db.dao.NotificacionDao;
import coop.magnesium.potassium.db.dao.NotificacionDestinatarioDao;
import coop.magnesium.potassium.db.entities.Notificacion;
import coop.magnesium.potassium.db.entities.TipoNotificacion;

import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by rsperoni on 22/01/18.
 */
@Stateless
public class NotificationesService {

    @Inject
    Logger logger;
    @EJB
    NotificacionDao notificacionDao;
    @EJB
    NotificacionDestinatarioDao notificacionDestinatarioDao;
    @Inject
    Event<MailEvent> mailEvent;
    @EJB
    ConfiguracionDao configuracionDao;

    @Asynchronous
    @Lock(LockType.READ)
    public void nuevaNotificacion(@Observes(during = TransactionPhase.AFTER_SUCCESS) Notificacion notificacion) {
        notificacionDao.save(notificacion);
        logger.info("# NUEVA NOTIFICACION: "+notificacion.getTipo()+" #");

        // Para notificaciones inmediatas.
        if (notificacion.getTipo() == TipoNotificacion.CARGAR_VALORES ||
                notificacion.getTipo() == TipoNotificacion.FACTURA_ERP ||
                notificacion.getTipo() == TipoNotificacion.GENERAR_REMITO) {

            String projectName = configuracionDao.getProjectName();
            List<String> emails = notificacionDestinatarioDao.findAllMailByTipo(notificacion.getTipo());
            mailEvent.fire(
                    new MailEvent(emails,
                            notificacion.getTexto(),
                            projectName + " - Trabajo para: " + notificacion.getTipo()));

            notificacion.setEnviado(true);
            notificacionDao.save(notificacion);
        }
    }
}
