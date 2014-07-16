
package com.example.mjmtools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PowerReceiver extends BroadcastReceiver {
    final String TAG = "MJM:PowerReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, MainService.class);
        String action = intent.getAction();
        MJMLog.d(TAG, "onReceive(), action: " + action);
        switch (action) {
            case Intent.ACTION_POWER_CONNECTED:
                service.putExtra(Keys.action, Keys.actionPowerConnected);
                break;
            case Intent.ACTION_POWER_DISCONNECTED:
                service.putExtra(Keys.action, Keys.actionPowerDisconnected);
                break;
            case Intent.ACTION_BOOT_COMPLETED:
                service.putExtra(Keys.action, Keys.actionBootCompleted);
                break;
            default:
                service.putExtra(Keys.action, Keys.actionNoop);
                break;
        }
        context.startService(service);
    }
}
