package com.cookandroid.project_dday;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Second_Activity extends AppCompatActivity {
    ImageView header_image;
    EditText my, ur;
    DatePicker dp;
    TextView main_dday, lover_name;
    String myName, urName, date;
    BufferedWriter bw1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.closeApp:
                onBackPressed();
                return true;
            case R.id.changeInfo:
                View dialog = View.inflate(Second_Activity.this, R.layout.dialog2, null);
                AlertDialog.Builder d = new AlertDialog.Builder(Second_Activity.this);
                d.setTitle("정보를 수정하고 있어요");
                d.setView(dialog);

                my = dialog.findViewById(R.id.my);
                ur = dialog.findViewById(R.id.ur);
                dp = dialog.findViewById(R.id.dp);

                d.setPositiveButton("저장 할래요!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FileChange();

                        Second_Activity.this.recreate();
                    }
                });
                d.setNegativeButton("취소할래요", null);
                d.show();
                return true;
            case R.id.bg_bright:
                header_image.setImageResource(R.mipmap.bg_bright);
                return true;
            case R.id.bg_dark:
                header_image.setImageResource(R.mipmap.test);
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "오늘 하루는 어떤가요?", Toast.LENGTH_SHORT).show();

        header_image = findViewById(R.id.header_image);
        main_dday = findViewById(R.id.main_dday);
        lover_name = findViewById(R.id.lover_name);

        settingInfos();
    }

    public void settingInfos(){
        Calendar todaCal = Calendar.getInstance(); //오늘 날짜
        Calendar ddayCal = Calendar.getInstance(); //받아올 날짜

        try{
            BufferedReader br = new BufferedReader(new FileReader(getFilesDir() + "info.txt"));
            String str ="";
            while((str=br.readLine()) != null){
                myName = str;
                urName = br.readLine();
                date = br.readLine();
            }
            br.close();
        } catch(Exception e){e.printStackTrace();}

        lover_name.setText(myName + " ♥ " + urName);

        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8, 10));
        ddayCal.set(year, month-1, day);
        // month 경우 -1 해줘야 함
        // 1일부터 +1 계산이니까 day-1

        long today = todaCal.getTimeInMillis()/86400000;
        long dday = ddayCal.getTimeInMillis()/86400000;
        long count = dday - today;
        int countDay = (int)count;
        // dday 날짜

        if(countDay > 0) {
            main_dday.setText("Day-" + Math.abs(countDay) + "❤️");
        }else if(countDay < 0){
            main_dday.setText("Day+" + Math.abs(countDay-1) + "❤️");
        }else{
            main_dday.setText("D-Day❤️");
        }
    }

    public void  FileChange(){
        final int c_month = dp.getMonth()+1;

        try {
            bw1 = new BufferedWriter(new FileWriter(getFilesDir() + "info.txt"));
            if (c_month < 10) {
                bw1.write(my.getText() + "\n" + ur.getText() + "\n" + dp.getYear() + "/" + '0' + c_month + "/" + dp.getDayOfMonth());
            } else {
                bw1.write(my.getText() + "\n" + ur.getText() + "\n" + dp.getYear() + "/" + c_month + "/" + dp.getDayOfMonth());
            }
            bw1.flush();
            bw1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        finishAffinity();
        System.runFinalization();
        System.exit(0);
    }
}