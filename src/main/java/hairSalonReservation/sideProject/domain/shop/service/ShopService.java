package hairSalonReservation.sideProject.domain.shop.service;

import com.querydsl.core.types.Order;
import hairSalonReservation.sideProject.common.annotation.CheckRole;
import hairSalonReservation.sideProject.common.cursor.CursorPageResponse;
import hairSalonReservation.sideProject.common.exception.ErrorCode;
import hairSalonReservation.sideProject.common.exception.ForbiddenException;
import hairSalonReservation.sideProject.common.exception.NotFoundException;
import hairSalonReservation.sideProject.common.util.JsonHelper;
import hairSalonReservation.sideProject.domain.auth.service.AuthService;
import hairSalonReservation.sideProject.domain.shop.dto.request.CreateShopRequest;
import hairSalonReservation.sideProject.domain.shop.dto.request.UpdateShopRequest;
import hairSalonReservation.sideProject.domain.shop.dto.response.CreateShopResponse;
import hairSalonReservation.sideProject.domain.shop.dto.response.ShopDetailResponse;
import hairSalonReservation.sideProject.domain.shop.dto.response.ShopSummaryResponse;
import hairSalonReservation.sideProject.domain.shop.entity.Shop;
import hairSalonReservation.sideProject.domain.shop.entity.ShopSortField;
import hairSalonReservation.sideProject.domain.shop.entity.ShopStatus;
import hairSalonReservation.sideProject.domain.shop.repository.ShopRepository;
import hairSalonReservation.sideProject.domain.shop.repository.ShopRepositoryCustomImpl;
import hairSalonReservation.sideProject.domain.user.entity.User;
import hairSalonReservation.sideProject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShopService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final ShopRepositoryCustomImpl shopRepositoryCustom;
    private final ShopTagMapperService shopTagMapperService;
    private final AuthService authService;

    @Transactional
    public CreateShopResponse createShop(CreateShopRequest request) {

        Long userId = authService.signup(request.ownerSignUpRequest()).id();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        Shop shop = Shop.of(
                user,
                request.name(),
                request.businessId(),
                request.address(),
                request.phoneNumber(),
                request.openTime(),
                request.endTime(),
                request.introduction(),
                JsonHelper.toJson(request.snsUriList())
        );

        shopRepository.save(shop);

        shopTagMapperService.createShopTagMapper(shop, new ArrayList<>(request.shopTagIdSet()));
        return CreateShopResponse.from(shop);
    }

    public CursorPageResponse<ShopSummaryResponse> readByFilter(String area, List<Long> tagList, String order, String sortFelid, String lastCursor) {

        List<ShopSummaryResponse> shopSummaryResponseList = shopRepositoryCustom
                .findByFilter(area, tagList, ShopSortField.valueOf(sortFelid), Order.valueOf(order), lastCursor);

        return CursorPageResponse.of(shopSummaryResponseList, ShopSummaryResponse::getId);
    }

    public ShopDetailResponse readShopDetail(Long shopId) {

        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new NotFoundException(ErrorCode.SHOP_NOT_FOUND));
        return ShopDetailResponse.from(shop);
    }

    @CheckRole("OWNER")
    public ShopDetailResponse readByOwnerId(Long userId){
        Shop shop = shopRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SHOP_NOT_FOUND));

        return  ShopDetailResponse.from(shop);

    }

    @CheckRole("OWNER")
    @Transactional
    public ShopDetailResponse updateShop(Long userId, Long shopId, UpdateShopRequest updateShopRequest) {

        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new NotFoundException(ErrorCode.SHOP_NOT_FOUND));
        Long shopOwnerId = shopRepositoryCustom.findShopOwnerIdByShopId(shopId);
        if (!userId.equals(shopOwnerId)) {throw new ForbiddenException(ErrorCode.FORBIDDEN);}

        shop.update(
                updateShopRequest.name(),
                updateShopRequest.address(),
                updateShopRequest.phoneNumber(),
                updateShopRequest.openTime(),
                updateShopRequest.endTime(),
                updateShopRequest.introduction(),
                JsonHelper.toJson(updateShopRequest.snsUriList())
        );
        shopTagMapperService.updateShopTagMapper(userId, shop, new ArrayList<>(updateShopRequest.shopTagIdSet()));
        return ShopDetailResponse.from(shop);
    }

    @CheckRole("OWNER")
    @Transactional
    public void deleteShop(Long userId, Long shopId) {

        Shop shop = shopRepository.findByIdAndIsDeletedFalse(shopId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.SHOP_NOT_FOUND));

        Long shopOwnerId = shopRepositoryCustom.findShopOwnerIdByShopId(shopId);
        if (!userId.equals(shopOwnerId)) {throw new ForbiddenException(ErrorCode.FORBIDDEN);}

        shop.delete();
    }
}
