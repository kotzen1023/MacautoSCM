package com.macauto.macautoscm.Data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.macauto.macautoscm.R;

import java.util.ArrayList;


public class HistoryAdapter extends ArrayAdapter<HistoryItem> {
    public static final String TAG = HistoryAdapter.class.getName();
    //private Context context;
    private LayoutInflater inflater = null;

    private int layoutResourceId;
    private ArrayList<HistoryItem> items = new ArrayList<>();
    public static SparseBooleanArray mSparseBooleanArray;
    //private static int contact_count = 0;

    public HistoryAdapter(Context context, int textViewResourceId,
                          ArrayList<HistoryItem> objects) {
        super(context, textViewResourceId, objects);
        //this.context = context;
        this.layoutResourceId = textViewResourceId;
        this.items = objects;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSparseBooleanArray = new SparseBooleanArray();
    }

    public int getCount() {
        return items.size();

    }

    public HistoryItem getItem(int position)
    {
        return items.get(position);
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {

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

            if (!item.getMsg().equals("")) {
                if (splitter.length == 2 && time_splitter.length == 3) {
                    holder.msg.setText(item.getMsg() + "     " +time_splitter[0] + ":" + time_splitter[1]);
                } else {
                    holder.msg.setText(getContext().getResources().getString(R.string.scm_order_no) + " " + item.getMsg() + " " +
                            getContext().getResources().getString(R.string.scm_time) + item.getDate());
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

    class ViewHolder {
        //ImageView icon;
        //TextView jid;
        ImageView action;
        TextView msg;
        //TextView date;
        //CheckBox ckbox;

        public ViewHolder(View view) {

            this.action = (ImageView) view.findViewById(R.id.title_icon);
            this.msg = (TextView) view.findViewById(R.id.history_msg);
            //this.date = (TextView) view.findViewById(R.id.history_time);
        }


    }
}
