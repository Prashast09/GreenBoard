package bean.red.greenboard.view.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.gcm.GcmListenerService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

import bean.red.greenboard.R;
import bean.red.greenboard.view.activity.MainActivity;

/**
 * Created by Shivam Seth on 4/30/2016.
 */
public class GCMPushReceiverService extends GcmListenerService {

    //This method will be called on every new message received
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //Getting the message from the bundle
        String message = data.getString("title");
        //Displaying a notiffication with the message
        sendNotification(data);
    }

    //This method is generating a notification and displaying the notification
    private void sendNotification(Bundle message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;
        Bitmap largeIcon = null;

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        if(message.getString("summarytext1").equals("Events"))
        {
            largeIcon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.events);

        }
        else if(message.getString("summarytext1").equals("Favours"))
        {
            largeIcon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.favours);

        }
        else if(message.getString("summarytext1").equals("Sports"))
        {
            largeIcon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.sports);
        }
        else if(message.getString("summarytext1").equals("Offers"))
        {
            largeIcon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.offers);
        }
        else if(message.getString("summarytext1").equals("Academics"))
        {
            largeIcon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.academics);
        }
        else if(message.getString("summarytext1").equals("General"))
        {
            largeIcon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.general);
        }
        else if(message.getString("summarytext1").equals("DirectorsDesk"))
        {
            largeIcon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.desk);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.BigTextStyle bigTextStyle =
                new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(message.getString("bigtext1")).setBigContentTitle(message.getString("contenttitle1")).setSummaryText(message.getString("summarytext1"));
        final NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.greenboard_notification)
                .setLargeIcon(largeIcon)
                .setContentTitle(message.getString("contenttitle2"))
                .setContentText(message.getString("contenttext2"))
                .setStyle(bigTextStyle)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        final long[] v = {500,1000};
        BroadcastReceiver vibrateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    noBuilder.setVibrate(v); }
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(vibrateReceiver, filter);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        noBuilder.setSound(uri);
        notificationManager.notify(m, noBuilder.build()); //0 = ID of notification

    }
}