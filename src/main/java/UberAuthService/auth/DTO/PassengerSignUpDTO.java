package UberAuthService.auth.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerSignUpDTO {


    private  String email;

    private  String  password;

    private  String phoneNumber;

    private String name;


}
