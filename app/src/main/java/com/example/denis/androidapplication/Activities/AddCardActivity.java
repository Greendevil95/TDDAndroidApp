package com.example.denis.androidapplication.Activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.denis.androidapplication.Cards.Card;
import com.example.denis.androidapplication.data.DatabaseHelper;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class AddCardActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase mDb;
    private ArrayAdapter adapter;
    private Spinner spinner;
    private EditText editFrontSIde;
    private EditText editBackSide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_activity);
        ArrayList<String> decks = new ArrayList<>();
        dbHelper = new DatabaseHelper(this);

        try {
            mDb = dbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        Cursor cursor = mDb.rawQuery("SELECT * FROM decks", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String str = cursor.getString(1);
            if (!decks.contains(str)) {
                decks.add(str);
            }
            cursor.moveToNext();
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, decks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner = findViewById(R.id.DecksSpinner);
        Log.e("ysf", "spin = " + spinner.toString());
        spinner.setAdapter(adapter);
    }

    public void addCard(View view) {

        editFrontSIde = findViewById(R.id.editFrontSide);
        editBackSide = findViewById(R.id.editBackSide);

       if (editFrontSIde.getText().toString().equals("") || editBackSide.getText().toString().equals(""))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Поля не должны быть пустыми!")
                    .setCancelable(false)
                    .setNegativeButton("ОК",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();

        }
        else
        {
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues contentValuesForCards = new ContentValues();
            ArrayList<String> arrayList = new ArrayList<>();
            Cursor cursor = database.rawQuery("SELECT * FROM cards", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String str = cursor.getString(1);
                    arrayList.add(str);
                cursor.moveToNext();
            }
            cursor.close();
            if(arrayList.contains(editFrontSIde.getText().toString()))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Данная карточка уже есть в колоде!")
                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                database.close();
            }
            else {
                Card card = new Card(editFrontSIde.getText().toString(), editBackSide.getText().toString());
                contentValuesForCards.put(DatabaseHelper.COLUMN_DATAFORREPEAT, card.getDateForRepeat());
                contentValuesForCards.put(DatabaseHelper.COLUMN_FRONTSIDE, card.getFrontSide());
                contentValuesForCards.put(DatabaseHelper.COLUMN_BACKSIDE, card.getBackSide());
                contentValuesForCards.put(DatabaseHelper.COLUMN_DECK, spinner.getSelectedItem().toString());
                contentValuesForCards.put(DatabaseHelper.COLUMN_INTERVAL, card.getInterval());
                contentValuesForCards.put(DatabaseHelper.COLUMN_EFACTOR, card.getEFactor());
                contentValuesForCards.put(DatabaseHelper.COLUMN_REPEITION, card.getRepetition());
                database.insert(DatabaseHelper.TABLE_CARDS, null, contentValuesForCards);
                database.close();
                editFrontSIde.setText("");
                editBackSide.setText("");
            }

        }

    }
}
