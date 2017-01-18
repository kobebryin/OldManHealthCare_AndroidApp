package com.example.jimapp.projectnhu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by 梁景威 on 2015/11/2.
 */
public class Health extends AppCompatActivity {

    public ImageButton jump_btn;
    public ImageButton history_btn;

    /**攔截返回健事件*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Health.this.finish();//關閉activity
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        jump_btn = (ImageButton)findViewById(R.id.jump_btn);
        jump_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Health.this,Health_jump.class);
                startActivity(intent);
            }
        });

        history_btn = (ImageButton)findViewById(R.id.history_btn);
        history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Health.this,History.class);
                startActivity(intent);
            }
        });

    }

}
