package com.example.jimapp.projectnhu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 梁景威 on 2015/11/2.
 */
public class  DBHelper extends SQLiteOpenHelper {

    private final static int _DBVersion = 1; //<-- 版本
    private final static String _DBName = "Health";  //<-- db name

    public DBHelper(Context context) {
        super(context, _DBName, null, _DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE health (" +
                "編號 integer primary key," +
                "日期 string no null, 心跳 integer no null, 體溫 integer no null, 血糖 integer no null, 收縮壓 integer no null, 舒張壓 integer no null)");
        db.execSQL("CREATE TABLE score (" +
                "編號 integer primary key," +
                "分數 integer no null)");
        db.execSQL("CREATE TABLE alarm (" +
                "_id integer primary key," +
                "time integer no null, name text no null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS health");
        db.execSQL("DROP TABLE IF EXISTS score");
        db.execSQL("DROP TABLE IF EXISTS alarm");
        onCreate(db);
    }
}
