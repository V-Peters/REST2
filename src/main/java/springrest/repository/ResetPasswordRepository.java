package springrest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import springrest.entity.ResetPassword;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Integer> {

  Optional<ResetPassword> findBySecret(String secret);

  boolean existsBySecret(String secret);

  void deleteBySecret(String secret);

  @Transactional
  void deleteByValidUntilBefore(LocalDateTime now);
}
