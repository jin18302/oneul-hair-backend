package hairSalonReservation.sideProject.domain.reservation.controller;

import hairSalonReservation.sideProject.domain.reservation.dto.request.CreateScheduleBlockRequest;
import hairSalonReservation.sideProject.domain.reservation.dto.response.ReadClosedDaysResponse;
import hairSalonReservation.sideProject.domain.reservation.dto.response.ScheduleBlockResponse;
import hairSalonReservation.sideProject.domain.reservation.dto.response.TimeSlotResponse;
import hairSalonReservation.sideProject.domain.reservation.service.ScheduleBlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ScheduleBlockController {

    private final ScheduleBlockService scheduleBlockService;

    @PostMapping("/designers/{designerId}/schedule-blocks")
    public ResponseEntity<ScheduleBlockResponse> createBlock(
            @RequestAttribute("userId") Long ownerId
            , @RequestBody CreateScheduleBlockRequest request
            , @PathVariable Long designerId
    ){
        ScheduleBlockResponse response = scheduleBlockService.createBlockByOwner(ownerId, designerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/auth/designers/{designerId}/off-days")
    public ResponseEntity<ReadClosedDaysResponse> readByDesignerId(@PathVariable(name = "designerId") Long designerId,
                                                                   @RequestParam(name = "month", required = true) Integer month){



        ReadClosedDaysResponse response = scheduleBlockService.readOffDaysByDesignerId(designerId, month);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/auth/designers/{designerId}/time-slots")
    public ResponseEntity<List<TimeSlotResponse>> readByDesignerAndDate(
            @PathVariable(name = "designerId") Long designerId,
            @RequestParam(name = "date", required = true) LocalDate date
    ){
        List<TimeSlotResponse> response = scheduleBlockService.readTimeSlotByDesignerId(designerId, date);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/designers/{designerId}/schedule-blocks")
    public ResponseEntity<Void> deleteBlock(
            @RequestAttribute("userId") Long ownerId,
            @PathVariable(name = "designerId") Long designerId,
            @RequestParam(name = "date", required = true) LocalDate date,
            @RequestParam(name = "time", required = true) LocalTime time
    ){
        scheduleBlockService.deleteBlock(ownerId, designerId, date, time);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
