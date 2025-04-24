package org.example.models.mappers;

import org.example.entity.Song;
import org.example.models.SongDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SongMapper {
    SongDto toSongDto(Song song);

    @Mapping(target = "resourceId", source = "songDto.id")
    @Mapping(target = "id", expression = "java(null)")
    Song toSong(SongDto songDto);
}
