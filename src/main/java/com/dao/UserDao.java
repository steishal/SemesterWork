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

    private final ConnectionProvider connectionProvider;

    public UserDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

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
        return null;
    }

    public User getUserByEmail(String email) throws DbException {
        String sql = "SELECT * FROM Users WHERE email = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToUser(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Error while fetching user by email", e);
        }
        return null;
    }

    public void saveUser(User user) throws DbException {
        String sql = "INSERT INTO Users (name, vk_link, telegram_link, email, phone_number, password_hash) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getVkLink());
            preparedStatement.setString(3, user.getTgLink());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPhoneNumber());
            preparedStatement.setString(6, user.getPassword());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Error saving user", e);
        }
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
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

    public void updateUser(User user) throws DbException {
        String sql = "UPDATE Users SET name = ?, vk_link = ?, telegram_link = ?, email = ?, phone_number = ?, password_hash = ? WHERE user_id = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

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

    public User getUserByUsernameAndPassword(String username, String password) throws DbException {
        String sql = "SELECT user_id, name, password_hash, email, phone_number, vk_link, telegram_link " +
                "FROM Users WHERE name = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedHash = resultSet.getString("password_hash");
                    if (PasswordUtils.verifyPassword(password, storedHash)) {
                        return mapResultSetToUser(resultSet);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DbException("Error while getting user by username and password", e);
        }
        return null;
    }

    public void followUser(int followerId, int followedId) throws DbException {
        String sql = "INSERT INTO Follows (follower_id, followed_id) VALUES (?, ?)";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, followerId);
            preparedStatement.setInt(2, followedId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Error while following user", e);
        }
    }

    public void unfollowUser(int followerId, int followedId) throws DbException {
        String sql = "DELETE FROM Follows WHERE follower_id = ? AND followed_id = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, followerId);
            preparedStatement.setInt(2, followedId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Error while unfollowing user", e);
        }
    }

    public List<Integer> getFollowers(int userId) throws DbException {
        String sql = "SELECT follower_id FROM Follows WHERE followed_id = ?";
        List<Integer> followers = new ArrayList<>();
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    followers.add(resultSet.getInt("follower_id"));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Error fetching followers", e);
        }
        return followers;
    }

    public List<Integer> getSubscriptions(int userId) throws DbException {
        String sql = "SELECT followed_id FROM Follows WHERE follower_id = ?";
        List<Integer> subscriptions = new ArrayList<>();
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    subscriptions.add(resultSet.getInt("followed_id"));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Error while fetching subscriptions", e);
        }
        return subscriptions;
    }

}




