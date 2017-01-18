package com.example.jimapp.projectnhu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/11/16.
 */
public class GPSOS_set extends Activity {
    //資料庫變數宣告
    private static String DATABASE_TABLE = "tel";
    private SQLiteDatabase db;
    private sosDB dbHelper;
    private final int num = 1;
    //變數宣告
    private EditText editText;
    private EditText editText2;

   /** public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            ConfirmExit();//按返回鍵，則執行退出確認
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void ConfirmExit(){//退出確認
        AlertDialog.Builder ad=new AlertDialog.Builder(GPSOS_set.this);
        ad.setTitle("詢問");
        ad.setMessage("確定要離開設定頁面? 尚未新增的資料都會不見喔!");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按鈕
            public void onClick(DialogInterface dialog, int i) {
                // TODO Auto-generated method stub
                GPSOS_set.this.finish();//關閉activity
                Intent intent = new Intent(GPSOS_set.this,GPSOS.class);
                startActivity(intent);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_layout);
        //資料庫常數設定
        dbHelper = new sosDB(this);
        db = dbHelper.getWritableDatabase();

        //宣告介面個變數
        setVar();
    }

    public void typeIn_btn_onClick(View view){
        Cursor c = db.query(DATABASE_TABLE,new String[]{"電話一","電話二"},null,null,null,null,"電話一",null);
        //check blank space in any editText
        String str=editText.getText().toString();
        String str1=editText2.getText().toString();

        if (str.equals("") || str1.equals("")){
            AlertDialog.Builder ad=new AlertDialog.Builder(GPSOS_set.this);
            ad.setTitle("警告");
            ad.setMessage("您有一欄位輸入為空白! 請重新填寫!");
            ad.setNeutralButton("好", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int i) {
                    // TODO Auto-generated method stub
                    editText.setText("");
                    editText2.setText("");
                }
            });
            ad.show();
        }
        else{
            if(c.getCount() == 0 ) {
                ContentValues cv = new ContentValues();
                cv.put("電話一", editText.getText().toString());
                cv.put("電話二", editText2.getText().toString());
                db.insert(DATABASE_TABLE, null, cv);
                Toast.makeText(this, "以新增聯絡人電話", Toast.LENGTH_SHORT).show();
            }
            else {
                ContentValues cv = new ContentValues();
                cv.put("電話一",editText.getText().toString());
                cv.put("電話二", editText2.getText().toString());
                db.update(DATABASE_TABLE, cv, "編號=" + num, null);
                Toast.makeText(this, "以修改聯絡人電話", Toast.LENGTH_SHORT).show();
            }
            GPSOS_set.this.finish();
        }
    }

    /**宣告介面個變數*/
    public void setVar(){
        editText = (EditText)findViewById(R.id.editText);
        editText2 = (EditText)findViewById(R.id.editText2);
    }

    public void text_close_onClick(View view) {
        GPSOS_set.this.finish();
    }

    /**關閉資料庫*/
    @Override
    protected void onStop() {
        super.onStop();
        db.close();
    }
}
