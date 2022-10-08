package com.task.readwritesource.readwritesourceimpl;

import com.task.readwritesource.DataReadWriteSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@RequiredArgsConstructor
@Getter
public class ConnectionReadWriteSource implements DataReadWriteSource<ResultSet> {
    private final Connection source;
    private final String table;

    /**
     * Reads db content to result set.
     *
     * @return Result set.
     */
    @Override
    @SneakyThrows
    public ResultSet getContent() {
        Statement statement = source.createStatement();
        return statement.executeQuery("SELECT * FROM " + table);
    }
}
