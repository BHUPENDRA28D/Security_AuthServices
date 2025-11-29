package UberAuthService.auth.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.engine.spi.CascadeStyle;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Booking extends BaseModel{

/*    //some composition we are trying to associate the entiy by composition.
    @OneToMany(mappedBy = "booking", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.LAZY)
   private Review review;  // defined a 1:1 relation btwn Booking and review;*/

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(TemporalType.DATE)
    private Date endTime;


    private Long totalDistance;

    @ManyToOne(fetch=FetchType.LAZY)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    private Passenger passenger;
}
