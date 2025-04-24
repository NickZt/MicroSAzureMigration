package com.example.resource.services;

import com.example.resource.models.SongDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SongServiceClient {
    private final RestTemplate restTemplate;
    private final String songServiceUrl;
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    public SongServiceClient(RestTemplate restTemplate, @Value("${song.service.url}") String songServiceUrl) {
        this.restTemplate = restTemplate;
        this.songServiceUrl = songServiceUrl;
    }

    public void createSong(SongDto songDto) {
        logger.info("Saving SongDto metadata using link:{}",songServiceUrl);
        restTemplate.postForObject(songServiceUrl, songDto, SongDto.class);
    }

    public void deleteSongsByResourceId(Long resourceId) {
        restTemplate.delete(songServiceUrl + "/by-resource?resourceId=" + resourceId);
    }

    public void deleteSongs(String ids) {
        restTemplate.delete(songServiceUrl + "?id="+ids);
    }
}