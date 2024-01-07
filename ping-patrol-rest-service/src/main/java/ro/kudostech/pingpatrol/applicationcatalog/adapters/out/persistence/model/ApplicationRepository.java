package ro.kudostech.pingpatrol.applicationcatalog.adapters.out.persistence.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationDbo, String> {

}
