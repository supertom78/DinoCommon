package fr.liksi.api.web;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/onlylabo")
    @Secured({"ROLE_LABO"})
    public String onlylabo() {
        return "ok";
    }

    @GetMapping("/onlylabooradmin")
    @Secured({"ROLE_LABO", "ROLE_ADMIN"})
    public String onlylabooradmin() {
        return "ok";
    }
}
