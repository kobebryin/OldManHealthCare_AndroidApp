package com.example.jimapp.projectnhu;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import java.util.Locale;


/**
 * Created by Jimmy on 2015/11/14.
 */
public class GPSOS extends AppCompatActivity implements LocationListener {

    //資料庫變數宣告
    private static String DATABASE_TABLE = "tel";
    private SQLiteDatabase db;
    private sosDB dbHelper;
    //GPS定會變數宣告
    private LocationManager lms;
    private String bestProvider = null;
    private boolean getService = false;
    //變數宣告
    private TextView textView16;
    private TextView textView17;
    private  TextView longitude_txt;
    private TextView latitude_txt;
    //警報變數宣告
    Handler aHandler;
    int count = 30;
    ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_SYSTEM, ToneGenerator.MAX_VOLUME);
    ImageButton alarm_btn;
    Camera camera = null;
    //警報開關
    boolean power = false;

    /**
     * 攔截返回健事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            ConfirmExit();//按返回鍵，則執行退出確認
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void ConfirmExit() {//退出確認
        AlertDialog.Builder ad = new AlertDialog.Builder(GPSOS.this);
        ad.setTitle("詢問");
        ad.setMessage("確定要離開GPSOS功能?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按鈕
            public void onClick(DialogInterface dialog, int i) {
                // TODO Auto-generated method stub
                GPSOS.this.finish();//關閉activity
            }
        });
        ad.setNegativeButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                //不退出不用執行任何操作
            }
        });
        ad.show();//顯示對話框
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsos);

        //資料庫常數設定
        dbHelper = new sosDB(this);
        db = dbHelper.getReadableDatabase();

        textView16 = (TextView) findViewById(R.id.textView16);
        textView17 = (TextView) findViewById(R.id.textView17);
        longitude_txt = (TextView) findViewById(R.id.longitude);
        latitude_txt = (TextView) findViewById(R.id.latitude);
        alarm_btn = (ImageButton)findViewById(R.id.alarm_btn);

        //獲取之前輸入的緊急連絡人電話
        getValue();

        //取得系統定位服務
        lms = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (lms.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //如果GPS定位開啟，呼叫locationServiceInitial()更新位置
            getService = true;
            Location location = lms.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            bestProvider = LocationManager.GPS_PROVIDER;
            getLocation(location);
        } else {
            AlertDialog.Builder ad = new AlertDialog.Builder(GPSOS.this);
            ad.setTitle("詢問");
            ad.setMessage("您尚未開啟GPS定位功能，本功能必須開啟GPS定位才可使用，要跳轉到設定頁面開啟GPS定位功能嗎?");
            ad.setCancelable(false);
            ad.setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按鈕
                public void onClick(DialogInterface dialog, int i) {
                    // TODO Auto-generated method stub
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));//開啟設定頁面
                    GPSOS.this.finish();//關閉activity
                }
            });
            ad.setNegativeButton("否", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int i) {
                    GPSOS.this.finish();//關閉activity
                }
            });
            ad.show();//顯示對話框
        }
    }

    //傳送求救簡訊Button聆聽事件
    public void send_btn_onClick(View view) {
        sendMessege();
    }
    //撥打給緊急電話Button聆聽事件
    public void call_btn_onClick(View view){
        AlertDialog.Builder ad = new AlertDialog.Builder(GPSOS.this);
        ad.setTitle("詢問");
        ad.setMessage("確定要撥給緊急電話 112 ?");
        ad.setPositiveButton("確定", new DialogInterface.OnClickListener() {//退出按鈕
            public void onClick(DialogInterface dialog, int i) {
                // TODO Auto-generated method stub
                String number = "112";
                Intent myIntentDial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                startActivity(myIntentDial);
            }
        });
        ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        ad.show();//顯示對話框
    }
    //撥打給預設聯絡人Button聆聽事件
    public void callperson_btn_onClick(View view){
        AlertDialog.Builder ad = new AlertDialog.Builder(GPSOS.this);
        ad.setTitle("詢問");
        ad.setMessage("請問要撥打給哪一位預設聯絡人 ?");
        ad.setPositiveButton("預設聯絡人1", new DialogInterface.OnClickListener() {//退出按鈕
            public void onClick(DialogInterface dialog, int i) {
                // TODO Auto-generated method stub
                String number = textView16.getText().toString();
                Intent myIntentDial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                startActivity(myIntentDial);
            }
        });
        ad.setNeutralButton("預設聯絡人2", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                // TODO Auto-generated method stub
                String number = textView17.getText().toString();
                Intent myIntentDial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                startActivity(myIntentDial);
            }
        });
        ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        ad.show();//顯示對話框
    }
    //警報聲響按鈕聆聽事件
    public void alarm_btn_onClick(View view){
        if(power == false){
            aHandler = new Handler();
            aHandler.post(runnable);
            power = true;
            }
        else {
            power = false;
            toneGenerator.stopTone();
            aHandler.removeCallbacks(runnable);
        }
    }
    final Runnable runnable = new Runnable() {          //Handler重複事件
        public void run() {
            // TODO Auto-generated method stub
            if(count > 0) {
                count--;
                //警報音
                toneGenerator.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_INTERGROUP);
                //閃光燈閃爍
                camera = Camera.open();
                Camera.Parameters params = camera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH); //flash
                camera.setParameters(params);
                camera.startPreview();
                camera.stopPreview();
                camera.release();
                //每0.3秒重複一次
                aHandler.postDelayed(runnable, 300);
                // alarm_btn.setVisibility(View.INVISIBLE); 按鈕消失
            }
            else{
                toneGenerator.stopTone();
                aHandler.removeCallbacks(runnable);
                count = 30;
                power = false;
                //alarm_btn.setVisibility(View.VISIBLE); 按鈕出現
            }
        }
    };

    /**新增緊急聯絡人Button聆聽事件      (2015.12.8 取消此按鈕，換成menu設定)
    public void set_btn_onClick(View view) {
        GPSOS.this.finish();
        Intent intent = new Intent(GPSOS.this, GPSOS_set.class);
        startActivity(intent);
    }*/

    //取得地址Button聆聽事件
    public void add_btn_onCLick(View view) {
        Location location = lms.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        AlertDialog.Builder ad = new AlertDialog.Builder(GPSOS.this);
        ad.setTitle("您現在位於:").setMessage(getAddressByLocation(location)).setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        ad.show();
    }

    //將定位資訊顯示在畫面中
    private void getLocation(Location location) {
        if (location != null) {
            TextView longitude_txt = (TextView) findViewById(R.id.longitude);
            TextView latitude_txt = (TextView) findViewById(R.id.latitude);

            Double longitude = location.getLongitude();    //取得經度
            Double latitude = location.getLatitude();    //取得緯度

            longitude_txt.setText(String.valueOf(longitude));
            latitude_txt.setText(String.valueOf(latitude));
        } else {
            Toast.makeText(this, "目前位置在室內或其他因素，暫時無法定位座標", Toast.LENGTH_SHORT).show();
        }
    }

    //當地點改變時
    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        getLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {   //當GPS或網路定位功能關閉時
        // TODO 自動產生的方法 Stub
        Toast.makeText(this, "請開啟gps", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {    //當GPS或網路定位功能開啟

    }

    @Override
    public void onProviderDisabled(String provider) {   //定位狀態改變
        //status=OUT_OF_SERVICE 供應商停止服務
        //status=TEMPORARILY_UNAVAILABLE 供應商暫停服務
    }

    public String getAddressByLocation(Location location) {
        String returnAddress = "";
        try {
            if (location != null) {
                Double longitude = location.getLongitude();    //取得經度
                Double latitude = location.getLatitude();    //取得緯度

                Geocoder gc = new Geocoder(this, Locale.TRADITIONAL_CHINESE);    //地區:台灣
                //自經緯度取得地址
                List<Address> lstAddress = gc.getFromLocation(latitude, longitude, 1);
                returnAddress = lstAddress.get(0).getAddressLine(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnAddress;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if (getService) {
            lms.requestLocationUpdates(bestProvider, 1000, 1, this);
            //服務提供者、更新頻率60000毫秒=1分鐘、最短距離、地點改變時呼叫物件
        }
        getValue();
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        if (getService) {
            lms.removeUpdates(this);    //離開頁面時停止更新
        }
        super.onPause();
    }
    //獲取SQLite裡的聯絡人資料
    public void getValue() {
        Cursor c = db.query(DATABASE_TABLE, new String[]{"電話一", "電話二"}, null, null, null, null, "電話一", null);
        if (c.getCount() != 0) {
            c.moveToNext();
            textView16.setText(c.getString(0));
            textView17.setText(c.getString(1));
        } else {
            textView16.setText("您尚未輸入電話");
            textView17.setText("您尚未輸入電話");
        }
    }
    //傳送簡訊
    public void sendMessege() {
        Location location = lms.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        String num1 = textView16.getText().toString();
        String num2 = textView17.getText().toString();
        String longitude = longitude_txt.getText().toString();
        String latitude = latitude_txt.getText().toString();
        String text1 = "求救!經度:" + longitude +",緯度:" + latitude + ",地址:" + getAddressByLocation(location) ;

        SmsManager sms = SmsManager.getDefault();
        try {
            sms.sendTextMessage(num1, null, text1, null, null);
            sms.sendTextMessage(num2, null, text1, null, null);
            Toast.makeText(this, "Send Msg!", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)  // 如果發送錯誤或其他錯誤發生
        {
            Toast.makeText(this, "發送簡訊到失敗。", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(GPSOS.this, GPSOS_set.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void tip_onClick(View view){
        Toast.makeText(GPSOS.this,"點選手機選項鍵以新增/更新聯絡人資料",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

}

