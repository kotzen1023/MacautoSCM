package com.macauto.macautoscm.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.macauto.macautoscm.Data.Constants;
import com.macauto.macautoscm.Data.FileOperation;
import com.macauto.macautoscm.Data.HistoryItem;
import com.macauto.macautoscm.Data.InitData;
import com.macauto.macautoscm.MainActivity;
import com.macauto.macautoscm.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ScmFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = ScmFirebaseMessagingService.class.getName();
    private Context context;

    static SharedPreferences pref ;
    //static SharedPreferences.Editor editor;
    private static final String FILE_NAME = "Preference";

    public ScmFirebaseMessagingService() {
        Log.d(TAG, "ScmFirebaseMessagingService init");

        //init folder, file
        FileOperation.init_folder_and_files();
        //read from file
        //context = this;

        //InitData initData = new InitData(context);
        //initData.init(context);




        //FirebaseMessaging.getInstance().subscribeToTopic("test");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Log.e(TAG, "=== ScmFirebaseMessagingService onCreate ===");

        //context = getBaseContext();

        pref = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        String account = pref.getString("ACCOUNT", "");
        String password = pref.getString("PASSWORD", "");

        //Log.d(TAG, "account = "+account);

        if (!account.equals("") && !password.equals("")) {
            Log.d(TAG, "*** auto subscrbe topic ***");
            FirebaseMessaging.getInstance().subscribeToTopic(account);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //SimpleDateFormat sdf = new SimpleDateFormat(context.getString(R.string.dateFormat));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData().toString());


        //Calling method to generate notification
        Object title = remoteMessage.getData().get("title");
        Object body = remoteMessage.getData().get("body");
        if (title != null && body != null) {
            sendNotification(title.toString(), body.toString());

            HistoryItem item = new HistoryItem();
            item.setAction(0);
            item.setTitle(title.toString());
            item.setMsg(body.toString());
            item.setDate(sdf.format(new Date()));
            InitData.notifyList.add(item);

            String msg = "";
            if (FileOperation.read_message().equals("")) {
                msg = item.getAction()+";"+item.getTitle()+";"+item.getMsg()+";"+item.getDate();
                FileOperation.append_message(msg);
            } else {
                msg = "&"+item.getAction()+";"+item.getTitle()+";"+item.getMsg()+";"+item.getDate();
                FileOperation.append_message(msg);
            }

            //send broadcast
            Intent newNotifyIntent = new Intent(Constants.ACTION.GET_NEW_NOTIFICATION_ACTION);
            sendBroadcast(newNotifyIntent);
        }


    }

    private void sendNotification(String messageTitle, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        int color = 0xff00a2c7;

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setColor(color)
                .setSmallIcon(R.drawable.m_mark)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
