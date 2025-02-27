package kakao.login.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kakao.login.entity.UserEntity;
import kakao.login.provider.JwtProvider;
import kakao.login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = parseBearerToken(request);
            if(token == null) {
                System.out.println("Invalid token");
                filterChain.doFilter(request, response);
                return;
            }

            String userId = jwtProvider.validate(token);
            if(userId == null) {
                System.out.println("User not found for userId: " + userId);
                filterChain.doFilter(request, response);
                return;
            }

            System.out.println("검증은 완료됨");

            UserEntity userEntity = userRepository.findByUserId(userId);
            String role = userEntity.getRole(); //role: ROLE_USER, ROLE_ADMIN

            // ROLE_USER, ROLE_DEVELOPER
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role));

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

            AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);

            System.out.println("User authenticated: " + userId + " with role: " + role);

        } catch (Exception e){
            System.out.println("???");
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        boolean hasAuthorization = StringUtils.hasText(authorization);
        if(!hasAuthorization) return null;

        boolean isBearer= authorization.startsWith("Bearer ");
        if(!isBearer) return  null;

        String token = authorization.substring(7);
        return token;
    }
}
