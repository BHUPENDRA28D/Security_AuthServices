package UberAuthService.auth.Controllers;

import UberAuthService.auth.DTO.PassengerResponseDTO;
import UberAuthService.auth.DTO.PassengerSignUpDTO;
import UberAuthService.auth.Services.AuthService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthContorller {


    private AuthService authService;

    public AuthContorller(AuthService authService) {
        this.authService = authService;
    }

  @PostMapping("/signup/passenger")
        public ResponseEntity<PassengerResponseDTO> signUp(@RequestBody PassengerSignUpDTO passengerSignUpRequestDTO){

            PassengerResponseDTO response  = authService.signUpPassenger(passengerSignUpRequestDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/signin")
    public ResponseEntity<?> signIn(){

        return new ResponseEntity<>("SIGNIN PROPERTIY", HttpStatus.OK);
    }
}
