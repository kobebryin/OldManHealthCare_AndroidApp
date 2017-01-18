package com.example.jimapp.projectnhu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by 梁景威 on 2015/10/27.
 */
public class index extends AppCompatActivity {
    //建構ImageBtn
    private ImageButton Alarm_btn;
    private ImageButton Flash_btn;
    private ImageButton Health_btn;
    private ImageButton magnyfying_btn;
    private ImageButton gpsos_btn;
    private ImageButton shake_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        //鬧鐘
        Alarm_btn = (ImageButton) findViewById(R.id.Alarm_btn);
        Alarm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(index.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //手電筒
        Flash_btn = (ImageButton) findViewById(R.id.Flash_btn);
        Flash_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(index.this, Flash.class);
                startActivity(intent);
            }
        });
        //健康紀錄簿
        Health_btn = (ImageButton) findViewById(R.id.Health_btn);
        Health_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(index.this, Health.class);
                startActivity(intent);
            }
        });
        //放大鏡
        magnyfying_btn = (ImageButton)findViewById(R.id.magnifying_btn);
        magnyfying_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(index.this,Magnifying.class);
                startActivity(intent);
            }
        });
        //GPSOS
        gpsos_btn = (ImageButton)findViewById(R.id.gpsos_btn);
        gpsos_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(index.this,GPSOS.class);
                startActivity(intent);
            }
        });

        //IShake Game
        shake_btn = (ImageButton)findViewById(R.id.shake_btn);
        shake_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(index.this,Shake_index.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings1) {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("About Us");
            ad.setMessage("程式開發:梁景威\n" +
                    "UI設計:林道祐、邱柏凱、魏伶僅、張諭荃\n" +
                    "指導教授:王佳文\n" +
                    "學校系所:南華大學電子商務管理學系");
            ad.setNegativeButton("離開", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            ad.setNeutralButton("Email我們", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri uri = Uri.parse("mailto:10126006@nhu.edu.tw");
                    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                    startActivity(i);
                }
            });
            ad.setPositiveButton("FaceBook", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri uri = Uri.parse("http://www.facebook.com/kobebryin");
                    Intent i = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(i);
                }
            });
            ad.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

