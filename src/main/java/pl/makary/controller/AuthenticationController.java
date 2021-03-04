package pl.makary.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.makary.model.AuthenticationRequest;
import pl.makary.model.AuthenticationResponse;
import pl.makary.service.impl.UserDetailsServiceImpl;
import pl.makary.util.JwtUtil;

@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtUtil jwtUtil;

    public AuthenticationController(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsServiceImpl, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }catch (AuthenticationException e){
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsServiceImpl
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
