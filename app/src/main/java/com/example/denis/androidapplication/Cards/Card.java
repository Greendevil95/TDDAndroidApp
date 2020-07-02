package com.example.denis.androidapplication.Cards;


import android.support.annotation.NonNull;

public class Card {
    private String frontSide;
    private String backSide;
    private long dateForRepeat;
    private long interval;
    private int repetition;
    private float EFactor;

    public Card(String frontSide,String backSide){
        this.frontSide = frontSide;
        this.backSide = backSide;
        repetition = 0;
        interval = 0;
        EFactor = 1.5F;
    }

    public Card(String frontSide, String backSide, long dateForRepeat, long interval, int repetition, float EFactor) {
        this.frontSide = frontSide;
        this.backSide = backSide;
        this.dateForRepeat = dateForRepeat;
        this.interval = interval;
        this.repetition = repetition;
        this.EFactor = EFactor;
    }

    public String getFrontSide() {
        return frontSide;
    }

    public String getBackSide() {
        return backSide;
    }

    public long getDateForRepeat(){return dateForRepeat;}

    public void setFrontSide(String frontSide) {
        this.frontSide = frontSide;
    }

    public void setBackSide(String backSide){
        this.backSide = backSide;
    }

    public void setDateForRepeat(Long dateForRepeat){
        this.dateForRepeat = dateForRepeat;
    }

    @NonNull
    @Override
    public String toString(){
        return "frontSide = " + frontSide + ", backSide = " + backSide;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public int getRepetition() {
        return repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    public float getEFactor() {
        return EFactor;
    }

    public void setEFactor(float EFactor) {
        this.EFactor = EFactor;
    }
}
