package com.macauto.macautoscm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String IID_TOKEN = FirebaseInstanceId.getInstance().getToken();

        Log.d(TAG, "IID_TOKEN = "+IID_TOKEN);
    }
}
