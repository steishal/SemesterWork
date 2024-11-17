package com.models;

import java.util.Objects;

public class User {
    private Integer id;
    private String username;
    private String password;
    private String role;
    private String email;
    private String phoneNumber;
    private String vkLink;
    private String tgLink;

    // Конструктор без аргументов (по умолчанию)
    public User() {
    }

    // Конструктор с параметрами для создания пользователя без ID
    public User(String username, String password, String role) {
        this.id = null;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Конструктор с параметрами для создания пользователя с ID
    public User(Integer id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Конструктор с параметрами для создания пользователя с ID
    public User(Integer id, String username, String password, String role, String email, String phoneNumber, String vkLink, String tgLink) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email != null ? email : ""; // Задаем пустую строку вместо null
        this.phoneNumber = phoneNumber != null ? phoneNumber : "";
        this.vkLink = vkLink != null ? vkLink : "";
        this.tgLink = tgLink != null ? tgLink : "";
    }

    // Геттер и сеттер для ID
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    // Геттер и сеттер для username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Геттер и сеттер для password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Геттер и сеттер для role
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Геттер и сеттер для email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email : "";
    }

    // Геттер и сеттер для phoneNumber
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber != null ? phoneNumber : "";
    }

    // Геттер и сеттер для vkLink
    public String getVkLink() {
        return vkLink;
    }

    public void setVkLink(String vkLink) {
        this.vkLink = vkLink != null ? vkLink : "";
    }

    // Геттер и сеттер для tgLink
    public String getTgLink() {
        return tgLink;
    }

    public void setTgLink(String tgLink) {
        this.tgLink = tgLink != null ? tgLink : "";
    }

    // Переопределение toString для отображения объекта User, исключая пароль
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", vkLink='" + vkLink + '\'' +
                ", tgLink='" + tgLink + '\'' +
                '}';
    }

    // Переопределение equals с учетом всех полей
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username.equals(user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                Objects.equals(phoneNumber, user.phoneNumber) &&
                Objects.equals(vkLink, user.vkLink) &&
                Objects.equals(tgLink, user.tgLink);
    }

    // Переопределение hashCode с учетом всех полей
    @Override
    public int hashCode() {
        return Objects.hash(username, password, email, phoneNumber, vkLink, tgLink);
    }
}


