package com.task.writingstrategy.writingstrategyimpl;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.task.writingstrategy.WritingStrategy;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@AllArgsConstructor
public class XMLWritingStrategy implements WritingStrategy {
    private File file;

    @SneakyThrows
    @Override
    public void write(List<?> objects) {
        XmlMapper mapper = new XmlMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        String xml = mapper.writeValueAsString(objects);
        Files.write(Path.of(file.getPath()), xml.getBytes());
    }
}
