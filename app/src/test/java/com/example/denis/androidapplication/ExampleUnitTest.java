package com.example.denis.androidapplication;

import com.example.denis.androidapplication.Cards.Card;
import com.example.denis.androidapplication.Cards.Deck;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    Card card = new Card("Cat", "Кот");


    @Test
    public void createCardForTest() {
        new Card("Cat", "Кот");
    }

    @Test
    public void checkCardFrontSideWithGetter() {
        Assert.assertEquals("Cat", card.getFrontSide());
    }

    @Test
    public void checkCardBackSideWithGetter() {
        Assert.assertEquals("Кот", card.getBackSide());
    }


    @Test
    public void checkCardFrontSideWithSetter() {
        card.setFrontSide("Dog");
        Assert.assertEquals("Dog", card.getFrontSide());
    }

    @Test
    public void checkCardBackSideWithSetter() {
        card.setBackSide("Пес");
        Assert.assertEquals("Пес", card.getBackSide());
    }

    @Test
    public void createAndFillDeck(){
        Deck deck = new Deck();
        deck.addCardsInDeck("Cow","Корова");
        deck.addCardsInDeck("Horse", "Лошадь");
        Assert.assertEquals("Horse",deck.deckOfCards.get(1).getFrontSide());
        Assert.assertEquals("Корова",deck.deckOfCards.get(0).getBackSide());
    }

    @Test
    public void selectRandomCardForTraining(){
        Deck deck = new Deck();
        deck.addCardsInDeck("Cow","Корова");
        deck.addCardsInDeck("Horse", "Лошадь");
        deck.addCardsInDeck("Shark", "Акула");
        deck.addCardsInDeck("Eagle", "Орел");
        for(int i = 0; i < 6; i++) {
            System.out.println(deck.takeRandomCardForTraining());
        }
    }
}