package com.dao;

import com.models.Comment;
import com.utils.ConnectionProvider;
import com.utils.DbException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDao {
    private final ConnectionProvider connectionProvider;

    public CommentDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    // Метод для добавления комментария
    public void addComment(Comment comment) throws DbException {
        String sql = "INSERT INTO Comments (post_id, user_id, content) VALUES (?, ?, ?)";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, comment.getPostId());
            preparedStatement.setString(2, comment.getAuthorId());
            preparedStatement.setString(3, comment.getContent());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DbException("Error while adding comment", e);
        }
    }

    // Метод для получения комментария по ID
    public Comment getCommentById(String commentId) throws DbException {
        String sql = "SELECT * FROM Comments WHERE comment_id = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, commentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToComment(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Error while fetching comment by ID", e);
        }
        return null; // Комментарий не найден
    }

    // Метод для получения всех комментариев к посту
    public List<Comment> getCommentsByPostId(String postId) throws DbException {
        String sql = "SELECT c.comment_id, c.post_id, c.user_id, c.content, c.created_at, u.name AS author_name " +
                "FROM Comments c " +
                "JOIN Users u ON c.user_id = u.user_id " +
                "WHERE c.post_id = ? " +
                "ORDER BY c.created_at DESC";

        List<Comment> comments = new ArrayList<>();
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, postId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Comment comment = mapResultSetToComment(resultSet);

                    // Устанавливаем имя автора
                    comment.setAuthorId(resultSet.getString("author_name"));

                    comments.add(comment);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Error while fetching comments for post", e);
        }
        return comments;
    }


    // Метод для обновления комментария
    public void updateComment(Comment comment) throws DbException {
        String sql = "UPDATE Comments SET content = ? WHERE comment_id = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, comment.getContent());
            preparedStatement.setString(2, comment.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DbException("Error while updating comment", e);
        }
    }

    // Метод для удаления комментария
    public void deleteComment(String commentId) throws DbException {
        String sql = "DELETE FROM Comments WHERE comment_id = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, commentId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DbException("Error while deleting comment", e);
        }
    }

    public void updateCommentContent(String commentId, String newContent) throws DbException {
        String sql = "UPDATE Comments SET content = ? WHERE comment_id = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, newContent);
            preparedStatement.setString(2, commentId);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new DbException("No comment found with ID: " + commentId);
            }
        } catch (SQLException e) {
            throw new DbException("Error updating comment content", e);
        }
    }

    // Приватный метод для преобразования ResultSet в объект Comment
    private Comment mapResultSetToComment(ResultSet resultSet) throws SQLException {
        Comment comment = new Comment();
        comment.setId(resultSet.getString("comment_id")); // Здесь мы используем setId, если в модели есть такой метод
        comment.setPostId(resultSet.getString("post_id"));
        comment.setAuthorId(resultSet.getString("user_id"));
        comment.setContent(resultSet.getString("content"));
        comment.setCreateDate(resultSet.getString("created_at"));

        // Вместо добавления поля автор, получаем имя автора по id
        String authorName = getAuthorNameById(resultSet.getString("user_id"));
        comment.setAuthorName(authorName); // Устанавливаем имя автора
        return comment;
    }

    // Пример метода для получения имени автора по его ID (можно заменить на ваш метод)
    private String getAuthorNameById(String authorId) {
        // Здесь должна быть логика для получения имени автора по его ID, например:
        // Получаем имя автора из базы данных или другого источника данных
        // Для примера можно вернуть заглушку
        return "Author Name"; // Реализуйте свою логику для получения имени по ID
    }


}

