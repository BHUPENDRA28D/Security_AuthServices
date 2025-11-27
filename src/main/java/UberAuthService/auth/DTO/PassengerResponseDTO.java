package UberAuthService.auth.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerResponseDTO {

    private  String id;
    private  String email;

    private  String  password;  //encrypted password!

    private  String phoneNumber;

    private String name;
    private Date createAt;
}
