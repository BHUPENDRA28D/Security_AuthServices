package UberAuthService.auth.Adaptor;

import UberAuthService.auth.DTO.PassengerSignUpDTO;
import UberAuthService.auth.Model.Passenger;


public interface ConvertPassengerSignUpDTOtoEntityAdaptor {

   public Passenger convertDTOtoEntity(PassengerSignUpDTO passengerSignUpDTO);
}
