package hairSalonReservation.sideProject.common.image.controller;

import hairSalonReservation.sideProject.common.image.dto.PresignedUrlResponse;
import hairSalonReservation.sideProject.common.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/images/upload/{domainPrefix}/{imageName}")
    public ResponseEntity<PresignedUrlResponse> getPresignedUploadUrl(@PathVariable(name = "imageName") String imageName,
                                                                      @PathVariable(name = "domainPrefix") String domainPrefix) {
        return ResponseEntity.ok(imageService.generatePresignedUploadUrl(domainPrefix, imageName));
    }


    @GetMapping("/images/view")
    public ResponseEntity<PresignedUrlResponse> getPresignedViewUrl(@RequestParam(name = "imageName") String imageName) {
        return ResponseEntity.ok( imageService.generatePresignedAccessUrl(imageName));
    }
}