package hairSalonReservation.sideProject.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotEmpty
        @Size(max = 15)
        String name,

        @NotEmpty
        @Email
        String email,

        @NotEmpty
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).{8,}$",
                message = "비밀번호는 소문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.")
        String password,

        @NotEmpty
        @Pattern( regexp = "^(010|011|016|017|018|019|02|0\\d{2})-\\d{3,4}-\\d{4}$"
                , message = "010-1234-567 형식으로 입력해주세요.")
        String phoneNumber,

        String gender,

        @NotEmpty
        String userRole
) {
}
