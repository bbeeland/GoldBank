package me.beeland.dunmoore.bank.handler;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandler {

    private HikariDataSource dataSource;
    private Connection connection;

    public DatabaseHandler(String host, int port, String database, String username, String password) {

        this.dataSource = new HikariDataSource();
        this.dataSource.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        this.dataSource.setUsername(username);
        this.dataSource.setPassword(password);

        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    public PreparedStatement prepareStatement(String statement) {
        try {
            return connection.prepareStatement(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getResults(PreparedStatement statement) {
        try {
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
