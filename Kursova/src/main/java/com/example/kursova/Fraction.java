package com.example.kursova;

import Person.alef;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Fraction {
    private final Color fcolor;
    private static final ObservableList<Fraction> listFraction = FXCollections.observableArrayList();
    private final ObservableList<PlanetarySystem> listSystem;
    private final ObservableList<alef> listObj;
    private final String name;

    public Fraction(String name, Color fcolor) {
        this.name = name;
        this.fcolor = fcolor;
        this.listSystem = FXCollections.observableArrayList();
        this.listObj = FXCollections.observableArrayList();

        listFraction.add(this);

        listSystem.addListener((ListChangeListener<PlanetarySystem>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (PlanetarySystem system : change.getAddedSubList()) {
                        system.setFraction(this);
                    }
                }
                if (change.wasRemoved()) {
                    for (PlanetarySystem system : change.getRemoved()) {
                        system.removeFraction();
                    }
                }
            }
        });
        // Запустити таймер для виклику generateResources кожну секунду
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (PlanetarySystem system : listSystem) {
                    system.generateResources();
                }
            }
        }, 0, 2000); // Викликати кожну секунду (1000 мс)
    }

    public void addObj(alef obj) {
        listObj.add(obj);
        obj.setFraction(this);
    }

    public void addSystem(PlanetarySystem planetarySystem) {
        listSystem.add(planetarySystem);
    }

    public void removeSystem(PlanetarySystem planetarySystem) {
        listSystem.remove(planetarySystem);
    }


    public String getName() {
        return name;
    }

    public Color getFcolor() {
        return fcolor;
    }

    public ObservableList<alef> getListObj() {
        return listObj;
    }

    public static ObservableList<Fraction> getListFraction(){return listFraction;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fraction fraction)) return false;
        return Objects.equals(getFcolor(), fraction.getFcolor()) && Objects.equals(listSystem, fraction.listSystem) && Objects.equals(listObj, fraction.listObj) && Objects.equals(getName(), fraction.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFcolor(), listSystem, listObj, getName());
    }

    public static Fraction getFractionByName(String name) {
        for (Fraction fraction : listFraction) {
            if (fraction.getName().equals(name)) {
                return fraction;
            }
        }
        return null;
    }
}
