package com.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {

    private static final int SALT_LENGTH = 16; // Длина соли (в байтах)
    private static final int ITERATIONS = 10000; // Количество итераций для повышения безопасности

    // Генерация случайной соли
    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Хэширование пароля с добавлением соли
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        // Генерация соли
        String salt = generateSalt();

        // Соединяем соль с паролем
        String saltedPassword = password + salt;

        // Хэшируем с использованием SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(saltedPassword.getBytes());

        // Конвертируем хэш в строку
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }

        // Возвращаем хэш и соль в одном значении, разделённые двоеточием
        return hexString.toString() + ":" + salt;
    }

    // Проверка пароля (сравнение введённого пароля с хранимым хэшем)
    public static boolean verifyPassword(String inputPassword, String storedHash) {
        try {
            // Разделяем хэш и соль
            String[] parts = storedHash.split(":");
            String storedPasswordHash = parts[0];
            String storedSalt = parts[1];

            // Хэшируем введённый пароль с той же солью
            String saltedInputPassword = inputPassword + storedSalt;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(saltedInputPassword.getBytes());

            // Конвертируем хэш в строку
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            // Сравниваем хэши
            return hexString.toString().equals(storedPasswordHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while hashing password", e);
        }
    }
}


