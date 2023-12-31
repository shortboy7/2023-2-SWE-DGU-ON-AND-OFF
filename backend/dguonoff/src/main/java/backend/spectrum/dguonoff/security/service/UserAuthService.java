package backend.spectrum.dguonoff.security.service;

import backend.spectrum.dguonoff.domain.user.dto.LoginRequest;
import backend.spectrum.dguonoff.domain.user.dto.LoginResponse;
import backend.spectrum.dguonoff.domain.user.dto.SignUpRequest;
import backend.spectrum.dguonoff.dao.User;
import backend.spectrum.dguonoff.dao.model.Role;
import backend.spectrum.dguonoff.domain.user.exception.UserDuplicateException;
import backend.spectrum.dguonoff.domain.user.repository.UserRepository;
import backend.spectrum.dguonoff.global.statusCode.ErrorCode;
import backend.spectrum.dguonoff.security.auth.jwt.CustomPasswordAuthenticationToken;
import backend.spectrum.dguonoff.security.auth.jwt.JwtAuthToken;
import backend.spectrum.dguonoff.security.auth.jwt.JwtAuthTokenProvider;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtAuthTokenProvider tokenProvider;

    public LoginResponse login(LoginRequest dto) throws AuthenticationException {
        CustomPasswordAuthenticationToken token = new CustomPasswordAuthenticationToken(
                dto.getId(), dto.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(token);
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findAny().orElseThrow(() -> new BadCredentialsException("Role 정보가 없습니다."));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = createToken((CustomPasswordAuthenticationToken) authentication);
        return new LoginResponse(jwtToken, role);
    }

    public void signUp(SignUpRequest dto) throws UserDuplicateException {
        Optional<User> optionalUser = userRepository.findById(dto.getId());
        if (optionalUser.isPresent())
            throw new UserDuplicateException(ErrorCode.USER_ID_DUPLICATE);
        User newUser = new User(dto.getId(),dto.getSid(), dto.getPassword(), Role.NORMAL, dto.getName(), dto.getMajor(), dto.getEmail());
        userRepository.save(newUser);
    }

    private String createToken(CustomPasswordAuthenticationToken token) {
        Map<String, String> claims = Map.of(
                "id", token.getId(),
                "name", token.getName(),
                "role", token.getRole()
        );

        JwtAuthToken jwtAuthToken = tokenProvider.createAuthToken(
                token.getPrincipal().toString(),
                token.getAuthorities().iterator().next().getAuthority(),
                claims
        );
        return jwtAuthToken.getToken();
    }
}
