package com.example.kursova;

import Person.alef;
import Person.beta;
import Person.gimel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SaveLoad {

    private static Pane content = Main.root.getContent();

    public static void saveLayoutData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Layout Data");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.save"));
        File file = fileChooser.showSaveDialog(new Stage());
        if (file == null) {
            return;
        }

        // Створіть список для зберігання даних
        List<MicroObj> alefDataList = new ArrayList<>();
        List<MicroObj> betaDataList = new ArrayList<>();
        List<MicroObj> gimelDataList = new ArrayList<>();
        List<FractionObj> fractionsDataList = new ArrayList<>();
        List<MacroObj> spaceSystemDataList = new ArrayList<>();

        for(PlanetarySystem planetarySystem : PlanetarySystem.listSystem){
            if(planetarySystem instanceof  PlanetarySystem){
                MacroObj obj = new MacroObj(planetarySystem.getName(),planetarySystem.getX(),planetarySystem.getY(),planetarySystem.getFraction().getName());
                spaceSystemDataList.add(obj);
            }
        }

        for(Fraction node : Fraction.getListFraction()){
            if(node instanceof Fraction){
                FractionObj obj = new FractionObj(node.getName(),node.getFcolor());
                fractionsDataList.add(obj);
            }
        }
        // Пройдіться по елементах контейнера content і перетворіть кожен об'єкт Alef на об'єкт AlefData
        for (Node obj : content.getChildren()) {
            if (obj.getClass() == alef.class) {
                MicroObj alefData = new MicroObj(((alef) obj).getName(), ((alef) obj).getHealth(), ((alef) obj).getDamage(), ((alef) obj).getX(), ((alef) obj).getY(),((alef) obj).isActive(),((alef) obj).getFraction().getName());
                alefDataList.add(alefData);
            } else if (obj.getClass() == beta.class) {
                MicroObj betaData = new MicroObj(((alef) obj).getName(), ((alef) obj).getHealth(), ((alef) obj).getDamage(), ((alef) obj).getX(), ((alef) obj).getY(),((alef) obj).isActive(),((alef) obj).getFraction().getName());
                betaDataList.add(betaData);
            } else if (obj.getClass() == gimel.class) {
                MicroObj gimeLData = new  MicroObj(((alef) obj).getName(), ((alef) obj).getHealth(), ((alef) obj).getDamage(), ((alef) obj).getX(), ((alef) obj).getY(),((alef) obj).isActive(),((alef) obj).getFraction().getName());
                gimelDataList.add(gimeLData);
            }
        }

        SettingsData settingsData = new SettingsData(content.getLayoutX(), content.getLayoutY());
        MacroObjectsData macroObjectsData = new MacroObjectsData(spaceSystemDataList);
        MicroObjectsData microObjectsData = new MicroObjectsData(alefDataList, betaDataList,gimelDataList);
        FractionsData fractionsData = new FractionsData(fractionsDataList);

        // Створіть об'єкт LayoutData з об'єктом MicroObjectsData
        LayoutData layoutData = new LayoutData(settingsData, microObjectsData,macroObjectsData,fractionsData);

        // Серіалізуйте об'єкт LayoutData в JSON
        Gson gson = new Gson();
        String json = gson.toJson(layoutData);

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(json);
            writer.close();
            System.out.println("Дані збережено у файл: " + file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class LayoutData {
        @SerializedName("settings")
        private SettingsData settings;
        @SerializedName("microObjects")
        private MicroObjectsData microObjects;
        @SerializedName("macroObjects")
        private MacroObjectsData macroObjects;
        @SerializedName("FractionData")
        private FractionsData fractionsData;

        public LayoutData(SettingsData settings, MicroObjectsData microObjects,MacroObjectsData macroObjectsData, FractionsData fractionsData) {
            this.settings = settings;
            this.microObjects = microObjects;
            this.macroObjects = macroObjectsData;
            this.fractionsData = fractionsData;
        }
    }

    private static class SettingsData {
        @SerializedName("layoutX")
        private double layoutX;
        @SerializedName("layoutY")
        private double layoutY;

        public SettingsData(double layoutX, double layoutY) {
            this.layoutX = layoutX;
            this.layoutY = layoutY;
        }
    }

    private static class MicroObjectsData {
        @SerializedName("alefList")
        private List<MicroObj> alefList;
        @SerializedName("betaList")
        private List<MicroObj> betaList;
        @SerializedName("gimelList")
        private List<MicroObj> gimelList;

        public MicroObjectsData(List<MicroObj> alefList, List<MicroObj> betaList, List<MicroObj> gimelList) {
            this.alefList = alefList;
            this.betaList = betaList;
            this.gimelList = gimelList;
        }
    }


    private static class MacroObjectsData {
        @SerializedName("spaceSystemList")
        private List<MacroObj> sysList;

        public MacroObjectsData(List<MacroObj> sysList) {
            this.sysList = sysList;
        }
    }

    public static class MacroObj {
        @SerializedName("name")
        private String name;
        @SerializedName("x")
        private int x;
        @SerializedName("y")
        private int y;
        @SerializedName("fractionNmae")
        private String fractionName;

        public MacroObj(String name, int x, int y,String fractionName) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.fractionName = fractionName;
        }
    }

    public static class MicroObj {
        @SerializedName("name")
        private String name;
        @SerializedName("health")
        private double health;
        @SerializedName("damage")
        private double damage;
        @SerializedName("x")
        private int x;
        @SerializedName("y")
        private int y;
        @SerializedName("active")
        private boolean active;
        @SerializedName("fractionNmae")
        private String fractionName;

        public MicroObj(String name, double health, double damage, int x, int y, boolean active, String fractionName) {
            this.name = name;
            this.health = health;
            this.damage = damage;
            this.x = x;
            this.y = y;
            this.active = active;
            this.fractionName = fractionName;
        }
    }

    private static class FractionsData {
        @SerializedName("Fractions")
        private List<FractionObj> fractionList;

        public FractionsData(List<FractionObj> fractionList) {
            this.fractionList = fractionList;
        }
    }

    private static class FractionObj {
        @SerializedName("name")
        private String name;
        @SerializedName("Color")
        private String fcolorName;

        public FractionObj(String name, Color fcolor) {
            this.name = name;
            this.fcolorName = fcolor.toString();
        }
    }

    public static void loadWorldFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Layout Data");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.save"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file == null) {
            return;
        }
        try (Reader reader = new FileReader(file)) {
            Gson gson = new GsonBuilder().create();
            LayoutData layoutData = gson.fromJson(reader, LayoutData.class);

            content.getChildren().clear();
            PlanetarySystem.getListSystem().clear();
            Fraction.getListFraction().clear();

            content.setLayoutX(layoutData.settings.layoutX);
            content.setLayoutY(layoutData.settings.layoutY);

            for (FractionObj fraction : layoutData.fractionsData.fractionList){
                Fraction.getListFraction().add(new Fraction(fraction.name,Color.valueOf(fraction.fcolorName)));
            }

            for (MicroObj alefData : layoutData.microObjects.alefList) {
                alef obj = new alef(alefData.name, alefData.health, alefData.damage, alefData.x, alefData.y);
                obj.setActive(alefData.active);
                Main.root.addObj(obj);
                Fraction.getFractionByName(alefData.fractionName).addObj(obj);
            }

            for (MicroObj betaData : layoutData.microObjects.betaList) {
                beta obj = new beta(betaData.name, betaData.health, betaData.damage, betaData.x, betaData.y);
                Main.root.addObj(obj);
                Fraction.getFractionByName(betaData.fractionName).addObj(obj);
            }

            for (MicroObj gimelData : layoutData.microObjects.gimelList) {
                gimel obj = new gimel(gimelData.name, gimelData.health, gimelData.damage, gimelData.x, gimelData.y);
                Main.root.addObj(obj);
                Fraction.getFractionByName(gimelData.fractionName).addObj(obj);
            }

            for (MacroObj macroObj : layoutData.macroObjects.sysList){
                PlanetarySystem planetarySystem = new PlanetarySystem(macroObj.name , macroObj.x , macroObj.y);
                Main.root.addSystem(planetarySystem);
                Fraction.getFractionByName(macroObj.fractionName).addSystem(planetarySystem);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
