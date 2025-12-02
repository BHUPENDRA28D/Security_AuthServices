package UberAuthService.auth.Services;

import UberAuthService.auth.Model.Passenger;
import UberAuthService.auth.Repositories.PassengerRepository;
import UberAuthService.auth.Helper.AuthPassengerDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


// This class is responsible for loading user in the form of UserDetails object for auth.
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private  PassengerRepository passengerRepository;



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

       Optional<Passenger> passenger = passengerRepository.findPassengerByEmail(email); //email is uniqe identifier.
        if(passenger.isPresent()) {
            return new AuthPassengerDetails(passenger.get());
        }
        else{
            throw new UsernameNotFoundException("Cannot find the passenger by the given Email !");
        }


    }
}
