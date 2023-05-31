package com.example.kursova;

import Person.alef;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.example.kursova.Main.root;

public class Console extends VBox {
    private TextArea outputTextArea;
    private TextField inputTextField;

    public Console() {
        // Створення елементів управління
        Label consoleLabel = new Label("Консоль");
        consoleLabel.setFont(Font.font("Arial", 18));
        consoleLabel.setStyle("-fx-font-weight: bold;");

        outputTextArea = new TextArea();
        outputTextArea.setEditable(false);
        outputTextArea.setPrefSize(800, 500);
        outputTextArea.setStyle("-fx-control-inner-background: #F0F0F0; -fx-text-fill: black;");

        inputTextField = new TextField();
        inputTextField.setPrefWidth(800);
        inputTextField.setOnAction(event -> handleCommand());
        inputTextField.setStyle("-fx-control-inner-background: #F0F0F0; -fx-text-fill: black;");

        Button closeButton = new Button("✖");
        closeButton.setOnAction(event -> hide());
        closeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-font-weight: bold;");

        BorderPane closeButtonPane = new BorderPane();
        closeButtonPane.setCenter(closeButton);
        BorderPane.setAlignment(closeButton, Pos.CENTER_RIGHT);

        this.getChildren().addAll(closeButtonPane , consoleLabel, outputTextArea, inputTextField);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setPrefSize(800, 600);
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
        this.setStyle("-fx-border-color: #000000; -fx-border-width: 4px; -fx-background-color: #808080; ");
        this.setVisible(false);
    }

    public void show() {
        this.setVisible(true);
    }

    public void hide() {
        this.setVisible(false);
    }

    private void handleCommand() {
        String command = inputTextField.getText();
        // Обробка команди тут

        // Вивід тексту у велике поле
        println(command);

        //Відправляємо команду на розпізнавання
        processConsoleCommand(command);

        // Очищення поля вводу після обробки команди
        inputTextField.clear();
    }

    public void println(String text) {
        outputTextArea.appendText(text + "\n");
    }

    private void processConsoleCommand(String line) {
        if (line.startsWith("/search")) {
            processSearchCommand(line);
        } else if (line.startsWith("/deletePS")) {
            processDeletePlanetarySystemCommand(line);
        } else if (line.startsWith("/deleteNPS")) {
            processDeleteNonPlanetarySystemObjectsCommand(line);
        } else if (line.startsWith("/listObjInWorld")) {
            listObjectsInWorld(line);
        } else if (line.startsWith("/listObjActive")) {
            listActiveObjects(line);
        } else if (line.startsWith("/listObjInSys")) {
            processListObjectsInPlanetarySystemCommand(line);
        } else if (line.startsWith("/help")) {
            displayHelp();
        } else {
            println("Невідома команда: " + line);
        }
    }

    private void processSearchCommand(String line) {
        String params = line.substring(8).trim();
        boolean found = false;
        for (alef obj : root.getListObj()) {
            if (obj.getName().equals(params)) {
                root.setPositionSearch(obj);
                found = true;
                break;
            }
        }
        if (!found) {
            println("Об'єкт з назвою '" + params + "' не знайдено.\n");
        }
    }

    private void processDeletePlanetarySystemCommand(String line) {
        String params = line.substring(9).trim();
        boolean found = false;

        for (PlanetarySystem sys : PlanetarySystem.getListSystem()) {
            if (sys.getName().equals(params)) {
                root.removeList(sys.getListObjInSys());
                found = true;
                break;
            }
        }
        if (!found) {
            println("Планетарну систему з назвою '" + params + "' не знайдено.\n");
        }
    }

    private void processDeleteNonPlanetarySystemObjectsCommand(String line) {
        String params = line.substring(10).trim();
        boolean found = false;

        for (PlanetarySystem sys : PlanetarySystem.getListSystem()) {
            if (sys.getName().equals(params)) {
                found = true;

                List<alef> objectsToRemove = new ArrayList<>();

                for (alef obj : root.getListObj()) {
                    if (!sys.getListObjInSys().contains(obj)) {
                        objectsToRemove.add(obj);
                    }
                }

                for (alef obj : objectsToRemove) {
                    root.removeObj(obj);
                }
                break;
            }
        }

        if (!found) {
            println("Планетарну систему з назвою '" + params + "' не знайдено.\n");
        }
    }

