package musico.services.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/test")
@RequiredArgsConstructor
public class TestController {

    @RequestMapping(path = "/prova")
    @PreAuthorize("hasAuthority('USER')")
    public String test() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            log.info("Principal: {}", authentication.getPrincipal());
            log.info("Authorities: {}", authentication.getAuthorities());
            log.info("Credentials: {}", authentication.getCredentials());
            log.info("Details: {}", authentication.getDetails());
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
        return "Test controller is up and running";
    }
}
