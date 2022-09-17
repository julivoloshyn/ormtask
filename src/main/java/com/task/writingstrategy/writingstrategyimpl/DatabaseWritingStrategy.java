package com.task.writingstrategy.writingstrategyimpl;

import com.task.ModelSQLHelper;
import com.task.writingstrategy.WritingStrategy;

import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.task.Main.withConnection;

public class DatabaseWritingStrategy implements WritingStrategy {

    @SneakyThrows
    @Override
    public void write(List<?> objects) {

        withConnection(connection -> {

            for (Object objectToInsert : objects) {
                ModelSQLHelper helper = new ModelSQLHelper(ModelSQLHelper.collectMetaInformation(connection, objectToInsert));
                String sql = helper.buildSQL(objectToInsert);

                PreparedStatement preparedStatement = null;
                try {
                    preparedStatement = connection.prepareStatement(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                helper.bindArguments(objectToInsert, preparedStatement);
                try {
                    preparedStatement.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return null;
        });
    }
}
