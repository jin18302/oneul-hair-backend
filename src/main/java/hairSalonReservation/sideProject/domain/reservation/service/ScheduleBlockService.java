package hairSalonReservation.sideProject.domain.reservation.service;


import com.fasterxml.jackson.core.type.TypeReference;
import hairSalonReservation.sideProject.common.exception.ErrorCode;
import hairSalonReservation.sideProject.common.exception.ForbiddenException;
import hairSalonReservation.sideProject.common.exception.NotFoundException;
import hairSalonReservation.sideProject.common.util.JsonHelper;
import hairSalonReservation.sideProject.domain.designer.entity.Designer;
import hairSalonReservation.sideProject.domain.designer.repository.DesignerRepository;
import hairSalonReservation.sideProject.domain.reservation.dto.request.CreateScheduleBlockRequest;
import hairSalonReservation.sideProject.domain.reservation.dto.response.ReadClosedDaysResponse;
import hairSalonReservation.sideProject.domain.reservation.dto.response.ReservationResponse;
import hairSalonReservation.sideProject.domain.reservation.dto.response.ScheduleBlockResponse;
import hairSalonReservation.sideProject.domain.reservation.dto.response.TimeSlotResponse;
import hairSalonReservation.sideProject.domain.reservation.entity.BlockType;
import hairSalonReservation.sideProject.domain.reservation.entity.ScheduleBlock;
import hairSalonReservation.sideProject.domain.reservation.repository.ReservationRepositoryCustomImpl;
import hairSalonReservation.sideProject.domain.reservation.repository.ScheduleBlockRepository;
import hairSalonReservation.sideProject.domain.reservation.repository.ScheduleBlockRepositoryCustomImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ScheduleBlockService {

    private final ScheduleBlockRepository scheduleBlockRepository;
    private final ScheduleBlockRepositoryCustomImpl blockRepositoryCustom;
    private final DesignerRepository designerRepository;
    private final ReservationRepositoryCustomImpl repositoryCustom;


    @Transactional
    public ScheduleBlockResponse createBlockByOwner(Long ownerId, Long designerId, CreateScheduleBlockRequest request) {
        Designer designer = designerRepository.findByIdAndIsDeletedFalse(designerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DESIGNER_NOT_FOUND));

        Long targetId = designer.getShop().getUser().getId();
        if (!ownerId.equals(targetId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }

        return createBlock(designer, request.date(), request.time(), request.isOffDay());

    }


    public ScheduleBlockResponse createBlock(Designer designer, LocalDate date, List<LocalTime> time, boolean isOffDay) {

        Optional<ScheduleBlock> targetBlock = blockRepositoryCustom.findByDesignerIdAndDate(designer.getId(), date);

        if (targetBlock.isEmpty()) {//TODO
            ScheduleBlock block = ScheduleBlock.of(designer, date, JsonHelper.toJson(time), isOffDay);
            scheduleBlockRepository.save(block);
            return ScheduleBlockResponse.from(block);

        } else {
            targetBlock.get().addTimeSlot(time);
            return ScheduleBlockResponse.from(targetBlock.get());
        }
    }


    //휴무일 조회 api
    public ReadClosedDaysResponse readOffDaysByDesignerId(Long designerId, Integer month) {

        List<ScheduleBlock> blockList = blockRepositoryCustom.findByDesignerIdAndMonth(designerId, month);
        return ReadClosedDaysResponse.from(blockList);
    }


    public List<TimeSlotResponse> readTimeSlotByDesignerId(Long designerId, LocalDate date) {

        Designer designer = designerRepository.findById(designerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DESIGNER_NOT_FOUND));

        Optional<ScheduleBlock> block = blockRepositoryCustom.findByDesignerIdAndDate(designerId, date);
        List<LocalTime> timeSlotList = JsonHelper.fromJsonToList(designer.getTimeSlotList(), new TypeReference<>() {
        });//디자이너의 모든 타임슬롯

        if (block.isEmpty()) {//블록이 없으면 걍 타임슬롯 그대로 반환함
            return timeSlotList.stream().map(t -> TimeSlotResponse.of(t, true, null)).toList();
        }

        List<ReservationResponse> reservationList = repositoryCustom.findByDesignerIdAndDate(designerId, date);
        List<LocalTime> blockTimeList = JsonHelper.fromJsonToList(block.get().getTimeList(), new TypeReference<>() {
        });

        return timeSlotList.stream().map(t -> {

            boolean isReservable = !blockTimeList.contains(t);
            boolean isTypeReservation = reservationList.stream().anyMatch(r -> r.time().equals(t));
            BlockType blockType = isReservable ? null : ( isTypeReservation ? BlockType.RESERVATION : BlockType.BLOCK);

            return TimeSlotResponse.of(t,  isReservable, blockType);
        }).toList();
    }

    @Transactional
    public void deleteBlock(Long ownerId, Long designerId, LocalDate date, LocalTime time) {

        Designer designer = designerRepository.findByIdAndIsDeletedFalse(designerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DESIGNER_NOT_FOUND));

        Long targetId = designer.getShop().getUser().getId();
        if (!ownerId.equals(targetId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }

        ScheduleBlock block = blockRepositoryCustom.findByDesignerIdAndDate(designerId, date)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BLOCK_NOT_FOUND));

        List<LocalTime> timeList = JsonHelper.fromJsonToList(block.getTimeList(), new TypeReference<List<LocalTime>>() {
        });
        if (timeList.contains(time)) {
            timeList.remove(time);
        } else {
            throw new NotFoundException(ErrorCode.BLOCK_NOT_FOUND);
        }
    }
}
