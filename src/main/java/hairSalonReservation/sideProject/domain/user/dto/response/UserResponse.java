package hairSalonReservation.sideProject.domain.user.dto.response;

import hairSalonReservation.sideProject.domain.user.entity.Gender;
import hairSalonReservation.sideProject.domain.user.entity.User;
import hairSalonReservation.sideProject.domain.user.entity.UserRole;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String profileImage,
        String name,
        String email,
        Gender gender,
        UserRole userRole,
        LocalDateTime createdAt
) {

    public static UserResponse from(User user){
        return new UserResponse(
                user.getId(),
                user.getProfileImage(),
                user.getName(),
                user.getEmail(),
                user.getGender(),
                user.getUserRole(),
                user.getCreatedAt()
                );
    }
}
