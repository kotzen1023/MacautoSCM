package com.macauto.macautoscm.Data;


import android.content.Context;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class InitData {
    private static final String TAG = InitData.class.getName();
    public static ArrayList<HistoryItem> notifyList = new ArrayList<>();

    private Context context;

    public InitData(Context context) {
        super();
        this.context = context;
    }

    public static void init(Context context) {
        Log.d(TAG, "[InitData start]");

        //read log file
        String msg = FileOperation.read_message();

        notifyList.clear();

        if (!msg.equals("")) {
            String items[] = msg.split("&");

            for (int i = 0; i < items.length; i++) {
                //Log.i(TAG, "==== item ====");
                HistoryItem item = new HistoryItem();
                String items_split[] = items[i].split(";");

                item.setAction(Integer.valueOf(items_split[0]));
                item.setTitle(items_split[1]);
                item.setMsg(items_split[2]);
                item.setDate(items_split[3]);
                //Log.i(TAG, "action = "+item.getAction());
                //Log.i(TAG, "msg = "+item.getMsg());
                //Log.i(TAG, "date = "+item.getDate());
                //itemHistory.add(item);
                notifyList.add(item);
            }
        }

        Log.d(TAG, "[InitData  end ]");
    }
}
