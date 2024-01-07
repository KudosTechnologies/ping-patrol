package ro.kudostech.pingpatrol.usermanagement.adapters.output.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.kudostech.pingpatrol.usermanagement.adapters.output.persistence.model.UserProfileDbo;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileDbo, String> {

  Optional<UserProfileDbo> findByEmail(String email);
}
