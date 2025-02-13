package com.dao;

import com.models.Category;
import com.utils.ConnectionProvider;
import com.utils.DbException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {

    private final ConnectionProvider connectionProvider;

    public CategoryDao(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public void saveCategory(Category category) throws DbException {
        String sql = "INSERT INTO Categories (name) VALUES (?)";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, category.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Ошибка при сохранении категории", e);
        }
    }
    public List<Category> getAllCategories() throws DbException {
        String sql = "SELECT category_id, name FROM Categories";
        List<Category> categories = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Category category = new Category();
                category.setCategoryId(resultSet.getInt("category_id"));
                category.setName(resultSet.getString("name"));
                categories.add(category);
            }
        } catch (SQLException e) {
            throw new DbException("Ошибка при получении списка категорий", e);
        }

        return categories;
    }
    public boolean isCategoryExists(String categoryName) throws DbException {
        String sql = "SELECT COUNT(*) FROM categories WHERE name = ?";
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, categoryName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
                return false;
            }
        } catch (SQLException e) {
            throw new DbException("Ошибка при проверке существования категории", e);
        }
    }

    public String getCategoryNameById(int categoryId) throws DbException {
        String sql = "SELECT name FROM Categories WHERE category_id = ?";
        String categoryName = null;

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, categoryId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    categoryName = resultSet.getString("name");
                }
            }
        } catch (SQLException e) {
            throw new DbException("Ошибка при получении названия категории по ID", e);
        }

        return categoryName;
    }
}
