package hairSalonReservation.sideProject.domain.designer.service;

import hairSalonReservation.sideProject.common.annotation.CheckRole;
import hairSalonReservation.sideProject.common.image.service.ImageService;
import hairSalonReservation.sideProject.common.util.JsonHelper;
import hairSalonReservation.sideProject.domain.designer.dto.request.CreateDesignerRequest;
import hairSalonReservation.sideProject.domain.designer.dto.request.UpdateDesignerRequest;
import hairSalonReservation.sideProject.domain.designer.dto.response.DesignerDetailResponse;
import hairSalonReservation.sideProject.domain.designer.dto.response.DesignerSummaryResponse;
import hairSalonReservation.sideProject.domain.designer.entity.Designer;
import hairSalonReservation.sideProject.domain.designer.repository.DesignerRepository;
import hairSalonReservation.sideProject.domain.designer.repository.DesignerRepositoryCustomImpl;
import hairSalonReservation.sideProject.domain.reservation.service.TimeSlotCreator;
import hairSalonReservation.sideProject.domain.shop.entity.Shop;
import hairSalonReservation.sideProject.domain.shop.repository.ShopRepository;
import hairSalonReservation.sideProject.common.exception.ErrorCode;
import hairSalonReservation.sideProject.common.exception.ForbiddenException;
import hairSalonReservation.sideProject.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DesignerService {

    private final DesignerRepository designerRepository;
    private final DesignerRepositoryCustomImpl designerRepositoryCustomImpl;
    private final ShopRepository shopRepository;
    private final TimeSlotCreator timeSlotCreator;
    private final ImageService imageService;

    @CheckRole("OWNER")
    @Transactional
    public DesignerDetailResponse createDesigner(Long userId, CreateDesignerRequest request ){

        Shop shop = shopRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException(ErrorCode.SHOP_NOT_FOUND));
        Long shopOwnerId = shop.getUser().getId();
        if(!shopOwnerId.equals(userId)){throw new ForbiddenException(ErrorCode.FORBIDDEN);}

        List<LocalTime> designerTimeSlot = timeSlotCreator.createTimeSlot(shop.getOpenTime(), shop.getEndTime());
        Designer designer = Designer.of(
                shop,
                request.name(),
                request.profileImage(),
                request.introduction(),
                JsonHelper.toJson(request.snsUriList()),
                JsonHelper.toJson(designerTimeSlot)

        );

        designerRepository.save(designer);
        return DesignerDetailResponse.from(designer);
    }

    public List<DesignerSummaryResponse> readByShop( Long shopId ){

        return designerRepositoryCustomImpl.findByShopId(shopId);
    }

    public List<DesignerSummaryResponse> readByOwner( Long ownerId ){

       Shop shop = shopRepository.findByUserId(ownerId)
               .orElseThrow(() -> new NotFoundException(ErrorCode.SHOP_NOT_FOUND));

       return designerRepositoryCustomImpl.findByShopId(shop.getId());
    }



    public DesignerDetailResponse readById(Long designerId){

        Designer designer = designerRepository.findById(designerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DESIGNER_NOT_FOUND));

        return DesignerDetailResponse.from(designer);
    }

    @CheckRole("OWNER")
    @Transactional
    public DesignerDetailResponse updateDesigner( Long userId, Long designerId,  UpdateDesignerRequest request ){

        Designer designer = designerRepository.findByIdAndIsDeletedFalse(designerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DESIGNER_NOT_FOUND));

        Long shopOwnerId = designer.getShop().getUser().getId();

        if(!shopOwnerId.equals(userId)){throw new ForbiddenException(ErrorCode.FORBIDDEN);}
        imageService.updateImage(designer.getProfileImage(), request.profileImage());

        designer.update(
                request.name(),
                request.profileImage(),
                request.introduction(),
                JsonHelper.toJson(request.snsUriList())
        );

        return DesignerDetailResponse.from(designer);
    }

    @CheckRole("OWNER")
    @Transactional
    public void deleteDesigner( Long userId, Long designerId ){

        Designer designer = designerRepository.findByIdAndIsDeletedFalse(designerId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DESIGNER_NOT_FOUND));

        Long shopOwnerId = designer.getShop().getUser().getId();
        if(!shopOwnerId.equals(userId)){throw new ForbiddenException(ErrorCode.FORBIDDEN);}

        designer.delete();
    }
}
