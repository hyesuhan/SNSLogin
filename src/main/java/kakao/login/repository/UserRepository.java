package kakao.login.repository;

import kakao.login.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    boolean existsByUserId(String userId);
    UserEntity findByUserId(String userId);
}
