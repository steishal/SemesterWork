package com.models;

import java.sql.Date;

public class Comment {

    private String id;
    private String content;
    private String authorId;
    private String postId;
    private Date createDate;

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    public Comment(String content, String authorId, String postId, Date createDate) {
        this.content = content;
        this.authorId = authorId;
        this.postId = postId;
        this.createDate = createDate;
    }

    public Comment(String id,String content, String authorId, String postId, Date createDate) {
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
