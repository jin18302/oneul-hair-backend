package hairSalonReservation.sideProject.domain.reservation.service;

import com.fasterxml.jackson.core.type.TypeReference;
import hairSalonReservation.sideProject.common.annotation.CheckRole;
import hairSalonReservation.sideProject.common.cursor.CursorPageResponse;
import hairSalonReservation.sideProject.common.exception.BadRequestException;
import hairSalonReservation.sideProject.common.exception.ErrorCode;
import hairSalonReservation.sideProject.common.exception.ForbiddenException;
import hairSalonReservation.sideProject.common.exception.NotFoundException;
import hairSalonReservation.sideProject.common.util.JsonHelper;
import hairSalonReservation.sideProject.domain.designer.entity.Designer;
import hairSalonReservation.sideProject.domain.designer.repository.DesignerRepository;
import hairSalonReservation.sideProject.domain.reservation.dto.request.CreateReservationRequest;
import hairSalonReservation.sideProject.domain.reservation.dto.request.UpdateReservationStatusRequest;
import hairSalonReservation.sideProject.domain.reservation.dto.response.ReservationResponse;
import hairSalonReservation.sideProject.domain.reservation.entity.Reservation;
import hairSalonReservation.sideProject.domain.reservation.entity.ScheduleBlock;
import hairSalonReservation.sideProject.domain.reservation.repository.ReservationRepository;
import hairSalonReservation.sideProject.domain.reservation.repository.ReservationRepositoryCustomImpl;
import hairSalonReservation.sideProject.domain.reservation.repository.ScheduleBlockRepository;
import hairSalonReservation.sideProject.domain.reservation.repository.ScheduleBlockRepositoryCustomImpl;
import hairSalonReservation.sideProject.domain.review.dto.response.ReviewResponse;
import hairSalonReservation.sideProject.domain.serviceMenu.entity.ServiceMenu;
import hairSalonReservation.sideProject.domain.serviceMenu.repository.ServiceMenuRepository;
import hairSalonReservation.sideProject.domain.user.entity.User;
import hairSalonReservation.sideProject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationRepositoryCustomImpl reservationRepositoryCustom;
    private final DesignerRepository designerRepository;
    private final ServiceMenuRepository serviceMenuRepository;
    private final UserRepository userRepository;
    private final ScheduleBlockRepositoryCustomImpl blockRepositoryCustom;
    private final ScheduleBlockService scheduleBlockService;


    @Transactional
    public ReservationResponse createReservation(Long userId, Long designerId, CreateReservationRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Designer designer = designerRepository.findByIdAndIsDeletedFalse(designerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DESIGNER_NOT_FOUND));

        ServiceMenu serviceMenu = serviceMenuRepository.findById(request.serviceMenuId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.SERVICE_MENU_NOT_FOUND));

        Optional<ScheduleBlock> block = blockRepositoryCustom.findByDesignerIdAndDate(designerId, request.date());

        if(block.isPresent()){
            List<LocalTime> blockTimeList = JsonHelper.fromJsonToList(block.get().getTimeList(), new TypeReference<List<LocalTime>>() {});

            boolean isExistReservation = reservationRepositoryCustom
                    .existByDesignerIdAndSlot(designerId, request.date(), request.time());

            if (isExistReservation || blockTimeList.contains(request.time())) {
                throw new BadRequestException(ErrorCode.TIME_SLOT_ALREADY_BOOKED);
            }
        }

        Reservation reservation = Reservation.of(serviceMenu, designer, user, request.date(), request.time());

        try {
            reservationRepository.save(reservation);
        } catch (DataIntegrityViolationException ex) {
            throw new BadRequestException(ErrorCode.TIME_SLOT_ALREADY_BOOKED);
        }

        ArrayList<LocalTime> timeList = new ArrayList<>();
        timeList.add(request.time());

        scheduleBlockService.createBlock(designer, request.date(), timeList , false);

        return ReservationResponse.from(reservation);
    }

    public List<ReservationResponse> readByDesignerIdAndDate(Long designerId, LocalDate date) {

        return reservationRepositoryCustom.findByDesignerIdAndDate(designerId, date);
    }

    public CursorPageResponse<ReservationResponse> readByUserId(Long userId) {

        List<ReservationResponse> responseList =  reservationRepositoryCustom.findByUserId(userId);
        return CursorPageResponse.of(responseList, ReservationResponse::id);
    }

    public ReservationResponse readById(Long userId, Long reservationId){

        Reservation reservation = reservationRepository.findById(reservationId)
               .orElseThrow(() -> new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND));

       if(!reservation.getUser().getId().equals(userId)){throw new ForbiddenException(ErrorCode.FORBIDDEN);}
       return ReservationResponse.from(reservation);
    }

    @Transactional
    public ReservationResponse cancelReservation(Long userId, Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!userId.equals(reservation.getUser().getId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }
        reservation.cancel();
        return ReservationResponse.from(reservation);
    }

    @CheckRole("OWNER")
    @Transactional
    public ReservationResponse updateReservationStatus(Long userId, Long reservationId, UpdateReservationStatusRequest request) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!userId.equals(reservation.getUser().getId())) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }
        reservation.updateReservationStatus(request.status());
        return ReservationResponse.from(reservation);
    }
}
