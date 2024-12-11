package com.dao;

import com.models.Like;
import com.utils.ConnectionProvider;
import com.utils.DbException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LikeDao {
    private final ConnectionProvider connectionProvider;

    public LikeDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public void save(Like like) throws DbException {
        String sql = "INSERT INTO Likes (post_id, user_id) VALUES (?, ?)";
        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, like.getPostId());
                preparedStatement.setInt(2, like.getUserId());
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DbException("Error while saving like", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DbException("Error while managing transaction for saving like", e);
        }
    }

    public void delete(int likeId) throws DbException {
        String sql = "DELETE FROM Likes WHERE like_id = ?";
        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, likeId);
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DbException("Error while deleting like", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DbException("Error while managing transaction for deleting like", e);
        }
    }
}
