package com.macauto.macautoscm;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.macauto.macautoscm.Data.Constants;

import com.macauto.macautoscm.Service.UpdateReadStatusService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryShow extends Activity {
    private static final String TAG = HistoryShow.class.getName();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.history_show);

        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_color_menu_classic));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_color_menu_classic, getTheme()));
        }

        ListView listView = (ListView) findViewById(R.id.listViewHistoryShow);

        Intent getintent = getIntent();


        String type = getintent.getStringExtra("HISTORY_TYPE");
        String message = getintent.getStringExtra("HISTORY_MSG");
        String date = getintent.getStringExtra("HISTORY_DATE");
        String title = getintent.getStringExtra("HISTORY_TITLE");
        String account = getintent.getStringExtra("ACCOUNT");
        String device_id = getintent.getStringExtra("DEVICEID");
        String read_sp = getintent.getStringExtra("READ_SP");



        Log.i(TAG, "type = "+type);
        Log.i(TAG, "title = "+title);
        Log.i(TAG, "message = "+message);
        Log.i(TAG, "date = "+date);
        Log.i(TAG, "account = "+account);
        Log.i(TAG, "device id = "+device_id);
        Log.i(TAG, "read sp = "+read_sp);

        //message = message.replace("#", "\n");

        /*Calendar c = Calendar.getInstance();

        Date start_date_compare = null;
        Date end_date_compare = null;

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.TAIWAN);
        try {
            start_date_compare = formatter.parse(start_date);
            end_date_compare = formatter.parse(end_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/



        List<Map<String, String>> items = new ArrayList<>();

        /*Map<String, String> item1 = new HashMap<>();
        item1.put("show_header", getResources().getString(R.string.macauto_mqtt_history_show_type));
        item1.put("show_msg", type);
        items.add(item1);*/

        /*Map<String, String> item2 = new HashMap<>();
        item2.put("show_header", getResources().getString(R.string.scm_show_title));
        item2.put("show_msg", title);
        items.add(item2);*/

        Map<String, String> item3 = new HashMap<>();
        item3.put("show_header", getResources().getString(R.string.scm_show_date));
        item3.put("show_msg", date);
        items.add(item3);

        Map<String, String> item4 = new HashMap<>();
        item4.put("show_header", getResources().getString(R.string.scm_order_no));
        item4.put("show_msg", message);
        items.add(item4);

        /*Map<String, String> item4 = new HashMap<>();
        item4.put("show_header", getResources().getString(R.string.macauto_meeting_show_subject));
        item4.put("show_msg", "\n\n"+subject);
        items.add(item4);

        Map<String, String> item5 = new HashMap<>();
        item5.put("show_header", getResources().getString(R.string.macauto_meeting_show_dept_name));
        item5.put("show_msg", dept_name);
        items.add(item5);

        Map<String, String> item6 = new HashMap<>();
        item6.put("show_header", getResources().getString(R.string.macauto_meeting_show_master));
        item6.put("show_msg", emp_name);
        items.add(item6);

        Map<String, String> item7 = new HashMap<>();
        item7.put("show_header", getResources().getString(R.string.macauto_meeting_show_room_name));
        item7.put("show_msg", "\n"+room_name);
        items.add(item7);

        Map<String, String> item8 = new HashMap<>();
        item8.put("show_header", getResources().getString(R.string.macauto_meeting_show_type));
        item8.put("show_msg", meeting_type);
        items.add(item8);



        Map<String, String> item9 = new HashMap<>();
        item9.put("show_header", getResources().getString(R.string.macauto_meeting_show_status));

        if (bad_sp.equals("Y"))
            item9.put("show_msg", getResources().getString(R.string.macauto_cancel));
            //holder.cancel.setText(Html.fromHtml("<font color='#FF0000'>" + context.getResources().getString(R.string.macauto_cancel) + "</font>"));
        else {
            if (start_date_compare != null && end_date_compare != null) {
                if (c.getTimeInMillis() > start_date_compare.getTime() && c.getTimeInMillis() < end_date_compare.getTime()) {
                    item9.put("show_msg", getResources().getString(R.string.macauto_going));
                    //holder.cancel.setText(Html.fromHtml("<font color='#0099FF'>" + context.getResources().getString(R.string.macauto_going) + "</font>"));
                } else if (c.getTimeInMillis() > end_date_compare.getTime()) {
                    item9.put("show_msg", getResources().getString(R.string.macauto_closed));
                    //holder.cancel.setText(Html.fromHtml("<font color='#FF8C00'>" + context.getResources().getString(R.string.macauto_closed) + "</font>"));
                } else
                    item9.put("show_msg", getResources().getString(R.string.macauto_on_time));
                //holder.cancel.setText(Html.fromHtml("<font color='#00FF00'>" + context.getResources().getString(R.string.macauto_on_time) + "</font>"));
                //item9.put("show_msg", bad_sp);
            }
        }
        items.add(item9);*/



        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                items, R.layout.history_show_item, new String[]{"show_header", "show_msg"},
                new int[]{R.id.history_show_header, R.id.history_show_msg});
        listView.setAdapter(simpleAdapter);

        //Log.i(TAG, "item[1] = "+listView.getAdapter().);

        /*if (read_sp.equals("true")) {
            Log.d(TAG, "This message had been read.");
        } else {

            Intent intent = new Intent(HistoryShow.this, UpdateReadStatusService.class);
            intent.setAction(Constants.ACTION.GET_MESSAGE_LIST_ACTION);
            intent.putExtra("ACCOUNT", account);
            intent.putExtra("DEVICE_ID", device_id);
            intent.putExtra("DOC_NO", message);
            startService(intent);
        }*/

    }

    @Override
    public void onBackPressed() {

        finish();
    }
}
