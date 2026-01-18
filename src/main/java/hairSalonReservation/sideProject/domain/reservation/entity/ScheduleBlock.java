package hairSalonReservation.sideProject.domain.reservation.entity;

import hairSalonReservation.sideProject.domain.designer.entity.Designer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "designer_id")
    private Designer designer;

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = true)
    private Reservation reservation;

    private LocalDate date;

    private LocalTime time;

    private BlockType blockType;


    private ScheduleBlock(Designer designer, @Nullable Reservation reservation, BlockType blockType, LocalDate date, LocalTime time) {
        this.designer = designer;
        this.reservation = reservation;
        this.date = date;
        this.time = time;
        this.blockType = blockType;

    }

    public static ScheduleBlock of(Designer designer,  BlockType blockType, LocalDate date, LocalTime time) {
        return new ScheduleBlock(designer, null, blockType, date, time);
    }

    public static ScheduleBlock of(Designer designer, Reservation reservation,  BlockType blockType, LocalDate date, LocalTime time) {
        return new ScheduleBlock(designer, reservation, blockType, date, time);
    }
}
