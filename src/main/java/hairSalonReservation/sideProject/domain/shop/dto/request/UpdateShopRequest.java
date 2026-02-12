package hairSalonReservation.sideProject.domain.shop.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public record UpdateShopRequest(
        @Size(max = 15) @NotEmpty String name,
        String mainImage,
        @NotEmpty String address,
        @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$") @NotEmpty String phoneNumber,
        @JsonFormat(pattern = "HH:mm") LocalTime openTime,
        @JsonFormat(pattern = "HH:mm") LocalTime endTime,
        @NotEmpty String introduction,
        List<String> snsUriList,
        Set<Long> shopTagIdSet
) {
}
