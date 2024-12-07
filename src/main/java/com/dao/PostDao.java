package com.dao;

import com.models.Post;
import com.utils.ConnectionProvider;
import com.utils.DbException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PostDao {
    private final ConnectionProvider connectionProvider;

    public PostDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    // Метод для получения поста по ID
    public Post getPostById(int id) throws DbException {
        String sql = "SELECT * FROM Posts WHERE post_id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            // Устанавливаем ID в запросе
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            // Если пост найден, преобразуем результат в объект Post
            if (resultSet.next()) {
                return mapResultSetToPost(resultSet);
            }
        } catch (SQLException e) {
            // Обрабатываем исключение
            throw new DbException("Error fetching post by ID", e);
        } finally {
            closeResources(preparedStatement, resultSet);
            if (connection != null) {
                connectionProvider.releaseConnection(connection);
            }
        }
        // Если пост не найден, возвращаем null
        return null;
    }

    public void savePost(Post post) throws DbException {
        // SQL-запрос для вставки нового поста
        String sql = "INSERT INTO Posts (user_id, category_id, content, create_date) VALUES (?, ?, ?, NOW())";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Получаем соединение из пула
            connection = connectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            // Устанавливаем параметры запроса
            preparedStatement.setInt(1, post.getUserId());
            preparedStatement.setInt(2, post.getCategoryId());
            preparedStatement.setString(3, post.getContent());

            // Выполняем запрос на добавление
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Error saving post", e);
        } finally {
            closeResources(preparedStatement, null);
            if (connection != null) {
                connectionProvider.releaseConnection(connection);
            }
        }
    }

    private Post mapResultSetToPost(ResultSet resultSet) throws SQLException {
        // Создаем объект Post и заполняем его данными из ResultSet
        Post post = new Post();
        post.setId(resultSet.getInt("post_id"));
        post.setUserId(resultSet.getInt("user_id"));
        post.setCategoryId(resultSet.getInt("category_id"));
        post.setContent(resultSet.getString("content"));
        post.setCreateDate(resultSet.getDate("create_date"));
        return post;
    }

    // Метод для получения всех постов
    public List<Post> getAllPosts() throws DbException {
        String sql = "SELECT * FROM Posts";
        Connection connection = null;
        List<Post> posts = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            // Итерация по результатам и преобразование каждой записи в объект Post
            while (resultSet.next()) {
                posts.add(mapResultSetToPost(resultSet));
            }
        } catch (SQLException e) {
            throw new DbException("Error fetching all posts", e);
        } finally {
            closeResources(preparedStatement, resultSet);
            if (connection != null) {
                connectionProvider.releaseConnection(connection);
            }
        }
        return posts;
    }

    public void updatePost(Post post) throws DbException {
        String sql = "UPDATE Posts SET user_id = ?, category_id = ?, content = ? WHERE post_id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            // Устанавливаем параметры запроса
            preparedStatement.setInt(1, post.getUserId());
            preparedStatement.setInt(2, post.getCategoryId());
            preparedStatement.setString(3, post.getContent());
            preparedStatement.setInt(4, post.getId());

            // Выполняем обновление
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Error updating post", e);
        } finally {
            closeResources(preparedStatement, null);
            if (connection != null) {
                connectionProvider.releaseConnection(connection);
            }
        }
    }

    public void deletePost(int id) throws DbException {
        String sql = "DELETE FROM Posts WHERE post_id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionProvider.getConnection();
            preparedStatement = connection.prepareStatement(sql);

            // Устанавливаем ID для удаления
            preparedStatement.setInt(1, id);

            // Выполняем запрос
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Error deleting post", e);
        } finally {
            closeResources(preparedStatement, null);
            if (connection != null) {
                connectionProvider.releaseConnection(connection);
            }
        }
    }

    // Вспомогательный метод для закрытия ресурсов
    private void closeResources(PreparedStatement preparedStatement, ResultSet resultSet) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

