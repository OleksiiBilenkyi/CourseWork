package com.example.kursova;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomLog {
    private static final String logFileName = "log.txt";
    private static SimpleDateFormat dateFormat;

    public CustomLog() throws IOException {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        createLogFile();

        String projectD = Main.class.getPackageName();
        log("Проект запущено " + projectD);
    }

    // Метод для створення файлу журналу
    private void createLogFile() {
        try {
            FileWriter fileWriter = new FileWriter(logFileName, true);

            // Додаємо два порожніх рядки перед початком запису в файл
            fileWriter.append("\n\n");

            // Закриваємо файл після запису
            fileWriter.close();
        } catch (IOException e) {
            // Виводимо повідомлення про помилку, якщо створення файлу не вдалося
            System.err.println("Помилка створення файлу : " + e.getMessage());
        }
    }

    // Метод для запису повідомлення в файл журналу
    public static void log(String message) {
        // Отримуємо поточну дату та час у заданому форматі
        String formattedDate = dateFormat.format(new Date());

        // Формуємо повідомлення для запису у форматі "дата і час - повідомлення"
        String logMessage = formattedDate + " - " + message;

        try (PrintWriter writer = new PrintWriter(new FileWriter(logFileName, true))) {
            // Записуємо повідомлення в кінець файлу журналу
            writer.println(logMessage);

            // Виводимо повідомлення на консоль
            Main.console.println(logMessage);
        } catch (IOException e) {
            // Виводимо повідомлення про помилку, якщо запис до файлу не вдалося
            System.err.println("Помилка запису в лог-файл: " + e.getMessage());
        }
    }
}
