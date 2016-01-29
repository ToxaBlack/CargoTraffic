package service;

import org.apache.commons.mail.EmailAttachment;
import play.libs.mailer.Email;
import play.libs.mailer.MailerPlugin;


import java.io.File;

/**
 * Created by dmitriy on 29.1.16.
 */
public class EmailService {

    public static void sendEmail(EmailAttributes emailAttributes) throws ServiceException {
        Email email = new Email()
                .setSubject(emailAttributes.getEmailTitle())
                .setFrom(emailAttributes.getFromEmail())
                .addTo(emailAttributes.getRecipientEmail())
                .setBodyText(emailAttributes.getEmailText());
        MailerPlugin.send(email);
    }
}
