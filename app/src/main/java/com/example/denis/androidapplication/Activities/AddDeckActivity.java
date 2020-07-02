package com.example.denis.androidapplication.Activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.denis.androidapplication.data.DatabaseHelper;

import java.util.ArrayList;

public class AddDeckActivity extends AppCompatActivity {

    private EditText deckName;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deck);
        deckName = findViewById(R.id.editDeck);
        dbHelper = new DatabaseHelper(this);



    }

    public void CreateDeck(View view) {
        if (deckName.getText().toString().equals(""))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Поле не должно быть пустым!")
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
            ContentValues contentValues = new ContentValues();
            ArrayList<String> arrayList = new ArrayList<>();
            Cursor cursor = database.rawQuery("SELECT * FROM decks", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String str = cursor.getString(1);
                arrayList.add(str);
                cursor.moveToNext();
            }
            cursor.close();
            if(arrayList.contains(deckName.getText().toString())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Данная колода уже существует!")
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
                contentValues.put(DatabaseHelper.COLUMN_DECKS, deckName.getText().toString());
                database.insert(DatabaseHelper.TABLE_DECKS, null, contentValues);
                database.close();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

        }

    }
}
