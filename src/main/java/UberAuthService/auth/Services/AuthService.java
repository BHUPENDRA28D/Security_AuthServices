package UberAuthService.auth.Services;

import UberAuthService.auth.DTO.PassengerResponseDTO;
import UberAuthService.auth.DTO.PassengerSignUpDTO;

public interface AuthService {

    public PassengerResponseDTO signUpPassenger(PassengerSignUpDTO passengerSignUpDTO);
}
