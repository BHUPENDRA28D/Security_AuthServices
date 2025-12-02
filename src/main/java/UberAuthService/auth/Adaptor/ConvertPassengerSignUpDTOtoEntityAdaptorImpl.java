package UberAuthService.auth.Adaptor;

import UberAuthService.auth.DTO.PassengerSignUpDTO;
import UberAuthService.auth.Model.Passenger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ConvertPassengerSignUpDTOtoEntityAdaptorImpl implements ConvertPassengerSignUpDTOtoEntityAdaptor{

//    private final BCryptPasswordEncoder bcryptPE;
     private final PasswordEncoder bcryptPE;

    public ConvertPassengerSignUpDTOtoEntityAdaptorImpl(PasswordEncoder bcryptPE) {
        this.bcryptPE = bcryptPE;
    }

    @Override
    public Passenger convertDTOtoEntity(PassengerSignUpDTO passengerSignUpDTO) {
        return Passenger.builder()
                .email(passengerSignUpDTO.getEmail())
                .name(passengerSignUpDTO.getName())
                .phoneNumber(passengerSignUpDTO.getPhoneNumber())
                .password(bcryptPE.encode(passengerSignUpDTO.getPassword()))  //TODO : Encrypt pasword!
                .build();
    }
}
