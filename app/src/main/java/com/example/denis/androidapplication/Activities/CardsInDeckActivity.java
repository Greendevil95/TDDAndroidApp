package com.example.denis.androidapplication.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class CardsInDeckActivity extends AppCompatActivity {

    private ListView cardsInDecks;
    private SQLiteDatabase mDb;
    private DatabaseHelper dbHelper;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_in_deck);
        cardsInDecks = findViewById(R.id.CardsInDeckList);
        fillDeck();

        cardsInDecks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showPopupMenu(view);
                return true;
            }
        });

    }

    public void fillDeck() {

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
        ArrayList<String> cards = new ArrayList<>();
        Cursor cursor = mDb.rawQuery("SELECT * FROM cards WHERE deck = '" + deckName + "'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String frontSide = cursor.getString(1);
            String backSide = cursor.getString(2);
            cards.add(frontSide + " - " + backSide);
            cursor.moveToNext();
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cards);
        cardsInDecks.setAdapter(adapter);
        cursor.close();
        mDb.close();
    }

    private void showPopupMenu(final View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.popupcardmenu);

        popupMenu
                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.deleteCard:
                                deleteCard(view);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
        popupMenu.show();
    }

    public void deleteCard(View view){
        TextView textView = (TextView) view;
        String strText = textView.getText().toString();
        String dbString = strText.substring(0, strText.indexOf(" "));
        System.out.println(dbString);
        try {
            mDb = dbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        mDb.delete(DatabaseHelper.TABLE_CARDS,DatabaseHelper.COLUMN_FRONTSIDE + "=?",new String[] {String.valueOf(dbString)});
        adapter.remove(strText);
    }
}

