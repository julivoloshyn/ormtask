package com.task.parsingstrategy;

import com.task.Table;
import com.task.readwritesource.DataReadWriteSource;

public interface ParsingStrategy <T extends DataReadWriteSource> {
    Table parseToTable(T content);
}
