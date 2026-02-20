package hairSalonReservation.sideProject.domain.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hairSalonReservation.sideProject.common.config.QueryProperties;
import hairSalonReservation.sideProject.common.cursor.OrderSpecifierFactory;
import hairSalonReservation.sideProject.domain.shop.dto.response.ShopSummaryResponse;
import hairSalonReservation.sideProject.domain.shop.entity.QShop;
import hairSalonReservation.sideProject.domain.shop.entity.ShopSortField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import static hairSalonReservation.sideProject.domain.shop.entity.QShop.shop;
import static hairSalonReservation.sideProject.domain.shop.entity.QShopTag.shopTag;
import static hairSalonReservation.sideProject.domain.shop.entity.QShopTagMapper.shopTagMapper;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ShopRepositoryCustomImpl implements ShopRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final OrderSpecifierFactory<QShop, ShopSortField> orderSpecifierFactory;
    private final QueryProperties queryProperties;

    @Override
    public List<ShopSummaryResponse> findByFilter(List<String> areaList, List<Long> tagList, ShopSortField sortField, Order order, String cursor) {

        List<Long> taggedShopList = filterShopIdsByAllTagsAndCursor(cursor, tagList, order, sortField);

        BooleanBuilder builder = new BooleanBuilder();

        if(areaList != null){areaList.forEach(area -> builder.or(shop.address.startsWith(area)));}
        if(!taggedShopList.isEmpty()){builder.and(shop.id.in(taggedShopList));}

        return queryFactory.select(
                        Projections.constructor(
                                ShopSummaryResponse.class,
                                shop.id,
                                shop.mainImage,
                                shop.name,
                                shop.introduction,
                                shop.address,
                                shop.shopStatus
                        ))
                .from(shop)
                .where(builder)
                .orderBy(orderSpecifierFactory.generateOrderSpecifier(shop, sortField, order))//정렬을 실행해주는 역할뿐임.
                .limit(queryProperties.getLimit() + 1)
                .fetch();
    }

    private List<Long> filterShopIdsByAllTagsAndCursor(String lastCursor, List<Long> tagList, Order order, ShopSortField sortField) {


        BooleanBuilder subQueryBuilder = new BooleanBuilder();

        if (!tagList.isEmpty()) {
            subQueryBuilder.and(shopTagMapper.shopTag.id.in(tagList));
        }


        if (lastCursor != null) {

            shop.id.gt(Integer.parseInt(lastCursor));

        }

        return queryFactory.select(shopTagMapper.shop.id)
                .from(shopTagMapper)
                .where(subQueryBuilder)
                .groupBy(shop.id)
                .having(shopTag.id.count().eq((long) tagList.size()))
                .fetch();
    }


    @Override
    public Long findShopOwnerIdByShopId(Long shopId) {
        return queryFactory.select(shop.user.id)
                .from(shop)
                .where(shop.id.eq(shopId))
                .fetchOne();
    }
}
