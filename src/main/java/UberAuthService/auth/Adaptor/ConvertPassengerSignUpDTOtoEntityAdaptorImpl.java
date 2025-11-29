package UberAuthService.auth.Adaptor;

import UberAuthService.auth.DTO.PassengerSignUpDTO;
import UberAuthService.auth.Model.Passenger;
import org.springframework.stereotype.Component;

@Component
public class ConvertPassengerSignUpDTOtoEntityAdaptorImpl implements ConvertPassengerSignUpDTOtoEntityAdaptor{

    @Override
    public Passenger convertDTOtoEntity(PassengerSignUpDTO passengerSignUpDTO) {
        return Passenger.builder()
                .email(passengerSignUpDTO.getEmail())
                .name(passengerSignUpDTO.getName())
                .phoneNumber(passengerSignUpDTO.getPhoneNumber())
                .password(passengerSignUpDTO.getPassword())
                .build();
    }
}
