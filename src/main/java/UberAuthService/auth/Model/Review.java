package UberAuthService.auth.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.function.DoubleToLongFunction;


@Builder
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "booking_review")
@Inheritance(strategy = InheritanceType.JOINED)  //SINGLE_TABLE | TABLE_PER_CLASS |
public class
Review extends BaseModel {



 @Column(nullable = false)
 private String content;

 private Double rating;

 @OneToOne(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
 @JoinColumn(nullable = false)
 private Booking booking;

 @Override
 public String toString() {
  return "Review: " + this.content + " " + this.rating + " " +" "+this.booking+  " " + this.createdAt;
 }

}
