package org.example;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.example.entity.Song;
import org.example.models.SongDto;
import org.example.models.mappers.SongMapper;
import org.example.repo.SongRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SongService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final SongRepository songRepository;
    private final SongMapper songMapper;

    @Autowired
    public SongService(SongRepository songRepository, SongMapper songMapper) {
        this.songRepository = songRepository;
        this.songMapper = songMapper;
    }

    private static List<Long> checkStringConvertToLongs(String ids) {
        List<Long> idList = new ArrayList<>();
        for (String s : ids.split(",")) {
            long parseLong;
            try {
                parseLong = Long.parseLong(s);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Invalid ID format: '%s'. Only positive integers are allowed".formatted(s));
            }
            idList.add(parseLong);
        }
        return idList;
    }

    @Transactional
    public Song createSong(@Valid SongDto songDto) throws KeyAlreadyExistsException {
        Song song = songMapper.toSong(songDto);
        Song createdSong = song;
        try {
            List<Song> checkRes = songRepository.findByResourceId(song.getResourceId());
            if (checkRes == null || checkRes.isEmpty()) {
                logger.info("save to songRepository entity {}", song);
                createdSong = songRepository.saveAndFlush(song);
            } else {
                logger.warn("We already have data for this song in the database...");
                throw new KeyAlreadyExistsException("Metadata for resource ID=%d already exists".formatted(song.getResourceId()));
            }
        } catch (OptimisticLockingFailureException e) {
            logger.info("OptimisticLockingFailureException...", e);
        }
        return createdSong;
    }

    public SongDto getSong(Long id) {
        logger.info("Fetching song metadata with ID: {}", id);
        Optional<Song> song = songRepository.findById(id);
        if (song.isPresent()) {
            return songMapper.toSongDto(song.get());
        } else {
            throw new NoSuchElementException(String.format("Song metadata for ID=%d not found", id));
        }
    }

    @Transactional
    public List<Long> deleteSongs(String ids) {
        if (ids.length() > 200) {
            throw new NumberFormatException("CSV string is too long: received %d characters, maximum allowed is 200".formatted(ids.length()));
        }
        List<Long> idList = checkStringConvertToLongs(ids);
        ArrayList<Long> idRes = new ArrayList<>();
        for (Long id : idList) {
            if (songRepository.existsById(id)) {
                logger.info("Deleting song metadata with ID: {}", id);
                songRepository.deleteById(id);
                logDeletion(id);
                idRes.add(id);
            } else {
                logger.warn("Song metadata not found with ID: {}", id);
            }
        }
        return idRes;
    }

    private void logDeletion(Long id) {
        logger.info("Song metadata with ID: {} deleted", id);
    }
}