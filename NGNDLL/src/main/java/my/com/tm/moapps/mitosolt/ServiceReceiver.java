package my.com.tm.moapps.mitosolt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by joe on 2/22/2016.
 */
public class ServiceReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(final Context context, Intent intent) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

// for test call tt...dial test

        if (Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction()) && Main.getInstance()!=null) {

            final String originalNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Main.getInstance().updateTheTextView(originalNumber);

            Log.e("APP", "outgoing,ringing:" + originalNumber);

        }


        Log.d("APP", "ACTION:" + intent.getAction());
        final String stringExtra = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (TelephonyManager.EXTRA_STATE_RINGING.equals(stringExtra)&& Main.getInstance()!=null) {
            final String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.e("APP", "incoming,ringing:" + incomingNumber);

            Main.getInstance().updateTheTextView(incomingNumber);

        }

        // for verify sub activity


        if (TelephonyManager.EXTRA_STATE_RINGING.equals(stringExtra)&& verify_sub.getInstance()!=null) {
            final String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.e("APP", "incoming,ringing:" + incomingNumber);

            verify_sub.getInstance().updateTheTextView1(incomingNumber);

        }

        if (Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction()) && verify_sub.getInstance()!=null) {

            final String originalNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            verify_sub.getInstance().updateTheTextView1(originalNumber);

            Log.e("APP", "outgoing,ringing:" + originalNumber);

        }
    }


}