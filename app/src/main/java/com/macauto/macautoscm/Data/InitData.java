package com.macauto.macautoscm.Data;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import java.util.ArrayList;


public class InitData {
    private static final String TAG = InitData.class.getName();
    public static ArrayList<HistoryItem> notifyList = new ArrayList<>();

    private Context context;
    static SharedPreferences pref ;
    static SharedPreferences.Editor editor;
    private static final String FILE_NAME = "Preference";

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

            Log.d(TAG, "-----------------------------");
            Log.d(TAG, msg);
            Log.d(TAG, "-----------------------------");

            String items[] = msg.split("&");

            for (int i = 0; i < items.length; i++) {
                Log.i(TAG, "==== item "+i+" ====");
                Log.i(TAG, items[i]);
                Log.i(TAG, "==== item "+i+" ====");
                HistoryItem item = new HistoryItem();
                String items_split[] = items[i].split(";");

                if (items_split[0].equals("")) {
                    item.setAction(0);
                } else {
                    item.setAction(Integer.valueOf(items_split[0]));
                }
                Log.i(TAG, "action = "+item.getAction());
                item.setTitle(items_split[1]);
                Log.i(TAG, "Title = "+item.getTitle());
                item.setMsg(items_split[2]);
                Log.i(TAG, "msg = "+item.getMsg());
                item.setDate(items_split[3]);
                Log.i(TAG, "date = "+item.getDate());
                //itemHistory.add(item);
                notifyList.add(item);
            }
        }

        Log.d(TAG, "[InitData  end ]");
    }
}
