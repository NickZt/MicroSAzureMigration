package com.example.resource.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SongDto(Long id,
                      @NotBlank(message = "Song name is required")
                      @Size(min = 1, max = 100)
                      String name,
                      @NotBlank(message = "Artist name is required")
                      @Size(min = 1, max = 100)
                      String artist,
                      @NotBlank(message = "Album is required")
                      @Size(min = 1, max = 100)
                      String album,
                      @NotBlank(message = "Duration is required")
                      @Pattern(regexp = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Duration must be in mm:ss format with leading zeros")
                      String duration,
                      @NotBlank(message = "Year is required")
                      @Pattern(regexp = "^(19\\d{2}|20[0-9]{2})$", message = "Year must be between 1900 and 2099")
                      String year) {
}
