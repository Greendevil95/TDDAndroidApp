package com.example.denis.androidapplication.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.denis.androidapplication.data.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase mDb;
    private ArrayAdapter adapter;
    ArrayList<String> decks;

    ListView decksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decks = new ArrayList<>();
        setContentView(R.layout.activity_main);
        decksList = findViewById(R.id.decksList);
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
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, decks);
        decksList.setAdapter(adapter);
        mDb.close();


        decksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                TextView textView = (TextView) itemClicked;
                String strText = textView.getText().toString();
                Intent intent = new Intent(MainActivity.this, TrainingActivity.class);
                intent.putExtra("name_of_deck", strText);
                startActivity(intent);
            }

        });

        decksList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showPopupMenu(view);
                return true;
            }
        });
    }

    private void showPopupMenu(final View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.popupmenu);

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ShowCards:
                                TextView textView = (TextView) view;
                                String strText = textView.getText().toString();
                                Intent intent = new Intent(MainActivity.this, CardsInDeckActivity.class);
                                intent.putExtra("name_of_deck", strText);
                                startActivity(intent);
                                return true;
                            case R.id.deleteDeck:
                                deleteDeck(view);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
        popupMenu.show();
    }

    public void deleteDeck(View view){

        TextView textView = (TextView) view;
        String strText = textView.getText().toString();
        try {
            mDb = dbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        mDb.delete(DatabaseHelper.TABLE_DECKS,DatabaseHelper.COLUMN_DECKS + "=?",new String[] {String.valueOf(strText)});
        mDb.delete(DatabaseHelper.TABLE_CARDS,DatabaseHelper.COLUMN_DECK + "=?",new String[] {String.valueOf(strText)});
        adapter.remove(strText);
    }


    @Override
    protected void onRestart() {
        super.onRestart();

    }

    public void addCard(View view) {
        if (decksList.getCount() > 0) {
            Intent intent = new Intent(this, test_activity.class);
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Для добавления карточек, нужно создать колоду!")
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
    }

    public void addDeck(View view) {
        Intent intent = new Intent(this, AddDeckActivity.class);
        startActivity(intent);
    }

}
