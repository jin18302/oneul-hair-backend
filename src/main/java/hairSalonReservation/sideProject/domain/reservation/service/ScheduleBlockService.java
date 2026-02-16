package hairSalonReservation.sideProject.domain.reservation.service;


import com.fasterxml.jackson.core.type.TypeReference;
import hairSalonReservation.sideProject.common.exception.ErrorCode;
import hairSalonReservation.sideProject.common.exception.ForbiddenException;
import hairSalonReservation.sideProject.common.exception.NotFoundException;
import hairSalonReservation.sideProject.common.util.JsonHelper;
import hairSalonReservation.sideProject.domain.designer.entity.Designer;
import hairSalonReservation.sideProject.domain.designer.repository.DesignerRepository;
import hairSalonReservation.sideProject.domain.reservation.dto.request.CreateScheduleBlockRequest;
import hairSalonReservation.sideProject.domain.reservation.dto.response.*;
import hairSalonReservation.sideProject.domain.reservation.entity.BlockType;
import hairSalonReservation.sideProject.domain.reservation.entity.ScheduleBlock;
import hairSalonReservation.sideProject.domain.reservation.repository.ReservationRepositoryCustomImpl;
import hairSalonReservation.sideProject.domain.reservation.repository.ScheduleBlockRepository;
import hairSalonReservation.sideProject.domain.reservation.repository.ScheduleBlockRepositoryCustomImpl;
import hairSalonReservation.sideProject.domain.shop.entity.Shop;
import hairSalonReservation.sideProject.domain.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ScheduleBlockService {

    private final ScheduleBlockRepository scheduleBlockRepository;
    private final ScheduleBlockRepositoryCustomImpl blockRepositoryCustom;
    private final DesignerRepository designerRepository;
    private final ShopRepository shopRepository;


    @Transactional
    public ScheduleBlockResponse createBlock(Long ownerId, Long designerId, CreateScheduleBlockRequest request) {
        Designer designer = designerRepository.findByIdAndIsDeletedFalse(designerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DESIGNER_NOT_FOUND));

        Long targetId = designer.getShop().getUser().getId();

        if (!ownerId.equals(targetId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }

        ScheduleBlock block = ScheduleBlock.of(designer, request.blockType(), request.date(), request.time());
        scheduleBlockRepository.save(block);
        return ScheduleBlockResponse.from(block);
    }

    public List<DesignerBlockResponse> readByShopAndDate(Long ownerId, LocalDate date){

        Shop shop = shopRepository.findByUserId(ownerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SHOP_NOT_FOUND));

        List<DesignerBlockResponse> responseList = new ArrayList<>();

        if(!ownerId.equals(shop.getUser().getId())){
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }

        List<ScheduleBlock> blockList =  blockRepositoryCustom.findByShopIdAndDate(shop.getId(), date);
        Map<Designer,List<ScheduleBlock>> blockMap = blockList.stream().collect(Collectors.groupingBy(ScheduleBlock::getDesigner));

        for(Designer designer : blockMap.keySet()){
            List<ScheduleBlockResponse> designerBlockList = blockMap.get(designer).stream().map(ScheduleBlockResponse::from).toList();
            responseList.add(DesignerBlockResponse.of(designer, designerBlockList));
        }
        return responseList;
    }

    //휴무일 조회 api
    public ReadClosedDaysResponse readDayOffByDesignerIdAndMonth(Long designerId, Integer month) {

        List<ScheduleBlock> blockList = blockRepositoryCustom.findByDesignerIdAndMonth(designerId, month);
        return ReadClosedDaysResponse.from(blockList);
    }

    public List<TimeSlotResponse> readTimeSlotByDesignerId(Long designerId, LocalDate date) {

        Designer designer = designerRepository.findById(designerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DESIGNER_NOT_FOUND));

        List<ScheduleBlock> blockList = blockRepositoryCustom.findByDesignerIdAndDate(designerId, date);
        List<LocalTime> timeSlotList = JsonHelper.fromJsonToList(designer.getTimeSlotList(), new TypeReference<>() {
        });//디자이너의 모든 타임슬롯

        if (blockList.isEmpty()) {//블록이 없으면 걍 타임슬롯 그대로 반환함
            return timeSlotList.stream().map(t -> TimeSlotResponse.of(date, t, true)).toList();
        }

        List<LocalTime> blockTimeList = blockList.stream().map(ScheduleBlock::getTime).toList();

        return timeSlotList.stream().map(t -> {
            boolean isReservable = !blockTimeList.contains(t);
            return TimeSlotResponse.of(date, t,  isReservable);
        }).toList();
    }

    @Transactional
    public void deleteBlock(Long ownerId, Long designerId, Long blockId) {

        Designer designer = designerRepository.findByIdAndIsDeletedFalse(designerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DESIGNER_NOT_FOUND));

        Long targetId = designer.getShop().getUser().getId();
        if (!ownerId.equals(targetId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }

        scheduleBlockRepository.deleteById(blockId);
    }
}
