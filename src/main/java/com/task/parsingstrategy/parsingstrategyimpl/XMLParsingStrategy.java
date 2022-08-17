package com.task.parsingstrategy.parsingstrategyimpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.task.Table;
import com.task.parsingstrategy.ParsingStrategy;
import com.task.readwritesource.readwritesourceimpl.FileReadWriteSource;
import lombok.SneakyThrows;

public class XMLParsingStrategy implements ParsingStrategy<FileReadWriteSource> {
    @SneakyThrows
    @Override
    public Table parseToTable(FileReadWriteSource content) {
        XmlMapper mapper = new XmlMapper();
        JsonNode result = mapper.readTree(content.getContent());
        return null;
    }
}
