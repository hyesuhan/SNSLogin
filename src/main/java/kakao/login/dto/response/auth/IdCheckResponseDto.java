package kakao.login.dto.response.auth;

import kakao.login.common.ResponseCode;
import kakao.login.common.ResponseMessage;
import kakao.login.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class IdCheckResponseDto extends ResponseDto {
    private IdCheckResponseDto() {
        super();
    }

    public static ResponseEntity<IdCheckResponseDto> success() {
        IdCheckResponseDto responseDto = new IdCheckResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    public static ResponseEntity<ResponseDto> duplicateId(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
