package com.example.denis.androidapplication.Activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.denis.androidapplication.Cards.Card;
import com.example.denis.androidapplication.Cards.Deck;
import com.example.denis.androidapplication.data.DatabaseHelper;
import com.example.denis.androidapplication.training.DegreeOfRemembering;
import com.example.denis.androidapplication.training.TrainingImpl;

import java.io.IOException;

public class TrainingActivity extends AppCompatActivity {


    private Deck deck = new Deck();
    private TrainingImpl tr = new TrainingImpl();
    private Deck deckForRepeat = new Deck();
    private Card cardForRemember;
    private SQLiteDatabase mDb;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        frontSideView = findViewById(R.id.frontSide);
        backSideView = findViewById(R.id.backSide);
        fillDeck(deck);
            deckForRepeat = tr.selectCardsForTraining(deck);
            cardForRemember = deckForRepeat.takeRandomCardForTraining();
            if(cardForRemember != null ) {
                frontSideView.setText(cardForRemember.getFrontSide());
                backSideView.setText(cardForRemember.getBackSide());
            }
            else {
                frontSideView.setVisibility(View.INVISIBLE);
                backSideView.setVisibility(View.INVISIBLE);
                AlertDialog.Builder builder = new AlertDialog.Builder(TrainingActivity.this);
                builder.setTitle("Нет карт которым нужна тернировка!")
                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        startActivity(new Intent(TrainingActivity.this, MainActivity.class));
                                    }
                                });

                AlertDialog alert = builder.create();
                alert.show();
            }
    }

    private TextView frontSideView;
    private TextView backSideView;

    public void fillDeck(Deck deck) {

        dbHelper = new DatabaseHelper(this);

        try {
            dbHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        try {
            mDb = dbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        Intent intent = getIntent();
        String deckName = intent.getStringExtra("name_of_deck");
        Cursor cursor = mDb.rawQuery("SELECT * FROM cards WHERE deck = '" + deckName + "'", null);
            cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String frontSide = cursor.getString(1);
                    String backSide = cursor.getString(2);
                    long dataForRepeat = cursor.getLong(3);
                    long interval = cursor.getLong(4);
                    int repetition = cursor.getInt(5);
                    float EFactor = cursor.getFloat(6);
                    deck.addCardsInDeckFromBD(frontSide, backSide, dataForRepeat, interval, repetition, EFactor);

                    cursor.moveToNext();
                }
                cursor.close();
                mDb.close();

        }

    public void onClickShowAnswer(View view) {
        backSideView.setVisibility(View.VISIBLE);
    }

    public void addAndShowNextCard(){
        saveCardsIntoDb(cardForRemember);
        deckForRepeat = tr.selectCardsForTraining(deck);
        if(deckForRepeat.deckOfCards.size()>0) {
            cardForRemember = deckForRepeat.takeRandomCardForTraining();
            if(deckForRepeat.deckOfCards.size()>=1) {
                frontSideView.setText(cardForRemember.getFrontSide());
                backSideView.setVisibility(View.INVISIBLE);
                backSideView.setText(cardForRemember.getBackSide());
            }
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(TrainingActivity.this);
            builder.setTitle("Тренировка окончена!")
                    .setCancelable(false)
                    .setNegativeButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    startActivity(new Intent(TrainingActivity.this,MainActivity.class));
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();

        }
    }

    public void saveCardsIntoDb(Card card){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValuesForCards = new ContentValues();
        contentValuesForCards.put(DatabaseHelper.COLUMN_DATAFORREPEAT,card.getDateForRepeat());
        contentValuesForCards.put(DatabaseHelper.COLUMN_FRONTSIDE,card.getFrontSide());
        contentValuesForCards.put(DatabaseHelper.COLUMN_BACKSIDE,card.getBackSide());
        contentValuesForCards.put(DatabaseHelper.COLUMN_INTERVAL,card.getInterval());
        contentValuesForCards.put(DatabaseHelper.COLUMN_EFACTOR,card.getEFactor());
        contentValuesForCards.put(DatabaseHelper.COLUMN_REPEITION,card.getRepetition());
        database.update(DatabaseHelper.TABLE_CARDS, contentValuesForCards, DatabaseHelper.COLUMN_FRONTSIDE + "=?", new String[] {String.valueOf(card.getFrontSide())});
        //database.insert(DatabaseHelper.TABLE_CARDS,null,contentValuesForCards);
        database.close();
    }


    public void onClickDontRemember(View view) {
        tr.trainingCards(cardForRemember,DegreeOfRemembering.notRemember);
        addAndShowNextCard();

    }

    public void onClickHard(View view) {
        tr.trainingCards(cardForRemember,DegreeOfRemembering.hardRemember);
        addAndShowNextCard();
    }

    public void onClickNormal(View view) {
        tr.trainingCards(cardForRemember,DegreeOfRemembering.normalRemember);
        addAndShowNextCard();
    }

    public void onClickEasy(View view) {
        tr.trainingCards(cardForRemember,DegreeOfRemembering.easyRemember);
        addAndShowNextCard();
    }

}
