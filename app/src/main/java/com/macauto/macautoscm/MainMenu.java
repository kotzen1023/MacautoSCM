package com.macauto.macautoscm;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.macauto.macautoscm.Data.Constants;
import com.macauto.macautoscm.Data.InitData;

import static com.macauto.macautoscm.Data.FileOperation.clear_record;
import static com.macauto.macautoscm.HistoryFragment.historyAdapter;
import static com.macauto.macautoscm.HistoryFragment.sortedNotifyList;

public class MainMenu extends AppCompatActivity {
    private static final String TAG = MainMenu.class.getName();

    private static final String TAB_1_TAG = "tab_1";
    private static final String TAB_2_TAG = "tab_2";

    public static MenuItem item_clear, item_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_menu);

        //init folder
        //FileOperation.mqtt_init_folder();

        /*if (isMyServiceRunning(MqttMainService.class) && InitData.mqttServiceIntent != null) {
            Log.d(TAG, "service is running!");
            //stopService(InitData.mqttServiceIntent);
            //InitData.mqttServiceIntent = null;
        } else {
            Log.d(TAG, "start service!");
            InitData.mqttServiceIntent = new Intent(ConnectionDetails.this, MqttMainService.class);
            startService(InitData.mqttServiceIntent);
        }*/

        InitView();


    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");



        super.onDestroy();

    }

    private void InitView() {
        FragmentTabHost mTabHost;

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(setIndicator(MainMenu.this, mTabHost.newTabSpec(TAB_1_TAG),
                R.drawable.tab_indicator_gen, getResources().getString(R.string.scm_history_tab), R.drawable.ic_history_white_48dp), HistoryFragment.class, null);

        mTabHost.addTab(setIndicator(MainMenu.this, mTabHost.newTabSpec(TAB_2_TAG),
                R.drawable.tab_indicator_gen, getResources().getString(R.string.scm_setting), R.drawable.ic_settings_white_48dp), SettingsFragment.class, null);

        //mTabHost.addTab(setIndicator(ConnectionDetails.this, mTabHost.newTabSpec(TAB_3_TAG),
        //        R.drawable.tab_indicator_gen, getResources().getString(R.string.macauto_mqtt_tab_publish), R.drawable.ic_cast_white_48dp), PublishFragment.class, null);




        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {


                switch (tabId) {
                    case "tab_1":
                        if (item_clear != null)
                            item_clear.setVisible(true);
                        if (item_search != null)
                            item_search.setVisible(true);
                        break;
                    case "tab_2":
                        if (item_clear != null)
                            item_clear.setVisible(false);
                        if (item_search != null)
                            item_search.setVisible(false);
                        break;

                    default:
                        break;

                }
            }
        });
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }
    @Override
    public void onResume() {

        Log.i(TAG, "onResume");
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

        item_search = menu.findItem(R.id.action_search);
        item_clear = menu.findItem(R.id.action_clear);

        //item_find.setVisible(false);

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
        switch (item.getItemId()) {
            case R.id.action_clear:

                Log.i(TAG, "item_clear");
                AlertDialog.Builder confirmdialog = new AlertDialog.Builder(MainMenu.this);
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
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        /*AlertDialog.Builder confirmdialog = new AlertDialog.Builder(ConnectionDetails.this);
        confirmdialog.setIcon(R.drawable.ic_exit_to_app_white_48dp);
        confirmdialog.setTitle(getResources().getString(R.string.macauto_back_to_menu_title));
        confirmdialog.setMessage(getResources().getString(R.string.macauto_back_to_menu_description));
        confirmdialog.setPositiveButton(getResources().getString(R.string.macauto_confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(ConnectionDetails.this, TopMenu.class);
                startActivity(intent);
                finish();

            }
        });
        confirmdialog.setNegativeButton(getResources().getString(R.string.macauto_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        confirmdialog.show();*/
    }

    private TabHost.TabSpec setIndicator(Context ctx, TabHost.TabSpec spec,
                                         int resid, String string, int genresIcon) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.tab_item, null);
        v.setBackgroundResource(resid);
        TextView tv = (TextView)v.findViewById(R.id.txt_tabtxt);
        ImageView img = (ImageView)v.findViewById(R.id.img_tabtxt);

        tv.setText(string);
        img.setBackgroundResource(genresIcon);
        return spec.setIndicator(v);
    }



    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
