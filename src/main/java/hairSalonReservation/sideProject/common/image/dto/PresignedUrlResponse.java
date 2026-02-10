package hairSalonReservation.sideProject.common.image.dto;

import javax.annotation.Nullable;

public record PresignedUrlResponse(String url, String imageName) {
    public static PresignedUrlResponse of(String url, @Nullable String imageName){
        return new PresignedUrlResponse(url, imageName);
    }
}
