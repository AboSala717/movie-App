package com.example.mohamed.popularmoviesstage1;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by mohamed on 5/19/2017.
 */

public class FavMovies extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.mohamed.popularmoviesstage1";
    static final String URL = "content://" + PROVIDER_NAME + "/favmovies";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String ID = "ID";
    static final String originalTitle = "originalTitle";
    static final String plotSynopsis = "plotSynopsis";
    static final String userRating = "userRating";
    static final String releaseDate = "releaseDate";
    static final String poster_path = "poster_path";
    static final String key1 = "key1";
    static final String key2 = "key2";
    static final String reviewUrl1 = "reviewUrl1";
    static final String reviewUrl2 = "reviewUrl2";

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "favmovies";
    static final String TABLE = "movies";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE IF NOT EXISTS " +TABLE+
                    " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " originalTitle TEXT NOT NULL, " +
                    " plotSynopsis TEXT NOT NULL, " +
                    " userRating TEXT NOT NULL, " +
                    " releaseDate TEXT NOT NULL, " +
                    " poster_path TEXT NOT NULL, " +
                    " key1 TEXT," +
                    " key2 TEXT, " +
                    " reviewUrl1 TEXT , " +
                    " reviewUrl2 TEXT );";


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL( " CREATE TABLE IF NOT EXISTS " +TABLE+
                    " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " originalTitle TEXT NOT NULL, " +
                    " plotSynopsis TEXT NOT NULL, " +
                    " userRating TEXT NOT NULL, " +
                    " releaseDate TEXT NOT NULL, " +
                    " poster_path TEXT NOT NULL, " +
                    " key1 TEXT," +
                    " key2 TEXT, " +
                    " reviewUrl1 TEXT , " +
                    " reviewUrl2 TEXT );");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  TABLE);
            onCreate(db);
        }
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE);
        Cursor c = qb.query(db,	strings, s, strings1 ,null, null, s1);

        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values){

    long rowID = db.insert(	TABLE, "", values);



    if (rowID > 0)
    {
        Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(_uri, null);
        return _uri;
    }
    throw new SQLException("Failed to add a record into " + uri);

}

    @Override
    public int delete(Uri uri, String s, String[] strings) {

        int count = 0;
        count = db.delete(TABLE, s, strings);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String s, String[] strings) {
        int count = 0 ;
        count = db.update(TABLE, values,s ,strings);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
