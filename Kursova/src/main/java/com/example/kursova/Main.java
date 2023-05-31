package com.example.kursova;

import Person.alef;
import Person.beta;
import Person.gimel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.io.*;
import java.util.*;

public class Main extends Application {

    public static final int WINDOW_WIDTH = 1600; // Ширина вікна
    public static final int WINDOW_HEIGHT = 800; // Висота вікна

    public static String Title = "Курсова робота"; // Заголовок вікна
    public static Image icon = new Image(Objects.requireNonNull(Main.class.getResource("icoStellaris.png")).toString()); // Іконка вікна

    public Scene scene;
    public static Console console = new Console(); // Створення консолі для відображення виводу
    public static CustomLog log;

    static {
        try {
            log = new CustomLog(); // Ініціалізація файлу журналу
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final long startTime = System.nanoTime();

    public static World root = new World(5000, 5000); // Створення світу з вказаними розмірами

    @Override
    public void start(Stage primaryStage) {

        // Створення планетних систем
        PlanetarySystem sun = new PlanetarySystem("Сонце", 500, 500);
        PlanetarySystem sirus = new PlanetarySystem("Сіріус", 500, 3500);
        PlanetarySystem prozion = new PlanetarySystem("Проціон", 3500, 1000);
        PlanetarySystem adam = new PlanetarySystem("Адам", 2300, 2400);

        // Додавання планетних систем до світу
        root.addSystem(sun);
        root.addSystem(sirus);
        root.addSystem(prozion);
        root.addSystem(adam);

        // Додавання меню
        addMenu();

        root.getChildren().add(console); // Додавання консолі до кореневого вузла
        console.setAlignment(Pos.CENTER); // Встановлення вирівнювання консолі по центру
        console.setLayoutX((((double) WINDOW_WIDTH / 2) + console.getWidth()) / 2); // Встановлення положення консолі по горизонталі
        console.setLayoutY(0); // Встановлення положення консолі по вертикалі


        Fraction human = new Fraction("Люди",Color.rgb(0,255,0)); // Створення фракції "Люди" зі зеленим кольором
        human.addSystem(sun); // Додавання планетної системи "Сонце" до фракції "Люди"
        human.addSystem(prozion);


        Fraction mushroom = new Fraction("Гриби",Color.rgb(0,0,255)); // Створення фракції "Гриби" з полупрозорим синім кольором
        mushroom.addSystem(sirus); // Додавання планетної системи "Сіріус" до фракції "Гриби"
        mushroom.addSystem(adam);

        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT); // Створення сцени з кореневим вузлом світу

        // Налаштування вікна
        primaryStage.setTitle(Title);
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(scene);
        primaryStage.show();


        Set<KeyCode> pressedKeys = new HashSet<>();
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case INSERT -> showMoreCreate();
                case DELETE -> {
                    for (alef obj : root.getListObj()) {
                        if(obj != null && obj.isActive()){
                            obj.dead();
                        }
                    }
                }
                case F1 -> {
                    if (console.isVisible()){
                        console.hide();
                    }else console.show();
                }
                case ESCAPE -> {
                    for (alef obj : root.getListObj()) {
                        if(obj != null && obj.isActive()){
                           obj.setActive(false);
                           obj.getHitbox().setStroke(Color.YELLOW);
                        }
                    }
                }
                default -> {
                }
            }
            pressedKeys.add(event.getCode());
            if (pressedKeys.contains(KeyCode.LEFT)) {
                for (alef obj : root.getListObj()) {
                    if (obj != null && obj.isActive()) {
                        int move = obj.getX() - obj.getMSpead();
                        obj.setX(move);
                        obj.setLayoutX(move);
                    }
                }
            }
            if (pressedKeys.contains(KeyCode.UP)) {
                for (alef obj : root.getListObj()) {
                    if(obj != null && obj.isActive()){
                    int move = obj.getY() - obj.getMSpead();

                    }
                }
            }
            if (pressedKeys.contains(KeyCode.RIGHT)) {
                for (alef obj : root.getListObj()) {
                    if(obj != null && obj.isActive()){
                    int move = obj.getX() + obj.getMSpead();
                    obj.setX(move);
                    obj.setLayoutX(move);
                    }
                }
            }
            if (pressedKeys.contains(KeyCode.DOWN)) {
                for (alef obj : root.getListObj()) {
                    if(obj != null && obj.isActive()){
                    int move = obj.getY() + obj.getMSpead();
                    obj.setY(move);
                    obj.setLayoutY(move);
                }
                }
            }
        });

        scene.setOnKeyReleased(event -> {
            pressedKeys.remove(event.getCode());
        });

    }

    public void addMenu(){

        // Створення верхнього меню
        MenuBar menuBar = new MenuBar();
        menuBar.setOpacity(0.3);

        // Створення обєктів меню
        Menu MenuOptions = new Menu("Настройки");
        menuBar.getMenus().add(MenuOptions);
        MenuItem save = new MenuItem("Зберегти");
        MenuOptions.getItems().add(save);

        save.setOnAction(actionEvent -> {
            SaveLoad.saveLayoutData();
        });

        MenuItem load = new MenuItem("Завантажити");
        MenuOptions.getItems().add(load);

        load.setOnAction(actionEvent -> {
            SaveLoad.loadWorldFromFile();
        });

        Menu MenuCreate = new Menu("Cтворити");
        menuBar.getMenus().add(MenuCreate);

        Menu MenuSearch = new Menu("Інформація");
        menuBar.getMenus().add(MenuSearch);


        MenuItem search = new MenuItem("Інформація");
        search.setOnAction(actionEvent -> info());
        MenuSearch.getItems().add(search);

        // Створення пункту меню "Корабель класу Alef" та його обробник події
        MenuItem menuItemAddAlef = new MenuItem("Корабель");
        menuItemAddAlef.setOnAction(event -> showMoreCreate());
        MenuCreate.getItems().add(menuItemAddAlef);

        root.getChildren().add(menuBar);

        //Анімація для Панелі користувача
        menuBar.setOnMouseEntered(event -> {
            menuBar.setOpacity(1); // збільшуємо прозорість, щоб область стала видимою
        });
        menuBar.setOnMouseExited(event -> {
            menuBar.setOpacity(0.3); // зменшуємо прозорість, щоб область знову стала невидимою
        });
    }

    public void showMoreCreate() {
        Stage stage = new Stage();
        stage.setTitle("Настройки створювання");

        TextField textFieldName = new TextField();
        textFieldName.setPromptText("Впишіть ім'я корабля");
        textFieldName.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        TextField healthTextField = new TextField();
        healthTextField.setPromptText("Здоров'я");
        healthTextField.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        TextField damageTextField = new TextField();
        damageTextField.setPromptText("Урон");
        damageTextField.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        TextField xTextField = new TextField();
        xTextField.setPromptText("X");
        xTextField.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        TextField yTextField = new TextField();
        yTextField.setPromptText("Y");
        yTextField.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        RadioButton alefRadioButton = new RadioButton("alef");
        alefRadioButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
        RadioButton betaRadioButton = new RadioButton("beta");
        betaRadioButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
        RadioButton gimelRadioButton = new RadioButton("gimel");
        gimelRadioButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        HBox boxF = new HBox();
        boxF.setAlignment(Pos.CENTER);
        ToggleGroup toggleGroupF = new ToggleGroup();

        RadioButton nullRadioButton = new RadioButton("Без фракціх");
        nullRadioButton.setSelected(true);
        nullRadioButton.setToggleGroup(toggleGroupF);
        boxF.getChildren().add(nullRadioButton);
        gimelRadioButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        for (Fraction fraction : Fraction.getListFraction()) {
            RadioButton radioButton = new RadioButton(fraction.getName());
            radioButton.setToggleGroup(toggleGroupF);
            radioButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
            boxF.getChildren().add(radioButton);
        }

        // Встановлюємо радіокнопку "alef" вибраною за замовчуванням
        alefRadioButton.setSelected(true);

        HBox boxRC = new HBox();
        boxRC.setAlignment(Pos.CENTER);
        ToggleGroup toggleGroupRC = new ToggleGroup();
        alefRadioButton.setToggleGroup(toggleGroupRC);
        betaRadioButton.setToggleGroup(toggleGroupRC);
        gimelRadioButton.setToggleGroup(toggleGroupRC);
        boxRC.getChildren().addAll(alefRadioButton, betaRadioButton, gimelRadioButton);


        Button setButton = new Button("Створити");
        setButton.setOnAction(event -> {
            String name = textFieldName.getText().isEmpty() ? alef.getRandomName() : textFieldName.getText();
            int health = healthTextField.getText().isEmpty() ? 100 : Integer.parseInt(healthTextField.getText());
            int damage = damageTextField.getText().isEmpty() ? 20 : Integer.parseInt(damageTextField.getText());
            int x = xTextField.getText().isEmpty() ? alef.getRandomX() : Integer.parseInt(xTextField.getText());
            int y = yTextField.getText().isEmpty() ? alef.getRandomY() : Integer.parseInt(yTextField.getText());

            alef obj = null;

            if (alefRadioButton.isSelected()) {
                obj = new alef(name, health, damage, x, y);
                root.addObj(obj);
            }

            if (betaRadioButton.isSelected()) {
                obj = new beta(name, health, damage, x, y);
                root.addObj(obj);
            }

            if (gimelRadioButton.isSelected()) {
                obj = new gimel(name, health, damage, x, y);
                root.addObj(obj);
            }

            // Додано перевірку на вибір радіобатону зі списку
            for (Node node : boxF.getChildren()) {
                if (!nullRadioButton.isSelected()) {
                    if ( node instanceof RadioButton radioButton) {
                            if (radioButton.isSelected()) {
                                Objects.requireNonNull(obj).setFraction(Objects.requireNonNull(Fraction.getFractionByName(radioButton.getText())));
                            }
                        }
                }
            }


            stage.close();
        });
        setButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8px 20px; -fx-border-radius: 5px;");
        Button cancelButton = new Button("Скасувати");
        cancelButton.setOnAction(event -> stage.close());
        cancelButton.setStyle("-fx-background-color: #ddd; -fx-text-fill: black; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8px 20px; -fx-border-radius: 5px; -fx-border-color: gray; -fx-border-width: 1px;");

        HBox buttonBox = new HBox(setButton, cancelButton);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);
        HBox xy = new HBox(xTextField, yTextField);
        xy.setAlignment(Pos.CENTER);
        VBox centerBox = new VBox();
        centerBox.setSpacing(10);
        centerBox.getChildren().addAll(
                textFieldName,
                healthTextField,
                damageTextField,
                new Label("Координати:"),
                xy,
                new Label("Виберіть тип:"),
                boxRC,
                new Label("Виберіть фракцію:"),
                boxF

        );
        centerBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(centerBox, buttonBox);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-background-color: #FFE4B5; -fx-border-color: black; -fx-border-radius: 5px;");

        Scene scene = new Scene(vbox, 350, 375);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        // Задайте ширину стовпців з PromptText:
        textFieldName.setPrefColumnCount(10);
        healthTextField.setPrefColumnCount(10);
        damageTextField.setPrefColumnCount(10);
        xTextField.setPrefColumnCount(10);
        yTextField.setPrefColumnCount(10);
    }

    public void info() {
        Stage stage = new Stage();

        // Створення елементів
        Label titleLabel = new Label("Статистика");

        Label microObjectsLabel = new Label("Кількість мікрооб'єктів : " + root.getListObj().size());
        Label macroObjectsLabel = new Label("Кількість макрооб'єктів : " + PlanetarySystem.listSystem.size());
        Label timeLabel = new Label("Час роботи програми : 0 сек.");

        //Список мікрообєктів

        ListView<alef> microObjectsListView = new ListView<>(root.getListObj());
        microObjectsListView.setCellFactory(list -> new ListCell<>() {
            @Override
            public void updateItem(alef item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    // Створити HBox для відображення тексту та об'єкта
                    HBox itemsP = new HBox();

                    Label nameLabel = new Label("Ім'я : " + item.getName());
                    // Створити елементи для поля здоров'я
                    Label healthLabel = new Label("Здоров'я : " + item.getHealth());
                    // Створити елементи для поля урону
                    Label damageLabel = new Label("Урон : " + item.getDamage());
                    // Створити елементи для поля координат
                    Label coordinatesLabel = new Label("Координати :(" + item.getX() + ";" + item.getY() + ")");

                    // Створити кнопки
                    Button searchButton = new Button("Найти");
                    Button resetButton = new Button("Змінити");
                    Button cloneButton = new Button("Клонувати");
                    Button deleteButton = new Button("Видалити");

                    searchButton.setOnAction(event -> root.setPositionSearch(item));

                    resetButton.setOnAction(event -> {
                        stage.close();
                        reset(item);
                    });

                    cloneButton.setOnAction(event -> {
                        root.addObj(item.clone());
                    });

                    deleteButton.setOnAction(event -> {
                        root.removeObj(item);
                    });

                    // Створити HBox для кнопок
                    HBox buttonBox = new HBox(10, searchButton, resetButton,cloneButton, deleteButton);
                    buttonBox.setAlignment(Pos.CENTER_RIGHT);

                    // Створити VBox для всіх елементів
                    VBox vBox = new VBox(10,
                            new HBox(10, nameLabel),
                            new HBox(10, healthLabel),
                            new HBox(10, damageLabel),
                            new HBox(10, coordinatesLabel),
                            buttonBox
                    );
                    vBox.setPadding(new Insets(10));

                    itemsP.getChildren().addAll(item.getSnapshotParameters(), vBox);

                    setGraphic(itemsP);
                }
            }
        });

        //Список макрообєктів
        ListView<PlanetarySystem> macroObjectsListView = new ListView<>(PlanetarySystem.listSystem);
        macroObjectsListView.setCellFactory(list -> new ListCell<>() {
            @Override
            public void updateItem(PlanetarySystem item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    // Створити HBox для відображення тексту та об'єкта
                    HBox itemsP = new HBox();

                    Label nameLabel = new Label("Ім'я : " + item.getName());


                    Label fractionNameLabel = new Label("Фракція : Відсутня");
                    if(item.getFraction()!= null){
                        fractionNameLabel.setText("Фракція : " + item.getFraction().getName());
                    }

                    // Елементи для кількість ресурсів
                    Label resurseLable = new Label("Кількість ресурсів : " + item.getResources());

                    // Елементи для поля координат
                    Label coordinatesLabel = new Label("Координати :(" + item.getX() + ";" + item.getY() + ")");


                    // Створити кнопки
                    Button searchButton = new Button("Найти");

                    searchButton.setOnAction(event -> root.setPositionSearch(item));


                    // Створити HBox для кнопок
                    HBox buttonBox = new HBox(searchButton);
                    buttonBox.setAlignment(Pos.CENTER_RIGHT);

                    // Створити VBox для всіх елементів
                    VBox vBox = new VBox(10,
                            new HBox(10, nameLabel),
                            new HBox(10, fractionNameLabel),
                            new HBox(10, resurseLable),
                            new HBox(10, coordinatesLabel),
                            buttonBox
                    );
                    vBox.setPadding(new Insets(10));

                    itemsP.getChildren().addAll(item.getObjImage(200), vBox);

                    setGraphic(itemsP);
                }
            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    microObjectsLabel.setText("Кількість мікрооб'єктів : " + root.getListObj().size());
                    macroObjectsLabel.setText("Кількість макрооб'єктів : " + PlanetarySystem.listSystem.size());
                    long currentTime = System.nanoTime();
                    double elapsedTime = (currentTime - startTime) / 1_000_000_000.0;
                    timeLabel.setText("Час роботи програми : " + elapsedTime + " сек.");
                    microObjectsListView.setItems(null);
                    microObjectsListView.setItems(root.getListObj());

                    macroObjectsListView.setItems(null);
                    macroObjectsListView.setItems(PlanetarySystem.listSystem);

                });
            }
        }, 0, 500);


        // Додавання елементів на сцену
        VBox rootW = new VBox();
        rootW.getChildren().addAll(titleLabel, microObjectsLabel, macroObjectsLabel, timeLabel,
                new Label("Мікрооб'єкти:"), microObjectsListView,
                new Label("Макрооб'єкти:"), macroObjectsListView);
        microObjectsListView.setPrefSize(450, 200);
        macroObjectsListView.setPrefSize(400, 250);
        Scene scene = new Scene(rootW);

        stage.setScene(scene);
        stage.setTitle("Статистика");
        stage.show();
    }

    public void reset( alef obj) {
        Stage stage = new Stage();
        stage.setTitle(obj.getName());

        TextField textFieldName = new TextField();
        textFieldName.setPromptText("Впишіть ім'я корабля");
        textFieldName.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        TextField healthTextField = new TextField();
        healthTextField.setPromptText("Здоров'я");
        healthTextField.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        TextField damageTextField = new TextField();
        damageTextField.setPromptText("Урон");
        damageTextField.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        TextField xTextField = new TextField();
        xTextField.setPromptText("X");
        xTextField.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        TextField yTextField = new TextField();
        yTextField.setPromptText("Y");
        yTextField.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black; -fx-background-color: white; -fx-border-color: gray;");

        RadioButton alefRadioButton = new RadioButton("alef");
        alefRadioButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
        RadioButton betaRadioButton = new RadioButton("beta");
        betaRadioButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");
        RadioButton gimelRadioButton = new RadioButton("gimel");
        gimelRadioButton.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        // Встановлюємо радіокнопку "alef" вибраною за замовчуванням
        alefRadioButton.setSelected(true);

        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        ToggleGroup toggleGroup = new ToggleGroup();
        alefRadioButton.setToggleGroup(toggleGroup);
        betaRadioButton.setToggleGroup(toggleGroup);
        gimelRadioButton.setToggleGroup(toggleGroup);
        box.getChildren().addAll(alefRadioButton, betaRadioButton, gimelRadioButton);

        Button setButton = new Button("Змінити");
        setButton.setOnAction(event -> {
            String name = textFieldName.getText().isEmpty() ? obj.getName() : textFieldName.getText();
            double health = healthTextField.getText().isEmpty() ? obj.getHealth() : Integer.parseInt(healthTextField.getText());
            double damage = damageTextField.getText().isEmpty() ? obj.getDamage() : Integer.parseInt(damageTextField.getText());
            int x = xTextField.getText().isEmpty() ? obj.getX() : Integer.parseInt(xTextField.getText());
            int y = yTextField.getText().isEmpty() ? obj.getY() : Integer.parseInt(yTextField.getText());
            root.removeObj(obj);
            if (alefRadioButton.isSelected()) {
                root.addObj(new alef(name, health, damage, x, y));
            }

            if (betaRadioButton.isSelected()) {
                root.addObj(new beta(name, health, damage, x, y));
            }

            if (gimelRadioButton.isSelected()) {
                root.addObj(new gimel(name, health, damage, x, y));
            }
            stage.close();
        });
        setButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8px 20px; -fx-border-radius: 5px;");
        Button cancelButton = new Button("Скасувати");
        cancelButton.setOnAction(event -> stage.close());
        cancelButton.setStyle("-fx-background-color: #ddd; -fx-text-fill: black; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8px 20px; -fx-border-radius: 5px; -fx-border-color: gray; -fx-border-width: 1px;");

        HBox buttonBox = new HBox(setButton, cancelButton);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);
        HBox xy = new HBox(xTextField, yTextField);
        xy.setAlignment(Pos.CENTER);
        VBox centerBox = new VBox();
        centerBox.setSpacing(10);
        centerBox.getChildren().addAll(
                textFieldName,
                healthTextField,
                damageTextField,
                new Label("Координати:"),
                xy,
                new Label("Виберіть тип:"),
                box
        );
        centerBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(centerBox, buttonBox);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-background-color: #FFE4B5; -fx-border-color: black; -fx-border-radius: 5px;");

        Scene scene = new Scene(vbox, 350, 300);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

// Задайте ширину стовпців з PromptText:
        textFieldName.setPrefColumnCount(10);
        healthTextField.setPrefColumnCount(10);
        damageTextField.setPrefColumnCount(10);
        xTextField.setPrefColumnCount(10);
        yTextField.setPrefColumnCount(10);


    }
}

