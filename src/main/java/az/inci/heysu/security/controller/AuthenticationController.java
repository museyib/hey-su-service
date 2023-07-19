package az.inci.heysu.security.controller;

import az.inci.heysu.security.domain.AuthenticationRequest;
import az.inci.heysu.security.domain.AuthenticationResponse;
import az.inci.heysu.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2")
public class AuthenticationController
{
    private final AuthenticationService authenticationService;

    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)
    {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
