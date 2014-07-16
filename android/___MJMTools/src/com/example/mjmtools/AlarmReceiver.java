
package com.example.mjmtools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    final String TAG = "MJM:AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getStringExtra(Keys.action);
        MJMLog.d(TAG, "onReceive(), action: " + action);
        Intent service = new Intent(context, MainService.class);
        service.putExtra(Keys.action, action);
        context.startService(service);
    }
}
