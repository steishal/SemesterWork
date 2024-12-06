package com.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class ConnectionProvider {
    private static ConnectionProvider instance;

    private Stack<Connection> connections; // Пул свободных соединений
    private Set<Connection> usedConnections; // Занятые соединения

    private static final int MAX_CONNECTIONS = 10; // Максимальное количество соединений

    private static final String DB_URL = "jdbc:mysql://localhost:3306/webapp";
    private static final String DB_USERNAME = "anastasia";
    private static final String DB_PASSWORD = "201710202814003621";

    // Приватный конструктор
    private ConnectionProvider() {
        connections = new Stack<>();
        usedConnections = new HashSet<>();

        try {
            // Регистрация драйвера MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Создание пула соединений
            for (int i = 0; i < MAX_CONNECTIONS; i++) {
                Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                connections.push(connection);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Метод для получения единственного экземпляра
    public static synchronized ConnectionProvider getInstance() {
        if (instance == null) {
            synchronized (ConnectionProvider.class) {
                if (instance == null) {
                    instance = new ConnectionProvider();
                }
            }
        }
        return instance;
    }

    // Получение соединения из пула
    public synchronized Connection getConnection() throws SQLException {
        Connection connection;
        try {
            connection = connections.pop(); // Забираем соединение из пула
            usedConnections.add(connection); // Помещаем его в список занятых
        } catch (EmptyStackException e) {
            // Если пул пуст, создаём новое соединение
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        }
        return connection;
    }

    // Освобождение соединения
    public synchronized void releaseConnection(Connection connection) {
        if (usedConnections.remove(connection)) {
            connections.push(connection); // Возвращаем соединение в пул
        }
    }

    // Закрытие всех соединений
    public void destroy() {
        for (Connection connection : usedConnections) {
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

