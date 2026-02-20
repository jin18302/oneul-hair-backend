package hairSalonReservation.sideProject.domain.reservation.entity;

import hairSalonReservation.sideProject.domain.serviceMenu.entity.ServiceMenu;
import hairSalonReservation.sideProject.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reservations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ServiceMenu serviceMenu;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne
    private ScheduleBlock scheduleBlock;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus = ReservationStatus.REQUESTED;

    private Reservation(ServiceMenu serviceMenu, User user, ScheduleBlock block) {
        this.serviceMenu = serviceMenu;
        this.user = user;
        this.scheduleBlock = block;
    }

    public static Reservation of(ServiceMenu serviceMenu, User user, ScheduleBlock block) {
        return new Reservation(serviceMenu, user, block);
    }

    public void cancel() {
        this.reservationStatus = ReservationStatus.CANCELLED;
    }

    public void updateReservationStatus(ReservationStatus status) {
        this.reservationStatus = status;
    }
}