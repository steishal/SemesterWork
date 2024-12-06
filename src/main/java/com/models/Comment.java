package com.models;

public class Comment {

    private String id;
    private String content;
    private String authorId;
    private String postId;
    private String createDate;
    private String authorName; // Новое поле

    public Comment() {

    }

    // Геттеры и сеттеры для нового поля
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", postId='" + postId + '\'' +
                ", authorId='" + authorId + '\'' +
                ", authorName='" + authorName + '\'' +  // Добавлено имя автора
                ", content='" + content + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }

    public Comment(String content, String authorId, String postId, String createDate) {
        this.content = content;
        this.authorId = authorId;
        this.postId = postId;
        this.createDate = createDate;
    }

    public Comment(String id, String content, String authorId, String postId, String createDate) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.postId = postId;
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}

