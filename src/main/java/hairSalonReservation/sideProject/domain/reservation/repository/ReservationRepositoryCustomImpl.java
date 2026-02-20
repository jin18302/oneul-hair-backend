package hairSalonReservation.sideProject.domain.reservation.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hairSalonReservation.sideProject.common.config.QueryProperties;
import hairSalonReservation.sideProject.domain.reservation.dto.response.ReservationResponse;
import hairSalonReservation.sideProject.domain.reservation.entity.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static hairSalonReservation.sideProject.domain.designer.entity.QDesigner.designer;
import static hairSalonReservation.sideProject.domain.reservation.entity.QReservation.reservation;
import static hairSalonReservation.sideProject.domain.shop.entity.QShop.shop;
import static hairSalonReservation.sideProject.domain.user.entity.QUser.user;


@Slf4j
@Repository
@RequiredArgsConstructor
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QueryProperties queryProperties;

    @Override
    public boolean existByDesignerIdAndSlot(Long designerId, LocalDate date, LocalTime time) {

        Long count = queryFactory
                .select(reservation.count())
                .from(reservation)
                .where(
                        reservation.scheduleBlock.designer.id.eq(designerId),
                        reservation.scheduleBlock.date.eq(date),
                        reservation.scheduleBlock.time.eq(time)
                )
                .fetchOne();

        return count != 0;
    }

    @Override
    public List<ReservationResponse> findByDesignerIdAndDate(Long designerId, LocalDate date) {

        return queryFactory
                .select(Projections.constructor(
                        ReservationResponse.class,
                        reservation.id,
                        reservation.serviceMenu.name,
                        reservation.scheduleBlock.designer.name,
                        reservation.reservationStatus,
                        reservation.scheduleBlock.date,
                        reservation.scheduleBlock.time
                ))
                .from(reservation)
                .where(
                        reservation.scheduleBlock.designer.id.eq(designerId),
                        reservation.scheduleBlock.date.eq(Objects.requireNonNullElseGet(date, LocalDate::now))
                )
                .fetch();
    }

    @Override
    public List<ReservationResponse> findByUserId(Long userId) {

        log.info("userId : {}", userId);

        return queryFactory.select(Projections.constructor(
                        ReservationResponse.class,
                        reservation.id,
                        reservation.serviceMenu.name,
                        reservation.scheduleBlock.designer.name,
                        reservation.reservationStatus,
                        reservation.scheduleBlock.date,
                        reservation.scheduleBlock.time
                ))
                .from(reservation)
                .where(reservation.user.id.eq(userId))
                .fetch();
    }

    @Override
    public List<Reservation> findByReservationSloat(LocalDate date, LocalTime time, Long cursor) {

       return queryFactory
                .select(reservation)
                .from(reservation)
                .join(reservation.user, user).fetchJoin()
                .join(reservation.scheduleBlock.designer, designer).fetchJoin()
                .join(designer.shop, shop).fetchJoin()
                .where(
                        reservation.scheduleBlock.date.eq(date),
                        reservation.scheduleBlock.time.eq(time),
                        reservation.id.gt(cursor)
                )
                .orderBy(reservation.id.asc())
                .limit(queryProperties.getLimit())
                .fetch();
    }
}
