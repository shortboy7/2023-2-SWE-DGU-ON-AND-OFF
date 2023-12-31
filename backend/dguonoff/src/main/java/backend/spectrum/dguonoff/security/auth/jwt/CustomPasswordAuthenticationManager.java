package backend.spectrum.dguonoff.security.auth.jwt;

import backend.spectrum.dguonoff.dao.User;
import backend.spectrum.dguonoff.domain.user.repository.UserRepository;
import backend.spectrum.dguonoff.global.statusCode.ErrorCode;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * 로그인 시, DB와 연결되어 password 확인하고
 * 인증을 위한 토큰을 만드는 작업을 하는 객체
 */
@Component
@RequiredArgsConstructor
public class CustomPasswordAuthenticationManager implements AuthenticationProvider {
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<User> optionalUser = userRepository.findById(authentication.getPrincipal().toString());
        if (optionalUser.isEmpty()) {
            throw new BadCredentialsException(ErrorCode.NOT_EXIST_USER.getMessage());
        }
        User user = optionalUser.get();
        if (!authentication.getCredentials().equals(user.getPassword())) {
            throw new BadCredentialsException(ErrorCode.USER_PASSWORD_NOT_MATCHED.getMessage());
        }
        CustomPasswordAuthenticationToken token = new CustomPasswordAuthenticationToken(
                user.getId(), user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString()))
        );
        token.setId(user.getId());
        token.setName(user.getName());
        token.setRole(user.getRole().toString());
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(CustomPasswordAuthenticationToken.class);
    }
}
