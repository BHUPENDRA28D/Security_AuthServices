package UberAuthService.auth.Adaptor;

import UberAuthService.auth.DTO.PassengerSignUpDTO;
import com.example.SpringBootEntityService.models.Passenger;



public interface ConvertPassengerSignUpDTOtoEntityAdaptor {

   public Passenger convertDTOtoEntity(PassengerSignUpDTO passengerSignUpDTO);
}
