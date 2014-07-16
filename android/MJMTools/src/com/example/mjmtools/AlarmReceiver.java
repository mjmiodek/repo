
package com.example.mjmtools;


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
