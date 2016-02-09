package service;

import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;

import javax.inject.Inject;

/**
 * Created by dmitriy on 29.1.16.
 */
public class EmailService {

    @Inject
    private MailerClient mailerClient;

    public Void sendEmail(EmailAttributes emailAttributes) throws ServiceException {
        Email email = new Email()
                .setSubject(emailAttributes.getEmailTitle())
                .setFrom(emailAttributes.getFromEmail())
                .addTo(emailAttributes.getRecipientEmail())
                .setBodyText(emailAttributes.getEmailText());
        mailerClient.send(email);
        return null;
    }
}
