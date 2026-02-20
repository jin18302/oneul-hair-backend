package hairSalonReservation.sideProject.domain.shop.dto.response;

import hairSalonReservation.sideProject.domain.shop.entity.Shop;
import hairSalonReservation.sideProject.domain.shop.entity.ShopStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record CreateShopResponse(
        Long id,
        String name,
        String mainImage,
        String businessId,
        String address,
        String phoneNumber,
        LocalTime openTime,
        LocalTime endTime,
        String introduction,
        String snsUriList,
        List<String> shopTagIdSet,
        ShopStatus shopStatus,
        LocalDateTime createdAt
) {

    public static CreateShopResponse from(Shop shop){

        List<String> shopTagList = shop.getShopTagMapperList().stream().map(s -> s.getShopTag().getName()).toList();

        return new CreateShopResponse(
                shop.getId(),
                shop.getMainImage(),
                shop.getName(),
                shop.getBusinessId(),
                shop.getAddress(),
                shop.getPhoneNumber(),
                shop.getOpenTime(),
                shop.getEndTime(),
                shop.getIntroduction(),
                shop.getSnsUriList(),
                shopTagList,
                shop.getShopStatus(),
                shop.getCreatedAt()
        );
    }
}
