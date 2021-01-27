package springrest.service;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  @Autowired
  UserService userService;

  @Value("${CROSS_ORIGIN}")
  String crossOrigin;

  @Value("${TRUSTIFI_URL}")
  String url;

  @Value("${TRUSTIFI_KEY}")
  String key;

  @Value("${TRUSTIFI_SECRET}")
  String secret;

  public void sendEmail(String username, String email) {
    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("application/json");
    String html = buildHTML(username);
    RequestBody body = RequestBody.create(mediaType, "{\n  \"recipients\": [{\"email\": \"" + email + "\", \"name\": \"User\", \"phone\":{\"country_code\":\"+1\",\"phone_number\":\"1111111111\"}}],\n  \"lists\": [],\n  \"contacts\": [],\n  \"attachments\": [],\n  \"title\": \"Passwort vergessen\",\n  \"html\": \"" + html + "\",\n  \"methods\": { \n    \"postmark\": false,\n    \"secureSend\": false,\n    \"encryptContent\": false,\n    \"secureReply\": false \n  }\n}");
    Request request = new Request.Builder()
        .url(url)
        .method("POST", body)
        .addHeader("x-trustifi-key", key)
        .addHeader("x-trustifi-secret", secret)
        .addHeader("Content-Type", "application/json")
        .build();
    try {
//      Response response = client.newCall(request).execute();      Das Senden von Emails erstmal abgestellt, da ich nur begrenze E-Mails senden kann
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String buildHTML(String username) {
    String crossOriginUrl = crossOrigin + "/set-new-password?rps=" + generateResetPasswordSecretAndUpdateUser(username);
    System.out.println(crossOriginUrl);
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

  private String generateResetPasswordSecretAndUpdateUser(String username) {
    String secret = RandomStringUtils.randomAlphanumeric(255);
    userService.setResetPasswordSecret(username, secret);
    return secret;
  }

}
