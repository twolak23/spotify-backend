package com.kubperf.kubernetesperformanceback.controllers;

import com.kubperf.kubernetesperformanceback.services.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SpotifyController {

        @Autowired
        public SpotifyService spotifyService;

        @GetMapping("/playlists")
        public Map<String, Object> fetchPlaylists() {
            return spotifyService.fetchPlaylists();
        }

        @GetMapping("/artist/albums")
        public Map<String, Object> fetchArtistAlbums() {
                return spotifyService.fetchArtistAlbums();
        }

        @GetMapping("/album/tracks")
        public Map<String, Object> fetchAlbumTracks() {
                return spotifyService.fetchAlbumTracks();
        }

        @GetMapping("/fetchPlaylists")
        public Map<String, Object> fetchOrSavePlaylists() {
                return spotifyService.fetchOrSavePlaylists();
        }

        @GetMapping("/fetchPlaylistsNoDelay")
        public Map<String, Object> fetchOrSavePlaylistsNoDelay() {
                return spotifyService.fetchOrSavePlaylistsNoDelay();
        }

}
