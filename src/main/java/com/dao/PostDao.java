package com.dao;

import com.models.Post;
import com.utils.ConnectionProvider;
import com.utils.DbException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class PostDao {
    private static final Logger log = LogManager.getLogger(PostDao.class);
    private final ConnectionProvider connectionProvider;

    public PostDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    // Метод для получения поста по ID
    public Post getPostById(int id) throws DbException {
        String sql = "SELECT * FROM Posts WHERE post_id = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPost(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DbException("Error fetching post by ID", e);
        }
        return null;
    }

    private boolean isCategoryValid(int categoryId) throws DbException {
        String sql = "SELECT 1 FROM Categories WHERE category_id = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, categoryId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DbException("Failed to validate category ID: " + categoryId, e);
        }
    }

    public Post savePost(Post post, List<InputStream> imageStreams, List<String> imageNames, String uploadDirPath) throws DbException {
        String sqlPost = "INSERT INTO Posts (user_id, category_id, content) VALUES (?, ?, ?)";
        String sqlImage = "INSERT INTO PostImages (post_id, image_path) VALUES (?, ?)";

        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement postStatement = connection.prepareStatement(sqlPost, Statement.RETURN_GENERATED_KEYS)) {
                postStatement.setInt(1, post.getUserId());
                postStatement.setInt(2, post.getCategoryId());
                postStatement.setString(3, post.getContent());
                postStatement.executeUpdate();

                try (ResultSet keys = postStatement.getGeneratedKeys()) {
                    if (keys.next()) {
                        post.setId(keys.getInt(1));
                    }
                }

                if (imageStreams != null && !imageStreams.isEmpty()) {
                    try (PreparedStatement imageStatement = connection.prepareStatement(sqlImage)) {
                        Path uploadDir = Paths.get(uploadDirPath, String.valueOf(post.getId()));
                        if (!Files.exists(uploadDir)) {
                            Files.createDirectories(uploadDir);
                        }

                        for (int i = 0; i < imageStreams.size(); i++) {
                            String imageName = imageNames.get(i);
                            InputStream imageStream = imageStreams.get(i);

                            Path imagePath = uploadDir.resolve(imageName);
                            Files.copy(imageStream, imagePath, StandardCopyOption.REPLACE_EXISTING);

                            imageStatement.setInt(1, post.getId());
                            imageStatement.setString(2, imagePath.toString());
                            imageStatement.addBatch();
                        }

                        imageStatement.executeBatch();
                    }
                }

                connection.commit();
            } catch (SQLException | IOException e) {
                connection.rollback();
                throw new DbException("Error saving post, transaction rolled back", e);
            }

        } catch (SQLException e) {
            throw new DbException("Error with database operation", e);
        }

        return post;
    }

    private Post mapResultSetToPost(ResultSet resultSet) throws SQLException, DbException {
        Post post = new Post();
        post.setId(resultSet.getInt("post_id"));
        post.setUserId(resultSet.getInt("user_id"));
        post.setCategoryId(resultSet.wasNull() ? null : resultSet.getInt("category_id"));
        post.setContent(resultSet.getString("content"));
        post.setCreateDate(resultSet.getDate("created_at"));
        post.setImages(getImagesForPost(post.getId()));
        return post;
    }

    public List<String> getImagesForPost(int postId) throws DbException {
        String sql = "SELECT image_path FROM PostImages WHERE post_id = ?";
        List<String> images = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, postId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    images.add(resultSet.getString("image_path"));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Error fetching images for post", e);
        }

        return images;
    }

    public List<Post> getAllPosts() throws DbException {
        String sqlPosts = "SELECT * FROM Posts";
        String sqlImages = "SELECT post_id, image_path FROM PostImages WHERE post_id IN (%s)";

        List<Post> posts = new ArrayList<>();
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement postStatement = connection.prepareStatement(sqlPosts);
             ResultSet resultSetPosts = postStatement.executeQuery()) {

            List<Integer> postIds = new ArrayList<>();
            Map<Integer, List<String>> postImagesMap = new HashMap<>();

            // Извлечение всех постов
            while (resultSetPosts.next()) {
                Post post = new Post();
                post.setId(resultSetPosts.getInt("post_id"));
                post.setUserId(resultSetPosts.getInt("user_id"));
                post.setCategoryId(resultSetPosts.getInt("category_id"));
                post.setContent(resultSetPosts.getString("content"));
                post.setCreateDate(resultSetPosts.getDate("created_at"));

                posts.add(post);
                postIds.add(post.getId());
            }

            if (!postIds.isEmpty()) {
                String idsPlaceholder = postIds.stream().map(id -> "?").collect(Collectors.joining(","));
                String formattedSql = String.format(sqlImages, idsPlaceholder);

                try (PreparedStatement imageStatement = connection.prepareStatement(formattedSql)) {
                    for (int i = 0; i < postIds.size(); i++) {
                        imageStatement.setInt(i + 1, postIds.get(i));
                    }

                    try (ResultSet resultSetImages = imageStatement.executeQuery()) {
                        while (resultSetImages.next()) {
                            int postId = resultSetImages.getInt("post_id"); // Получаем post_id
                            String imagePath = resultSetImages.getString("image_path");
                            postImagesMap.computeIfAbsent(postId, k -> new ArrayList<>())
                                    .add(imagePath);
                        }
                    }
                }
            }

            // Присваиваем изображения каждому посту
            for (Post post : posts) {
                post.setImages(postImagesMap.getOrDefault(post.getId(), Collections.emptyList()));
            }
        } catch (SQLException e) {
            throw new DbException("Error fetching posts with images", e);
        }

        return posts;
    }




    public void updatePost(Post post) throws DbException {
        String sql = "UPDATE Posts SET user_id = ?, category_id = ?, content = ? WHERE post_id = ?";

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, post.getUserId());
            if (post.getCategoryId() == null) {
                preparedStatement.setNull(2, Types.INTEGER);
            } else {
                preparedStatement.setInt(2, post.getCategoryId());
            }

            preparedStatement.setString(3, post.getContent());
            preparedStatement.setInt(4, post.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DbException("No post found with provided ID for update.");
            }
        } catch (SQLException e) {
            throw new DbException("Error updating post", e);
        }
    }

    public void deletePost(int id) throws DbException {
        String checkSql = "SELECT COUNT(*) FROM Posts WHERE post_id = ?";
        String deleteSql = "DELETE FROM Posts WHERE post_id = ?";

        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false); // Отключаем автокоммит для транзакции

            try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
                checkStatement.setInt(1, id);
                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) == 0) {
                        throw new DbException("No post found with provided ID for deletion.");
                    }
                }
            }

            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
                deleteStatement.setInt(1, id);
                deleteStatement.executeUpdate();
            }

            connection.commit(); // Подтверждаем транзакцию
        } catch (SQLException e) {
            throw new DbException("Error deleting post, transaction rolled back", e);
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

