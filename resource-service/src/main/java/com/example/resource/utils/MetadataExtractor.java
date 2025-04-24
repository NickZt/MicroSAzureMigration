package com.example.resource.utils;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class MetadataExtractor {

    public Metadata extractMetadata(byte[] fileData) throws IOException, SAXException, TikaException {
        InputStream input = new ByteArrayInputStream(fileData);
        Metadata metadata = new Metadata();
        BodyContentHandler handler = new BodyContentHandler();
        Mp3Parser parser = new Mp3Parser();
        parser.parse(input, handler, metadata, new ParseContext());

        // Convert duration from seconds to mm:ss format
        String duration = metadata.get("xmpDM:duration");
        if (duration != null) {
            int seconds = (int) Double.parseDouble(duration);
            String formattedDuration = String.format("%02d:%02d", seconds / 60, seconds % 60);
            metadata.set("xmpDM:duration", formattedDuration);
        }

        return metadata;
    }
}