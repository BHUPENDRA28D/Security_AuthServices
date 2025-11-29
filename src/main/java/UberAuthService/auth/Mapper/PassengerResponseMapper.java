package UberAuthService.auth.Mapper;

import UberAuthService.auth.Adaptor.ConvertPassengerSignUpDTOtoEntityAdaptor;
import UberAuthService.auth.DTO.PassengerResponseDTO;
import UberAuthService.auth.Model.Passenger;
import UberAuthService.auth.Repositories.PassengerRepository;
import org.springframework.stereotype.Component;

@Component
public class PassengerResponseMapper {



    public PassengerResponseDTO toDTO(Passenger passenger){
        return PassengerResponseDTO.builder()
                .id(String.valueOf(passenger.getId()))
                .email(passenger.getEmail())
                .name(passenger.getName())
                .phoneNumber(passenger.getPhoneNumber())
                .password(passenger.getPassword())   // encrypted
                .createAt(passenger.getCreatedAt())
                .build();
    }
}
