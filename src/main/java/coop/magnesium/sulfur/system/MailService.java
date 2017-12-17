package coop.magnesium.sulfur.system;

import coop.magnesium.sulfur.utils.Logged;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.logging.Logger;

/**
 * Created by rsperoni on 17/12/17.
 */
@Singleton
public class MailService {


    @Inject
    Logger logger;
    @Resource(mappedName = "java:/zohoMail")
    private Session mailSession;

    public static String generarEmailRecuperacionClave(String token, String frontendHost, String frontendPath) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hacé click en el siguiente enlace para recuperar tu contraseña: ").append("\n\n");
        String url = frontendHost + frontendPath + token;
        sb.append(url).append("\n\n");
        return sb.toString();

    }

    @PostConstruct
    public void init() {
    }

    @Logged
    @Asynchronous
    @Lock(LockType.READ)
    public void sendMail(@Observes(during = TransactionPhase.AFTER_SUCCESS) MailEvent event) {
        try {
            MimeMessage m = new MimeMessage(mailSession);
            Address[] to = event.getTo().stream().map(this::toAddress).toArray(InternetAddress[]::new);
            m.setRecipients(Message.RecipientType.TO, to);
            m.setSubject(event.getSubject(), "UTF-8");
            m.setSentDate(new java.util.Date());
            m.setText(event.getMessage(), "UTF-8");
            Transport.send(m);
        } catch (MessagingException e) {
            logger.severe(e.getMessage());
        }
    }

    private InternetAddress toAddress(String s) {
        try {
            return new InternetAddress(s);
        } catch (AddressException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
