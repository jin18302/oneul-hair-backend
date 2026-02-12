package hairSalonReservation.sideProject.domain.shop.controller;

import hairSalonReservation.sideProject.common.cursor.CursorPageResponse;
import hairSalonReservation.sideProject.domain.shop.dto.request.CreateShopRequest;
import hairSalonReservation.sideProject.domain.shop.dto.request.DeleteShopRequest;
import hairSalonReservation.sideProject.domain.shop.dto.request.UpdateShopRequest;
import hairSalonReservation.sideProject.domain.shop.dto.response.CreateShopResponse;
import hairSalonReservation.sideProject.domain.shop.dto.response.ShopDetailResponse;
import hairSalonReservation.sideProject.domain.shop.dto.response.ShopSummaryResponse;
import hairSalonReservation.sideProject.domain.shop.service.ShopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping("/auth/shops")//o
    public ResponseEntity<CreateShopResponse> createShop(@RequestBody @Valid CreateShopRequest createShopRequest){
        CreateShopResponse createShopResponse = shopService.createShop(createShopRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createShopResponse);
    }

    @GetMapping("/auth/shops")//o
    public ResponseEntity<CursorPageResponse<ShopSummaryResponse>> readByFilter(
            @RequestParam(required = false, name = "area") List<String> areaList,
            @RequestParam(required = false, name = "tagIdList") List<Long> tagList,// TODO: 사이즈 제한을 두어야함
            @RequestParam(required = false, name = "lastCursor", defaultValue = "0")String lastCursor,
            @RequestParam(required = false, name = "order", defaultValue = "ASC")String order,
            @RequestParam(required = false, name = "sortFelid", defaultValue = "CREATED_AT") String sortFelid
    ) {
        CursorPageResponse<ShopSummaryResponse> shopResponsePage = shopService.readByFilter(areaList, tagList, order, sortFelid, lastCursor);
        return ResponseEntity.ok(shopResponsePage);
    }

    @GetMapping("/auth/shops/{shopId}")//o
    public ResponseEntity<ShopDetailResponse> readShopDetail(@PathVariable("shopId") Long shopId) {

        ShopDetailResponse shopDetailResponse = shopService.readShopDetail(shopId);
        return ResponseEntity.ok(shopDetailResponse);
    }

    @GetMapping("/shops")
    public ResponseEntity<ShopDetailResponse> readByOwnerId(@RequestAttribute("userId") Long userId){

        ShopDetailResponse shopDetailResponse = shopService.readByOwnerId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(shopDetailResponse);
    }

    @PatchMapping("/shops/{shopId}")
    public ResponseEntity<ShopDetailResponse> updateShop(//x
            @RequestAttribute("userId") Long userId,
            @PathVariable("shopId") Long shopId,
            @RequestBody @Valid UpdateShopRequest updateShopRequest
    ){

        ShopDetailResponse shopDetailResponse = shopService.updateShop(userId, shopId, updateShopRequest);
        return ResponseEntity.ok(shopDetailResponse);
    }

    @DeleteMapping("/shops/{shopId}")//x
    public ResponseEntity<Void> deleteShop(
            @RequestAttribute("userId") Long userId,
            @PathVariable("shopId") Long shopId,
            DeleteShopRequest request
    ){
        shopService.deleteShop(userId, shopId, request);
        return ResponseEntity.ok().build();
    }
}
