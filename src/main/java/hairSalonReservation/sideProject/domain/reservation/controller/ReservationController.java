package hairSalonReservation.sideProject.domain.reservation.controller;

import hairSalonReservation.sideProject.common.cursor.CursorPageResponse;
import hairSalonReservation.sideProject.domain.reservation.dto.request.CreateReservationRequest;
import hairSalonReservation.sideProject.domain.reservation.dto.request.UpdateReservationStatusRequest;
import hairSalonReservation.sideProject.domain.reservation.dto.response.ReservationResponse;
import hairSalonReservation.sideProject.domain.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/api")
@RestController
@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/designers/{designerId}/reservations")//o
    public ResponseEntity<ReservationResponse> createReservation(
            @RequestAttribute("userId") Long userId,
            @PathVariable(name = "designerId") Long designerId,
            @RequestBody @Valid CreateReservationRequest request) {

        ReservationResponse response = reservationService.createReservation(userId, designerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/designers/{designerId}/reservations")
    public ResponseEntity<List<ReservationResponse>> readByDesignerId(//x
                                                                      @PathVariable(name = "designerId") Long designerId,
                                                                      @RequestParam(name = "date", required = false) LocalDate date) {

        List<ReservationResponse> reservationResponseList = reservationService.readByDesignerIdAndDate(designerId, date);
        return ResponseEntity.status(HttpStatus.OK).body(reservationResponseList);
    }

    @GetMapping("/reservations")
    public ResponseEntity<CursorPageResponse<ReservationResponse>> readByUserId(//o
                                                                                @RequestAttribute("userId") Long userId,
                                                                                @RequestParam(name = "lastCursor", required = false, defaultValue = "1") int lastCursor) {

        CursorPageResponse<ReservationResponse> responsePage = reservationService.readByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responsePage);
    }

    @GetMapping("/reservations/{reservationId}")
    public ResponseEntity<ReservationResponse> readByReservationId(
            @RequestAttribute("userId") Long userId,
            @PathVariable(name = "reservationId") Long reservationId) {

        ReservationResponse response = reservationService.readById(userId, reservationId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PatchMapping("/reservations/{reservationId}")
    public ResponseEntity<ReservationResponse> cancelReservation(//x
                                                                 @RequestAttribute("userId") Long userId,
                                                                 @PathVariable(name = "reservationId") Long reservationId
    ) {

        ReservationResponse response = reservationService.cancelReservation(userId, reservationId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/designers/reservations/{reservationId}")
    public ResponseEntity<ReservationResponse> updateReservationStatus(//x
                                                                       @RequestAttribute("userId") Long userId,
                                                                       @PathVariable(name = "reservationId") Long reservationId,
                                                                       @RequestBody @Valid UpdateReservationStatusRequest request
    ) {
        ReservationResponse response = reservationService.updateReservationStatus(userId, reservationId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
