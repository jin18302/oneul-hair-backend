package hairSalonReservation.sideProject.domain.shop.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import hairSalonReservation.sideProject.domain.auth.dto.request.SignUpRequest;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public record CreateShopRequest(
        @Size(max = 15) @NotEmpty String name,
        @NotEmpty String businessId,
        @NotEmpty String address,
        @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$") @NotEmpty String phoneNumber,
        @JsonFormat(pattern = "HH:mm") @NotNull LocalTime openTime,
        @JsonFormat(pattern = "HH:mm") @NotNull LocalTime endTime,
        @NotEmpty String introduction,
        List<String> snsUriList,
        Set<Long> shopTagIdSet,
        SignUpRequest ownerSignUpRequest
) {
}
