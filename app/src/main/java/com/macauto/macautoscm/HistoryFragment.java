package com.macauto.macautoscm;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.macauto.macautoscm.Data.Constants;
import com.macauto.macautoscm.Data.HistoryAdapter;
import com.macauto.macautoscm.Data.HistoryItem;
import com.macauto.macautoscm.Data.InitData;
import com.macauto.macautoscm.Service.GetMessageService;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HistoryFragment extends Fragment {
    private static final String TAG = HistoryFragment.class.getName();

    private Context context;
    private ListView listView;
    ProgressDialog loadDialog = null;

    public ArrayAdapter<Spanned> arrayAdapter = null;
    public static ArrayList<HistoryItem> historyItemArrayList = new ArrayList<>();
    public static ArrayList<HistoryItem> sortedNotifyList = new ArrayList<>();
    public static HistoryAdapter historyAdapter;
    private ChangeListener changeListener = null;
    //private Connection connection;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    //private Spanned[] history;
    private static boolean isRegisterChangeListener = false;

    static SharedPreferences pref ;
    static SharedPreferences.Editor editor;
    private static final String FILE_NAME = "Preference";

    private static String account;
    private static String device_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.history_fragment, container, false);

        context = getContext();

        pref = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        account = pref.getString("ACCOUNT", "");
        device_id = pref.getString("DEVICEID", "");


        IntentFilter filter;

        listView = (ListView) view.findViewById(R.id.listViewHistory);
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
                    historyAdapter = new HistoryAdapter(context, R.layout.history_item, InitData.notifyList);
                    listView.setAdapter(historyAdapter);

                    loadDialog.dismiss();
                }

                else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.GET_HISTORY_LIST_SORT_COMPLETE)) {
                    historyAdapter = new HistoryAdapter(context, R.layout.history_item, sortedNotifyList);
                    listView.setAdapter(historyAdapter);
                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.GET_NEW_NOTIFICATION_ACTION);
            filter.addAction(Constants.ACTION.GET_HISTORY_LIST_SORT_COMPLETE);
            filter.addAction(Constants.ACTION.GET_MESSAGE_LIST_COMPLETE);
            context.registerReceiver(mReceiver, filter);
            isRegister = true;
            Log.d(TAG, "registerReceiver mReceiver");
        }



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


        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

    public void toast(String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    private class ChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            // connection object has change refresh the UI



            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    /*arrayAdapter.clear();
                    arrayAdapter.addAll(Connections.getInstance(getActivity()).getConnection(InitData.clientHandle).history());
                    arrayAdapter.notifyDataSetChanged();*/


                    //historyAdapter.clear();
                    //historyAdapter.addAll(historyItemArrayList);
                    historyAdapter.notifyDataSetChanged();

                }
            });

        }
    }


}
