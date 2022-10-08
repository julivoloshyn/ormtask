package com.task.parsingstrategy.parsingstrategyimpl;

import com.task.parsingstrategy.table.Table;
import com.task.parsingstrategy.ParsingStrategy;
import com.task.readwritesource.readwritesourceimpl.FileReadWriteSource;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CSVParsingStrategy implements ParsingStrategy<FileReadWriteSource> {

    public static final String DELIMITER = ", ";
    public static final String COMMENT = "--";

    /**
     * Puts content to Table.
     *
     * @param content File content.
     * @return Table.
     */
    @Override
    public Table parseToTable(FileReadWriteSource content) {
        List<String> lines = Arrays.asList(content.getContent().split(System.lineSeparator()));
        Map<Integer, String> mapping = buildMapping(lines.get(0));
        Map<Integer, Map<String, String>> result = buildTable(lines.subList(1, lines.size()), mapping);
        return new Table(result);
    }

    /**
     * Builds table from rows.
     *
     * @param lines List of lines.
     * @param mapping First line (headers).
     * @return Map (key, Map(key, value)).
     */
    private Map<Integer, Map<String, String>> buildTable(List<String> lines, Map<Integer, String> mapping) {
        Map<Integer, Map<String, String>> result = new LinkedHashMap<>();

        for (int index = 0; index < lines.size(); index++) {
            String line = lines.get(index);
            result.put(index, buildRow(mapping, line));
        }

        return result;
    }

    /**
     * Processes rows.
     *
     * @param mapping Row in json file.
     * @param line Row in json file.
     * @return Map (key, value).
     */
    private Map<String, String> buildRow(Map<Integer, String> mapping, String line) {
        Map<String, String> nameToValueMap = new LinkedHashMap<>();
        String[] rowItems = splitLine(line);

        for (int rowIndex = 0; rowIndex < rowItems.length; rowIndex++) {
            String value = rowItems[rowIndex];
            nameToValueMap.put(mapping.get(rowIndex), value);
        }

        return nameToValueMap;
    }

    /**
     * Processes first line and deletes comment.
     *
     * @param firstLine First line.
     * @return Map (key, value).
     */
    private Map<Integer, String> buildMapping(String firstLine) {
        Map<Integer, String> map = new LinkedHashMap<>();
        String[] array = splitLine(firstLine);

        for (int index = 0; index < array.length; index++) {
            String value = array[index];

            if (value.contains(COMMENT)) {
                value = value.split(COMMENT)[0];
            }
            map.put(index, value.trim());
        }

        return map;
    }


    /**
     * Splits lines by delimiter.
     *
     * @param line String.
     * @return Array of strings.
     */
    private static String[] splitLine(String line) {
        return line.split(DELIMITER);
    }
}
