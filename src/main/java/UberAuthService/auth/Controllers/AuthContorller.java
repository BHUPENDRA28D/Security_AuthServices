package UberAuthService.auth.Controllers;

import UberAuthService.auth.DTO.AuthRequestDTO;
import UberAuthService.auth.DTO.PassengerResponseDTO;
import UberAuthService.auth.DTO.PassengerSignUpDTO;
import UberAuthService.auth.Services.AuthService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthContorller {


    private AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthContorller(AuthenticationManager authenticationManager, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @PostMapping("/signup/passenger")
        public ResponseEntity<PassengerResponseDTO> signUp(@RequestBody PassengerSignUpDTO passengerSignUpRequestDTO){

            PassengerResponseDTO response  = authService.signUpPassenger(passengerSignUpRequestDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


/*
    @PostMapping("/signin/passenger")
    public ResponseEntity<?> signIn(@RequestBody AuthRequestDTO authRequestDTO){
        System.out.println("Request received " +authRequestDTO.getEmail() + " " + authRequestDTO.getPassword());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(),authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            return new ResponseEntity<>("Successfull auth",HttpStatus.OK);
        }
        return new ResponseEntity<>("Auth not Successfull PROPERTIY "+100, HttpStatus.OK);
    }
*/

    @PostMapping("/signin/passenger")
    public ResponseEntity<?> signIn(@RequestBody AuthRequestDTO authRequestDTO) {
        System.out.println("\n\nreq received\n\n");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequestDTO.getEmail(),
                            authRequestDTO.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                return new ResponseEntity<>("Successful auth", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Auth failed (not authenticated)", HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception ex) { // AuthenticationException or its subclasses
            ex.printStackTrace(); // prints full stack trace in console/logs
            System.out.println("Auth error: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
            return new ResponseEntity<>("Auth error: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }


/*
    @PostMapping("/fetch-comments")
    public ResponseEntity<?> fetchComments() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://jsonplaceholder.typicode.com/comments";

        String response = restTemplate.getForObject(url, String.class);

        return ResponseEntity.ok(response);
    }*/


}
