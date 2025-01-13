package kakao.login.controller;

import jakarta.validation.Valid;
import kakao.login.dto.request.auth.*;
import kakao.login.dto.response.auth.*;
import kakao.login.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/id-check")
    public ResponseEntity<?super IdCheckResponseDto> idCheck(
            @RequestBody @Valid IdCheckRequestDto requestBody
            ){
        System.out.println("Request body: " + requestBody);
        return authService.idCheck(requestBody);
    }

    @PostMapping("/email-certification")
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification (
            @RequestBody @Valid EmailCertificationRequestDto requestBody
            ) {
        ResponseEntity<? super EmailCertificationResponseDto> response = authService.emailCertification(requestBody);
        return response;
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("Test endpoint accessed");
        return "Test successful";
    }

    @PostMapping("/check-certification")
    public ResponseEntity<?super CheckCertificationResponseDto> checkCertification (
            @RequestBody @Valid CheckCertificationRequestDto requestBody
    ) {
        ResponseEntity<? super CheckCertificationResponseDto> response = authService.checkCertification(requestBody);
        return response;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?super SignUpResponseDto> signup(
            @RequestBody @Valid SignUpRequestDto requestBody
            ) {
        ResponseEntity<?super SignUpResponseDto> response = authService.signUp(requestBody);
        return response;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?super SignInResponseDto> signIn(
            @RequestBody @Valid SignInRequestDto requestBody
            ) {
        ResponseEntity<?super SignInResponseDto> response = authService.signIn(requestBody);
        return response;
    }
}
