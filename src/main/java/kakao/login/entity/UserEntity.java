package kakao.login.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kakao.login.dto.request.auth.SignUpRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user")
@Table(name = "user")
public class UserEntity {
    @Id
    private String userId;
    private String password;
    private String email;
    private String type;
    private String role;

    public UserEntity(SignUpRequestDto dto) {
        this.userId = dto.getId();
        this.password = dto.getPassword();
        this.email = dto.getEmail();
        this.type = "app";
        this.role = "ROLE_USER";
    }

    public UserEntity(String userId, String email, String type) {
        this.userId = userId;
        this.password = "PassWord";
        this.email = email;
        this.type = type;
        this.role = "ROLE_USER";
    }
}
