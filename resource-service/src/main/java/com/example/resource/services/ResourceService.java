package com.example.resource.services;


import com.example.resource.entity.Resource;
import com.example.resource.models.SongDto;
import com.example.resource.repo.ResourceRepository;
import com.example.resource.utils.MetadataExtractor;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static com.example.resource.controllers.ResourceController.CONTENT_TYPE;

@Service
public class ResourceService {
    public static final String INVALID_FILE_FORMAT_S_ONLY_MP_3_FILES_ARE_ALLOWED = "Invalid file format: %s. Only MP3 files are allowed";
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final ResourceRepository resourceRepository;
    private final SongServiceClient songServiceClient;
    private final MetadataExtractor metadataExtractor;

    @Autowired
    public ResourceService(ResourceRepository resourceRepository, SongServiceClient songServiceClient, MetadataExtractor metadataExtractor) {
        this.resourceRepository = resourceRepository;
        this.songServiceClient = songServiceClient;
        this.metadataExtractor = metadataExtractor;
    }

    private static List<Long> checkStringConvertToLongs(String ids) throws InvalidKeyException {
        List<Long> idList = new ArrayList<>();
        for (String s : ids.split(",")) {
            long parseLong;
            try {
                parseLong = Long.parseLong(s);
            } catch (NumberFormatException e) {
                throw new InvalidKeyException("Invalid ID format: '%s'. Only positive integers are allowed".formatted(s));
            }
            idList.add(parseLong);
        }
        return idList;
    }

    private void extractMetadataAndSaveIt(Metadata metadata, Long id) {
        SongDto songDto = new SongDto(id, metadata.get("title"), metadata.get("xmpDM:artist"), metadata.get("xmpDM:album"), metadata.get("xmpDM:duration"), metadata.get("xmpDM:releaseDate"));
        songServiceClient.createSong(songDto);
    }

    public Resource saveResource(byte[] body) throws TikaException {
        logger.info("Saving RAW resource...");
        Resource resource = new Resource();
        resource.setData(body);
        Metadata metadata = checkDataExtractMetaData(resource);
        resource = resourceRepository.save(resource);
        logResourceData(resource);
        extractMetadataAndSaveIt(metadata, resource.getId());
        logger.info("SongDto metadata created for resource ID: {}", resource.getId());
        return resource;
    }

    private Metadata checkDataExtractMetaData(Resource resource) throws TikaException {
        Tika tika = new Tika();
        String type = tika.detect(resource.getData());
        if (!type.equalsIgnoreCase(CONTENT_TYPE)) {
            throw new TikaException(INVALID_FILE_FORMAT_S_ONLY_MP_3_FILES_ARE_ALLOWED.formatted(type));
        }
        Metadata metadata;
        try {
            metadata = metadataExtractor.extractMetadata(resource.getData());
            logger.info("SongDto metadata created {}", metadata);

        } catch (IOException | SAXException | TikaException e) {
            throw new TikaException(INVALID_FILE_FORMAT_S_ONLY_MP_3_FILES_ARE_ALLOWED.formatted(type));
        }
        if (metadata == null) {
            throw new TikaException(INVALID_FILE_FORMAT_S_ONLY_MP_3_FILES_ARE_ALLOWED.formatted(type));
        }
        return metadata;
    }

    private void logResourceData(Resource resource) {
        logger.info("Resource saved with ID: {}", resource.getId());
    }

    public Resource getResource(Long id) throws NoSuchElementException {
        Resource resource = resourceRepository.findById(id).orElse(null);
        if (resource == null) {
            throw new NoSuchElementException(String.format("Resource with ID=%d not found", id));
        }
        return resource;
    }

    public List<Long> deleteResources(String ids) throws InvalidKeyException {
        if (ids.length() > 200) {
            throw new InvalidKeyException("CSV string is too long: received %d characters, maximum allowed is 200".formatted(ids.length()));
        }
        List<Long> idList = checkStringConvertToLongs(ids);
        ArrayList<Long> idRes = new ArrayList<>();
        for (Long resourceId : idList) {
            if (resourceRepository.existsById(resourceId)) {
                logger.debug("Deleting resource with ID: {}", resourceId);
                resourceRepository.deleteById(resourceId);
                logger.info("Resource with ID: {} deleted", resourceId);
                idRes.add(resourceId);
            } else {
                logger.warn("Resource not found with ID: {}", resourceId);
            }
        }
        songServiceClient.deleteSongs(ids);
        return idRes;
    }


}