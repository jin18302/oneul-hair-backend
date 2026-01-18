package hairSalonReservation.sideProject.domain.shop.controller;

import hairSalonReservation.sideProject.domain.shop.dto.request.CreateShopTagRequest;
import hairSalonReservation.sideProject.domain.shop.dto.response.ShopTagResponse;
import hairSalonReservation.sideProject.domain.shop.service.ShopTagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ShopTagController {

    private final ShopTagService shopTagService;

    @PostMapping("/shop-tags")
    public ResponseEntity<ShopTagResponse> createShopTag(@RequestBody @Valid CreateShopTagRequest request){

        ShopTagResponse shopTagResponse = shopTagService.createShopTag(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(shopTagResponse);
    }

    @GetMapping("/shop-tags")
    public ResponseEntity<List<ShopTagResponse>> readAllShopTag(){
        List<ShopTagResponse> shopTagResponseList = shopTagService.readAllShopTag();
        return ResponseEntity.status(HttpStatus.OK).body(shopTagResponseList);
    }


    @DeleteMapping("/shop-tags/{shopTagId}")
    public ResponseEntity<Void> deleteShopTag(@PathVariable("shopTagId") Long shopTagId){
        shopTagService.deleteShopTag(shopTagId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
