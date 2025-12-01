package UberAuthService.auth.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AppSecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder(); // this is done in here because springsecurity do not take decision for your that which constructor you want to chosee!

    }



    @Bean
    //make exception for particullar req
    public SecurityFilterChain filteringCriteria(HttpSecurity http)throws  Exception {
       return http
               .csrf(csrf ->csrf.disable())
               .authorizeHttpRequests(auth->
                       auth
                      .requestMatchers("/api/v1/auth/signup/*").permitAll()
                       .requestMatchers("/api/v1/auth/signin/*").permitAll()
//                       .requestMatchers("/api/v1/auth/fetch-comments/*").permitAll()
                               .anyRequest().authenticated()

               ) .build();
    }

}
