package com.example.flashcardaudiotracks.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.flashcardaudiotracks.Card;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "dbFlashCard";

    // below int is our database version
    private static final int DB_VERSION = 1;

    //holds all the names for sets
    private static final String TABLE_NAME_SETS = "setTable";

    //holds all cards -> categorized by set name value
    private static final String TABLE_NAME_CARDS = "cardTable";

    // below variable is for our id column.
    private static final String ID_COL_SET = "idSet";
    private static final String ID_COL_CARD = "isCard";

//    // below variable is for our course name column
    private static final String NAME_COL_SETS = "setName";

    //private static final String NAME_COL_CARDS = "cardsName";

    // below variable id for our course duration column.
    private static final String QUANTITY_COL = "quantity";

//     below variable for our course description column.
    private static final String TERM_COL = "term";
//
//    // below variable is for our course tracks column.
    private static final String DEF_COL = "definition";

    private static final String CARD_POS_COL = "cardPosition";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        onCreateSet(db);
        onCreateCards(db);
    }

    private void onCreateSet(SQLiteDatabase db){
        String query = "CREATE TABLE " + TABLE_NAME_SETS + " ("
                + ID_COL_SET + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL_SETS + " TEXT,"
                + QUANTITY_COL + " TEXT)";

        db.execSQL(query);
    }

    private void onCreateCards(SQLiteDatabase db){
        String query = "CREATE TABLE " + TABLE_NAME_CARDS + " ("
                + ID_COL_CARD + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL_SETS + " TEXT,"
                + TERM_COL + " TEXT,"
                + DEF_COL + " TEXT,"
                + CARD_POS_COL + " TEXT)";

        db.execSQL(query);
    }

    public void addSet(String originalSetName, String setName, String quantity){
//        Log.d("DB", "Adding / Updating Set");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //if the set name is being changed
        if(!originalSetName.equals(setName)){
            values.put(NAME_COL_SETS, setName);
            //update the SET Table
            db.update(TABLE_NAME_SETS, values, "setName=?", new String[]{originalSetName});
            //update the CARDS Table
            db.update(TABLE_NAME_CARDS, values, "setName=?", new String[]{originalSetName});
        }else {
            if (setExists(db, setName) == false) {
//                Log.d("DB", "Adding Set.");
                values.put(NAME_COL_SETS, setName);
                values.put(QUANTITY_COL, quantity);
                db.insert(TABLE_NAME_SETS, null, values);
            } else {
//                Log.d("DB", "Set already exists.");
                values.put(QUANTITY_COL, quantity);
                db.update(TABLE_NAME_SETS, values, "setName=?", new String[]{setName});
            }
        }

        db.close();
    }

    public void addAndUpdateCards(String setName, ArrayList<Card> cards){
//        Log.d("DB", "Add / Updating Cards");

        SQLiteDatabase db = this.getWritableDatabase();
        for(int i = 0; i < cards.size(); i++){
            String pos = "" + i;
            String term = cards.get(i).getTerm();
            String def = cards.get(i).getDef();
            if(cardExists(db, setName, pos)){ //if exists -> update db | else add to db
//                Log.d("DB", "Card exists.");
                updateCard(db, setName, term, def, pos);
            }else{
                Log.d("DB", "Card doesn't exist.");
                addCard(db, setName, term, def, pos);
            }
        }
        db.close();
    }

    public void addCard(SQLiteDatabase db, String setName, String term, String def, String cardPos){
        ContentValues values = new ContentValues();
        values.put(NAME_COL_SETS, setName);
        values.put(TERM_COL, term);
        values.put(DEF_COL, def);
        values.put(CARD_POS_COL, cardPos); //position of card in ArrayList<Card>
        db.insert(TABLE_NAME_CARDS, null, values);
//        Log.d("DB", "ADDED CARD.");
    }

    //pass in updates ArrayList<Cards>
    public void updateCard(SQLiteDatabase db, String setName, String term, String def, String pos){
        ContentValues values = new ContentValues();
        values.put(TERM_COL, term);
        values.put(DEF_COL, def);
        db.update(TABLE_NAME_CARDS, values, "setName=? AND cardPosition=?", new String[]{setName, pos});
    }

    public boolean setExists(SQLiteDatabase db, String setName){
//        Log.d("DB", "Set exist?");
//        String sql = "SELECT EXISTS (SELECT * FROM " + TABLE_NAME_SETS + " WHERE setName= " + setName + " LIMIT 1)";
        String sql = "SELECT * FROM " + TABLE_NAME_SETS + " WHERE setName='"+setName+"'";
        Cursor c = db.rawQuery(sql, null);
//        Log.d("SET Query: ", sql);

        if(c.moveToFirst()){
            c.close();
            return true;
        }else{
            c.close();
            return false;
        }
    }

    public boolean cardExists(SQLiteDatabase db, String setName, String pos) {
        String sql = "SELECT * FROM " + TABLE_NAME_CARDS + " WHERE setName='" + setName + "' AND cardPosition='" + pos + "'";
        Cursor c = db.rawQuery(sql, null);
//        Log.d("CARD Query: ", sql);

        if(c.moveToFirst()){
            c.close();
            return true;
        }else{
            c.close();
            return false;
        }
    }

    //    public void deleteSet(String setName){
//        //query to delete set in setTable
//        SQLiteDatabase db = this.getWritableDatabase();
//        //delete set entry in set Table
//        db.delete(TABLE_NAME_SETS, "setName=?", new String[]{setName});
//        //delete all cards in set that is being deleted
//        db.delete(TABLE_NAME_CARDS, "setName=?", new String[]{setName});
//        db.close();
//    }

//    public void deleteCard(String setName, String cardPos){
//        //query to find card
//        //delete card
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_NAME_CARDS, "setName=? AND cardPos=?", new String[]{setName, cardPos});
//        db.close();
//    }

    public ArrayList<String> showSetTable(){
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorSets = db.rawQuery("SELECT * FROM " + TABLE_NAME_SETS, null);

        // on below line we are creating a new array list.
        ArrayList<String> setList = new ArrayList<String>();

        // moving our cursor to first position.
        if (cursorSets.moveToFirst()) {
            do {
                setList.add(cursorSets.getString(1));
            }while(cursorSets.moveToNext());

        }
        // at last closing our cursor
        // and returning our array list.
        cursorSets.close();
        return setList;
    }

    public ArrayList<Card> showCardTable(String setName){
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_CARDS + " WHERE setName=?", new String[]{setName});

        // on below line we are creating a new array list.
        ArrayList<Card> cardList = new ArrayList<Card>();

        // moving our cursor to first position.
        if (cursor.moveToFirst()) {
            do {

                cardList.add(new Card(cursor.getString(2),
                        cursor.getString(3)));
            } while (cursor.moveToNext());
            // moving our cursor to next.
        }

        cursor.close();
        return cardList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SETS);
        onCreate(db);
    }
}
