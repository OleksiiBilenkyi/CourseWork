package com.example.kursova;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class MiniMap extends Pane {
    private final Rectangle frame;
    private final ImageView imageView;
    private final double index;

    public MiniMap(Pane container, double index) {
        this.index = index;
        imageView = new ImageView();
        getChildren().add(imageView);

        // Оновлення зображення кожні 0,1 секунди
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            // Оновлення зображення потрібно виконати на головному потоці JavaFX
            Platform.runLater(() -> {
                // Отримання нового зображення з контейнера
                Image image = getMiniMap(container, container.getPrefWidth() * index, container.getPrefHeight() * index);
                imageView.setImage(image);
            });
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Додавання рамки
        frame = new Rectangle();
        frame.setStroke(Color.RED);
        frame.setStrokeWidth(2);
        frame.setFill(Color.TRANSPARENT);
        frame.setWidth(Main.WINDOW_WIDTH * index);
        frame.setHeight(Main.WINDOW_HEIGHT * index);
        getChildren().add(frame);

        // Обробник події перетягування миші
        this.setOnMouseDragged(event -> {
            double frameWidth = frame.getWidth();
            double frameHeight = frame.getHeight();

            double x = event.getX() - frameWidth / 2;
            double y = event.getY() - frameHeight / 2;

            // Обмеження переміщення рамки в межах MiniMap
            double maxX = getWidth() - frameWidth;
            double maxY = getHeight() - frameHeight;

            // Перевірка та корекція координат рамки
            if (x < 0) {
                x = 0;
            } else if (x > maxX) {
                x = maxX-1;
            }

            if (y < 0) {
                y = 0;
            } else if (y > maxY) {
                y = maxY-1;
            }

            // Оновлення позиції рамки та контейнера
            frame.setX(x);
            frame.setY(y);
            container.setLayoutX(-(x / index));
            container.setLayoutY(-(y / index));
        });

        // Обробник події кліку миші
        this.setOnMouseClicked(event -> {
            double frameWidth = frame.getWidth();
            double frameHeight = frame.getHeight();

            double x = event.getX() - frameWidth / 2;
            double y = event.getY() - frameHeight / 2;

            // Обмеження переміщення рамки в межах MiniMap
            double maxX = getWidth() - frameWidth;
            double maxY = getHeight() - frameHeight;

            // Перевірка та корекція координат рамки
            if (x < 0) {
                x = 0;
            } else if (x > maxX) {
                x = maxX-1;
            }

            if (y < 0) {
                y = 0;
            } else if (y > maxY) {
                y = maxY-1;
            }

            // Оновлення позиції рамки та контейнера
            frame.setX(x);
            frame.setY(y);
            container.setLayoutX(-(x / index));
            container.setLayoutY(-(y / index));
        });
    }

    // Оновлення позиції рамки
    public void updateFramePosition(double x, double y) {
        frame.setX(-x*index);
        frame.setY(-y*index);
    }

    // Отримання зображення MiniMap
    public static Image getMiniMap(Pane container, double targetWidth, double targetHeight) {
        // Налаштування масштабування для зменшення розмірів
        double scaleX =  targetWidth / container.getWidth();
        double scaleY =  targetHeight / container.getHeight();
        Scale scale = new Scale(scaleX, scaleY);
        container.getTransforms().add(scale);

        // Створення об'єкту WritableImage з розмірами targetWidth x targetHeight
        WritableImage snapshot = new WritableImage((int) targetWidth, (int) targetHeight);

        // Виконання знімку сцени в об'єкт WritableImage
        container.snapshot(new SnapshotParameters(), snapshot);

        // Видалення масштабування з контейнера
        container.getTransforms().remove(scale);

        return snapshot;
    }
}
