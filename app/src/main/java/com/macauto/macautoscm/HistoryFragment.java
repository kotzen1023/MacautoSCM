package com.macauto.macautoscm;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.util.Log;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.ListView;


import com.macauto.macautoscm.data.Constants;
import com.macauto.macautoscm.data.HistoryAdapter;
import com.macauto.macautoscm.data.HistoryItem;

import com.macauto.macautoscm.service.GetMessageService;
import com.macauto.macautoscm.service.UpdateReadStatusService;


import java.util.ArrayList;

import me.leolin.shortcutbadger.ShortcutBadger;

import static android.content.Context.MODE_PRIVATE;

public class HistoryFragment extends Fragment {
    private static final String TAG = HistoryFragment.class.getName();

    private Context context;
    private ListView listView;
    ProgressDialog loadDialog = null;

    //public ArrayAdapter<Spanned> arrayAdapter = null;
    public static ArrayList<HistoryItem> historyItemArrayList = new ArrayList<>();
    public static ArrayList<HistoryItem> sortedNotifyList = new ArrayList<>();
    public HistoryAdapter historyAdapter;
    //private ChangeListener changeListener = null;
    //private Connection connection;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    //private Spanned[] history;
    //private static boolean isRegisterChangeListener = false;

    static SharedPreferences pref ;
    //static SharedPreferences.Editor editor;
    private static final String FILE_NAME = "Preference";

    private static String account;
    private static String device_id;
    //private static int select_item_index = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.history_fragment, container, false);

        context = getContext();

        if (context != null) {
            pref = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
            account = pref.getString("ACCOUNT", "");
            device_id = pref.getString("WIFIMAC", "");
        } else {
            account = "";
            device_id = "";
        }





        IntentFilter filter;

        listView = view.findViewById(R.id.listViewHistory);
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryItem item = historyAdapter.getItem(position);

                if (item != null) {
                    if (item.isRead_sp()) {
                        Log.d(TAG, "read sp true");
                    } else {
                        item.setRead_sp(true);

                        Intent intent = new Intent(context, UpdateReadStatusService.class);
                        intent.setAction(Constants.ACTION.GET_MESSAGE_LIST_ACTION);
                        intent.putExtra("ACCOUNT", account);
                        intent.putExtra("DEVICE_ID", device_id);
                        intent.putExtra("DOC_NO", item.getMsg());
                        context.startService(intent);
                    }


                    Intent intent = new Intent(context, HistoryShow.class);
                    intent.putExtra("HISTORY_TYPE", String.valueOf(item.getAction()));
                    intent.putExtra("HISTORY_TITLE", item.getTitle());
                    intent.putExtra("HISTORY_MSG", item.getMsg());
                    intent.putExtra("HISTORY_DATE", item.getDate());
                    intent.putExtra("ACCOUNT", account);
                    intent.putExtra("DEVICEID", device_id);
                    intent.putExtra("READ_SP", String.valueOf(item.isRead_sp()));
                    startActivity(intent);
                }
            }
        });

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction() != null) {
                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_NEW_NOTIFICATION_ACTION)) {
                        Log.d(TAG, "receive brocast !");

                        //historyAdapter.notifyDataSetChanged();
                        Intent getintent = new Intent(context, GetMessageService.class);
                        getintent.setAction(Constants.ACTION.GET_MESSAGE_LIST_ACTION);
                        getintent.putExtra("ACCOUNT", account);
                        getintent.putExtra("DEVICE_ID", device_id);
                        context.startService(getintent);


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_MESSAGE_LIST_COMPLETE)) {
                        Log.d(TAG, "receive brocast GET_MESSAGE_LIST_COMPLETE!");
                        historyAdapter = new HistoryAdapter(context, R.layout.history_item, historyItemArrayList);
                        listView.setAdapter(historyAdapter);
                        loadDialog.dismiss();

                        int badgeCount = 0;
                        for (int i=0; i<historyItemArrayList.size(); i++) {
                            if (!historyItemArrayList.get(i).isRead_sp()) {
                                badgeCount++;
                            }
                        }

                        ShortcutBadger.applyCount(context, badgeCount);
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_HISTORY_LIST_SORT_COMPLETE)) {
                        historyAdapter = new HistoryAdapter(context, R.layout.history_item, sortedNotifyList);
                        listView.setAdapter(historyAdapter);
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_MESSAGE_LIST_CLEAR)) {
                        Log.d(TAG, "receive brocast GET_MESSAGE_LIST_CLEAR!");
                        if (historyAdapter != null)
                            historyAdapter.notifyDataSetChanged();


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_MESSAGE_DATA)) {
                        Log.d(TAG, "receive brocast GET_MESSAGE_DATA!");

                        if (intent.getExtras() != null) {
                            HistoryItem item = new HistoryItem();
                            item.setMsg(intent.getExtras().getString("po_no"));
                            item.setDate(intent.getExtras().getString("send_datetime"));
                            item.setRead_sp(intent.getExtras().getBoolean("read_sp"));


                            historyItemArrayList.add(item);
                        }


                    }
                }


            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.GET_NEW_NOTIFICATION_ACTION);
            filter.addAction(Constants.ACTION.GET_HISTORY_LIST_SORT_COMPLETE);
            filter.addAction(Constants.ACTION.GET_MESSAGE_LIST_COMPLETE);
            filter.addAction(Constants.ACTION.GET_MESSAGE_LIST_CLEAR);
            filter.addAction(Constants.ACTION.GET_MESSAGE_DATA);
            context.registerReceiver(mReceiver, filter);
            isRegister = true;
            Log.d(TAG, "registerReceiver mReceiver");
        }

        //run on create
        Intent intent = new Intent(context, GetMessageService.class);
        intent.setAction(Constants.ACTION.GET_MESSAGE_LIST_ACTION);
        intent.putExtra("ACCOUNT", account);
        intent.putExtra("DEVICE_ID", device_id);
        context.startService(intent);

        loadDialog = new ProgressDialog(context);
        loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadDialog.setTitle("Loading...");
        loadDialog.setIndeterminate(false);
        loadDialog.setCancelable(false);

        loadDialog.show();

        //Intent intent = new Intent(context, GetMessageService.class);
        //intent.setAction(Constants.ACTION.GET_MESSAGE_LIST_ACTION);
        //intent.putExtra("ACCOUNT", account);
        //intent.putExtra("DEVICE_ID", device_id);
        //context.startService(intent);

        return view;
    }

    @Override
    public void onDestroyView() {
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

        super.onDestroyView();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }
    @Override
    public void onResume() {

        Log.i(TAG, "onResume");

        /*if (sortedNotifyList.size() > 0) {
            historyAdapter = new HistoryAdapter(context, R.layout.history_item, sortedNotifyList);
            listView.setAdapter(historyAdapter);
        } else {
            historyAdapter = new HistoryAdapter(context, R.layout.history_item, InitData.notifyList);
            listView.setAdapter(historyAdapter);
        }*/
        if (historyAdapter != null)
            historyAdapter.notifyDataSetChanged();





        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

    /*public void toast(String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }*/
}
