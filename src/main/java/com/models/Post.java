package com.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Post {

    private Integer id;
    private Integer userId;
    private Integer categoryId;
    private String content;
    private Date createDate;
    private ArrayList<String> images;

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", content='" + content + '\'' +
                '}';
    }
}

