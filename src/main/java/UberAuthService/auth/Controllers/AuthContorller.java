package UberAuthService.auth.Controllers;

import UberAuthService.auth.DTO.AuthRequestDTO;
import UberAuthService.auth.DTO.AuthResponseDto;
import UberAuthService.auth.DTO.PassengerResponseDTO;
import UberAuthService.auth.DTO.PassengerSignUpDTO;
import UberAuthService.auth.Services.AuthService;
import UberAuthService.auth.Services.JWTService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthContorller {


    @Value("${cookie.expiry}")
    private  int cookiexpiry;

    private AuthService authService;
    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    public AuthContorller(AuthenticationManager authenticationManager, JWTService jwtService, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @PostMapping("/signup/passenger")
        public ResponseEntity<PassengerResponseDTO> signUp(@RequestBody PassengerSignUpDTO passengerSignUpRequestDTO){

            PassengerResponseDTO response  = authService.signUpPassenger(passengerSignUpRequestDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/signin/passenger")
    public ResponseEntity<?> signIn(@RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response) {
        System.out.println("Request received " + authRequestDTO.getEmail() + " " + authRequestDTO.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(), authRequestDTO.getPassword()));
            if (authentication.isAuthenticated()) {
/*
            //Generate JWT token to send it to client.
            Map<String,Object> payLoad = new HashMap<>();
            payLoad.put("email",authRequestDTO.getEmail());
           // String jwtToken = jwtService.createToken(payLoad, authentication.getPrincipal().toString());


         */
                String jwtToken = jwtService.createToken(authRequestDTO.getEmail());
                ResponseCookie cookie = ResponseCookie.from("JwtToken", jwtToken)
                        .httpOnly(true)
                        .secure(false)
                        .path("/")
                        .maxAge(cookiexpiry)
                        .build();

//            response.setHeader("custome header","12345");
                response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
                return new ResponseEntity<>(AuthResponseDto.builder().success(true).build(), HttpStatus.OK);
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponseDto.builder().success(false).build());
        }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AuthResponseDto.builder().success(false).build());



       }

       /*
       * VALIDATE API
       * */
     @GetMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletResponse response, HttpServletRequest request){

         System.out.println("Inside Validate Controller");

         // accsess our token in our reques!

       for (Cookie cookie: request.getCookies()){
           System.out.println(cookie.getName()+ "  "+cookie.getValue());
       }


         return new ResponseEntity<>("Scussess",HttpStatus.OK);
     }


    }

