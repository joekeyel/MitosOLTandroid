package my.com.tm.moapps.mitosolt;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import me.leolin.shortcutbadger.ShortcutBadger;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
             //   scheduleJob();
            } else {
                // Handle message within 10 seconds
               // handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            sendNotification(remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.


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
                .setContentTitle("MiTOS OLT Notification")
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



        if(chatacitivity.getinstance()!=null){

            chatacitivity.getinstance().addmesage(msg);
            ShortcutBadger.applyCount(chatacitivity.getinstance(), 1);



        }




    }
}
