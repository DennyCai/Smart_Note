package com.denny.android.idea_note.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NoteAlarmRecevier extends BroadcastReceiver {
    public NoteAlarmRecevier() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.e(NoteAlarmRecevier.class.getSimpleName(),intent.getAction());
    }
}
