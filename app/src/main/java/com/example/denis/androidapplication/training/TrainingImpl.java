package com.example.denis.androidapplication.training;

import com.example.denis.androidapplication.Cards.Card;
import com.example.denis.androidapplication.Cards.Deck;


public class TrainingImpl implements Training {

    private int quality;

    @Override
    public Deck selectCardsForTraining(Deck deck) {
        if (deck != null) {
            long now = System.currentTimeMillis();
            Deck deckForRepeat = new Deck();
            for (int i = 0; i < deck.deckOfCards.size(); i++) {
                long dateForRepeat = deck.deckOfCards.get(i).getDateForRepeat();
                if (now >= dateForRepeat) {
                    deckForRepeat.deckOfCards.add(deck.deckOfCards.get(i));
                }
            }
            return deckForRepeat;
        } else throw new NullPointerException("Can't training empty deck!");
    }

    @Override
    public void trainingCards(Card card, DegreeOfRemembering degreeOfRemembering) {
        if (card != null) {
            switch (degreeOfRemembering) {
                case notRemember:
                    quality = 0;
                    break;
                case hardRemember:
                    quality = 1;
                    break;
                case normalRemember:
                    quality = 2;
                    break;
                case easyRemember:
                    quality = 3;
                    break;

            }

            //System.out.println("Значение оценки " + quality);
            if (quality < 0 || quality > 3) {
                throw new IllegalArgumentException("Illegal argument for quality!");
            }

            int repetitions = card.getRepetition();
            float easiness = card.getEFactor();
            long interval = card.getInterval();

            // easiness factor
            easiness = (float) Math.max(1.3, easiness + 0.1 - (3.0 - quality) * (0.08 + (3.0 - quality) * 0.02));
            /*System.out.println("Easy factor " + easiness);*/

            // repetitions
            if (quality == 0) {
                repetitions = 0;
            } else {
                repetitions += 1;
            }

            // interval
            if (repetitions == 0) {
                interval = 0;
            } else if (repetitions == 1) {
                interval = 1;
            } else if (repetitions == 2) {
                interval = 6;
            } else {
                interval = Math.round(interval * easiness);
            }
        /*System.out.println("Значение повторов " + repetitions);
        System.out.println("Значение интервала " + interval);*/

            // next practice
            int secondsInDay = 60 * 60 * 24;
            long now = System.currentTimeMillis();
            long nextPracticeDate = now + secondsInDay * interval;
        /*System.out.println("Сейчас " + now);
        System.out.println("Следующий повтор " + nextPracticeDate);
        System.out.println((nextPracticeDate - now));
        System.out.println(secondsInDay * interval);*/

            // Store the nextPracticeDate in the database
            card.setRepetition(repetitions);
            card.setEFactor(easiness);
            card.setInterval(interval);
            card.setDateForRepeat(nextPracticeDate);
        } else throw new NullPointerException("Can't training empty card!");
    }

}

