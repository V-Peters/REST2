package springrest.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import springrest.entity.ResetPassword;
import springrest.repository.ResetPasswordRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {

  private final ResetPasswordRepository resetPasswordRepository;

  public String generateAndSaveResetPasswordSecret(int userId) {
    String secret = RandomStringUtils.randomAlphanumeric(255);
    saveResetPasswordSecret(userId, secret);
    return secret;
  }

  private void saveResetPasswordSecret(int userId, String secret) {
    ResetPassword resetPassword = new ResetPassword(0, userId, secret, LocalDateTime.now().plusMinutes(10));
    resetPasswordRepository.save(resetPassword);
  }

  public void deleteResetPasswordSecret(String rps) {
    resetPasswordRepository.deleteBySecret(rps);
  }

  @Scheduled(fixedRate = 60000)
  public void deleteOldResetPasswordSecrets() {
    resetPasswordRepository.deleteByValidUntilBefore(LocalDateTime.now());
  }

  public boolean existsByResetPasswordSecret(String rps) {
    return resetPasswordRepository.existsBySecret(rps);
  }

  public int getUserId(String secret) {
    return resetPasswordRepository.findBySecret(secret).orElse(null).getUserId();
  }

  public boolean existsById(int userId) {
    return resetPasswordRepository.existsByUserId(userId);
  }
}
