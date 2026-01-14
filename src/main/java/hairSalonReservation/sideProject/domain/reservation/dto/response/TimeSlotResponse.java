package hairSalonReservation.sideProject.domain.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import hairSalonReservation.sideProject.domain.reservation.entity.BlockType;

import java.time.LocalTime;

public record TimeSlotResponse(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm") LocalTime time, boolean isReservable, BlockType blockType) {

    public static TimeSlotResponse of (LocalTime time, boolean isReservable, BlockType blockType){
        return new TimeSlotResponse(time, isReservable, blockType );
    }
}
