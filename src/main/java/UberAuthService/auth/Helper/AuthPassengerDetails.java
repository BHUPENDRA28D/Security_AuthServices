package UberAuthService.auth.Helper;

//import UberAuthService.auth.Model.Passenger;
import  com.example.SpringBootEntityService.models.Passenger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
// why we need this class?
// Because spring security works on UserDetails polymorphic type for auth.
//blueprint to hodling user info.
public class AuthPassengerDetails extends Passenger implements UserDetails {

    private String username; //email /name/id/phoneNumber
    private String password;

    public AuthPassengerDetails(Passenger passenger ) {
        this.password = passenger.getPassword();
        this.username = passenger.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
