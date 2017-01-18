package com.example.jimapp.projectnhu;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public Intent it;
    Calendar calendar;
    //按鈕變數宣告
    public ImageButton setClockBtn;
    //ListView宣告
    private ListView listView;
    public ArrayList<String> items;
    private ArrayAdapter<String> listAdapter;
    //DB
    private static String DATABASE_TABLE = "alarm";
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    //delete鬧鐘變數
    private String name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ListView變數呼叫
        listView = (ListView)findViewById(R.id.listView);
        items = new ArrayList<>();
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,items);
        listView.setAdapter(listAdapter);
        //DB
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        //按鈕變數呼叫
        setClockBtn = (ImageButton) findViewById(R.id.setClockBtn);

        calendar = Calendar.getInstance();
        getValue();         //ListView show DB data
        delClock();         //ListView長按事件
        tip();              //提示如何刪除鬧鐘
        //setClock();       // 試寫 未使用.2015/11/29

        setClockBtn.setOnClickListener(new View.OnClickListener() {     //設定鬧鐘按鈕
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Alarm_add.class);
                startActivity(intent);
            }
        });
    }

    public void tip(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,"長按選項已刪除鬧鐘",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void delClock(){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                String[] value = listView.getItemAtPosition(position).toString().split("======");
                name = value[2];

                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                ad.setTitle("詢問?").setMessage("確定要刪除這筆用藥鬧鐘資料?");
                ad.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //利用WHERE查出ID，在用這ID刪掉同樣requestcode的PendingIntent觸發的Alarm
                        Cursor cursor = db.rawQuery("SELECT _id FROM " + DATABASE_TABLE + " WHERE name = " + "'" + name + "'", null);
                        cursor.moveToNext();
                        int identity = cursor.getInt(0);
                        db.delete(DATABASE_TABLE, "_id=" + identity, null);   //DB資料刪除
                        Intent it = new Intent(MainActivity.this, AlarmReceiver.class);
                        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, identity, it, 0);
                        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarm.cancel(pi);
                        Toast.makeText(MainActivity.this, "id" + String.valueOf(identity) + "名稱:" + name + "藥物提醒以刪除", Toast.LENGTH_SHORT).show();

                        MainActivity.this.finish();
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);

                    }
                });
                ad.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                ad.show();
                return false;
            }
        });
    }

    public void getValue(){
        //DB資料設定到ListView裡
        Cursor c = db.query(DATABASE_TABLE, new String[]{"_id", "time", "name"}, null, null, null, null, "_id", null);
        SimpleDateFormat formatter = new SimpleDateFormat ("HH:mm a");
        String str = "";
        c.moveToNext();
        for(int i = 0; i < c.getCount(); i++) {
            str = formatter.format(c.getLong(1));
            items.add(c.getString(0) + "======" + str + "======" + c.getString(2));
            c.moveToNext();
        }
        listView.setAdapter(listAdapter);
    }

    /**關閉資料庫*/
    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }

    @Override
    protected void onResume(){
        super.onResume();
        //清空ListView資料
        listAdapter.clear();
        listView.setAdapter(listAdapter);
        //重抓資料庫的值
        getValue();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    /**public void setClock(){
     Cursor c = db.query(DATABASE_TABLE, new String[]{"編號","時間"}, null, null, null, null, "時間", null);
     if (c.getCount() != 0) {
     c.moveToNext();
     String str1 ;
     String str2 ;
     SimpleDateFormat formatter1 = new SimpleDateFormat ("HH");
     str1 = formatter1.format(c.getLong(1));
     SimpleDateFormat formatter2 = new SimpleDateFormat ("mm");
     str2 = formatter2.format(c.getLong(1));
     calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(str1));
     calendar.set(Calendar.MINUTE,Integer.parseInt(str2));
     calendar.set(Calendar.SECOND, 0);

     //AlarmManager呼叫
     it = new Intent(MainActivity.this, AlarmReceiver.class);
     it.putExtra("msg", "playVoice");
     pi = PendingIntent.getBroadcast(MainActivity.this, 1, it, PendingIntent.FLAG_ONE_SHOT);
     alarm = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);
     alarm.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis() , pi);
     } else {}
     }*/

}


