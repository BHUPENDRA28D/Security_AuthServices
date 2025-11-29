package UberAuthService.auth.Model;

import jakarta.persistence.Entity;

public enum BookingStatus {

    SCHEDULED,

    CANCALED,

    CAB_ARRIVED,

    ASSIGING_DRIVERE,

    IN_RIDE,

    COMPLETED

    //TO way to handel enum as string or numbers.
}
