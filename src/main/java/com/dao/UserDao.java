package com.dao;

import com.models.User;
import com.util.ConnectionProvider;
import com.util.DbException;
import com.util.PasswordUtils;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final ConnectionProvider connectionProvider;

    // Конструктор принимает экземпляр ConnectionProvider
    public UserDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    // Метод для получения пользователя по имени пользователя
    public User getUserByUsername(String username) throws DbException {
        String sql = "SELECT * FROM Users WHERE name = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Error while fetching user by username", e);
        }
        return null; // Пользователь не найден
    }

    // Метод для сохранения пользователя
    public void saveUser(User user) throws DbException {
        String sql = "INSERT INTO Users (name, vk_link, telegram_link, email, phone_number, password_hash) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Хэшируем пароль перед сохранением
            String hashedPassword = PasswordUtils.hashPassword(user.getPassword());

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getVkLink());
            preparedStatement.setString(3, user.getTgLink());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPhoneNumber());
            preparedStatement.setString(6, hashedPassword);
            preparedStatement.executeUpdate();

        } catch (SQLException | NoSuchAlgorithmException e) {
            throw new DbException("Error saving user", e);
        }
    }

    // Метод для получения пользователя по ID
    public User getUserById(int id) throws DbException {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Error fetching user by ID", e);
        }
        return null;
    }

    // Метод для получения всех пользователей
    public List<User> getAllUsers() throws DbException {
        String sql = "SELECT * FROM Users";
        List<User> users = new ArrayList<>();
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
        } catch (SQLException e) {
            throw new DbException("Error fetching all users", e);
        }
        return users;
    }

    // Метод для обновления информации о пользователе
    public void updateUser(User user) throws DbException {
        String sql = "UPDATE Users SET name = ?, vk_link = ?, telegram_link = ?, email = ?, phone_number = ?, password_hash = ? WHERE user_id = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Хэшируем пароль перед обновлением
            String hashedPassword = PasswordUtils.hashPassword(user.getPassword());

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getVkLink());
            preparedStatement.setString(3, user.getTgLink());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPhoneNumber());
            preparedStatement.setString(6, hashedPassword);
            preparedStatement.setInt(7, user.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException | NoSuchAlgorithmException e) {
            throw new DbException("Error updating user", e);
        }
    }

    // Метод для удаления пользователя по ID
    public void deleteUser(int id) throws DbException {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DbException("Error deleting user", e);
        }
    }

    // Метод для получения пользователя по имени и паролю
    public User getUserByUsernameAndPassword(String username, String password) throws DbException {
        String sql = "SELECT user_id, name, password_hash, email, phone_number, vk_link, telegram_link " +
                "FROM Users WHERE name = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Получаем хэш из базы данных
                    String storedHash = resultSet.getString("password_hash");

                    // Проверяем совпадение паролей
                    if (PasswordUtils.verifyPassword(password, storedHash)) {
                        return mapResultSetToUser(resultSet);
                    } else {
                        return null; // Неверный пароль
                    }
                }
            }
        } catch (SQLException e) {
            throw new DbException("Error while getting user by username and password", e);
        }
        return null; // Пользователь не найден
    }

    // Приватный метод для преобразования ResultSet в объект User
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        // Извлекаем данные из ResultSet с учетом возможных null значений
        Integer id = resultSet.getInt("user_id");
        String name = resultSet.getString("name");
        String passwordHash = resultSet.getString("password_hash");

        // Поле role может быть null, так как оно не используется в таблице
        String role = "user"; // По умолчанию присваиваем роль "user"

        // Создаем объект User
        User user = new User(id, name, passwordHash, role);

        // Безопасно извлекаем ссылки, если они могут быть null
        user.setVkLink(resultSet.getString("vk_link"));
        user.setTgLink(resultSet.getString("telegram_link"));

        // Прочие поля с возможными null значениями
        user.setEmail(resultSet.getString("email"));
        user.setPhoneNumber(resultSet.getString("phone_number"));

        return user;
    }

}



