package com.em.posloyalty.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.em.posloyalty.consts.Consts;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMIntentService extends IntentService
{
    private static final String TAG = GCMIntentService.class.getSimpleName();

    Handler han;

    public static final String INSERT = "insert";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";

    @Override
    public void onCreate()
    {
        super.onCreate();
        han = new Handler();
    }

    public GCMIntentService()
    {

        super(Consts.GCM_INTENT_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {

        Log.i(TAG, "new push");

        Bundle extras = intent.getExtras();
        GoogleCloudMessaging googleCloudMessaging = GoogleCloudMessaging.getInstance(this);

        String messageType = googleCloudMessaging.getMessageType(intent);

        if (!extras.isEmpty())
        {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
            {
                processNotification(Consts.GCM_SEND_ERROR, extras);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
            {
                processNotification(Consts.GCM_DELETED_MESSAGE, extras);
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
            {
                // Post notification of received message.
                processNotification(Consts.GCM_RECEIVED, extras);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }


    private void processNotification(String type, final Bundle extras)
    {




        /*han.post(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(getApplicationContext(), extras.toString(), Toast.LENGTH_LONG).show();
            }
        });
*/

    }


}
