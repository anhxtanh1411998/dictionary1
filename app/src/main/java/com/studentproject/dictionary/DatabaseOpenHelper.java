package com.studentproject.dictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseOpenHelper extends SQLiteAssetHelper {
    //private static final String DATABASE_NAME = "viet_anh.db";
    private static final int DATABASE_VERSION = 1;
    private String DATABASE;
    private String DATABASE_NAME ;
    private Context context;
    private int next =0;
    private int i = 0;

    public DatabaseOpenHelper(Context context, String DATABASE, String DATABASE_NAME) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.DATABASE_NAME = DATABASE_NAME;
        this.context = context;
        this.DATABASE = DATABASE;

    }

    public void addContact(translate translate) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word", translate.getWord());
        values.put("content", translate.getContent());
        sqLiteDatabase.insert(DATABASE_NAME, null, values);
        sqLiteDatabase.close();
    }


    public ArrayList<translate> getAllContact() {
        String Select = "Select * from "+DATABASE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(Select, null);
        ArrayList<translate> listItems = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String html = Html.fromHtml(cursor.getString(2)).toString();
                translate items = new translate();
                items.setWord(cursor.getString(1));
                items.setContent(html);
                listItems.add(items);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return listItems;
    }

    public List<String> getWord(String textChange) {
        List<String> listWord = new ArrayList<>();
        if(textChange.length()==0) {return listWord;}
        //String Select = "Select * from viet_anh";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from " + DATABASE + " where word like ?", new String[]{textChange + "%"});
        if (cursor.moveToFirst()) {
            do {
                listWord.add(cursor.getString(1));
                i++;
            } while (cursor.moveToNext() && i%5!=0);
        }
        cursor.close();
        sqLiteDatabase.close();
        return listWord;
    }

    public ArrayList<translate> getListmore() {
        String Select = "Select * from "+DATABASE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(Select, null);
        ArrayList<translate> listItems = new ArrayList<>();
        Log.w("DAtabase","12345");
        cursor.moveToPosition(next);
        do {
            String html = Html.fromHtml(cursor.getString(2)).toString();
            translate items = new translate();
            items.setWord(cursor.getString(1));
            items.setContent(html);
            listItems.add(items);
            next++;
        } while (cursor.moveToNext() && next % 4 != 0);
        cursor.close();
        sqLiteDatabase.close();
        return listItems;
    }

    public ArrayList<translate> Search(String textChange) {
        if(textChange.length()==0) {i=0;next=0;return  getListmore();}
        //String Select = ("Select * from anh_viet where word = ?", new String[]{textChange + ""});
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from " + DATABASE +" where word like ?", new String[]{textChange + "%"});
        ArrayList<translate> listItems = new ArrayList<>();
        //cursor.moveToPosition(i);
        if (cursor.moveToFirst()) {
            do {
                String html = Html.fromHtml(cursor.getString(2)).toString();
                translate items = new translate();
                items.setWord(cursor.getString(1));
                items.setContent(html);
                listItems.add(items);
                i++;
            } while (cursor.moveToNext() && i%5!=0);
        }
        cursor.close();
        sqLiteDatabase.close();
        return listItems;

    }

    public void deleteContact(translate listItems) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete("anh_viet", "word" + " = ?",
                new String[]{String.valueOf(listItems.getWord())});
        sqLiteDatabase.close();
    }

    public void Upgrate(translate listItems, String word, String content) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word", listItems.getWord());
        values.put("content", listItems.getContent());
        sqLiteDatabase.update("anh_viet", values, "word" + " = ?", new String[]{String.valueOf(listItems.getWord()), String.valueOf(listItems.getContent())});
        sqLiteDatabase.close();
    }

    public void DeleteAll() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        if(sqLiteDatabase!=null) {
            sqLiteDatabase.delete(DATABASE, null, null);
        }
    }

    public int getContactCount() {
        String countQuery = "SELECT  * FROM viet_anh";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();

        // return count
        return cursor.getCount();
    }


}
