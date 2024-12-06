package com.dao;

import com.models.User;
import com.utils.ConnectionProvider;
import com.utils.DbException;
import com.utils.PasswordUtils;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    // Провайдер соединения с базой данных, обеспечивающий доступ к Connection
    private final ConnectionProvider connectionProvider;

    // Конструктор принимает ConnectionProvider
    public UserDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    // Получение пользователя по имени
    public User getUserByUsername(String username) throws DbException {
        // SQL-запрос для выбора пользователя по имени
        String sql = "SELECT * FROM Users WHERE name = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try  {
            connection = connectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            // Устанавливаем параметр имени в SQL-запросе
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Если пользователь найден, преобразуем результат в объект User
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            // Генерируем пользовательское исключение в случае ошибки
            throw new DbException("Error while fetching user by username", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Логируем ошибку закрытия
                }
            }
            // Возвращаем соединение в пул, если оно было получено
            if (connection != null) {
                connectionProvider.releaseConnection(connection);
            }
        }
        // Возвращаем null, если пользователь не найден
        return null;
    }




    public User getUserByEmail(String email) throws DbException {
        String sql = "SELECT * FROM Users WHERE email = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            // Устанавливаем параметр имени в SQL-запросе
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Если пользователь найден, преобразуем результат в объект User
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            // Генерируем пользовательское исключение в случае ошибки
            throw new DbException("Error while fetching user by username", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Логируем ошибку закрытия
                }
            }
            // Возвращаем соединение в пул, если оно было получено
            if (connection != null) {
                connectionProvider.releaseConnection(connection);
            }
        }
        // Возвращаем null, если пользователь не найден
        return null;
    }



    // Сохранение нового пользователя
    public void saveUser(User user) throws DbException {
        System.out.println(user.getUsername());
        System.out.println(user.getVkLink());
        System.out.println(user.getTgLink());
        System.out.println(user.getEmail());
        System.out.println(user.getPhoneNumber());
        System.out.println(user.getPassword());
        // SQL-запрос для вставки нового пользователя
        String sql = "INSERT INTO Users (name, vk_link, telegram_link, email, phone_number, password_hash) VALUES (?, ?, ?, ?, ?, ?)";

        Connection connection = null; // Объявляем соединение
        PreparedStatement preparedStatement = null;
        try {
            // Получаем соединение из пула
            connection = connectionProvider.getConnection();

            // Готовим SQL-запрос
            preparedStatement = connection.prepareStatement(sql);

            // Устанавливаем параметры запроса
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getVkLink());
            preparedStatement.setString(3, user.getTgLink());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPhoneNumber());
            preparedStatement.setString(6, user.getPassword());

            // Выполняем запрос на добавление
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Error saving user", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Логируем ошибку закрытия
                }
            }
            // Возвращаем соединение в пул, если оно было получено
            if (connection != null) {
                connectionProvider.releaseConnection(connection);
            }
        }
    }



    // Приватный метод для преобразования ResultSet в объект User
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        // Создаем объект User и заполняем его данными из ResultSet
        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setUsername(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password_hash"));
        user.setVkLink(resultSet.getString("vk_link"));
        user.setTgLink(resultSet.getString("telegram_link"));
        user.setEmail(resultSet.getString("email"));
        user.setPhoneNumber(resultSet.getString("phone_number"));
        return user;
    }



    // Метод для получения пользователя по ID
    public User getUserById(int id) throws DbException {
        // SQL-запрос для поиска пользователя по идентификатору
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            // Устанавливаем ID в запросе
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Если пользователь найден, преобразуем результат в объект User
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            // Обрабатываем исключение
            throw new DbException("Error fetching user by ID", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Логируем ошибку закрытия
                }
            }
            // Возвращаем соединение в пул, если оно было получено
            if (connection != null) {
                connectionProvider.releaseConnection(connection);
            }
        }
        // Если пользователь не найден, возвращаем null
        return null;
    }



    // Метод для получения всех пользователей
    public List<User> getAllUsers() throws DbException {
        // SQL-запрос для получения всех пользователей
        String sql = "SELECT * FROM Users";
        Connection connection = null;
        List<User> users = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        try{
            connection = connectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Итерация по результатам и преобразование каждой записи в объект User
            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            // Обрабатываем ошибку
            throw new DbException("Error fetching all users", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Логируем ошибку закрытия
                }
            }
            // Возвращаем соединение в пул, если оно было получено
            if (connection != null) {
                connectionProvider.releaseConnection(connection);
            }
        }
        return users;
    }



    // Метод для обновления информации о пользователе
    public void updateUser(User user) throws DbException {
        // SQL-запрос для обновления данных пользователя
        String sql = "UPDATE Users SET name = ?, vk_link = ?, telegram_link = ?, email = ?, phone_number = ?, password_hash = ? WHERE user_id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            // Хэшируем пароль перед обновлением
            String hashedPassword = PasswordUtils.hashPassword(user.getPassword());

            // Устанавливаем параметры запроса
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getVkLink());
            preparedStatement.setString(3, user.getTgLink());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPhoneNumber());
            preparedStatement.setString(6, hashedPassword);
            preparedStatement.setInt(7, user.getId());

            // Выполняем обновление
            preparedStatement.executeUpdate();

        } catch (SQLException | NoSuchAlgorithmException e) {
            // Генерируем исключение в случае ошибки
            throw new DbException("Error updating user", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Логируем ошибку закрытия
                }
            }
            // Возвращаем соединение в пул, если оно было получено
            if (connection != null) {
                connectionProvider.releaseConnection(connection);
            }
        }
    }



    // Метод для удаления пользователя по ID
    public void deleteUser(int id) throws DbException {
        // SQL-запрос для удаления пользователя по идентификатору
        String sql = "DELETE FROM Users WHERE user_id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = connectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            // Устанавливаем ID для удаления
            preparedStatement.setInt(1, id);

            // Выполняем запрос
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            // Обрабатываем ошибку
            throw new DbException("Error deleting user", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Логируем ошибку закрытия
                }
            }
            // Возвращаем соединение в пул, если оно было получено
            if (connection != null) {
                connectionProvider.releaseConnection(connection);
            }
        }
    }



    // Метод для получения пользователя по имени и паролю
    public User getUserByUsernameAndPassword(String username, String password) throws DbException {
        // SQL-запрос для поиска пользователя по имени
        String sql = "SELECT user_id, name, password_hash, email, phone_number, vk_link, telegram_link " +
                "FROM Users WHERE name = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            // Устанавливаем имя пользователя в запросе
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Если пользователь найден, проверяем пароль
                if (resultSet.next()) {
                    // Получаем хэш пароля из базы данных
                    String storedHash = resultSet.getString("password_hash");

                    // Проверяем совпадение введенного пароля с хэшем
                    if (PasswordUtils.verifyPassword(password, storedHash)) {
                        return mapResultSetToUser(resultSet);
                    } else {
                        return null; // Неверный пароль
                    }
                }
            }
        } catch (SQLException e) {
            // Генерируем исключение в случае ошибки
            throw new DbException("Error while getting user by username and password", e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace(); // Логируем ошибку закрытия
                }
            }
            // Возвращаем соединение в пул, если оно было получено
            if (connection != null) {
                connectionProvider.releaseConnection(connection);
            }
        }
        // Возвращаем null, если пользователь не найден
        return null;
    }
}




