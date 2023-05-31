package com.example.kursova;

import Person.alef;
import Person.beta;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PlanetarySystem extends StackPane {

    public static Image systemImage = new Image(Main.class.getResource("System.png").toString(), 400, 400, false, false);
    private final ObservableList<alef> listObjInSys = FXCollections.observableArrayList();
    public static ObservableList<PlanetarySystem> listSystem = FXCollections.observableArrayList();
    private Fraction fraction;
    private String name;
    private int x;
    private int y;
    private static final int size = 800;
    private ImageView imageView = new ImageView(systemImage); // Текстура
    private Rectangle hitbox = new Rectangle(size, size); // Хітбокс
    private Text text;
    private Text num;

    private static Random random = new Random();

    private double maxHp = 400;
    private double hp = maxHp;

    private int resources = 20;
    private double damage = 50;

    public PlanetarySystem(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;

        this.setPrefSize(size, size);

        hitbox.setFill(Color.GRAY);
        hitbox.setStroke(Color.BLACK);
        hitbox.setStrokeWidth(3);
        hitbox.setArcWidth(size);
        hitbox.setArcHeight(size);


        Circle circle = new Circle(200, Color.TRANSPARENT);
        circle.setFill(new ImagePattern(imageView.getImage()));

        text = new Text(name);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        num = new Text(String.valueOf(listObjInSys.size()));
        num.setFont(Font.font("Arial", FontWeight.BOLD, 30));

        VBox vbox = new VBox(text, num);
        vbox.setAlignment(Pos.CENTER);
        listSystem.add(this);
        this.getChildren().addAll(hitbox, circle, vbox);


        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                listObjInSys.clear();
                circle.setRotate(circle.getRotate() + 1);
                if (fraction != null) {
                    text.setText(name + " " + fraction.getName());
                } else {
                    text.setText(name + " " + "Вільна");
                }
                Iterator<alef> iterator = Main.root.getListObj().iterator();
                while (iterator.hasNext()) {
                    alef object = iterator.next();
                    if (getBoundsInParent().intersects(object.getBoundsInParent())) {
                        if (!listObjInSys.contains(object)) {
                            listObjInSys.add(object);
                        }
                    }
                }
                num.setText(String.valueOf(hp));
            }
        };

        animationTimer.start();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, event -> {
                    for (alef object : Main.root.getListObj()) {
                        if (!(object.getFraction() == getFraction())) {
                            if (getBoundsInParent().intersects(object.getBoundsInParent())) {
                                if (hp <= 0) {
                                    if (fraction != null) {
                                        fraction.removeSystem(PlanetarySystem.this);
                                    }
                                    if (object.getFraction() != null) {
                                        object.getFraction().addSystem(PlanetarySystem.this);
                                    }
                                }
                                if (getBoundsInParent().intersects(object.getBoundsInParent())) {
                                    attack(object);
                                    takeDamage(object);
                                }
                            }
                        }
                    }
                }),
                new KeyFrame(Duration.millis(700), event -> {})
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void generateResources() {
        Platform.runLater(() -> {
            if (getFraction() != null) {
                resources++;
                if (getResources() >= 10) {
                    Random random1 = new Random();
                    if(random1.nextInt(100) > 50){
                        setResources(getResources() - 10);
                        alef obj = new alef();
                        obj.setX(getRandomXinSys());
                        obj.setY(getRandomYinSys());
                        Main.root.addObj(obj);
                        fraction.addObj(obj);
                    }else {
                        setResources(getResources() - 10);
                        beta obj = new beta();
                        obj.setX(getRandomXinSys());
                        obj.setY(getRandomYinSys());
                        Main.root.addObj(obj);
                        fraction.addObj(obj);
                    }
                }
            }
        });
    }

    public void setFraction(Fraction fraction) {
        if (this.fraction != null) {
            this.fraction.removeSystem(this);
        }
        this.fraction = fraction;
        this.hp = maxHp;
        hitbox.setFill(fraction.getFcolor());
        text.setText(name + " " + fraction.getName());
    }

    public void removeFraction() {
        if (this.fraction != null) {
            this.fraction.removeSystem(this);
            this.fraction = null;
            hitbox.setFill(Color.GRAY);
            text.setText(name);
        }
    }

    public Fraction getFraction() {
        return fraction;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public double getHp() {
        return hp;
    }

    public double getRotation() {
        return imageView.getRotate();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public double getWidthP() {
        return hitbox.getWidth();
    }

    public double getHeightP() {
        return hitbox.getHeight();
    }

    public static ObservableList<PlanetarySystem> getListSystem() {
        return listSystem;
    }

    public ObservableList<alef> getListObjInSys() {
        return listObjInSys;
    }

    public int getResources() {
        return resources;
    }

    public void setResources(int resources) {
        this.resources = resources;
    }

    public int getRandomXinSys() {
        return random.nextInt((int) getPrefWidth()) + getX();
    }

    public int getRandomYinSys() {
        return random.nextInt((int) getPrefHeight()) + getY();
    }

    public void attack(alef obj) {
        if (obj != null) {
            if (obj.getFraction() == null || !obj.getFraction().equals(this.fraction)) {
                // Розрахунок пошкодження атаки
                double attackDamage = damage;

                // Зменшення пошкодження в залежності від броні цілі
                attackDamage -= obj.getArmor();

                // Застосування пошкодження до цілі
                obj.setHealth(obj.getHealth() - attackDamage);

                // Перевірка на смерть цілі
                if (obj.getHealth() <= 0) {
                    System.out.println("Ціль " + obj.getName() + " була убита!");
                    obj.dead();
                } else {
                    System.out.println("Ціль " + obj.getName() + " зазнала пошкоджень: " + attackDamage + " HP");
                }
            }
        }
    }

    public void takeDamage(alef obj) {
        if(this.getFraction() == null){
            // Розрахунок пошкодження атаки
            double attackDamage = obj.getDamage();
            // Застосування пошкодження до цілі
            setHp(getHp() - attackDamage);
        }else if (!this.getFraction().equals(obj.getFraction())) {
            // Розрахунок пошкодження атаки
            double attackDamage = obj.getDamage();
            // Застосування пошкодження до цілі
            setHp(getHp() - attackDamage);
        }
    }

    public ImageView getObjImage(double size) {
        // Отримуємо зображення об'єкту з використанням снапшота
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        Image image = this.snapshot(parameters, null);

        // Змінюємо розмір отриманого зображення до бажаного розміру
        double scaleFactor = size / Math.max(image.getWidth(), image.getHeight());
        WritableImage scaledImage = new WritableImage(
                (int) (image.getWidth() * scaleFactor),
                (int) (image.getHeight() * scaleFactor));
        PixelWriter writer = scaledImage.getPixelWriter();
        PixelReader reader = image.getPixelReader();
        for (int y = 0; y < scaledImage.getHeight(); y++) {
            for (int x = 0; x < scaledImage.getWidth(); x++) {
                writer.setArgb(x, y, reader.getArgb((int) (x / scaleFactor), (int) (y / scaleFactor)));
            }
        }
        return new ImageView(scaledImage);
    }
}
