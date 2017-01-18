package com.example.jimapp.projectnhu;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by 梁景威 on 2015/10/27.
 */
public class Flash extends AppCompatActivity{
    Camera camera = null;
    private boolean isopened;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
    }

    public void power_onClick(View view) {
        if (!isopened) {
            Toast.makeText(getApplicationContext(), "手電筒已打開", Toast.LENGTH_SHORT).show();
            camera = Camera.open(); //open camera
            Parameters params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_TORCH); //flash
            camera.setParameters(params);
            camera.startPreview();
            isopened = true;
        } else {
            Toast.makeText(getApplicationContext(), "手電筒已關閉", Toast.LENGTH_SHORT).show();
            camera.stopPreview(); // close flash
            camera.release(); // close camera
            isopened = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isopened == true){
        camera.stopPreview(); // close flash
        camera.release(); // close camera
         }
    }
}
