package hairSalonReservation.sideProject.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserInfoRequest(
        @NotEmpty
        @Size(max = 15)
        String name,

        String profileImage,

        @NotEmpty
        @Email
        String email,

        @NotEmpty
        @Pattern( regexp = "^(010|011|016|017|018|019|02|0\\d{2})-\\d{3,4}-\\d{4}$"
                , message = "010-1234-567 형식으로 입력해주세요.")
        String phoneNumber
) {
}
