package fr.liksi.shared.api;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class SecurityTestController {
    @GetMapping("/api/test")
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ROLE_LABO", "ROLE_MANAGER", "ROLE_ADMIN"})
    public @ResponseBody String test() {
        return "hello";
    }

}
