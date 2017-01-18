package com.example.jimapp.projectnhu;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by 梁景威 on 2015/11/2.
 */
public class Health_jump extends AppCompatActivity {
    //資料庫變數宣告
    private static String DATABASE_TABLE = "health";
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    //各資料變數
    private EditText date_et;
    private NumberPicker jump_np, sugar_np, temp_np, pressure1_np, pressure2_np;
    private Button reset_btn;

    //DatePickerDialog需要的變數
    private int mYear, mMonth, mDay;
    static final int DATE_DIALOG_ID = 1;
    private Calendar calendar;
    private DatePickerDialog dpd;

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
        AlertDialog.Builder ad=new AlertDialog.Builder(Health_jump.this);
        ad.setTitle("詢問");
        ad.setMessage("確定要離開新增資料頁面嗎?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按鈕
            public void onClick(DialogInterface dialog, int i) {
                // TODO Auto-generated method stub
                Health_jump.this.finish();//關閉activity
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_jump);

        reset_btn = (Button)findViewById(R.id.reset_btn);

        //日期常數設定
        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        //資料庫常數設定
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        //預設日期
        date_et = (EditText)findViewById(R.id.date_et);
        date_et.setText(setDateFormat(mYear,mMonth,mDay));

        //numberPicker的設定
        jump_np = (NumberPicker)findViewById((R.id.jump_np));
        jump_np.setMaxValue(400);
        jump_np.setMinValue(40);
        jump_np.setValue(75);
        temp_np = (NumberPicker)findViewById((R.id.temp_np));
        temp_np.setMaxValue(45);
        temp_np.setMinValue(25);
        temp_np.setValue(36);
        sugar_np = (NumberPicker)findViewById((R.id.sugar_np));
        sugar_np.setMaxValue(400);
        sugar_np.setMinValue(40);
        sugar_np.setValue(100);
        pressure1_np = (NumberPicker)findViewById((R.id.pressure1_np));
        pressure1_np.setMaxValue(400);
        pressure1_np.setMinValue(40);
        pressure1_np.setValue(120);
        pressure2_np = (NumberPicker)findViewById((R.id.pressure2_np));
        pressure2_np.setMaxValue(400);
        pressure2_np.setMinValue(40);
        pressure2_np.setValue(80);

    }

    /**關閉資料庫*/
    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    /**處發日期對話視窗*/
    public void date_et_onClick(View view){
        showDialog(DATE_DIALOG_ID);
        dpd.updateDate(mYear, mMonth, mDay);
    }

    /**日期對話視窗選擇*/
    @Override
    protected Dialog onCreateDialog(int id) {
        date_et = (EditText) findViewById(R.id.date_et);
        switch (id) {
            case DATE_DIALOG_ID:
                dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        mYear = year;
                        mMonth = month;
                        mDay = day;
                        date_et.setText(setDateFormat(year, month, day));
                        date_et.setEnabled(false);
                        reset_btn.setVisibility(View.VISIBLE);
                    }
                }, mYear, mMonth, mDay);
                return dpd;
        }
            return null;
    }

    /**日期的格式 */
    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        if(String.valueOf(dayOfMonth).length() == 1)
            return String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + "0" + String.valueOf(dayOfMonth);
        else
            return String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth);

    }

    /**新增按鈕聆聽事件*/
    public void sure_btn_onClick(View view){
            String id;
            ContentValues cv = new ContentValues();
            cv.put("日期", date_et.getText().toString());
            cv.put("心跳", jump_np.getValue());
            cv.put("體溫", temp_np.getValue());
            cv.put("血糖", sugar_np.getValue());
            cv.put("收縮壓", pressure1_np.getValue());
            cv.put("舒張壓", pressure2_np.getValue());

            db.insert(DATABASE_TABLE, null, cv);
            id = "新增資料成功, 日期: " + date_et.getText().toString();

            Health_jump.this.finish();

            Toast.makeText(Health_jump.this, id, Toast.LENGTH_LONG).show();
    }

    /**重設按鈕聆聽事件*/
    public void reset_btn_onClick(View view) {
        date_et.setText("");
        date_et.setEnabled(true);

        jump_np.setValue(75);
        temp_np.setValue(36);
        sugar_np.setValue(100);
        pressure1_np.setValue(120);
        pressure2_np.setValue(80);
    }


}


