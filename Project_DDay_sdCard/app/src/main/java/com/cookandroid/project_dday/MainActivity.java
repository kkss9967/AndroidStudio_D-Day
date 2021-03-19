package com.cookandroid.project_dday;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends Activity {
    EditText myName, urName;
    DatePicker datePicker;
    Button btn1;
    TextView myNameView, dDate;
    View dialogView;

    BufferedWriter bw, bw2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity_main);

        SharedPreferences pref = getSharedPreferences("checkFirst", Activity.MODE_PRIVATE);
        boolean checkFirst = pref.getBoolean("checkFirst", false);
        if (checkFirst == false) {
            // 최초 실행일 경우
            // super.onCreate(savedInstanceState);
           //setContentView(R.layout.first_activity_main);

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("checkFirst", true);
            editor.commit();

            myName = findViewById(R.id.myName);
            urName = findViewById(R.id.urName);
            datePicker = findViewById(R.id.datePicker);
            btn1 = findViewById(R.id.btn1);

            btn1.setOnClickListener(new View.OnClickListener() { //확인 버튼 눌렀을 경우
                public void onClick(View v) {
                    // 정보 셋팅
                    dialogView = View.inflate(MainActivity.this, R.layout.dialog1, null);
                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                    dlg.setTitle("입력 정보가 맞나요?");
                    dlg.setView(dialogView);

                    myNameView = dialogView.findViewById(R.id.myNameView);
                    dDate = dialogView.findViewById(R.id.dDate);

                    final int month = datePicker.getMonth() + 1;
                    myNameView.setText(myName.getText() + " ♥ " + urName.getText());
                    if (month < 10) {
                        dDate.setText(datePicker.getYear() + "/" + '0' + month + "/" + datePicker.getDayOfMonth());
                    } else {
                        dDate.setText(datePicker.getYear() + "/" + month + "/" + datePicker.getDayOfMonth());
                    }

                    dlg.setPositiveButton("맞아요!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                bw = new BufferedWriter(new FileWriter(getFilesDir() + "info.txt", true));
                                bw.write(myName.getText().toString() + "\n" + urName.getText().toString() + "\n" + dDate.getText());
                                bw.flush();
                                bw.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // 파일에 정보 저장
                            Intent intent = new Intent(getApplicationContext(), Second_Activity.class);
                            startActivity(intent);
                            // 다음 화면으로 넘어가기
                        }
                    });
                    dlg.setNegativeButton("다시 쓸래요!", null);
                    dlg.show();
                }
            });
        }else{
            //최초 실행이 아닐 때
            // 잠금화면 설정이 되어있는지 체크를 여기서 해야 함
            // Intent 오빠가 만드는 Activity로
            Intent intent = new Intent(getApplicationContext(), Second_Activity.class);
            startActivity(intent);
        }
    }

}