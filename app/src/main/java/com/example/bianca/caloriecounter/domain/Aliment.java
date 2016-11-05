package com.example.bianca.caloriecounter.domain;



import java.io.Serializable;

/**
 * Created by bianca on 05.11.2016.
 */

@SuppressWarnings("serial")
public class Aliment implements Serializable {
    private String name;
    private int calorii;
    private int grasimi;
    private int proteine;
    private int carbohidrati;

    public Aliment(String name, int calorii, int grasimi, int proteine, int carbohidrati) {
        this.name = name;
        this.calorii = calorii;
        this.grasimi = grasimi;
        this.proteine = proteine;
        this.carbohidrati = carbohidrati;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalorii() {
        return calorii;
    }

    public void setCalorii(int calorii) {
        this.calorii = calorii;
    }

    public int getGrasimi() {
        return grasimi;
    }

    public void setGrasimi(int grasimi) {
        this.grasimi = grasimi;
    }

    public int getProteine() {
        return proteine;
    }

    public void setProteine(int proteine) {
        this.proteine = proteine;
    }

    public int getCarbohidrati() {
        return carbohidrati;
    }

    public void setCarbohidrati(int carbohidrati) {
        this.carbohidrati = carbohidrati;
    }
}
