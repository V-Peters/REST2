package springrest.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final ResetPasswordService resetPasswordService;

  @Value("${CROSS_ORIGIN}")
  private String crossOrigin;

  @Value("${MAILGUN_DOMAIN}")
  private String domain;

  @Value("${MAILGUN_API_KEY}")
  private String key;

  public boolean sendEmail(int userId, String username, String email) {
    try {
      HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + domain + "/messages")
      .basicAuth("api", key)
          .queryString("from", "donotreply@meeting-user.com")
          .queryString("to", email)
          .queryString("subject", "Passwort vergessen")
          .queryString("html", buildHTML(username, userId))
          .asJson();
      return request.getStatus() == 200;
    } catch (UnirestException e) {
      e.printStackTrace();
      return false;
    }
  }

  public String buildHTML(String username, int userId) {
    String crossOriginUrl = crossOrigin + "/set-new-password?rps=" + resetPasswordService.generateAndSaveResetPasswordSecret(userId);
    return
        "<p>Hallo " + username + ",</p>" +
        "<p>Sie erhalten diese E-Mail, weil Sie angegeben haben Ihr Passwort für <b>Meeting-User</b> vergessen zu haben.</p>" +
        "<p>Sollten Sie diese E-Mail versehentlich zugesendet bekommen haben, dann können Sie sie einfach ignorieren.</p>" +
        "<p><br></p>" +
        "<p>Um ein neues Passwort für ihren Account eingeben zu können klicken Sie <a href='" + crossOriginUrl + "'>hier.</a></p>" +
        "<p><br></p>" +
        "<p>Alternativ können Sie auch diesen Link in Ihren Browser kopieren:</p>" +
        "<a href='" + crossOriginUrl + "'>" + crossOriginUrl + "</a>" +
        "<p><br></p>" +
        "<p>Sie können übrigens nicht auf diese E-Mail antworten.</p>" +
        "<p><br></p>" +
        "<p><br></p>" +
        "<p>Mit Freundlichen Grüßen</p>" +
        "<p>Ihr Meeting-User Support</p>";
  }

}
