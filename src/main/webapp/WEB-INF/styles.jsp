<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<style>
    body {
        font-family: 'Arial', sans-serif;
        margin: 0;
        padding: 0;
        display: flex;
        background-color: #f9f9f9;
    }
    .open-btn {
        position: fixed;
        top: 20px;
        left: 20px;
        background-color: #4a90e2;
        color: white;
        padding: 10px;
        border-radius: 5px;
        cursor: pointer;
        font-size: 1.5em;
        transition: background-color 0.3s ease;
        z-index: 100;
    }

    .post-container {
        position: relative; /* Чтобы блок с действиями был внутри контейнера */
        width: 600px;
        margin: 10px auto;
        padding: 15px 15px 15px 60px; /* Добавляем отступ слева для кнопок */
        border: 1px solid #e1e1e1;
        border-radius: 10px;
        background-color: #fff;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        margin-bottom: 5px;
    }

    a {
        text-decoration: none;
    }

    .like-button {
        background: none;
        border: none;
        padding: 0;
        cursor: pointer;
        display: flex;
        align-items: center;
    }

    .like-button i {
        font-size: 18px;
        color: #ccc;
        transition: transform 0.2s ease, color 0.2s ease;
    }

    .like-button i.liked {
        color: #e74c3c;
    }

    .likes-count {
        font-size: 16px;
        margin-left: 5px;
        color: #888;
    }

    .comment-button {
        border: none;
        background: none;
        cursor: pointer;
        padding: 0;
        display: flex;
        align-items: center;
        color: #555; /* Цвет для иконки комментария */
    }

    .comment-button i {
        font-size: 18px;
        transition: transform 0.2s ease, color 0.2s ease;
    }

    .comment-button i:hover {
        color: #3498db; /* Голубой цвет при наведении */
    }

    /* Количество комментариев */
    .comments-count {
        font-size: 16px;
        margin-left: 5px;
        color: #888;
    }

    .sidebar {
        width: 250px;
        background-color: #4a90e2;
        color: white;
        position: fixed;
        top: 0;
        left: -300px; /* Начальное скрытое состояние */
        height: 100vh;
        padding: 20px;
        box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
        transition: left 0.3s ease; /* Плавный переход */
    }

    .sidebar h2 {
        text-align: center;
        margin-bottom: 40px;
        font-size: 1.8em;
    }

    .sidebar a {
        display: block;
        color: white;
        padding: 12px;
        text-decoration: none;
        font-size: 1.2em;
        margin: 10px 0;
        border-radius: 5px;
        transition: background-color 0.3s;
    }

    .sidebar a:hover {
        background-color: #2c7dc6;
    }

    .content {
        margin-left: 270px;
        padding: 10px;
        width: calc(100% - 270px);
        transition: margin-left 0.3s ease; /* Плавный сдвиг контента */
    }

    /* Стили для постов */
    .post-container {
        width: 600px;
        margin: 20px auto;
        padding: 15px;
        border: 1px solid #e1e1e1;
        border-radius: 10px;
        background-color: #fff;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }
    .post-header {
        display: flex;
        align-items: center;
        margin-bottom: 10px;
    }

    .post-author-avatar {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        margin-right: 10px;
    }

    .post-author-name {
        font-weight: bold;
        color: #4a76a8;
        text-decoration: none;
    }

    .post-create-date {
        font-size: 0.9rem;
        color: #666;
    }

    .post-content {
        margin-bottom: 12px;
    }

    .post-content p {
        margin: 0;
        font-size: 1rem;
        line-height: 1.5;
    }

    .post-image {
        max-width: 100%;
        border-radius: 10px;
        margin-top: 10px;
    }

    .post-actions {
        display: flex;
        flex-direction: row; /* Расположить элементы вертикально */
        align-items: flex-start; /* Выровнять элементы по левому краю */
        gap: 10px; /* Расстояние между элементами */
        font-size: 16px;
        color: #555;
        position: absolute; /* Абсолютное позиционирование */
        margin-top: 5px;
        position: relative;
        top: -5px;
    }
</style>

