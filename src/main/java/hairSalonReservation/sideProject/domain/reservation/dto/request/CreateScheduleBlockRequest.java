package hairSalonReservation.sideProject.domain.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import hairSalonReservation.sideProject.domain.reservation.entity.BlockType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record CreateScheduleBlockRequest(
        @JsonFormat(pattern = "yyyy-MM-dd") @NotNull LocalDate date,
        @JsonFormat(pattern = "HH:mm") LocalTime time,
        BlockType blockType
) {
}
