package com.example.kursova;

import Person.alef;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("ALL")
public class World extends Pane {

    // Контейнер для фону і об'єктів
    private static final Pane content = new Pane();

    // Змінні для обробки подій миші
    private double mouseX;
    private double mouseY;
    private double maxX;
    private double maxY;

    private final MiniMap map;

    public World(int contentWidth, int contentHeight) {
        content.setPrefSize(contentWidth, contentHeight);

        // Завантаження фонового зображення
        Image image = new Image(Objects.requireNonNull(Main.class.getResource("space.png")).toString());
        BackgroundImage backgroundTexture = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        content.setBackground(new Background(backgroundTexture));

        // Додавання контейнера до головного Pane
        getChildren().add(content);

        // Додавання MiniMap
        map = new MiniMap(content, 0.1);
        getChildren().add(map);
        map.setTranslateX(Main.WINDOW_WIDTH - 500);
        map.setTranslateY(Main.WINDOW_HEIGHT - 500);

        // Обробники подій миші для переміщення контенту в межах ResizableContainer
        content.setOnMousePressed((MouseEvent event) -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });

        content.setOnMouseDragged((MouseEvent event) -> {
            double deltaX = event.getSceneX() - mouseX;
            double deltaY = event.getSceneY() - mouseY;

            double newLayoutX = content.getLayoutX() + deltaX;
            double newLayoutY = content.getLayoutY() + deltaY;

            // Обмеження переміщення контенту в межах ResizableContainer
            if (newLayoutX > 0) {
                newLayoutX = 0;
            } else if (newLayoutX + content.getWidth() < maxX) {
                newLayoutX = maxX - content.getWidth();
            }

            if (newLayoutY > 0) {
                newLayoutY = 0;
            } else if (newLayoutY + content.getHeight() < maxY) {
                newLayoutY = maxY - content.getHeight();
            }

            // Оновлення позиції контейнера
            content.setLayoutX(newLayoutX);
            content.setLayoutY(newLayoutY);

            map.updateFramePosition(newLayoutX, newLayoutY);

            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });

        // Обмеження переміщення контенту в межах ResizableContainer
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        maxX = screenBounds.getWidth() - content.getWidth();
        maxY = screenBounds.getHeight() - content.getHeight();
    }

    public void addSystem(PlanetarySystem system) {
        // Додавання макрооб'єкту
        content.getChildren().add(system);
        system.setLayoutX(system.getX());
        system.setLayoutY(system.getY());
    }

    public ObservableList<alef> getListObj() {
        ObservableList<alef> alefObjects = FXCollections.observableArrayList();
        Iterator<Node> iterator = content.getChildren().iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node instanceof alef) {
                alefObjects.add((alef) node);
            }
        }
        return alefObjects;
    }


    public void addObj(alef obj) {
        if (content.getChildren().contains(obj)) {
            CustomLog.log(obj + " вже присутній на карті.");
        } else {
            content.getChildren().add(obj);
            obj.setLayoutX(obj.getX());
            obj.setLayoutY(obj.getY());
            CustomLog.log(obj + " успішно доданий до карти.");
        }
    }

    public void removeObj(alef obj) {
        if (content.getChildren().contains(obj)) {
            content.getChildren().remove(obj);
            CustomLog.log(obj + " успішно видалений з карти.");
        } else {
            CustomLog.log(obj + " відсутній на карті.");
        }
    }

    public void removeList(List<alef> objects) {
        List<Node> objectsToRemove = new ArrayList<>();
        for (Node node : content.getChildren()) {
            if (objects.contains(node)) {
                objectsToRemove.add(node);
            }
        }
        content.getChildren().removeAll(objectsToRemove);
    }

    public void setPositionSearch(StackPane node) {
        // Отримання координат об'єкту
        double nodeWidth = node.getBoundsInParent().getWidth();
        double nodeHeight = node.getBoundsInParent().getHeight();

        // Центрування об'єкту на екрані
        double centerX = -(node.getLayoutX() + nodeWidth / 2 - maxX / 2);
        double centerY = -(node.getLayoutY() + nodeHeight / 2 - maxY / 2);

        if (centerX > 0) {
            centerX = 0;
        } else if (centerX + content.getWidth() < maxX) {
            centerX = maxX - content.getWidth();
        }

        if (centerY > 0) {
            centerY = 0;
        } else if (centerY + content.getHeight() < maxY) {
            centerY = maxY - content.getHeight();
        }

        content.setLayoutX(centerX);
        content.setLayoutY(centerY);
    }

    public Pane getContent() {
        return content;
    }
}
