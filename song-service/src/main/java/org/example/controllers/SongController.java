package org.example.controllers;

import jakarta.validation.Valid;
import org.example.SongService;
import org.example.entity.Song;
import org.example.models.SongDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/songs")
public class SongController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final SongService songService;

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Long>> createSong(@Valid @RequestBody SongDto songDto) {
        logger.info("Received request to create song metadata for resource ID: {} with song meta: {}", songDto.id(), songDto);
        Song createdSong = songService.createSong(songDto);
        logger.info("Song metadata created successfully with ID: {}", createdSong.getId());
        return ResponseEntity.ok().body(Map.of("id", createdSong.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDto> getSong(@PathVariable Long id) {
        logger.info("Received request to get song metadata with ID: {}", id);
        SongDto response = songService.getSong(id);
        logger.info("Song metadata retrieved successfully with ID: {}", id);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, List<Long>>> deleteSongs(@RequestParam("id") String ids) {
        logger.info("Received request to delete song metadata with IDs: {}", ids);
        List<Long> idResult = songService.deleteSongs(ids);
        logger.info("Song metadata deleted successfully with IDs: {}", idResult);
        return ResponseEntity.ok().body(Map.of("ids", idResult));
    }
}