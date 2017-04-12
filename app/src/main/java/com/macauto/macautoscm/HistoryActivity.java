package com.macauto.macautoscm;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.macauto.macautoscm.Data.Constants;
import com.macauto.macautoscm.Data.HistoryAdapter;
import com.macauto.macautoscm.Data.HistoryItem;
import com.macauto.macautoscm.Data.InitData;

import java.util.ArrayList;

import static com.macauto.macautoscm.Data.FileOperation.clear_record;


public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = HistoryActivity.class.getName();

    private Context context;
    private ListView listView;

    //public ArrayAdapter<Spanned> arrayAdapter = null;
    //public ArrayList<HistoryItem> historyItemArrayList = new ArrayList<>();
    public ArrayList<HistoryItem> sortedNotifyList = new ArrayList<>();
    public HistoryAdapter historyAdapter;

    //private Connection connection;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    //private Spanned[] history;
    //private static boolean isRegisterChangeListener = false;
    //private MenuItem item_clear;
    //private static SearchView searchView;

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


                } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_HISTORY_LIST_SORT_COMPLETE)) {
                    historyAdapter = new HistoryAdapter(context, R.layout.history_item, sortedNotifyList);
                    listView.setAdapter(historyAdapter);
                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.GET_NEW_NOTIFICATION_ACTION);
            filter.addAction(Constants.ACTION.GET_HISTORY_LIST_SORT_COMPLETE);
            context.registerReceiver(mReceiver, filter);
            isRegister = true;
            Log.d(TAG, "registerReceiver mReceiver");
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");

        if (isRegister && mReceiver != null) {
            try {
                context.unregisterReceiver(mReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            isRegister = false;
            mReceiver = null;
        }



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

        if (sortedNotifyList.size() > 0) {
            historyAdapter = new HistoryAdapter(context, R.layout.history_item, sortedNotifyList);
            listView.setAdapter(historyAdapter);
        } else {
            historyAdapter = new HistoryAdapter(context, R.layout.history_item, InitData.notifyList);
            listView.setAdapter(historyAdapter);
        }




        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //item_clear = menu.findItem(R.id.action_clear);

        try {
            //SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search_keeper));
            searchView.setOnQueryTextListener(queryListener);
        }catch(Exception e){
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Intent intent;
        /*switch (item.getItemId()) {
            case R.id.action_clear:

                Log.i(TAG, "item_clear");
                AlertDialog.Builder confirmdialog = new AlertDialog.Builder(HistoryActivity.this);
                confirmdialog.setIcon(R.drawable.ic_warning_black_48dp);
                confirmdialog.setTitle(getResources().getString(R.string.scm_warning));
                confirmdialog.setMessage(getResources().getString(R.string.scm_clear_msg));
                confirmdialog.setPositiveButton(getResources().getString(R.string.scm_confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        InitData.notifyList.clear();
                        clear_record();

                        historyAdapter.notifyDataSetChanged();

                    }
                });
                confirmdialog.setNegativeButton(getResources().getString(R.string.scm_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                confirmdialog.show();


                //Intent deleteIntent = new Intent(Constants.ACTION.MQTT_CLEAR_HISTORY);
                //sendBroadcast(deleteIntent);
                break;
        }*/
        return true;
    }

    @Override
    public void onBackPressed() {

    }

    final private android.support.v7.widget.SearchView.OnQueryTextListener queryListener = new android.support.v7.widget.SearchView.OnQueryTextListener() {
        //searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Intent intent;

            //ArrayList<MeetingListItem> list = new ArrayList<>();
            sortedNotifyList.clear();
            if (!newText.equals("")) {



                //ArrayList<PasswordKeeperItem> list = new ArrayList<PasswordKeeperItem>();
                for (int i = 0; i < InitData.notifyList.size(); i++) {
                    if (InitData.notifyList.get(i).getTitle().contains(newText)) {
                        sortedNotifyList.add(InitData.notifyList.get(i));
                    } else if (InitData.notifyList.get(i).getMsg().contains(newText)) {
                        sortedNotifyList.add(InitData.notifyList.get(i));
                    } else if (InitData.notifyList.get(i).getDate().contains(newText)) {
                        sortedNotifyList.add(InitData.notifyList.get(i));
                    }
                }

                //passwordKeeperArrayAdapter = new PasswordKeeperArrayAdapter(Password_Keeper.this, R.layout.passwd_keeper_browsw_item, list);
                //listView.setAdapter(passwordKeeperArrayAdapter);

            } else {
                //ArrayList<PasswordKeeperItem> list = new ArrayList<PasswordKeeperItem>();

                for (int i = 0; i < InitData.notifyList.size(); i++) {
                    sortedNotifyList.add(InitData.notifyList.get(i));
                }


                //passwordKeeperArrayAdapter = new PasswordKeeperArrayAdapter(Password_Keeper.this, R.layout.passwd_keeper_browsw_item, list);
                //listView.setAdapter(passwordKeeperArrayAdapter);
            }

            //meetingArrayAdapter = new MeetingArrayAdapter(context, R.layout.meeting_list_item, list);
            //AllFragment.resetAdapter(list);
            //AllFragment.listView.setAdapter(AllFragment.meetingArrayAdapter);
            intent = new Intent(Constants.ACTION.GET_HISTORY_LIST_SORT_COMPLETE);
            sendBroadcast(intent);


            return false;
        }
    };
}
