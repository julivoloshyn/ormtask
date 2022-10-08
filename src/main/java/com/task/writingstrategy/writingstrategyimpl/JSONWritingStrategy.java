package com.task.writingstrategy.writingstrategyimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.task.writingstrategy.WritingStrategy;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class JSONWritingStrategy implements WritingStrategy {
    private File file;

    /**
     * Parses list to json format.
     *
     * @param objects List of content.
     */
    @SneakyThrows
    @Override
    public void write(List<?> objects) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        String json = mapper.writeValueAsString(objects);
        Files.write(Path.of(file.getPath()), json.getBytes());
    }
}
