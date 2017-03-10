package com.macauto.macautoscm;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.macauto.macautoscm.Data.Constants;
import com.macauto.macautoscm.Data.HistoryAdapter;
import com.macauto.macautoscm.Data.HistoryItem;
import com.macauto.macautoscm.Data.InitData;

import java.util.ArrayList;

import static com.macauto.macautoscm.Data.FileOperation.clear_record;


public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = HistoryFragment.class.getName();

    private Context context;
    private ListView listView;

    public ArrayAdapter<Spanned> arrayAdapter = null;
    public ArrayList<HistoryItem> historyItemArrayList = new ArrayList<>();
    public HistoryAdapter historyAdapter;

    //private Connection connection;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    //private Spanned[] history;
    private static boolean isRegisterChangeListener = false;
    private MenuItem item_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_fragment);

        context = getBaseContext();
        IntentFilter filter;

        listView = (ListView) findViewById(R.id.listViewHistory);
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryItem item = historyAdapter.getItem(position);

                if (item != null) {
                    Intent intent = new Intent(context, HistoryShow.class);
                    intent.putExtra("HISTORY_TYPE", String.valueOf(item.getAction()));
                    intent.putExtra("HISTORY_TITLE", item.getTitle());
                    intent.putExtra("HISTORY_MSG", item.getMsg());
                    intent.putExtra("HISTORY_DATE", item.getDate());
                    startActivity(intent);
                }
            }
        });

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_NEW_NOTIFICATION_ACTION)) {
                    Log.d(TAG, "receive brocast !");

                    historyAdapter.notifyDataSetChanged();

                    /*Log.d(TAG, "ConnectionDetails.clientHandle = "+ InitData.clientHandle);
                    //connection = Connections.getInstance(context).getConnection(InitData.clientHandle);
                    if (InitData.connection != null) {


                        historyAdapter = new HistoryAdapter(context, R.layout.mqtt_list_view_history_item, InitData.itemHistory);
                        listView.setAdapter(historyAdapter);

                    }*/

                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.GET_NEW_NOTIFICATION_ACTION);
            context.registerReceiver(mReceiver, filter);
            isRegister = true;
            Log.d(TAG, "registerReceiver mReceiver");
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");



        super.onDestroy();

    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }
    @Override
    public void onResume() {

        Log.i(TAG, "onResume");

        /*if (InitData.connection != null) {

            historyAdapter = new HistoryAdapter(context, R.layout.mqtt_list_view_history_item, InitData.itemHistory);
            listView.setAdapter(historyAdapter);
        }*/

        historyAdapter = new HistoryAdapter(context, R.layout.history_item, InitData.notifyList);
        listView.setAdapter(historyAdapter);


        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        item_clear = menu.findItem(R.id.action_clear);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Intent intent;
        switch (item.getItemId()) {
            case R.id.action_clear:

                Log.i(TAG, "item_clear");
                InitData.notifyList.clear();
                clear_record();

                historyAdapter.notifyDataSetChanged();
                //Intent deleteIntent = new Intent(Constants.ACTION.MQTT_CLEAR_HISTORY);
                //sendBroadcast(deleteIntent);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {

    }
}
