package hairSalonReservation.sideProject.domain.auth.service;

import hairSalonReservation.sideProject.common.util.PasswordEncoder;
import hairSalonReservation.sideProject.domain.auth.dto.request.LoginRequest;
import hairSalonReservation.sideProject.domain.auth.dto.response.LoginResponse;
import hairSalonReservation.sideProject.domain.user.entity.User;
import hairSalonReservation.sideProject.common.exception.BadRequestException;
import hairSalonReservation.sideProject.common.exception.ConflictException;
import hairSalonReservation.sideProject.common.exception.ErrorCode;
import hairSalonReservation.sideProject.common.exception.NotFoundException;
import hairSalonReservation.sideProject.common.util.JwtUtil;
import hairSalonReservation.sideProject.domain.auth.dto.request.SignUpRequest;
import hairSalonReservation.sideProject.domain.auth.dto.response.SignUpResponse;
import hairSalonReservation.sideProject.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignUpResponse signup(SignUpRequest request) {

        if(userRepository.existsByEmail(request.email())){throw new ConflictException(ErrorCode.DUPLICATE_EMAIL);}

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.of(request.name(), request.profileImage(), request.email(), encodedPassword, request.phoneNumber(), request.gender(), request.userRole());
        userRepository.save(user);

        return SignUpResponse.from(user);
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email()).orElseThrow(()-> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        if(!passwordEncoder.matches(request.password(), user.getPassword())){throw new BadRequestException(ErrorCode.INVALID_PASSWORD);}

        String accessToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());
        return LoginResponse.of(accessToken);
    }
}
