package hairSalonReservation.sideProject.domain.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import hairSalonReservation.sideProject.domain.reservation.entity.BlockType;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimeSlotResponse(
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @JsonFormat(pattern = "HH:mm") LocalTime time,
        boolean isReservable
){
    public static TimeSlotResponse of (LocalDate date, LocalTime time, boolean isReservable){
        return new TimeSlotResponse(date, time, isReservable);
    }
}
