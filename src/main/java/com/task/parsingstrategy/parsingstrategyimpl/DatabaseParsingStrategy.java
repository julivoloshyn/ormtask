package com.task.parsingstrategy.parsingstrategyimpl;

import com.task.parsingstrategy.table.Table;
import com.task.parsingstrategy.ParsingStrategy;
import com.task.readwritesource.readwritesourceimpl.ConnectionReadWriteSource;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedHashMap;
import java.util.Map;

public class DatabaseParsingStrategy implements ParsingStrategy<ConnectionReadWriteSource> {

    /**
     * Puts content to table.
     *
     * @param content Content from db.
     * @return Table.
     */
    @Override
    public Table parseToTable(ConnectionReadWriteSource content) {
        ResultSet rs = content.getContent();
        Map<Integer, Map<String, String>> result = buildTable(rs);
        return new Table(result);
    }

    /**
     * Builds table from the result set.
     *
     * @param rs Result set.
     * @return Map (key, Map(key, value)).
     */
    @SneakyThrows
    private Map<Integer, Map<String, String>> buildTable(ResultSet rs) {
        ResultSetMetaData metadata = rs.getMetaData();

        Map<Integer, Map<String, String>> result = new LinkedHashMap<>();
        int rowId = 0;
        while (rs.next()) {
            Map<String, String> row = new LinkedHashMap<>();
            for (int index = 1; index < metadata.getColumnCount(); index++) {
                row.put(metadata.getColumnName(index), rs.getString(index));
            }
            result.put(rowId, row);
            rowId++;
        }

        return result;
    }
}
