package hairSalonReservation.sideProject.domain.shop.dto.response;

import hairSalonReservation.sideProject.domain.shop.entity.Shop;
import hairSalonReservation.sideProject.domain.shop.entity.ShopStatus;
import hairSalonReservation.sideProject.domain.shop.entity.ShopTag;
import hairSalonReservation.sideProject.domain.shop.entity.ShopTagMapper;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record ShopDetailResponse(
        Long id,
        String name,
        String mainImage,
        String address,
        String phoneNumber,
        LocalTime openTime,
        LocalTime endTime,
        String introduction,
        String snsUriList,
        List<String> shopTagList,
        ShopStatus shopStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {
    public static ShopDetailResponse from(Shop shop) {

        List<String> shopTagList = shop.getShopTagMapperList().stream().map(s -> s.getShopTag().getName()).toList();

        return new ShopDetailResponse(
                shop.getId(),
                shop.getMainImage(),
                shop.getName(),
                shop.getAddress(),
                shop.getPhoneNumber(),
                shop.getOpenTime(),
                shop.getEndTime(),
                shop.getIntroduction(),
                shop.getSnsUriList(),
                shopTagList,
                shop.getShopStatus(),
                shop.getCreatedAt(),
                shop.getUpdatedAt(),
                shop.getDeletedAt()
        );
    }
}
