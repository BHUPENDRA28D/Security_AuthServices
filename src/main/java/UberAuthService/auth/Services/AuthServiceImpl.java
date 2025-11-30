package UberAuthService.auth.Services;

import UberAuthService.auth.Adaptor.ConvertPassengerSignUpDTOtoEntityAdaptor;
import UberAuthService.auth.DTO.PassengerResponseDTO;
import UberAuthService.auth.DTO.PassengerSignUpDTO;
import UberAuthService.auth.Mapper.PassengerResponseMapper;
import UberAuthService.auth.Model.Passenger;
import UberAuthService.auth.Repositories.PassengerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class AuthServiceImpl implements AuthService{

    private final PassengerRepository passengerRepository;
    private final ConvertPassengerSignUpDTOtoEntityAdaptor adaptor;
    private final PassengerResponseMapper mapper;

    private  final BCryptPasswordEncoder bCrypPE;

    public AuthServiceImpl(PassengerRepository passengerRepository, ConvertPassengerSignUpDTOtoEntityAdaptor adaptor, PassengerResponseMapper mapper, BCryptPasswordEncoder bCrypPE) {
        this.passengerRepository = passengerRepository;
        this.adaptor = adaptor;
        this.mapper = mapper;
        this.bCrypPE = bCrypPE;
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

//    @GetMapping("/signin")
//    public PassengerResponseDTO s(PassengerSignUpDTO passengerSignUpDTO) {
//
//        //convert DTO ---> ENTITY
//        Passenger passenger=  adaptor.convertDTOtoEntity(passengerSignUpDTO);
//        //save to db
//        Passenger saved = passengerRepository.save(passenger);
//
//        // returing response!
//        return mapper.toDTO(saved);
//
//    }

}
