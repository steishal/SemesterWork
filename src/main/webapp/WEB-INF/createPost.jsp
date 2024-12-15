<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Создание поста</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .form-container {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 600px;
        }

        h2 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            font-size: 16px;
            margin-bottom: 8px;
            color: #333;
        }

        .form-group input,
        .form-group textarea {
            width: 100%;
            padding: 10px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 4px;
            background-color: #f9f9f9;
            box-sizing: border-box;
        }

        .form-group input[type="file"] {
            padding: 3px;
        }

        .form-group textarea {
            resize: vertical;
        }

        .form-group button {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 12px 20px;
            font-size: 16px;
            border-radius: 4px;
            cursor: pointer;
            width: 100%;
        }

        .form-group button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

<div class="form-container">
    <h2>Создать пост</h2>
    <form action="${pageContext.request.contextPath}/createPost" method="POST" enctype="multipart/form-data">
        <div class="form-group">
            <label for="content">Контент:</label>
            <textarea id="content" name="content" rows="4" required placeholder="Введите текст вашего поста"></textarea>
        </div>
        <div class="form-group">
            <label for="categoryId">ID категории:</label>
            <input type="number" id="categoryId" name="categoryId" required>
        </div>
        <div class="form-group">
            <label for="image0">Изображение 1:</label>
            <input type="file" id="image0" name="image0">
        </div>
        <div class="form-group">
            <label for="image1">Изображение 2:</label>
            <input type="file" id="image1" name="image1">
        </div>
        <div class="form-group">
            <label for="image2">Изображение 3:</label>
            <input type="file" id="image2" name="image2">
        </div>
        <div class="form-group">
            <button type="submit">Создать пост</button>
        </div>
    </form>
</div>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        document.querySelector('form').addEventListener('submit', function (event) {
            const files = document.querySelectorAll('input[type="file"]');
            const maxSize = 50 * 1024 * 1024; // 50 MB

            for (let fileInput of files) {
                if (fileInput.files[0] && fileInput.files[0].size > maxSize) {
                    alert(`Файл ${fileInput.files[0].name} превышает размер 50 MB.`);
                    event.preventDefault(); // Предотвращает отправку формы
                    return;
                }
            }
        });
    });
</script>

</body>
</html>



