package UberAuthService.auth.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@JsonIgnoreProperties("hibernateLazyInitializer" ,"handler", "bookings")
public class Driver extends  BaseModel {

    private  String name;

    @Column(nullable = false,unique = true)
    private String licenseNumber;

    private String phoneNumber;

//    private String address;


    // 1:m ,Driver :Booking..\

    @OneToMany(mappedBy = "driver",fetch = FetchType.LAZY)

    @Fetch(value=FetchMode.SUBSELECT)
    private List<Booking> bookings;

}
