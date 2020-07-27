package com.cookandroid.project_dday;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Second_Activity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    ImageView header_image;
    EditText my, ur;
    DatePicker dp;
    TextView main_dday, lover_name, day100, day300, year1, year2;
    String myName, urName, date, forBG;
    Bitmap bitmap, newBitmap;
    BufferedWriter bw1, bw2;

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
            case R.id.bg_change:
                permissionRequest();
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
                return true;
        }
        return false;
    }

    public void permissionRequest(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        Uri image = data.getData();
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);
            header_image.setImageBitmap(bitmap);

            bgChange(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBase64String(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

    public void bgChange(Bitmap bitmap){
        try {
            bw2 = new BufferedWriter(new FileWriter(getFilesDir() + "bgInfo.txt"));
            String a = getBase64String(bitmap);
            bw2.write(a);
            bw2.flush();
            bw2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "오늘 하루는 어떤가요?", Toast.LENGTH_SHORT).show();

        header_image = findViewById(R.id.header_image);
        main_dday = findViewById(R.id.main_dday);
        lover_name = findViewById(R.id.lover_name);

        day100 = findViewById(R.id.day_100);
        day300 = findViewById(R.id.day_300);
        year1 = findViewById(R.id.year1);
        year2 = findViewById(R.id.year2);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

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

            BufferedReader br2 = new BufferedReader(new FileReader(getFilesDir() + "bgInfo.txt"));
            String bg = "";

            while((bg = br2.readLine()) != null){
                forBG = bg;

                byte[] decodedByteArray = Base64.decode(forBG, Base64.NO_WRAP);
                newBitmap = BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
            }
        } catch(Exception e){e.printStackTrace();}

        if(newBitmap != null) {
            header_image.setImageBitmap(newBitmap);
        }else{
            header_image.setImageResource(R.mipmap.test);
        }

        lover_name.setText(myName + " ❤ " + urName);

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
            main_dday.setText("Day+1❤️");
        }

        day100.setText("100일: DAY-" + (100 - Math.abs(countDay-1)));
        day300.setText("300일: DAY-" + (300 - Math.abs(countDay-1)));
        year1.setText("1년: DAY-" + (365 - Math.abs(countDay-1)));
        year2.setText("2년: DAY-" + (730 - Math.abs(countDay-1)));

        if(Math.abs(countDay-1) == 100){
            day100.setVisibility(View.GONE);
        }else if(Math.abs(countDay-1) == 300){
            day300.setVisibility(View.GONE);
        }else if(Math.abs(countDay-1) == 365){
            year1.setVisibility(View.GONE);
        }else if(Math.abs(countDay-1) == 730){
            year2.setVisibility(View.GONE);
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