package hairSalonReservation.sideProject.domain.shop.dto.response;

import com.fasterxml.jackson.core.type.TypeReference;
import hairSalonReservation.sideProject.common.util.JsonHelper;
import hairSalonReservation.sideProject.domain.shop.entity.Shop;
import hairSalonReservation.sideProject.domain.shop.entity.ShopStatus;
import lombok.Getter;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

@Getter
public final class ShopSummaryResponse {
    private final Long id;
    private final String mainImage;
    private final String name;
    private final String introduction;
    private final String address;
    private final ShopStatus shopStatus;

    public ShopSummaryResponse(
            Long id,
            String mainImage,
            String name,
            String introduction,
            String address,
            ShopStatus shopStatus
    ) {
        this.id = id;
        this.mainImage = mainImage;
        this.name = name;
        this.introduction = introduction;
        this.address = address;
        this.shopStatus = shopStatus;
    }


    public static ShopSummaryResponse from(Shop shop) {
        return new ShopSummaryResponse(
                shop.getId(),
                shop.getMainImage(),
                shop.getName(),
                shop.getIntroduction(),
                shop.getAddress(),
                shop.getShopStatus());
    }

}
