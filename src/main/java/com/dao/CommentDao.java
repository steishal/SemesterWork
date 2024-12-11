package com.dao;

import com.models.Comment;
import com.utils.ConnectionProvider;
import com.utils.DbException;
import com.models.Like;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDao {

    private final ConnectionProvider connectionProvider;

    public CommentDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public void addComment(Comment comment) throws DbException {
        String sql = "INSERT INTO Comments (post_id, user_id, content) VALUES (?, ?, ?)";
        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, comment.getPostId());
                preparedStatement.setString(2, comment.getAuthorId());
                preparedStatement.setString(3, comment.getContent());
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DbException("Error while adding comment", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DbException("Error while managing transaction for adding comment", e);
        }
    }

    public Comment getCommentById(String commentId) throws DbException {
        String sql = "SELECT * FROM Comments WHERE comment_id = ?";
        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, commentId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        connection.commit();
                        return mapResultSetToComment(resultSet);
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DbException("Error while fetching comment by ID", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DbException("Error while managing transaction for fetching comment by ID", e);
        }
        return null;
    }

    private String getAuthorNameById(String authorId) throws DbException {
        String sql = "SELECT name FROM Users WHERE user_id = ?";
        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, authorId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        connection.commit();
                        return resultSet.getString("name");
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DbException("Error while fetching author name by ID", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DbException("Error while managing transaction for fetching author name", e);
        }
        return null;
    }

    public List<Comment> getCommentsByPostId(String postId) throws DbException {
        String sql = "SELECT c.comment_id, c.post_id, c.user_id, c.content, c.created_at, u.name AS author_name " +
                "FROM Comments c " +
                "JOIN Users u ON c.user_id = u.user_id " +
                "WHERE c.post_id = ? " +
                "ORDER BY c.created_at DESC";

        List<Comment> comments = new ArrayList<>();
        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, postId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Comment comment = new Comment();
                        comment.setId(resultSet.getString("comment_id"));
                        comment.setPostId(resultSet.getString("post_id"));
                        comment.setAuthorId(resultSet.getString("user_id"));
                        comment.setContent(resultSet.getString("content"));
                        comment.setCreateDate(resultSet.getString("created_at"));
                        comment.setAuthorName(resultSet.getString("author_name"));
                        comments.add(comment);
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DbException("Error while fetching comments for post", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DbException("Error while managing transaction for fetching comments", e);
        }
        return comments;
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

    public void updateComment(Comment comment) throws DbException {
        String sql = "UPDATE Comments SET content = ? WHERE comment_id = ?";
        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, comment.getContent());
                preparedStatement.setString(2, comment.getId());
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DbException("Error while updating comment", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DbException("Error while managing transaction for updating comment", e);
        }
    }

    public void deleteComment(String commentId) throws DbException {
        String sql = "DELETE FROM Comments WHERE comment_id = ?";
        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, commentId);
                preparedStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DbException("Error while deleting comment", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DbException("Error while managing transaction for deleting comment", e);
        }
    }

    public void updateCommentContent(String commentId, String newContent) throws DbException {
        String sql = "UPDATE Comments SET content = ? WHERE comment_id = ?";
        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newContent);
                preparedStatement.setString(2, commentId);
                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated == 0) {
                    throw new DbException("No comment found with ID: " + commentId);
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DbException("Error updating comment content", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DbException("Error while managing transaction for updating comment content", e);
        }
    }

    private Comment mapResultSetToComment(ResultSet resultSet) throws SQLException, DbException {
        Comment comment = new Comment();
        comment.setId(resultSet.getString("comment_id"));
        comment.setPostId(resultSet.getString("post_id"));
        comment.setAuthorId(resultSet.getString("user_id"));
        comment.setContent(resultSet.getString("content"));
        comment.setCreateDate(resultSet.getString("created_at"));

        String authorName = getAuthorNameById(resultSet.getString("user_id"));
        comment.setAuthorName(authorName);
        return comment;
    }
}

