package com.macauto.macautoscm.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.macauto.macautoscm.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class HistoryAdapter extends ArrayAdapter<HistoryItem> {
    public static final String TAG = HistoryAdapter.class.getName();
    private Context context;
    private LayoutInflater inflater = null;

    private int layoutResourceId;
    private ArrayList<HistoryItem> items = new ArrayList<>();
    //public static SparseBooleanArray mSparseBooleanArray;
    //private static int contact_count = 0;

    private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    //private SimpleDateFormat AMBIENT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private String current_date;

    public HistoryAdapter(Context context, int textViewResourceId,
                          ArrayList<HistoryItem> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.layoutResourceId = textViewResourceId;
        this.items = objects;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //mSparseBooleanArray = new SparseBooleanArray();
        current_date = DATE_FORMAT.format(new Date());
    }

    public int getCount() {
        return items.size();

    }

    public HistoryItem getItem(int position)
    {
        return items.get(position);
    }

    @NonNull
    public View getView(int position, View convertView,@NonNull ViewGroup parent) {

        //Log.e(TAG, "getView = " + position);
        View view;
        ViewHolder holder;


        if (convertView == null || convertView.getTag() == null) {
            /*LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();

            holder.jid = (TextView) convertView.findViewById(R.id.contact_jid);
            holder.avatar = (ImageView) convertView.findViewById(R.id.contact_icon);
            convertView.setTag(holder);*/
            view = inflater.inflate(layoutResourceId, null);
            holder = new ViewHolder(view);
            view.setTag(holder);


        } else {
            view = convertView;
            //Log.e(TAG, "here!");
            holder = (HistoryAdapter.ViewHolder) view.getTag();
        }


        HistoryItem item = items.get(position);


        if (item != null) {

            String splitter[] = item.getDate().split(" ");
            String time_splitter[] = splitter[1].split(":");

            String end_date = splitter[1].substring(0, splitter[1].length() -3);

            Date date = null;
            String newWord = item.getDate();

            try {

                date = DATE_FORMAT.parse(newWord);
                //Log.e(TAG, "After parse ==>"+date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (current_date.substring(0,10).equals(splitter[0])) {
                holder.day.setText(context.getResources().getString(R.string.date_today));
                holder.date.setText(end_date);
            } else {
                if (date != null) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    holder.day.setText(splitter[0]);

                    if (splitter[0].substring(0,4).equals(current_date.substring(0,4))) {
                        holder.day.setText(splitter[0].substring(5,10));
                    } else {
                        holder.day.setText(splitter[0]);
                    }

                    holder.date.setText(end_date);

                } else {
                    String msg = splitter[0]+" " + end_date;
                    holder.date.setText(msg);
                }
            }

            if (!item.getMsg().equals("")) {
                if (splitter.length == 2 && time_splitter.length == 3) {
                    holder.msg.setText(item.getMsg());
                } else {
                    String msg = getContext().getResources().getString(R.string.scm_order_no) + " " + item.getMsg();
                    holder.msg.setText(msg);
                }
            } else {
                Log.e(TAG, "item.getMsg() == null");
            }

            //holder.date.setText(item.getDate());

            if (item.isRead_sp()) {
                holder.action.setImageResource(R.drawable.star_green);

            } else {
                holder.action.setImageResource(R.drawable.ic_fiber_new_black_48dp);

            }
        }


        return view;
    }

    private class ViewHolder {
        //ImageView icon;
        //TextView jid;
        ImageView action;
        TextView day;
        TextView date;
        TextView msg;
        //TextView date;
        //CheckBox ckbox;

        private ViewHolder(View view) {

            this.action = view.findViewById(R.id.title_icon);
            this.day = view.findViewById(R.id.history_day);
            this.date = view.findViewById(R.id.history_time);
            this.msg = view.findViewById(R.id.history_msg);
            //this.date = (TextView) view.findViewById(R.id.history_time);
        }


    }
}
