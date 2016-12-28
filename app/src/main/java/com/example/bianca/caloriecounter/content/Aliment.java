package com.example.bianca.caloriecounter.content;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bianca on 19.11.2016.
 */

public class Aliment {
    private String name;
    private double calories;
    private double proteins;
    private double carbs;
    private double fats;

    public Aliment(){}

    public Aliment(String name, double calories, double proteins, double carbs, double fats) {
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

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getProteins() {
        return proteins;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    @Override
    public String toString() {
        return "Aliment{" +
                "name='" + name + '\'' +
                ", calories = " + calories +
                ", proteins = " + proteins +
                ", carbs = " + carbs +
                ", fats = " + fats +
                '}';
    }

    public String toStringFancy() {
        return "\nname = '" + name + '\'' +
                "\ncalories = " + calories +
                "\nproteins = " + proteins +
                "\ncarbs = " + carbs +
                "\nfats = " + fats;
    }

    public String toJsonString() {
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("calories", calories);
            jsonObject.put("proteins", proteins);
            jsonObject.put("carbs", carbs);
            jsonObject.put("fats", fats);

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }
}
