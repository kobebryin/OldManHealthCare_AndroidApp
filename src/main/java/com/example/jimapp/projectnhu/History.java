package com.example.jimapp.projectnhu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by 梁景威 on 2015/11/2.
 */
public class History extends AppCompatActivity {
    private static String DATABASE_TABLE = "health";
    private SQLiteDatabase db;
    private TextView output;
    private DBHelper dbHelper;

    private ImageButton showResult_sugar_btn;
    private ImageButton showResult_jump_btn;
    private ImageButton showResult_pressure_btn;
    private ImageButton showResult_temp_btn;

    /**攔截返回健事件*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            ConfirmExit();//按返回鍵，則執行退出確認
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void ConfirmExit(){//退出確認
        AlertDialog.Builder ad=new AlertDialog.Builder(History.this);
        ad.setTitle("詢問");
        ad.setMessage("確定離開查詢歷史紀錄?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按鈕
            public void onClick(DialogInterface dialog, int i) {
                // TODO Auto-generated method stub
                History.this.finish();//關閉activity
            }
        });
        ad.setNegativeButton("否",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                //不退出不用執行任何操作
            }
        });
        ad.show();//顯示對話框
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_history);

        showResult_sugar_btn = (ImageButton)findViewById(R.id.showResult_sugar_btn);
        showResult_jump_btn = (ImageButton)findViewById(R.id.showResult_jump_btn);
        showResult_pressure_btn = (ImageButton)findViewById(R.id.showResult_pressure_btn);
        showResult_temp_btn = (ImageButton)findViewById(R.id.showResult_temp_btn);
        output = (TextView)findViewById(R.id.textView11);

        dbHelper = new DBHelper(this);
        db = dbHelper.getReadableDatabase();
    }

    /**全部顯示
    public void onClick(View view){
        SqlQuery("SELECT * FROM " + DATABASE_TABLE);
    }*/

    /**血壓的圖表顯示*/
    public void showResult_pressure_btn_onClick(View view){
        Intent intent = new Intent(History.this,aChart_pressure.class);
        startActivity(intent);
        History.this.finish();
    }
    /**顯示血壓*/
    public void searchpressure_onClick(View view){
        //set aChart Button's visibility
        showResult_jump_btn.setVisibility(View.INVISIBLE);
        showResult_pressure_btn.setVisibility(View.VISIBLE);
        showResult_sugar_btn.setVisibility(View.INVISIBLE);
        showResult_temp_btn.setVisibility(View.INVISIBLE);

        String[] colNames;
        String str = "";
        Cursor c = db.query(DATABASE_TABLE,new String[]{"日期","收縮壓","舒張壓"},null,null,null,null,"日期",null);
        colNames = c.getColumnNames();

        str += colNames[0] + "\t\t\t\t\t\t\t\t\t\t";
        str += colNames[1] + "\t\t";
        str += colNames[2] + "\t\t";
        str += "\n";

        c.moveToNext();

        for(int i = 0; i < c.getCount(); i++) {
            str += c.getString(0) + "\t\t\t\t";
            str += c.getString(1) + "\t\t\t\t\t";
            str += c.getString(2) + "\n";

            c.moveToNext();
        }
        output.setText(str.toString());
    }

    /**心跳的圖表顯示*/
    public void showResult_temp_btn_onClick(View view){
        Intent intent = new Intent(History.this,aChart_temp.class);
        startActivity(intent);
        History.this.finish();
    }
    /**顯示體溫*/
    public void searchtemp_onClick(View view){
        //set aChart Button's visibility
        showResult_jump_btn.setVisibility(View.INVISIBLE);
        showResult_pressure_btn.setVisibility(View.INVISIBLE);
        showResult_sugar_btn.setVisibility(View.INVISIBLE);
        showResult_temp_btn.setVisibility(View.VISIBLE);

        String[] colNames;
        String str = "";
        Cursor c = db.query(DATABASE_TABLE,new String[]{"日期","體溫"},null,null,null,null,"日期",null);
        colNames = c.getColumnNames();

        str += colNames[0] + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
        str += colNames[1] + "\t\t\t\t\t\t\t\t";
        str += "\n";

        c.moveToNext();

        for(int i = 0; i < c.getCount(); i++) {
            str += c.getString(0) + "\t\t\t\t\t\t\t";
            str += c.getString(1) + "\n";

            c.moveToNext();
        }
        output.setText(str.toString());
    }

