package com.spotifybackend.services;

import com.spotifybackend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthorizationService {

    @Autowired
    public User user;

    @Value("${app.auth.username}")
    private String username;
    @Value("${app.auth.password}")
    private String password;

    private final static String API_PREFIX = "https://accounts.spotify.com/";
    private final static String ACCESS_TOKEN = "access_token";
    private final static ParameterizedTypeReference<Map<String, Object>> MAP_RESPONSE =
            new ParameterizedTypeReference<>() {};

    private final RestTemplate restTemplate;


    public AuthorizationService() {
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Object> authorization() {
        String url = API_PREFIX + "api/token";

        //create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);

        //create body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        //create request
        HttpEntity<?> request = new HttpEntity<Object>(body, headers);

        // make a request
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.POST, request, MAP_RESPONSE);

        user.setToken(response.getBody().get(ACCESS_TOKEN).toString());

        return response.getBody();
    }
}
