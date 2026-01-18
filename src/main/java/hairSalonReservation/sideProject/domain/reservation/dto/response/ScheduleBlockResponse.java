package hairSalonReservation.sideProject.domain.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.type.TypeReference;
import hairSalonReservation.sideProject.common.util.JsonHelper;
import hairSalonReservation.sideProject.domain.reservation.entity.BlockType;
import hairSalonReservation.sideProject.domain.reservation.entity.ScheduleBlock;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ScheduleBlockResponse(
        Long id, LocalDate date,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") LocalTime time,
        BlockType blockType
){
    public static ScheduleBlockResponse from(ScheduleBlock block) {
        return new ScheduleBlockResponse(block.getId(), block.getDate(), block.getTime(), block.getBlockType());
    }
}
