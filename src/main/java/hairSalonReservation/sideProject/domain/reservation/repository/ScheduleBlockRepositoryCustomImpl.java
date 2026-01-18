package hairSalonReservation.sideProject.domain.reservation.repository;

import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hairSalonReservation.sideProject.domain.designer.entity.Designer;
import hairSalonReservation.sideProject.domain.reservation.dto.response.ReadClosedDaysResponse;
import hairSalonReservation.sideProject.domain.reservation.entity.BlockType;
import hairSalonReservation.sideProject.domain.reservation.entity.ScheduleBlock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static hairSalonReservation.sideProject.domain.designer.entity.QDesigner.designer;
import static hairSalonReservation.sideProject.domain.reservation.entity.QScheduleBlock.scheduleBlock;

@Repository
@RequiredArgsConstructor
public class ScheduleBlockRepositoryCustomImpl implements ScheduleBlockRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ScheduleBlock> findByDesignerIdAndDate(Long designerId, LocalDate date) {

        return
                queryFactory.select(scheduleBlock)
                        .from(scheduleBlock)
                        .join(scheduleBlock.designer, designer).fetchJoin()
                        .where(
                                scheduleBlock.date.eq(date),
                                scheduleBlock.designer.id.eq(designerId))
                        .fetch();
    }

    @Override
    public List<ScheduleBlock> findByDesignerIdAndMonth(Long designerId, Integer month) {
        return
                queryFactory.select(scheduleBlock)
                        .from(scheduleBlock)
                        .join(scheduleBlock.designer, designer).fetchJoin()
                        .where(
                                scheduleBlock.designer.id.eq(designerId),
                                scheduleBlock.date.month().eq(month),
                                scheduleBlock.blockType.eq(BlockType.DAYOFF)
                        )
                        .fetch();
    }

    @Override
    public List<ScheduleBlock> findByShopIdAndDate(Long shopId, LocalDate date) {

      return queryFactory.select(scheduleBlock)
                .from(scheduleBlock)
                .join(scheduleBlock.designer).fetchJoin()
                .where(
                        designer.shop.id.eq(shopId),
                        scheduleBlock.date.eq(date))
                .fetch();


    }

}
