package com.goodeye.goodeye;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lihao on 16/6/6.
 */

public class FeedReaderDbHelper extends SQLiteOpenHelper {


    private static final String INT_TYPE = " INTEGER";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_LOCKED=
            "CREATE TABLE " + FeedReaderContract.idlockedt.il_TABLE_NAME + " (" +
                    FeedReaderContract.idlockedt.il_CID + " INTEGER NOT NULL PRIMARY KEY autoincrement," +
                    FeedReaderContract.idlockedt.il_ID + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.idlockedt.il_Locked + INT_TYPE +

                    " )";

    private static final String SQL_CREATE_NVALUE=
            "CREATE TABLE " + FeedReaderContract.valuet.v_TABLE_NAME + " (" +
                    FeedReaderContract.valuet.v_CID + " INTEGER NOT NULL PRIMARY KEY autoincrement," +
                    FeedReaderContract.valuet.v_NAME + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.valuet.v_VALUE + TEXT_TYPE +

                    " )";

    private static final String SQL_DELETE_LOCKED =
            "DROP TABLE IF EXISTS "+FeedReaderContract.idlockedt.il_TABLE_NAME;

    private static final String SQL_DELETE_NVALUE =
            "DROP TABLE IF EXISTS "+FeedReaderContract.valuet.v_TABLE_NAME ;

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "FeedReader.db";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_LOCKED);
        db.execSQL(SQL_CREATE_NVALUE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_LOCKED);
        db.execSQL(SQL_DELETE_NVALUE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
