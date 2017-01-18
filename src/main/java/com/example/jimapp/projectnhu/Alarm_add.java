package com.example.jimapp.projectnhu;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 2015/11/22.
 */
public class Alarm_add extends Activity {
    //資料庫變數宣告
    private static String DATABASE_TABLE = "alarm";
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    //time picker dialog ID
    static final int TIME_DIALOG_ID = 1;
    Calendar c;
    private int mHour;
    private int mMinute;
    int setHour;
    int setMin;
    private EditText drugName_et;
    private TextView textView22;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_alarm_add);

        drugName_et = (EditText)findViewById(R.id.drugName_et);
        textView22 = (TextView)findViewById(R.id.textView22);

        c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        //資料庫常數設定
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
    }

    public void setAlarm_btn_onClick(View view){
        showDialog(TIME_DIALOG_ID);
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    setHour = hourOfDay;
                    setMin = minute;

                    /** ---------- 鬧鐘語法 ---------- **/
                    c.set(Calendar.HOUR_OF_DAY, setHour);
                    c.set(Calendar.MINUTE, setMin);
                    c.set(Calendar.SECOND, 0);

                    SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss a");
                    String str = df.format(c.getTimeInMillis());
                    textView22.setText(str);
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                TimePickerDialog tpd= new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, true);
                return tpd;
        }
        return null;
    }

    public void addAlarm_btn_onClick(View view){
        int id; //PendingIntent ID
        long time;
        if ("".equals(drugName_et.getText().toString().trim()) || "".equals(textView22.getText().toString().trim())) {
            Toast.makeText(this,"藥品名稱/響鈴時間請勿輸入空白!",Toast.LENGTH_SHORT).show();
        }
        else {
            //存入資料庫
            ContentValues cv = new ContentValues();
            cv.put("time", c.getTimeInMillis());
            cv.put("name", drugName_et.getText().toString());
            db.insert(DATABASE_TABLE, null, cv);
            //設定鬧鐘
            Cursor cursor = db.rawQuery("SELECT _id, time FROM " + DATABASE_TABLE + " WHERE name = " + "'" + drugName_et.getText().toString() + "'", null);
            cursor.moveToNext();
            id = cursor.getInt(0);
            time = cursor.getLong(1);

            Intent it = new Intent(Alarm_add.this, AlarmReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(Alarm_add.this, id, it, 0);
            AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarm.set(AlarmManager.RTC_WAKEUP, (time), pi);

            Alarm_add.this.finish();
        }
    }

    public void reset_btn_onClick(View view){
        textView22.setText("");
        drugName_et.setText("");
    }

    public void text_close_onClick(View view) {
        Alarm_add.this.finish();
    }

    /**關閉資料庫*/
    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }
}

//ALARMMANAGER語法test
/**String str1 ;
 String str2 ;
 SimpleDateFormat formatter1 = new SimpleDateFormat ("HH");
 str1 = formatter1.format(c.getLong(1));
 SimpleDateFormat formatter2 = new SimpleDateFormat ("mm");
 str2 = formatter2.format(c.getLong(1));
 calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(str1));
 calendar.set(Calendar.MINUTE,Integer.parseInt(str2));
 calendar.set(Calendar.SECOND, 0);
 //AlarmManager呼叫
 it = new Intent(Alarm_add.this, AlarmReceiver.class);
 it.putExtra("msg", "playVoice");
 pi = PendingIntent.getBroadcast(Alarm_add.this, 1, it, PendingIntent.FLAG_ONE_SHOT);
 alarm = (AlarmManager) Alarm_add.this.getSystemService(Context.ALARM_SERVICE);
 alarm.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis() , pi);*/
