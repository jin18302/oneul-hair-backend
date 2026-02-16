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

    private LocalDate date;

    private LocalTime time;

    private BlockType blockType;


    private ScheduleBlock(Designer designer,  BlockType blockType, LocalDate date, LocalTime time) {
        this.designer = designer;
        this.date = date;
        this.time = time;
        this.blockType = blockType;

    }

    public static ScheduleBlock of(Designer designer,  BlockType blockType, LocalDate date, LocalTime time) {
        return new ScheduleBlock(designer, blockType, date, time);
    }
}
