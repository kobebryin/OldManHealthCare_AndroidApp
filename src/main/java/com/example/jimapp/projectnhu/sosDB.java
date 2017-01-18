package com.example.jimapp.projectnhu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/11/16.
 */
public class sosDB extends SQLiteOpenHelper {

    private final static int _DBVersion = 1; //<-- 版本
    private final static String _DBName = "GPSOS";  //<-- db name

    public sosDB(Context context) {
        super(context, _DBName, null, _DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tel (" +
                "編號 integer primary key," +
                "電話一 text no null, 電話二 text no null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS tel");
        onCreate(db);
    }
}

