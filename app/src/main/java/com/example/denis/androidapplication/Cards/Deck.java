package com.example.denis.androidapplication.Cards;

import java.util.ArrayList;
import java.util.Random;

public class Deck {

    public ArrayList<Card> deckOfCards = new ArrayList<>();


    public void addCardsInDeck(String frontSide, String backSide) {
        deckOfCards.add(new Card(frontSide, backSide));
    }

    public void addCardsInDeckFromBD(String frontSide, String backSide, long dataForRepeat, long interval,int repetition, float EFactor){
        deckOfCards.add(new Card(frontSide,backSide,dataForRepeat,interval,repetition,EFactor));
    }

    public  Card takeRandomCardForTraining(){
        if (deckOfCards.size()> 0) {
            int rnd = new Random().nextInt(deckOfCards.size());
            return deckOfCards.get(rnd);
        }
        else return null;
    }

    public Integer cardsCount(){
        return deckOfCards.size();
    }

    public Card getCardsByIndex(int index){
        return deckOfCards.get(index);
    }


}
