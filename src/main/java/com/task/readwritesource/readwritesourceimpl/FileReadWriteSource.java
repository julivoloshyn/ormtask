package com.task.readwritesource.readwritesourceimpl;

import com.task.readwritesource.DataReadWriteSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Getter
public class FileReadWriteSource implements DataReadWriteSource<String> {
    private final File source;

    /**
     * Reads file content to string.
     *
     * @return Content from file.
     */
    @Override
    @SneakyThrows
    public String getContent() {
        return FileUtils.readFileToString(source, StandardCharsets.UTF_8);
    }
}
