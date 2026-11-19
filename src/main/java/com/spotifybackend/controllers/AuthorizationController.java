package com.spotifybackend.controllers;

import com.spotifybackend.services.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthorizationController {

    @Autowired
    public AuthorizationService authorizationService;

    @GetMapping("/authorizationUser")
    public Map<String, Object> authorizationUser() {
        return authorizationService.authorization();
    }
}
