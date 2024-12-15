package com.dao;

import com.models.Like;
import com.utils.ConnectionProvider;
import com.utils.DbException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LikeDao {
    private final ConnectionProvider connectionProvider;

    public LikeDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public List<Like> getLikesByPostId(String postId) throws DbException {
        String sql = "SELECT like_id, post_id, user_id FROM Likes WHERE post_id = ?";

        List<Like> likes = new ArrayList<>();
        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, postId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Like like = new Like();
                        like.setId(resultSet.getInt("like_id"));
                        like.setPostId(resultSet.getInt("post_id"));
                        like.setUserId(resultSet.getInt("user_id"));
                        likes.add(like);
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DbException("Error while fetching likes for post", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DbException("Error while managing transaction for fetching likes", e);
        }
        return likes;
    }

    public boolean toggleLike(int postId, int userId) throws DbException {
        String checkSql = "SELECT 1 FROM Likes WHERE post_id = ? AND user_id = ?";
        String deleteSql = "DELETE FROM Likes WHERE post_id = ? AND user_id = ?";
        String insertSql = "INSERT INTO Likes (post_id, user_id) VALUES (?, ?)";
        try (Connection connection = connectionProvider.getConnection()) {
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, postId);
                checkStmt.setInt(2, userId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
                            deleteStmt.setInt(1, postId);
                            deleteStmt.setInt(2, userId);
                            deleteStmt.executeUpdate();
                        }
                        return false;
                    }
                }
            }

            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                insertStmt.setInt(1, postId);
                insertStmt.setInt(2, userId);
                insertStmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            throw new DbException("Error toggling like", e);
        }
    }

    public boolean isUserLiked(int postId, int userId) throws DbException {
        String sql = "SELECT 1 FROM Likes WHERE post_id = ? AND user_id = ?";
        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, postId);
                preparedStatement.setInt(2, userId);

                try (ResultSet rs = preparedStatement.executeQuery()) {
                    boolean result = rs.next();
                    connection.commit();
                    return result;
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new DbException("Error checking if user liked post", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DbException("Error while managing transaction for checking like", e);
        }
    }
}


