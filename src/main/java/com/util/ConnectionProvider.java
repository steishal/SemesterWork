package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
    private static ConnectionProvider instance; // Единственный экземпляр Singleton
    private Connection connection;

    // URL подключения к базе данных
    private static final String DB_URL = "jdbc:mysql://localhost:3306";
    private static final String DB_USERNAME = "anastasia";
    private static final String DB_PASSWORD = "201710202814003621";

    // Приватный конструктор для Singleton
    private ConnectionProvider() throws DbException {
        try {
            // Регистрация драйвера MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Создание соединения
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            throw new DbException("Unable to connect to the database", e);
        }
    }

    // Метод для получения единственного экземпляра ConnectionProvider
    public static ConnectionProvider getInstance() throws DbException {
        if (instance == null) {
            instance = new ConnectionProvider();
        }
        return instance;
    }

    // Метод для получения соединения
    public Connection getConnection() {
        return connection;
    }
}