    private void listObjectsInWorld(String line) {
        boolean found = false;
        List<alef> list = root.getListObj();
        println("На карті находиться " + root.getListObj().size() + " мікрооб'єктів");

        // Перевірка флагу для сортування
        if (line.contains("-sortObjectsAlphabetically")) {
            sortObjectsAlphabetically(list);
        } else if (line.contains("-sortObjectsHealt")) {
            sortObjectsHealt(list);
        } else if (line.contains("-sortObjectsSpead")) {
            sortObjectSpead(list);
        }

        for (alef obj : list) {
            println(obj.toString());
            found = true;
        }
        if (!found) {
            println("На карті не існує мікрооб'єктів.\n");
        }
    }

    private void listActiveObjects(String line) {
        boolean found = false;
        List<alef> list = new ArrayList<>();
        for (alef obj : root.getListObj()) {
            if (obj != null && obj.isActive()) {
                list.add(obj);
            }
        }

        // Перевірка флагу для сортування
        if (line.contains("-sortObjectsAlphabetically")) {
            sortObjectsAlphabetically(list);
        } else if (line.contains("-sortObjectsHealt")) {
            sortObjectsHealt(list);
        } else if (line.contains("-sortObjectsSpead")) {
            sortObjectSpead(list);
        }

        println("На карті находиться " + list.size() + " активних мікрооб'єктів");
        for (alef obj : list) {
            println(obj.toString());
            found = true;
        }
        if (!found) {
            println("На карті не існує активних мікрооб'єктів.\n");
        }
    }

    private void processListObjectsInPlanetarySystemCommand(String line) {
        String params = line.substring(13).trim();

        boolean found = false;
        for (PlanetarySystem sys : PlanetarySystem.getListSystem()) {
            if (sys.getName().equals(params)) {
                println("В системі " + params + " находиться " + sys.getListObjInSys().size() + " мікрооб'єктів.");

                List<alef> list = new ArrayList<>(sys.getListObjInSys());

                // Перевірка флагу для сортування
                if (line.contains("-sortObjectsAlphabetically")) {
                    sortObjectsAlphabetically(list);
                } else if (line.contains("-sortObjectsHealt")) {
                    sortObjectsHealt(list);
                } else if (line.contains("-sortObjectsSpead")) {
                    sortObjectSpead(list);
                }

                for (alef obj : list) {
                    println(obj.toString());
                }
                found = true;
            }
        }
        if (!found) {
            println("Не існує мікрооб'єктів в системі " + params + ".\n");
        }
    }

    private void displayHelp() {
        println("Доступні команди:");
        println("/search [назваОб'єкта] - Пошук об'єкта за назвою");
        println("/deletePS [назваСистеми] - Видалення планетарної системи за назвою");
        println("/deleteNPS [назваСистеми] - Видалення всіх об'єктів, що не належать до вказаної планетарної системи");
        println("/listObjInSys [назваСистеми] - Список мікрооб'єктів у вказаній планетарній системі");
        println("/listObjInWorld - Список всіх мікрооб'єктів у світі");
        println("/listObjActive - Список активних мікрооб'єктів у світі\n");
        println("Флаги сортування:");
        println("-sortObjectsAlphabetically - Сортувати об'єкти за алфавітом");
        println("-sortObjectsHealt - Сортувати об'єкти за станом здоров'я");
        println("-sortObjectsSpead - Сортувати об'єкти за швидкістю");
    }

    private void sortObjectsAlphabetically(List<alef> list) {
        list.sort(Comparator.comparing(alef::getName));
    }

    private void sortObjectsHealt(List<alef> list) {
        list.sort(Comparator.comparingDouble(alef::getHealth).reversed());
    }

    private void sortObjectSpead(List<alef> list) {
        list.sort(Comparator.comparingInt(alef::getMSpead).reversed());
    }
}