    /**心跳的圖表顯示*/
    public void showResult_jump_btn_onClick(View view){
        Intent intent = new Intent(History.this,aChart_jump.class);
        startActivity(intent);
        History.this.finish();
    }
    /**顯示心跳*/
    public void searchjump_onClick(View view){
        //set aChart Button's visibility
        showResult_jump_btn.setVisibility(View.VISIBLE);
        showResult_pressure_btn.setVisibility(View.INVISIBLE);
        showResult_sugar_btn.setVisibility(View.INVISIBLE);
        showResult_temp_btn.setVisibility(View.INVISIBLE);

        String[] colNames;
        String str = "";
        Cursor c = db.query(DATABASE_TABLE,new String[]{"日期","心跳"},null,null,null,null,"日期",null);
        colNames = c.getColumnNames();

        str += colNames[0] + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
        str += colNames[1] + "\t\t\t\t\t\t\t\t";
        str += "\n";

        c.moveToNext();

        for(int i = 0; i < c.getCount(); i++) {
            str += c.getString(0) + "\t\t\t\t\t\t\t";
            str += c.getString(1) + "\n";

            c.moveToNext();
        }
        output.setText(str.toString());
    }

    /**血糖的圖表顯示*/
    public void showResult_sugar_btn_onClick(View view){
        Intent intent = new Intent(History.this,aChart_sugar.class);
        startActivity(intent);
        History.this.finish();
    }
    /**顯示血糖*/
    public void searchsugar_onClick(View view){
        //set aChart Button's visibility
        showResult_jump_btn.setVisibility(View.INVISIBLE);
        showResult_pressure_btn.setVisibility(View.INVISIBLE);
        showResult_sugar_btn.setVisibility(View.VISIBLE);
        showResult_temp_btn.setVisibility(View.INVISIBLE);

        String[] colNames;
        String str = "";
        Cursor c = db.query(DATABASE_TABLE, new String[]{"日期", "血糖"}, null, null, null, null, "日期", null);
        colNames = c.getColumnNames();

        str += colNames[0] + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
        str += colNames[1] + "\t\t\t\t\t\t\t\t";
        str += "\n";

        c.moveToNext();

        for(int i = 0; i < c.getCount(); i++) {
            str += c.getString(0) + "\t\t\t\t\t\t\t";
            str += c.getString(1) + "\n";

            c.moveToNext();
        }
        output.setText(str.toString());
    }

    /**配合全部顯示
    public void SqlQuery(String sql){
        String[] colNames;
        String str = "";
        Cursor c = db.rawQuery(sql,null);
        colNames = c.getColumnNames();

        for(int i = 0; i < colNames.length; i++)
            str += colNames[i] + "\t\t";
        str += "\n";

        c.moveToFirst();

        for(int i = 0; i < c.getCount(); i++){
            str += c.getString(0) + "\t\t\t\t\t";
            str += c.getString(1) + "\t\t\t\t\t";
            str += c.getString(2) + "\t\t\t\t\t";
            str += c.getString(3) + "\t\t\t\t\t";
            str += c.getString(4) + "\t\t\t\t\t";
            str += c.getString(5) + "\t\t\t\t\t";
            str += c.getString(6) + "\n";

            c.moveToNext();
        }
        output.setText(str.toString());
    }*/

    /**關閉資料庫*/
    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
}
