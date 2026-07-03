package com.kubperf.kubernetesperformanceback.services;

import com.kubperf.kubernetesperformanceback.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class SpotifyService {

    @Autowired
    public User user;

    public Map<String, Object> tempPlaylists;

    private final static String API_PREFIX = "https://api.spotify.com/v1/";
    private final static String USER = "adamzebr";
    private final static String ARTIST = "5LHRHt1k9lMyONurDHEdrp";
    private final static String ALBUM = "6hHIX3lfGKnZ2ji41YZMVV";
    private final static ParameterizedTypeReference<Map<String, Object>> MAP_RESPONSE =
            new ParameterizedTypeReference<>() {};

    private final RestTemplate restTemplate;

    public SpotifyService() {
        this.restTemplate = new RestTemplate();
    }

    public Map<String, Object> makeRequest(String url) {
        //create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());

        String urlTemplate = UriComponentsBuilder
                .fromUriString(url)
                .build().toUriString();

        HttpEntity<?> request = new HttpEntity<>(null, headers);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(urlTemplate, HttpMethod.GET, request, MAP_RESPONSE);

        System.out.println(response.getBody());
        return response.getBody();
    }

    public Map<String, Object> fetchPlaylists() {
        String url = API_PREFIX + "users/" + USER + "/playlists";
        return makeRequest(url);
    }

    public Map<String, Object> fetchArtistAlbums() {
        String url = API_PREFIX + "artists/" + ARTIST + "/albums";
        return makeRequest(url);
    }

    public Map<String, Object> fetchAlbumTracks() {
        String url = API_PREFIX + "albums/" + ALBUM + "/tracks";
        return makeRequest(url);
    }

    public Map<String, Object> fetchOrSavePlaylists() {
        String url = API_PREFIX + "users/" + USER + "/playlists";
        this.tempPlaylists = makeRequest(url);
        return this.tempPlaylists;
    }
    public Map<String, Object> fetchOrSavePlaylistsNoDelay() {
        if (tempPlaylists != null) {
            return tempPlaylists;
        } else {
            String url = API_PREFIX + "users/" + USER + "/playlists";
            this.tempPlaylists = makeRequest(url);
            return this.tempPlaylists;
        }
    }

}
