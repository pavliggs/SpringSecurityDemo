package com.khovaylo.springsecuritydemo.rest;

import com.khovaylo.springsecuritydemo.model.User;
import com.khovaylo.springsecuritydemo.repository.UserRepository;
import com.khovaylo.springsecuritydemo.security.jwt.JwtProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pavel Khovaylo
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public AuthenticationRestController(AuthenticationManager authenticationManager,
                                        UserRepository userRepository,
                                        JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDto request) {
        String userEmail = request.getEmail();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail,
                                                request.getPassword()));
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
            String token = jwtProvider.createToken(userEmail, user.getRole().name());
            Map<Object, Object> response = new HashMap<>();
            response.put("email", userEmail);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException ex) {
            return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}