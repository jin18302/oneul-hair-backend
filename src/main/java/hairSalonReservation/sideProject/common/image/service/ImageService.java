package hairSalonReservation.sideProject.common.image.service;

import hairSalonReservation.sideProject.common.image.dto.PresignedUrlResponse;
import hairSalonReservation.sideProject.common.exception.ErrorCode;
import hairSalonReservation.sideProject.common.exception.ExternalServiceException;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageService {

    @Value("${minio.bucket}")
    String bucket;

    @Value("${minio.expiry}")
    int expiry;

    private final MinioClient minioClient;

    public PresignedUrlResponse generatePresignedUploadUrl(String domainPrefix, String objectName) {

        String imageKey = createImageKey(domainPrefix, objectName);

        GetPresignedObjectUrlArgs preUrlRequest = GetPresignedObjectUrlArgs.builder()
                .method(Method.PUT)
                .bucket(bucket)
                .object(imageKey)
                .expiry(expiry)
                .build();
        try {
            String url = minioClient.getPresignedObjectUrl(preUrlRequest);
            return PresignedUrlResponse.of(url, imageKey);
        } catch (Exception e) {
            log.error("오류 원인 :{}", e.getMessage());
            throw new ExternalServiceException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    public PresignedUrlResponse generatePresignedAccessUrl(String fileName) {

        GetPresignedObjectUrlArgs getUrlRequest = GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucket)
                .object(fileName)
                .expiry(expiry)
                .build();
        try {
            String url = minioClient.getPresignedObjectUrl(getUrlRequest);
            return PresignedUrlResponse.of(url, null);
        } catch (Exception e) {
            throw new ExternalServiceException(ErrorCode.IMAGE_VIEW_FAILED);
        }
    }

    private String createImageKey(String domainPrefix, String imageName) {
        return domainPrefix + "/" + (UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE) + imageName;
    }

    public void deleteImage(List<String> objectKey) {
        try {
            List<DeleteObject> deleteObjectList = objectKey.stream()
                    .map(DeleteObject::new)
                    .toList();

            // 삭제 요청 후 반환되는 결과를 반드시 순회해야 합니다.
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(
                    RemoveObjectsArgs.builder()
                            .bucket(bucket)
                            .objects(deleteObjectList)
                            .build()
            );

            // 중요: 이 루프를 돌아야 실제 삭제가 확정되며 에러를 확인할 수 있습니다.
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                log.error("삭제 실패 - 객체명: {}, 사유: {}", error.objectName(), error.message());
            }

            log.info("삭제 요청 프로세스 완료");
        } catch (Exception e) {
            log.error("삭제 중 예외 발생: {}", e.getMessage());
        }
    }

    public void updateImage(String prevKey, String currentKey) {
        if (!prevKey.equalsIgnoreCase(currentKey)) {
            List<String> imageKeyList = new ArrayList<>();
            imageKeyList.add(prevKey);
            deleteImage(imageKeyList);
        }
    }

    @PostConstruct
    private void isExistBucket() {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }
        } catch (Exception ex) {
            throw new ExternalServiceException(ErrorCode.MINIO_INITIALIZATION_FAILED);
        }
    }
}
