package com.example.jimapp.projectnhu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ZoomControls;

/**
 * Created by Administrator on 2015/11/11.
 */
public class Magnifying extends Activity  {

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    public ZoomControls zoomControls;
    int zoomNum = 28;

    /**攔截返回健事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            ConfirmExit();//按返回鍵，則執行退出確認
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void ConfirmExit(){//退出確認
        AlertDialog.Builder ad=new AlertDialog.Builder(Magnifying.this);
        ad.setTitle("詢問");
        ad.setMessage("確定要離開放大鏡功能?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按鈕
            public void onClick(DialogInterface dialog, int i) {
                // TODO Auto-generated method stub
                Magnifying.this.finish();//關閉activity
            }
        });
        ad.setNegativeButton("否",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                //不退出不用執行任何操作
            }
        });
        ad.show();//顯示對話框
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magnifying);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new MySHCallback());

        //ZoomControls
        zoomControls = (ZoomControls)findViewById(R.id.zoomControls);
    }

    /**當觸控螢幕會自動對焦*/
    public void onClick(View view){
        camera.autoFocus(autoFocus);
    }
    /**surfaceHolder*/
    class MySHCallback implements SurfaceHolder.Callback {
        public void surfaceCreated(SurfaceHolder holder) {
            if(camera == null)
            {
                camera = Camera.open();
                try {camera.setPreviewDisplay(holder);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            camera.startPreview();
            setZoom(28);

            zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(zoomNum<58) {
                        zoomNum = zoomNum+10;
                        setZoom(zoomNum);
                    }
                }
            });
            zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(zoomNum>8) {
                       zoomNum = zoomNum-10;
                       setZoom(zoomNum);
                   }
                }
            });

            /**switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        setZoom(48);
                    else
                        setZoom(28);
                }
            });*/
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            camera.stopPreview();
            camera.release();
        }
    }
    /**自動對焦*/
    Camera.AutoFocusCallback autoFocus= new Camera.AutoFocusCallback(){
        public void onAutoFocus(boolean success, Camera camera) {
        }
    };

    private void setZoom(int mValue)
    {
        Camera.Parameters mParams = camera.getParameters();
        mParams.setZoom(mValue);
        camera.setParameters(mParams);
    }

    @Override
    protected void onStop(){
        super.onStop();
    }
}
