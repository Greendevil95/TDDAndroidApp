package com.example.denis.androidapplication.training;

import com.example.denis.androidapplication.Cards.Card;
import com.example.denis.androidapplication.Cards.Deck;

public interface Training {

     Deck selectCardsForTraining(Deck deck);

     void trainingCards(Card card, DegreeOfRemembering degreeOfRemembering );

}
