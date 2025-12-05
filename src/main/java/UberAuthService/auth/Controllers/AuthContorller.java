package UberAuthService.auth.Controllers;

import UberAuthService.auth.DTO.AuthRequestDTO;
import UberAuthService.auth.DTO.AuthResponseDto;
import UberAuthService.auth.DTO.PassengerResponseDTO;
import UberAuthService.auth.DTO.PassengerSignUpDTO;
import UberAuthService.auth.Model.Passenger;
import UberAuthService.auth.Model.RefreshToken;
import UberAuthService.auth.Repositories.PassengerRepository;
import UberAuthService.auth.Services.AuthService;
import UberAuthService.auth.Services.JWTService;
import UberAuthService.auth.Services.RefreshTokenService;
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
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthContorller {


    @Value("${cookie.expiry}")
    private  int cookiexpiry;

    @Autowired
    PassengerRepository passengerRepository;

    private AuthService authService;
    private RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;


    public AuthContorller(AuthenticationManager authenticationManager, JWTService jwtService, RefreshTokenService refreshTokenService, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
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





                // ----------------------------------------
                // 2️⃣ Generate REFRESH token
                // ----------------------------------------
                // 2️⃣ Fetch passenger from DB (to get ID for refresh token)
                Passenger passenger = passengerRepository
                        .findPassengerByEmail(authRequestDTO.getEmail())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                RefreshToken refreshToken = refreshTokenService.createRefreshToken(passenger.getId());

                ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                        .httpOnly(true)
                        .secure(false)
                        .path("/")
                        .maxAge(7 * 24 * 3600) // 7 days
                        .build();

                response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());


                System.out.println("\n\n\n\nBoth JWT Access Token and Refresh Token created\n\n\n\n");
                 // 4️⃣ Respond success
                return ResponseEntity.ok(
                        AuthResponseDto.builder().success(true).build());



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






    /*
    * Refresh API
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request,
                                      HttpServletResponse response){

     //  1. Extract refresh token from cookies
        String refreshValue = null;
        Cookie [] cookies = request.getCookies();

        if(cookies !=null){
            for(Cookie c: cookies){
                if("refreshToken".equals(c.getName())){
                    refreshValue =c.getValue();
                    break;
                }
            }
        }

        if(refreshValue == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Refresh Token missing");
        }

//     2. Validate refresh token (not expired & present in DB

        Optional<RefreshToken> optional = refreshTokenService.validateRefreshToken(refreshValue);
        if(optional.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid or expired refresh token. Please login again.");
        }

        RefreshToken stored = optional.get();

        // 3. Fetch user email from DB using userId
        Passenger passenger = passengerRepository.findById(stored.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));



        // 4. Create new ACCESS token
        String newAccessToken = jwtService.createToken(passenger.getEmail());

        ResponseCookie accessCookie = ResponseCookie.from("JwtToken", newAccessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(cookiexpiry)
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        // 5. (Optional but recommended) Rotate refresh token → new token, delete old
        RefreshToken newRefresh = refreshTokenService.createRefreshToken(stored.getUserId());
        refreshTokenService.deleteByToken(refreshValue);

        ResponseCookie newRefreshCookie = ResponseCookie.from("refreshToken", newRefresh.getToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(7 * 24 * 3600)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, newRefreshCookie.toString());

        // 6. Response
        return ResponseEntity.ok(
                AuthResponseDto.builder()
                        .success(true)
                        .build()
        );

    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {

        // 1. Extract refresh token from Cookie
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if (c.getName().equals("RefreshToken")) {
                    refreshToken = c.getValue();
                }
            }
        }

        if (refreshToken != null) {
            refreshTokenService.deleteByToken(refreshToken);
        }

        // 2. Remove Access Token cookie
        Cookie jwtCookie = new Cookie("JwtToken", null);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(0); // delete
        response.addCookie(jwtCookie);

        // 3. Remove Refresh Token cookie
        Cookie rtCookie = new Cookie("RefreshToken", null);
        rtCookie.setPath("/");
        rtCookie.setHttpOnly(true);
        rtCookie.setMaxAge(0); // delete
        response.addCookie(rtCookie);

        return ResponseEntity.ok("Logged out successfully");
    }





}