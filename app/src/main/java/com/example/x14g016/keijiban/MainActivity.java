package com.example.x14g016.keijiban;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final String GAS_ADR = "https://script.google.com/macros/s/AKfycbzdduXKRiJIwx5oeYsqk0lD7DlPD4dM31xpKpQzYkwi4fDA5vbK/exec";
    private EditText mEditname;
    private EditText mEditmsg;
    private Button sendBtn;
    private Button recvdBtn;
    private Date date;
    private LinearLayout mChatLayout;

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.button){
            mEditname = (EditText)findViewById(R.id.editText);
            mEditmsg =  (EditText)findViewById(R.id.editText2);
            String name = mEditname.getText().toString();
            String msg =  mEditmsg.getText().toString();
            if(name=="" &&msg=="") {
                final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                date = new Date(System.currentTimeMillis());
                sendData(date, mEditname.getText().toString(), mEditmsg.getText().toString());
            }
      //      }else{
      //          Toast toast = Toast.makeText(
      //                  this, "名前、メッセージを入力してください。", Toast.LENGTH_SHORT);
      //          toast.setGravity(Gravity.TOP | Gravity.RIGHT, 0, 0);
      //          toast.show();
      //     }
        }else if(view.getId()==R.id.button2){
            recvdData();
        }
    }

    /**
     * To work on unit tests, switch the Test Artifact in the Build Variants view.
     */
    class SendData{
        public Date date;
        public String msg;
        public String name;
    }
    class RecvData{
        public int ret;
        public String[][] values;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendBtn = (Button)findViewById(R.id.button);
        recvdBtn =(Button)findViewById(R.id.button2);

        sendBtn.setOnClickListener(this);
        recvdBtn.setOnClickListener(this);

    }

    public void sendData(Date date, String msg, String name){
        SendData sendData = new SendData();
        sendData.date = date;
        sendData.msg = msg;
        sendData.name = name;

        RecvData recvData = Json.send(GAS_ADR,sendData,RecvData.class);

        if(recvData != null && recvData.ret == 1)
            System.out.println("送信完了");
        else
            System.out.println("送信エラー");
    }

    public void recvdData(){
        RecvData recvData = Json.send(GAS_ADR,null,RecvData.class);
        mChatLayout = (LinearLayout)findViewById(R.id.chat);
        mChatLayout.removeAllViews();
        if(recvData != null && recvData.ret == 1){
            for(String[] value : recvData.values){
                TextView textView = new TextView(MainActivity.this);
                textView.setText(value[0]+"\t"+value[1]+"\t"+value[2]);
               mChatLayout.addView(textView);
            }
        }
        else
            System.out.println("送信エラー");


    }
}
