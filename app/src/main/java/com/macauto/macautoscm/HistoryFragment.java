package com.macauto.macautoscm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    private static final String TAG = HistoryFragment.class.getName();

    private Context context;
    private ListView listView;

    public ArrayAdapter<Spanned> arrayAdapter = null;
    public ArrayList<HistoryItem> historyItemArrayList = new ArrayList<>();
    public HistoryAdapter historyAdapter;
    private ChangeListener changeListener = null;
    //private Connection connection;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    //private Spanned[] history;
    private static boolean isRegisterChangeListener = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.history_fragment, container, false);

        context = getContext();
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

        /*if (InitData.connection != null && isRegisterChangeListener) {
            InitData.connection.removeChangeListener(null);
            changeListener = null;
            isRegisterChangeListener = false;
        }*/

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

        /*if (InitData.connection != null) {

            historyAdapter = new HistoryAdapter(context, R.layout.mqtt_list_view_history_item, InitData.itemHistory);
            listView.setAdapter(historyAdapter);
        }*/

        historyAdapter = new HistoryAdapter(context, R.layout.history_item, InitData.notifyList);
        listView.setAdapter(historyAdapter);


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
