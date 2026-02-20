package hairSalonReservation.sideProject.domain.shop.repository;

import com.querydsl.core.types.Order;
import hairSalonReservation.sideProject.domain.shop.dto.response.ShopSummaryResponse;
import hairSalonReservation.sideProject.domain.shop.entity.ShopSortField;

import java.util.List;

public interface ShopRepositoryCustom {

    List<ShopSummaryResponse> findByFilter(List<String> areaList, List<Long> tagList, ShopSortField sortField, Order order, String cursor);

    Long findShopOwnerIdByShopId(Long shopId);


}
