package com.denny.android.idea_note.recevier;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import com.denny.android.idea_note.MainActivity;
import com.denny.android.idea_note.R;

public class NoteAlarmRecevier extends BroadcastReceiver {
    public NoteAlarmRecevier() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.e(NoteAlarmRecevier.class.getSimpleName(),intent.getAction());
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
//                new Intent(context, MainActivity.class), 0);
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new Notification.Builder(context)
//                .setAutoCancel(true)
//                .setContentTitle("SmamrtNote")
//                .setContentText("有一条提示信息")
//                .setTicker("有一条提示信息")
//                .setWhen(System.currentTimeMillis())
////                .setSound(context.get)
//                .setContentIntent(pendingIntent)
//                .getNotification();
//        manager.notify(1,notification);
//        context.startActivity(new Intent(context,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        notify(context);
    }

    private void notify(Context context){
        android.support.v4.app.NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.icdoit)
                        .setContentTitle("Smart Note")
                        .setContentText("你有一条新的提示！");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);//WTFD
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, builder.build());
    }
}
