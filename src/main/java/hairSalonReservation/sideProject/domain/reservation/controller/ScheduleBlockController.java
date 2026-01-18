package hairSalonReservation.sideProject.domain.reservation.controller;

import hairSalonReservation.sideProject.domain.reservation.dto.request.CreateScheduleBlockRequest;
import hairSalonReservation.sideProject.domain.reservation.dto.response.DesignerBlockResponse;
import hairSalonReservation.sideProject.domain.reservation.dto.response.ReadClosedDaysResponse;
import hairSalonReservation.sideProject.domain.reservation.dto.response.ScheduleBlockResponse;
import hairSalonReservation.sideProject.domain.reservation.dto.response.TimeSlotResponse;
import hairSalonReservation.sideProject.domain.reservation.service.ScheduleBlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
        ScheduleBlockResponse response = scheduleBlockService.createBlock(ownerId, designerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/auth/designers/{designerId}/off-days")
    public ResponseEntity<ReadClosedDaysResponse> readByDesignerId(@PathVariable(name = "designerId") Long designerId,
                                                                   @RequestParam(name = "month", required = true) Integer month){

        ReadClosedDaysResponse response = scheduleBlockService.readDayOffByDesignerIdAndMonth(designerId, month);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/shops/schedule-blocks")
    public ResponseEntity<List<DesignerBlockResponse>> readByShopAndDate(
            @RequestAttribute(name = "userId")Long ownerId,
            @RequestParam(name = "date") LocalDate date){

        List<DesignerBlockResponse> responseList = scheduleBlockService.readByShopAndDate(ownerId, date);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/auth/designers/{designerId}/time-slots")
    public ResponseEntity<List<TimeSlotResponse>> readByDesignerAndDate(
            @PathVariable(name = "designerId") Long designerId,
            @RequestParam(name = "date", required = true) LocalDate date
    ){
        List<TimeSlotResponse> response = scheduleBlockService.readTimeSlotByDesignerId(designerId, date);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/designers/{designerId}/schedule-blocks/{blockId}")
    public ResponseEntity<Void> deleteBlock(
            @RequestAttribute("userId") Long ownerId,
            @PathVariable(name = "designerId") Long designerId,
           @PathVariable(name = "blockId") Long blockId
    ){
        scheduleBlockService.deleteBlock(ownerId, designerId, blockId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
