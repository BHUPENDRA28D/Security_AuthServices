package UberAuthService.auth.Services;

import UberAuthService.auth.Adaptor.ConvertPassengerSignUpDTOtoEntityAdaptor;
import UberAuthService.auth.DTO.PassengerResponseDTO;
import UberAuthService.auth.DTO.PassengerSignUpDTO;
import UberAuthService.auth.Mapper.PassengerResponseMapper;
import UberAuthService.auth.Model.Passenger;
import UberAuthService.auth.Repositories.PassengerRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    public PassengerRepository passengerRepository;
    public ConvertPassengerSignUpDTOtoEntityAdaptor adaptor;
    public PassengerResponseMapper mapper;

    public AuthServiceImpl(PassengerRepository passengerRepository, ConvertPassengerSignUpDTOtoEntityAdaptor adaptor, PassengerResponseMapper mapper) {
        this.passengerRepository = passengerRepository;
        this.adaptor = adaptor;
        this.mapper = mapper;
    }

    /*-------------------

         SIGNUP METHODE
         */
    @Override
    public PassengerResponseDTO signUpPassenger(PassengerSignUpDTO passengerSignUpDTO) {

        //convert DTO ---> ENTITY
         Passenger passenger=  adaptor.convertDTOtoEntity(passengerSignUpDTO);
        //save to db
        Passenger saved = passengerRepository.save(passenger);

        // returing response!
        return mapper.toDTO(saved);

    }
}
