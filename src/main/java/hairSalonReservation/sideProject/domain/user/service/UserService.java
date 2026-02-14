package hairSalonReservation.sideProject.domain.user.service;

import hairSalonReservation.sideProject.common.exception.BadRequestException;
import hairSalonReservation.sideProject.common.exception.ErrorCode;
import hairSalonReservation.sideProject.common.exception.NotFoundException;
import hairSalonReservation.sideProject.common.image.service.ImageService;
import hairSalonReservation.sideProject.common.util.PasswordEncoder;
import hairSalonReservation.sideProject.domain.user.dto.request.DeleteUserRequest;
import hairSalonReservation.sideProject.domain.user.dto.request.UpdateUserInfoRequest;
import hairSalonReservation.sideProject.domain.user.dto.response.UserResponse;
import hairSalonReservation.sideProject.domain.user.entity.User;
import hairSalonReservation.sideProject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    public UserResponse readUserInfo(Long userId){

        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse updateUserInfo(Long userId, UpdateUserInfoRequest request){

        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

       imageService.updateImage(user.getProfileImage(), request.profileImage());

        user.update(request);
        return UserResponse.from(user);
    }

    @Transactional
    public void deleteUser(Long userId, DeleteUserRequest request){

        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new BadRequestException(ErrorCode.INVALID_PASSWORD);
        }

        userRepository.deleteById(userId);

        List<String> image= new ArrayList<>();
        image.add(user.getProfileImage());
        imageService.deleteImage(image);
    }

}
