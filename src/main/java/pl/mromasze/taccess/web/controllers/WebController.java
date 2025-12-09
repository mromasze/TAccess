package pl.mromasze.taccess.web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @GetMapping("/")
    public String home() {
        return "TAccess REST API - Use /api endpoints";
    }
    
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}