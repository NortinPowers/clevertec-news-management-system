package by.clevertec.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import by.clevertec.auth.JwtRequest;
import by.clevertec.auth.domain.User;
import by.clevertec.auth.security.CustomUserDetails;
import by.clevertec.auth.token.JwtTokenManager;
import by.clevertec.auth.util.UserTestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtTokenManager jwtTokenManager;

    @Mock
    private AuthenticationManager authenticationManager;

    private final String username;
    private final JwtRequest request;
    private final User user;

    {
        user = UserTestBuilder.builder()
                .withRole(null)
                .build()
                .buildUser();
        username = user.getUsername();
        request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(user.getPassword());
    }

    @Test
    public void getTokenShouldReturnToken_whenCalled() {
        UserDetails userDetails = new CustomUserDetails(user);
        String expectedToken = "testToken";

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(null);
        given(userDetailsService.loadUserByUsername(username))
                .willReturn(userDetails);
        given(jwtTokenManager.generateJwtToken(userDetails))
                .willReturn(expectedToken);

        String actualToken = authService.getToken(request);

        assertEquals(expectedToken, actualToken);
    }

    @Test
    public void getTokenShouldThrowBadCredentialsException_whenUserNotAuthentication() {
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willThrow(new BadCredentialsException("not matter"));

        assertThrows(BadCredentialsException.class, () -> authService.getToken(request));

        verify(authenticationManager, atLeastOnce()).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(userDetailsService);
        verifyNoInteractions(jwtTokenManager);
    }

    @Test
    public void getTokenShouldThrowUsernameNotFoundException_userNotFound() {
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(null);
        given(userDetailsService.loadUserByUsername(username))
                .willThrow(new UsernameNotFoundException("nor matter"));

        assertThrows(UsernameNotFoundException.class, () -> authService.getToken(request));

        verifyNoInteractions(jwtTokenManager);
    }
}
