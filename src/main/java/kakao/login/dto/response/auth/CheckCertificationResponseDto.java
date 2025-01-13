package kakao.login.dto.response.auth;

import kakao.login.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class CheckCertificationResponseDto extends ResponseDto {

    private CheckCertificationResponseDto() {
        super();
    }

    public static ResponseEntity<CheckCertificationResponseDto> success() {
        CheckCertificationResponseDto responseDto = new CheckCertificationResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    public static ResponseEntity<ResponseDto> certificationFail() {
        ResponseDto responseDto = new ResponseDto();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
    }
}
