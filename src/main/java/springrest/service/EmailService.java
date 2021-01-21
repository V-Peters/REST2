package springrest.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  public void sendEmail(String email) {
    Email from = new Email("test@example.com");
    String subject = "Hello World from the SendGrid Java Library!";
    Email to = new Email(email);
    Content content = new Content("text/plain", "Hello, Email!");
    Mail mail = new Mail(from, subject, to, content);

    SendGrid sg = new SendGrid(System.getenv("${SENDGRID_API_KEY}"));
    Request request = new Request();
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);
      System.out.println(response.getStatusCode());
      System.out.println(response.getBody());
      System.out.println(response.getHeaders());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

//  public void sendEmail(String email) {
//    System.out.println(email);
//    createEmail(email);
//  }
//
//  public void createEmail(String email) {
//    Properties prop = new Properties();
//    prop.put("mail.smtp.auth", true);
//    prop.put("mail.smtp.starttls.enable", "true");
//    prop.put("mail.smtp.host", "smtp.mailtrap.io");
//    prop.put("mail.smtp.port", "25");
//    prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");
//
//    Session session = Session.getInstance(prop, new Authenticator() {
//      @Override
//      protected PasswordAuthentication getPasswordAuthentication() {
//        return new PasswordAuthentication("mail.pop3.user", "mail.pop3.password");
//      }
//    });
//
//    Message message = new MimeMessage(session);
//    try {
//      message.setFrom(new InternetAddress("test@test.com"));
//      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
//      message.setSubject("Mail Subject");
//
//      String msg = "This is my first email using JavaMailer";
//
//      MimeBodyPart mimeBodyPart = new MimeBodyPart();
//      mimeBodyPart.setContent(msg, "text/html");
//
//      Multipart multipart = new MimeMultipart();
//      multipart.addBodyPart(mimeBodyPart);
//
//      message.setContent(multipart);
//
//      Transport.send(message);
//
//    } catch (MessagingException e) {
//      e.printStackTrace();
//    }
//  }

}
