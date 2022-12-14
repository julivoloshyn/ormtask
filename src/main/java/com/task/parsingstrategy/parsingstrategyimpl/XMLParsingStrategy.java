package com.task.parsingstrategy.parsingstrategyimpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.task.parsingstrategy.table.Table;
import com.task.parsingstrategy.ParsingStrategy;
import com.task.readwritesource.readwritesourceimpl.FileReadWriteSource;
import lombok.SneakyThrows;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class XMLParsingStrategy implements ParsingStrategy<FileReadWriteSource> {


    /**
     * Puts xml content to Table.
     *
     * @param content File content.
     * @return Table.
     */
    @SneakyThrows
    @Override
    public Table parseToTable(FileReadWriteSource content) {
        XmlMapper mapper = new XmlMapper();
        JsonNode tree = mapper.readTree(content.getContent());
        Map<Integer, Map<String, String>> result = buildTable(tree);

        return new Table(result);
    }

    /**
     * Builds filled map with values from json file.
     *
     * @param tree Array json node.
     * @return Map (key, Map(key, value)).
     */
    private Map<Integer, Map<String, String>> buildTable(JsonNode tree) {
        Map<Integer, Map<String, String>> map = new LinkedHashMap<>();
        int index = 0;

        for (JsonNode each : tree) {
            for (JsonNode node : each) {
                Map<String, String> item = buildRow(node);
                map.put(index, item);
                index++;
            }
        }

        return map;
    }

    /**
     * Puts each row from json file to a map.
     *
     * @param node Row in json file.
     * @return Map (key, value).
     */
    private Map<String, String> buildRow(JsonNode node) {
        Map<String, String> item = new LinkedHashMap<>();
        Iterator<Map.Entry<String, JsonNode>> itr = node.fields();

        while (itr.hasNext()) {
            Map.Entry<String, JsonNode> next = itr.next();
            item.put(next.getKey(), next.getValue().textValue());
        }

        return item;
    }
}
