package com.kubperf.kubernetesperformanceback.controllers;

import com.kubperf.kubernetesperformanceback.services.AuthoriaztionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthorizationController {

    @Autowired
    public AuthoriaztionService authoriaztionService;

    @GetMapping("/authorizationUser")
    public Map<String, Object> authorizationUser() {
        return authoriaztionService.authorization();
    }
}
