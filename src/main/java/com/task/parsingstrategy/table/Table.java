package com.task.parsingstrategy.table;

import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;


@RequiredArgsConstructor
public class Table {

    public final Map<Integer, Map<String, String>> table;

    public int size() {
        return table.size();
    }

    /**
     * Gets row by index.
     *
     * @param row Some row from table.
     * @return Map(key, value).
     */
    public Map<String, String> getTableRowByIndex(int row) {
        Map<String, String> result = table.get(row);
        return result == null ? null : new LinkedHashMap<>(result);
    }

}
