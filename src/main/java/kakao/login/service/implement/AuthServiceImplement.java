package kakao.login.service.implement;

import kakao.login.common.CertificationNumber;
import kakao.login.dto.request.auth.*;
import kakao.login.dto.response.ResponseDto;
import kakao.login.dto.response.auth.*;
import kakao.login.entity.CertificationEntity;
import kakao.login.entity.UserEntity;
import kakao.login.provider.EmailProvider;
import kakao.login.provider.JwtProvider;
import kakao.login.repository.CertificationRepository;
import kakao.login.repository.UserRepository;
import kakao.login.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {

    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;
    private final EmailProvider emailProvider;
    private final JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto) {

        try {

            String userId = dto.getId();
            boolean isExistId = userRepository.existsByUserId(userId);
            if(isExistId) return IdCheckResponseDto.duplicateId();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return IdCheckResponseDto.success();
    }

    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto) {
        try {

            String userId = dto.getId();
            String email = dto.getEmail();

            boolean isExistId = userRepository.existsByUserId(userId);
            if(isExistId) return EmailCertificationResponseDto.duplicateEmail();

            String certificationNumber = CertificationNumber.getCertificationNumber();

            boolean isSuccess = emailProvider.sendCertificationMail(email, certificationNumber);
            if(!isSuccess) return EmailCertificationResponseDto.mailSendFail();

            CertificationEntity certificationEntity = new CertificationEntity(userId, email, certificationNumber);
            certificationRepository.save(certificationEntity);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return EmailCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {
        try {

            String userId = dto.getId();
            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();

            CertificationEntity certificationEntity = certificationRepository.findByUserId(userId);
            if(certificationEntity == null) return CheckCertificationResponseDto.certificationFail();

            boolean isMatch = certificationEntity.getEmail().equals(email) && certificationEntity.getCertificationNumber().equals(certificationNumber);
            if(!isMatch) return CheckCertificationResponseDto.certificationFail();


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CheckCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {
        try {

            String userId = dto.getId();
            boolean isExistId = userRepository.existsByUserId(userId);
            if(isExistId) return SignUpResponseDto.duplicateId();

            String email= dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();

            CertificationEntity certificationEntity = certificationRepository.findByUserId(userId);
            boolean isMatched =
                    certificationEntity.getEmail().equals(email) && certificationEntity.getCertificationNumber().equals(certificationNumber);
            if(!isMatched) return SignUpResponseDto.certificationFail();

            String password = dto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            dto.setPassword(encodedPassword);

            UserEntity userEntity = new UserEntity(dto);
            userRepository.save(userEntity);

            certificationRepository.deleteByUserId(userId);


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return SignUpResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {
        String token = null;
        try {

            String userId = dto.getId();
            UserEntity userEntity = userRepository.findByUserId(userId);
            if(userEntity == null) return SignInResponseDto.signInFail(token);

            String password = dto.getPassword();
            String encodedPassword = userEntity.getPassword();
            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            if(!isMatched) return SignInResponseDto.signInFail(token);

            token = jwtProvider.create(userId);


        } catch (Exception e) {
            e.printStackTrace();
            return SignInResponseDto.signInFail(token);
        }
        return SignInResponseDto.success(token);
    }
}
