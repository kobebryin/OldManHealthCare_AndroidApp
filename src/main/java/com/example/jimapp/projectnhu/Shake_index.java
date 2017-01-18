package com.example.jimapp.projectnhu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;


/**
 * Created by Administrator on 2015/11/21.
 */
public class Shake_index extends Activity {
    //Read DB
    private static String DATABASE_TABLE = "score";
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            ConfirmExit();//按返回鍵，則執行退出確認
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void ConfirmExit() {//退出確認
        AlertDialog.Builder ad = new AlertDialog.Builder(Shake_index.this);
        ad.setTitle("詢問?").setMessage("確定要離開遊戲嗎?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Shake_index.this.finish();
            }
        });
        ad.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        ad.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_index);

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();
    }

    public void leave_btn_onClick(View view){
        AlertDialog.Builder ad = new AlertDialog.Builder(Shake_index.this);
        ad.setTitle("詢問?").setMessage("確定要離開遊戲嗎?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Shake_index.this.finish();
            }
        });
        ad.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ad.show();
    }

    public void start_btn_onClick(View view){
        Intent intent = new Intent(Shake_index.this,Shake.class);
        startActivity(intent);
    }

    public void check_btn_onClick(View view){
        AlertDialog.Builder ad = new AlertDialog.Builder(Shake_index.this);
        ad.setTitle("歷史紀錄");
        ad.setMessage(getValue());
        ad.setCancelable(false);
        ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ad.show();
    }

    public String getValue() {
        String history = "";
        int n = 1;
        Cursor c = db.query(DATABASE_TABLE, new String[]{"分數"}, null, null, null, null, "分數 desc", "5");
            c.moveToNext();
            for(int i = 0; i < c.getCount(); i++) {
                history += "第" + n++ + "名:\t" + c.getString(0) + "\n";
                c.moveToNext();
            }
        return history;
        }

}
