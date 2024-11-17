package com.models;

import java.sql.Array;
import java.sql.Date;
import java.util.ArrayList;

public class Post {

    private Integer id;
    private Integer userId;
    private Integer categoryId;
    private String content;
    private Date createDate;
    private ArrayList<String> images;

    public Post(Integer userId, String content, Date createDate) {
        this.id = null;
        this.userId = userId;
        this.content = content;
        this.createDate = createDate;
    }

    public Post(Integer id, Integer userId, String content, Date createDate) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
