package com.models;

public class Category {

    private String categoryName;
    private Integer id;

    public Category(String categoryName, Integer id) {
        this.categoryName = categoryName;
        this.id = id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    public String getCategoryName() {
        return categoryName;
    }
    public Integer getId() {
        return id;
    }
}
