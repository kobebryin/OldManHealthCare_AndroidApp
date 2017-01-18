package com.example.jimapp.projectnhu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/11/21.
 */
public class Shake extends Activity  {

    //資料庫變數宣告
    private static String DATABASE_TABLE = "score";
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    private SensorManager mSensorManager;   //體感(Sensor)使用管理
    private Sensor mSensor;                 //體感(Sensor)類別
    private float mLastX;                    //x軸體感(Sensor)偏移
    private float mLastY;                    //y軸體感(Sensor)偏移
    private float mLastZ;                    //z軸體感(Sensor)偏移
    private long mLastUpdateTime;           //觸發時間
    private double mSpeed;                 //甩動力道數度
    private static final int UPTATE_INTERVAL_TIME = 70;  //觸發間隔時間
    private static final int SPEED_SHRESHOLD = 3000;      //甩動力道數度設定值 (數值越大需甩動越大力，數值越小輕輕甩動即會觸發)
    private TextView score_tv;
    private TextView sec_tv;
    public int score = 0;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        //資料庫常數設定
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        //取得體感(Sensor)服務使用權限
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        //取得手機Sensor狀態設定
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //註冊體感(Sensor)甩動觸發Listener
        mSensorManager.registerListener(SensorListener, mSensor,SensorManager.SENSOR_DELAY_GAME);

        score_tv = (TextView)findViewById(R.id.score_tv);
        sec_tv = (TextView)findViewById(R.id.sec_tv);
        score_tv.setText(""+score);

        //倒數計時
        new CountDownTimer(15000,1000){
            @Override
            public void onFinish() {
                mSensorManager.unregisterListener(SensorListener); //停止動感偵測
                //增加歷史紀錄
                ContentValues cv = new ContentValues();
                cv.put("分數", score);
                db.insert(DATABASE_TABLE, null, cv);

                AlertDialog.Builder ad = new AlertDialog.Builder(Shake.this);
                ad.setTitle("TimeUP!");
                ad.setMessage("時間到囉!\n" + "總分數為:" + score);
                ad.setCancelable(false);
                ad.setPositiveButton("重玩", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Shake.this.finish();
                        Intent intent = new Intent(Shake.this, Shake.class);
                        startActivity(intent);
                    }
                });
                ad.setNegativeButton("結束", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Shake.this.finish();
                    }
                });
                ad.show();
            }
            @Override
            public void onTick(long millisUntilFinished) {
                sec_tv.setText("\t" + millisUntilFinished / 1000 + "\t");
            }
        }.start();
    }

    private SensorEventListener SensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent mSensorEvent) {
            //當前觸發時間
            long mCurrentUpdateTime = System.currentTimeMillis();
            //觸發間隔時間 = 當前觸發時間 - 上次觸發時間
            long mTimeInterval = mCurrentUpdateTime - mLastUpdateTime;
            //若觸發間隔時間< 70 則return;
            if (mTimeInterval < UPTATE_INTERVAL_TIME) return;
            mLastUpdateTime = mCurrentUpdateTime;

            //取得xyz體感(Sensor)偏移
            float x = mSensorEvent.values[0];
            float y = mSensorEvent.values[1];
            float z = mSensorEvent.values[2];

            //甩動偏移速度 = xyz體感(Sensor)偏移 - 上次xyz體感(Sensor)偏移
            float mDeltaX = x - mLastX;
            float mDeltaY = y - mLastY;
            float mDeltaZ = z - mLastZ;

            mLastX = x;
            mLastY = y;
            mLastZ = z;

            //體感(Sensor)甩動力道速度公式
            mSpeed = Math.sqrt(mDeltaX * mDeltaX + mDeltaY * mDeltaY + mDeltaZ * mDeltaZ)/ mTimeInterval * 10000;

            //若體感(Sensor)甩動速度大於等於甩動設定值則進入 (達到甩動力道及速度)
            if (mSpeed >= SPEED_SHRESHOLD) {
                //達到搖一搖甩動後要做的事情
                score++;
                score_tv.setText(""+score);
            }

        }

        public void onAccuracyChanged(Sensor sensor , int accuracy) {
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在程式關閉時移除體感(Sensor)觸發
        mSensorManager.unregisterListener(SensorListener);
    }

    /**關閉資料庫*/
    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }
}
