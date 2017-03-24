package com.macauto.macautoscm;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ErrorTimer extends Activity{
    private static final String TAG = ErrorTimer.class.getName();

    static SharedPreferences pref ;
    static SharedPreferences.Editor editor;
    private static final String FILE_NAME = "Preference";
    private Long startTime;
    //private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error_timer);



        pref = getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        startTime = pref.getLong("ERROR_TIME_DELAY", System.currentTimeMillis()+301000);

        MyCount timercount = new MyCount(startTime - System.currentTimeMillis(), 1000);

        Log.e(TAG, "start time = "+startTime+", current = "+System.currentTimeMillis());

        timercount.start();

        //handler.removeCallbacks(updateTimer);
        //設定Delay的時間
        //handler.postDelayed(updateTimer, 1000);


    }

    public class MyCount extends CountDownTimer
    {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            NumberFormat f = new DecimalFormat("00");
            final TextView time = (TextView) findViewById(R.id.timer);
            Long spentTime = startTime - System.currentTimeMillis() ;

            Long minius = (spentTime/1000)/60;

            Long seconds = (spentTime/1000) % 60;
            time.setText(f.format(minius)+":"+f.format(seconds));
        }

        @Override
        public void onFinish() {
            editor = pref.edit();
            editor.putBoolean("LOGIN_ERROR", false);
            editor.apply();

            Intent mainIntent = new Intent(ErrorTimer.this, LoginActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }
}
