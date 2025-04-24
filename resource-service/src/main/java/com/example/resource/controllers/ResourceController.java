package com.example.resource.controllers;


import com.example.resource.entity.Resource;
import com.example.resource.services.ResourceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidKeyException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/resources")
@Validated
public class ResourceController {
    public static final String CONTENT_TYPE = "audio/mpeg";
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Long>> uploadResource(HttpEntity<byte[]> requestEntity) throws TikaException {
        logger.info("Received raw byte upload request");
        Resource resource = resourceService.saveResource(requestEntity.getBody());
        logger.info("Resource uploaded successfully with ID: {}", resource.getId());
        return ResponseEntity.ok().body(Map.of("id", resource.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getResource(@PathVariable @Valid @Min(1) Long id) throws NoSuchElementException {
        logger.info("Received request to get resource with ID: {}", id);
        Resource resource = resourceService.getResource(id);
        logger.info("Resource retrieved successfully with ID: {}", id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(CONTENT_TYPE)).body(resource.getData());
    }

    @DeleteMapping
    public ResponseEntity<Map<String, List<Long>>> deleteResources(@RequestParam("id") String ids) throws InvalidKeyException {
        logger.info("Received request to delete resources with IDs: {}", ids);
        List<Long> idResult = resourceService.deleteResources(ids);
        logger.info("Resources deleted successfully with IDs: {}", ids);
        return ResponseEntity.ok().body(Map.of("ids", idResult));
    }
}