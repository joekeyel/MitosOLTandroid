package my.com.tm.moapps.mitosolt;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMNotificationIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCMNotificationIntentService";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);
        Log.e("test recieve", "------------------------------ Catch");

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                sendNotification("Deleted messages on server: "
                        + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {


                Log.i(TAG, "Completed" + SystemClock.elapsedRealtime());
                for (int i = 0; i < 3; i++) {
                    Log.i(TAG,
                            "Working. " + (i + 1) + "/5 @ "
                                    + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }

                }





                sendNotification("" + extras.get(Config.MESSAGE_KEY2));

                // cause popup windows         addmessage("" + extras.get(Config.MESSAGE_KEY2));


//                Intent in_new = new Intent(this, summary.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                in_new.putExtra("Notif", extras.get(Config.MESSAGE_KEY2).toString());
//
//                if( LoadTT.getInstance() == null || Main_Activity.getInstance() == null || Main.getInstance() == null || updatereasoncode.getInstance() == null ){
//
//                    startActivity(in_new);
//                }


                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {


        Log.d(TAG, "Preparing to send notification...: " + msg);
        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);


       // final String sanomsg = msg.toString();

//        if (summary.getInstance() != null) {
//            summary.getInstance().finish();
//        }

        addmessage(msg);

        Intent in = new Intent(this, chatacitivity.class);
        in.putExtra("Notif", msg);


        PendingIntent contentIntenti = PendingIntent.getActivity(this,0
                ,in, PendingIntent.FLAG_UPDATE_CURRENT);

        int numMessages = 0;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.mipmap.iconngnd_2)
                .setContentTitle("MiTOS Notification")
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setAutoCancel(false)
                .setContentText(msg)
                .setNumber(++numMessages)
                .setContentIntent(contentIntenti)
                .setOnlyAlertOnce(true)
                .setTicker("New TT From MCC");


        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Log.d(TAG, "Notification sent successfully.");

    }

    private void addmessage(String msg){

//        if(chatacitivity.getinstance()== null || chatacitivity.getinstance().equals("")) {
//
//
//
//            Intent in = new Intent(this, chatacitivity.class);
//            in.putExtra("Notif", msg);
//
//            startActivity(in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
////            chatacitivity.getinstance().moveTaskToBack(true);
//
//        }

        if(chatacitivity.getinstance()!=null){

            chatacitivity.getinstance().addmesage(msg);



        }




    }
}

