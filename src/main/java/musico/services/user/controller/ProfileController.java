package musico.services.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping(path = "/user/profile")
public class ProfileController {
    @PostMapping(path = "/create")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public String createProfile(Principal principal) {
        log.info("Principal: {}", principal);
        return "Profile created";
    }

    @GetMapping(path = "/get")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public String getProfile(Principal principal, Authentication authentication) {
        log.info("Principal: {}", principal);
        return "Profile retrieved";
    }

}
