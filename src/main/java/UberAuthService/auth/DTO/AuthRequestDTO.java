package UberAuthService.auth.DTO;


import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDTO {

    private  String email;
    private  String password;
}
