package com.example.bianca.caloriecounter.content;

/**
 * Created by bianca on 19.11.2016.
 */

public class Aliment {
    private String name;
    private int calories;
    private int proteins;
    private int carbs;
    private int fats;

    public Aliment(){}

    public Aliment(String name, int calories, int proteins, int carbs, int fats) {
        this.name = name;
        this.calories = calories;
        this.proteins = proteins;
        this.carbs = carbs;
        this.fats = fats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProteins() {
        return proteins;
    }

    public void setProteins(int proteins) {
        this.proteins = proteins;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getFats() {
        return fats;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    @Override
    public String toString() {
        return "Aliment{" +
                "name='" + name + '\'' +
                ", calories=" + calories +
                ", proteins=" + proteins +
                ", carbs=" + carbs +
                ", fats=" + fats +
                '}';
    }
}
