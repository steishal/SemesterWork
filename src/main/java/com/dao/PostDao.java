package com.dao;

import com.models.Like;
import com.models.Post;
import com.utils.ConnectionProvider;
import com.utils.DbException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class PostDao {
    private final ConnectionProvider connectionProvider;

    public PostDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public Post getPostById(int id) throws DbException {
        String sql = "SELECT * FROM Posts WHERE post_id = ?";
        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        connection.commit();
                        return mapResultSetToPost(resultSet);
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DbException("Error fetching post by ID", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DbException("Error managing transaction for fetching post by ID", e);
        }
        return null;
    }

    private boolean isCategoryValid(int categoryId) throws DbException {
        String sql = "SELECT 1 FROM Categories WHERE category_id = ?";
        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, categoryId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    boolean isValid = resultSet.next();
                    connection.commit();
                    return isValid;
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new DbException("Failed to validate category ID: " + categoryId, e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DbException("Error managing transaction for validating category ID", e);
        }
    }


    public Post savePost(Post post, List<InputStream> imageStreams, List<String> imageNames, String uploadDirPath) throws DbException {

        if (!isCategoryValid(post.getCategoryId())) {
            throw new DbException("Invalid category ID: " + post.getCategoryId());
        }

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

        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, postId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        images.add(resultSet.getString("image_path"));
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DbException("Error fetching images for post", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DbException("Error managing transaction for fetching images", e);
        }

        return images;
    }

    public List<Post> getAllPosts() throws DbException {
        String sqlPosts = "SELECT * FROM Posts";
        String sqlImages = "SELECT post_id, image_path FROM PostImages WHERE post_id IN (%s)";

        List<Post> posts = new ArrayList<>();
        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement postStatement = connection.prepareStatement(sqlPosts);
                 ResultSet resultSetPosts = postStatement.executeQuery()) {

                List<Integer> postIds = new ArrayList<>();
                Map<Integer, List<String>> postImagesMap = new HashMap<>();

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
                                int postId = resultSetImages.getInt("post_id");
                                String imagePath = resultSetImages.getString("image_path");
                                postImagesMap.computeIfAbsent(postId, k -> new ArrayList<>())
                                        .add(imagePath);
                            }
                        }
                    }
                }

                for (Post post : posts) {
                    post.setImages(postImagesMap.getOrDefault(post.getId(), Collections.emptyList()));
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DbException("Error fetching posts with images", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DbException("Error managing transaction for fetching posts", e);
        }

        return posts;
    }

    public void updatePost(Post post) throws DbException {
        String sql = "UPDATE Posts SET user_id = ?, category_id = ?, content = ? WHERE post_id = ?";

        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DbException("Error updating post", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DbException("Error managing transaction for updating post", e);
        }
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

    public void deletePost(int id, String uploadDirPath) throws DbException {
        String checkSql = "SELECT COUNT(*) FROM Posts WHERE post_id = ?";
        String selectImagesSql = "SELECT image_path FROM PostImages WHERE post_id = ?";
        String deleteImagesSql = "DELETE FROM PostImages WHERE post_id = ?";
        String deleteLikesSql = "DELETE FROM Likes WHERE post_id = ?";
        String deleteCommentsSql = "DELETE FROM Comments WHERE post_id = ?";
        String deletePostSql = "DELETE FROM Posts WHERE post_id = ?";

        try (Connection connection = connectionProvider.getConnection()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
                    checkStatement.setInt(1, id);
                    try (ResultSet resultSet = checkStatement.executeQuery()) {
                        if (resultSet.next() && resultSet.getInt(1) == 0) {
                            throw new DbException("No post found with provided ID for deletion.");
                        }
                    }
                }

                List<String> imagePaths = new ArrayList<>();
                try (PreparedStatement selectImagesStatement = connection.prepareStatement(selectImagesSql)) {
                    selectImagesStatement.setInt(1, id);
                    try (ResultSet resultSet = selectImagesStatement.executeQuery()) {
                        while (resultSet.next()) {
                            imagePaths.add(resultSet.getString("image_path"));
                        }
                    }
                }

                if (!imagePaths.isEmpty()) {
                    try (PreparedStatement deleteImagesStatement = connection.prepareStatement(deleteImagesSql)) {
                        deleteImagesStatement.setInt(1, id);
                        deleteImagesStatement.executeUpdate();
                    }

                    Path uploadDir = Paths.get(uploadDirPath, String.valueOf(id));
                    for (String imagePath : imagePaths) {
                        Path filePath = uploadDir.resolve(imagePath);
                        try {
                            Files.deleteIfExists(filePath);
                        } catch (IOException e) {
                            System.err.println("Failed to delete image file: " + filePath + " - " + e.getMessage());
                        }
                    }
                }

                try (PreparedStatement deleteLikesStatement = connection.prepareStatement(deleteLikesSql)) {
                    deleteLikesStatement.setInt(1, id);
                    deleteLikesStatement.executeUpdate();
                }

                try (PreparedStatement deleteCommentsStatement = connection.prepareStatement(deleteCommentsSql)) {
                    deleteCommentsStatement.setInt(1, id);
                    deleteCommentsStatement.executeUpdate();
                }

                try (PreparedStatement deletePostStatement = connection.prepareStatement(deletePostSql)) {
                    deletePostStatement.setInt(1, id);
                    deletePostStatement.executeUpdate();
                }

                connection.commit();
            } catch (SQLException | DbException e) {
                connection.rollback();
                throw new DbException("Error deleting post, transaction rolled back", e);
            }
        } catch (SQLException e) {
            throw new DbException("Database connection error", e);
        }
    }

}
