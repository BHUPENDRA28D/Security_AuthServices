package UberAuthService.auth.Repositories;


//import UberAuthService.auth.Model.Passenger;
//import UberAuthService.auth.Model.Passenger;
import com.example.SpringBootEntityService.models.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {



    Optional<Passenger> findPassengerByEmail(String email);
    Optional<String > deleteUserById(Long userId);

}
