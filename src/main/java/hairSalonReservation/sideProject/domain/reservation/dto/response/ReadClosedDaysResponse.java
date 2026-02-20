package hairSalonReservation.sideProject.domain.reservation.dto.response;

import hairSalonReservation.sideProject.domain.reservation.entity.ScheduleBlock;

import java.time.LocalDate;
import java.util.List;

public record ReadClosedDaysResponse(List<LocalDate> closedDays) {

    public static ReadClosedDaysResponse from(List<ScheduleBlock> blockList){
        return new ReadClosedDaysResponse(blockList.stream().map(ScheduleBlock::getDate).toList());
    }
}
