package io.github.zerthick.mailboxes.util;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SQLUtil {

    private static SqlService sql = Sponge.getServiceManager().provide(SqlService.class).get();

    private static DataSource getDataSource() throws SQLException {
        return sql.getDataSource("jdbc:h2:./config/mailboxes/data");
    }

    private static String prefix(String string) {
        return '\'' + string + '\'';
    }

    public static void createTable(String name, List<String> columns) throws SQLException {

        Connection connection = getDataSource().getConnection();

        PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " +
                name.toUpperCase() +
                "(" + columns.stream().map(String::toUpperCase).collect(Collectors.joining(", ")) + ")"
        );

        statement.executeUpdate();

        connection.close();
    }

    public static void dropTable(String name) throws SQLException {

        Connection connection = getDataSource().getConnection();

        PreparedStatement statement = connection.prepareStatement("DROP TABLE " + name.toUpperCase());

        statement.executeUpdate();

        connection.close();
    }

    public static void select(String tableName, Consumer<ResultSet> consumer) throws SQLException {

        ResultSet resultSet;

        Connection connection = getDataSource().getConnection();

        PreparedStatement statement = connection.prepareStatement("SELECT * " +
                " FROM " + tableName.toUpperCase()
        );

        resultSet = statement.executeQuery();

        consumer.accept(resultSet);

        connection.close();
    }

    public static void select(String tableName, String primaryKey, String primaryKeyValue, Consumer<ResultSet> consumer) throws SQLException {

        ResultSet resultSet;

        Connection connection = getDataSource().getConnection();

        PreparedStatement statement = connection.prepareStatement("SELECT * " +
                " FROM " + tableName.toUpperCase() +
                " WHERE " + primaryKey + "=" + prefix(primaryKeyValue)
        );

        resultSet = statement.executeQuery();

        consumer.accept(resultSet);

        connection.close();
    }

    public static void merge(String tableName, String primaryKey, String primaryKeyValue, List<String> values) throws SQLException {

        Connection connection = getDataSource().getConnection();

        PreparedStatement statement = connection.prepareStatement("MERGE INTO " + tableName.toUpperCase() +
                " KEY(" + primaryKey + ")" +
                "VALUES(" + prefix(primaryKeyValue) + ", " +
                values.stream().map(SQLUtil::prefix).collect(Collectors.joining(", ")) +
                ")"
        );

        statement.executeUpdate();

        connection.close();
    }

    public static void mergeMap(String tableName, String primaryKey, Map<String, List<String>> primaryKeyValueMap) throws SQLException {

        Connection connection = getDataSource().getConnection();

        PreparedStatement statement = connection.prepareStatement("MERGE INTO " + tableName.toUpperCase() +
                " KEY(" + primaryKey + ")" +
                " VALUES(" + primaryKeyValueMap.entrySet().stream()
                .map(stringListEntry -> prefix(stringListEntry.getKey().toUpperCase()) + ", " + stringListEntry.getValue().stream().map(SQLUtil::prefix).collect(Collectors.joining(", ")))
                .collect(Collectors.joining("), (")) +
                ")"
        );
        statement.executeUpdate();

        connection.close();
    }
}
