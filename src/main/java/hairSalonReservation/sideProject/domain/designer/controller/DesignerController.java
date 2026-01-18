package hairSalonReservation.sideProject.domain.designer.controller;

import hairSalonReservation.sideProject.domain.designer.dto.request.CreateDesignerRequest;
import hairSalonReservation.sideProject.domain.designer.dto.request.UpdateDesignerRequest;
import hairSalonReservation.sideProject.domain.designer.dto.response.DesignerDetailResponse;
import hairSalonReservation.sideProject.domain.designer.dto.response.DesignerSummaryResponse;
import hairSalonReservation.sideProject.domain.designer.service.DesignerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class DesignerController {

    private final DesignerService designerService;


    @PostMapping("/shops/designers")
    public ResponseEntity<DesignerDetailResponse> createDesigner(
            @RequestAttribute("userId") Long userId,
            @RequestBody @Valid CreateDesignerRequest request
    ){
        DesignerDetailResponse designerResponse = designerService.createDesigner(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(designerResponse);
    }

    @GetMapping("/auth/shops/{shopId}/designers")
    public ResponseEntity<List<DesignerSummaryResponse>> readByShopId(@PathVariable(name = "shopId") Long shopId){

        List<DesignerSummaryResponse> designerSummaryResponseList = designerService.readByShop(shopId);
        return ResponseEntity.status(HttpStatus.OK).body(designerSummaryResponseList);
    }

    @GetMapping("/shops/designers")//o
    public ResponseEntity<List<DesignerSummaryResponse>> readByOwner(@RequestAttribute("userId") Long userId){
        List<DesignerSummaryResponse> designerSummaryResponseList = designerService.readByOwner(userId);
        return ResponseEntity.status(HttpStatus.OK).body(designerSummaryResponseList);
    }

    @GetMapping("/auth/designers/{designerId}")//o
    public ResponseEntity<DesignerDetailResponse> readById(@PathVariable(name = "designerId") Long designerId){

        DesignerDetailResponse designerDetailResponse = designerService.readById(designerId);
        return ResponseEntity.status(HttpStatus.OK).body(designerDetailResponse);
    }

    @PatchMapping("/designers/{designerId}")
    public ResponseEntity<DesignerDetailResponse> updateDesigner(
            @RequestAttribute("userId") Long userId,
            @PathVariable(name = "designerId") Long designerId,
            @RequestBody @Valid UpdateDesignerRequest request
    ){
        DesignerDetailResponse designerDetailResponse = designerService.updateDesigner(userId, designerId, request);
        return ResponseEntity.status(HttpStatus.OK).body(designerDetailResponse);
    }

    @DeleteMapping("/designers/{designerId}")
    public ResponseEntity<Void> deleteDesigner(
            @RequestAttribute("userId") Long userId,
            @PathVariable(name = "designerId") Long designerId
    ){
        designerService.deleteDesigner(userId, designerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
