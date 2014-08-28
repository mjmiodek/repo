
package com.example.mjmtools;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class MainService extends Service {
    final long second = 1000;
    final long minute = 60 * second;
    final long hour = 60 * minute;
    final String TAG = "MJM:MainService";

    private WifiManager mWifiManager;

    private WifiManager getWifiManager() {
        if (mWifiManager == null)
            mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        return mWifiManager;
    }

    private PowerManager mPowerManager;

    private PowerManager getPowerManager() {
        if (mPowerManager == null)
            mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        return mPowerManager;
    }

    private AlarmManager mAlarmManager;

    private AlarmManager getAlarmManager() {
        if (mAlarmManager == null) mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        return mAlarmManager;
    }

    private long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    private void setWifiEnabled(boolean state) {
        if (getWifiManager().isWifiEnabled() != state) {
            getWifiManager().setWifiEnabled(state);
            MJMLog.d(TAG, "setWifiEnabled(" + state + "): done");
        }
    }

    private Boolean mMobileDataManagerInitialized = false;
    private Method mGetMobileDataEnabledMethod;
    private Method mSetMobileDataEnabledMethod;
    private ConnectivityManager mConnectivityManager;
    private Object mIConnectivityManager;

    private void setMobiledataEnabled(boolean state) {
        try {
            if (mMobileDataManagerInitialized == false) {
                mConnectivityManager = (ConnectivityManager) getApplicationContext()
                        .getSystemService(CONNECTIVITY_SERVICE);
                Class<?> cmClass = Class.forName(mConnectivityManager.getClass().getName());
                mGetMobileDataEnabledMethod = cmClass.getDeclaredMethod("getMobileDataEnabled");
                mGetMobileDataEnabledMethod.setAccessible(true);
                Field icmField = cmClass.getDeclaredField("mService");
                icmField.setAccessible(true);
                mIConnectivityManager = icmField.get(mConnectivityManager);
                Class<?> icmClass = Class.forName(mIConnectivityManager.getClass().getName());
                mSetMobileDataEnabledMethod = icmClass.getDeclaredMethod("setMobileDataEnabled",
                        Boolean.TYPE);
                mSetMobileDataEnabledMethod.setAccessible(true);
                mMobileDataManagerInitialized = true;
            }
            Boolean mobileDataEnabled = (Boolean) mGetMobileDataEnabledMethod
                    .invoke(mConnectivityManager);
            if (mobileDataEnabled != state) {
                mSetMobileDataEnabledMethod.invoke(mIConnectivityManager, state);
                MJMLog.d(TAG, "setMobiledataEnabled(" + state + "): done");
            }
        } catch (Exception e) {
            MJMLog.e(TAG, "setMobiledataEnabled(" + state + "): error");
            return;
        }
    }

    private Queue<PendingIntent> mScheduledAlarmQueue = new LinkedList<PendingIntent>();

    private void scheduleAlarm(long delay, String action) {
        Intent myAlarm = new Intent(getApplicationContext(), AlarmReceiver.class);
        myAlarm.putExtra(Keys.action, action);
        PendingIntent alarm = PendingIntent.getBroadcast(getApplicationContext(), 0, myAlarm,
                PendingIntent.FLAG_CANCEL_CURRENT);
        mScheduledAlarmQueue.add(alarm);
        getAlarmManager().set(AlarmManager.RTC_WAKEUP, getCurrentTime() + delay, alarm);
    }

    private void cancelScheduledAlarms() {
        Iterator<PendingIntent> it = mScheduledAlarmQueue.iterator();
        while (it.hasNext()) {
            getAlarmManager().cancel(it.next());
        }
        mScheduledAlarmQueue.clear();
    }

    private boolean getPowerConnected() {
        Context context = getApplicationContext();
        Intent intent = context.registerReceiver(null, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
        int state = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return state == BatteryManager.BATTERY_PLUGGED_AC
                || state == BatteryManager.BATTERY_PLUGGED_USB;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkDailyRoadsVoyagerFiles();
        checkPhone2pcFiles();
        if (intent == null) {
            MJMLog.d(TAG, "onStartCommand(): null");
            checkPowerState();
            return Service.START_STICKY;
        }
        final String action = intent.getStringExtra(Keys.action);
        MJMLog.d(TAG, "onStartCommand(): " + action);
        switch (action) {
            case Keys.actionPowerConnected:
                onPowerConnected();
                break;
            case Keys.actionPowerDisconnected:
                onPowerDisconnected();
                break;
            case Keys.actionPowerDisconnectedCheckScreenAndDisable:
                onPowerDisconnectedCheckScreenAndDisable();
                break;
            case Keys.actionPowerOffPeriodic:
                onPowerOffPeriodic();
                break;
            case Keys.actionBootCompleted:
                onBootCompleted();
                break;
            case Keys.actionNoop:
                onNoop();
                break;
        }
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        checkPowerState();
    }

    @Override
    public void onDestroy() {
    }

    /// actions

    private void checkPowerState() {
        scheduleAlarm(0, getPowerConnected() ? Keys.actionPowerConnected
                : Keys.actionPowerDisconnected);
    }

    private void onPowerConnected() {
        MJMLog.d(TAG, "onPowerConnected()");
        cancelScheduledAlarms();
        if (MJMToolsApplication.getPowerConnectedEnableWifi()) setWifiEnabled(true);
        if (MJMToolsApplication.getPowerConnectedEnableMobiledata()) setMobiledataEnabled(true);
        if (MJMToolsApplication.getPowerConnectedEnableAutosync())
            ContentResolver.setMasterSyncAutomatically(true);
    }

    private void onPowerDisconnected() {
        MJMLog.d(TAG, "onPowerDisconnected()");
        onPowerDisconnectedCheckScreenAndDisable();
    }

    private void onPowerDisconnectedCheckScreenAndDisable() {
        MJMLog.d(TAG, "onPowerDisconnectedCheckScreenAndDisable()");
        if (getPowerManager().isScreenOn()) {
            startPowerDisconnectedAlarm();
        } else {
            if (MJMToolsApplication.getPowerDisconnectedDisableWifi()) setWifiEnabled(false);
            if (MJMToolsApplication.getPowerDisconnectedDisableMobiledata())
                setMobiledataEnabled(false);
            if (MJMToolsApplication.getPowerDisconnectedDisableAutosync())
                ContentResolver.setMasterSyncAutomatically(false);
            startPowerOffAlarm();
        }
    }

    private void startPowerDisconnectedAlarm() {
        MJMLog.d(TAG, "startPowerDisconnectedAlarm()");
        long delay = MJMToolsApplication.getPowerDisconnectedPeriodicTime() * minute;
        scheduleAlarm(delay, Keys.actionPowerDisconnectedCheckScreenAndDisable);
    }

    private void onPowerOffPeriodic() {
        MJMLog.d(TAG, "onPowerOffPeriodic()");
        if (!getPowerManager().isScreenOn()) {
            if (MJMToolsApplication.getPowerOffDisableWifi()) setWifiEnabled(false);
            if (MJMToolsApplication.getPowerOffDisableMobiledata()) setMobiledataEnabled(false);
        }
        startPowerOffAlarm();
    }

    private void startPowerOffAlarm() {
        MJMLog.d(TAG, "startPowerOffAlarm()");
        long delay = MJMToolsApplication.getPowerOffPeriodicTime() * minute;
        scheduleAlarm(delay, Keys.actionPowerOffPeriodic);
    }

    private void onBootCompleted() {
        MJMLog.d(TAG, "onBootCompleted()");
    }

    private void onNoop() {
        MJMLog.d(TAG, "onNoop()");
    }

    private void checkDailyRoadsVoyagerFiles() {
        if (!getPowerConnected()) return;
        MJMLog.d(TAG, "checkDailyRoadsVoyagerFiles()");
        try {
            File sd = Environment.getExternalStorageDirectory();
            File dr = new File(sd, "dailyroads");
            File drv = new File(dr, "Videos");
            File dbmobile = new File(sd, "db-mobile");
            File phone2pc = new File(dbmobile, "phone2pc");
            File[] files = drv.listFiles();
            for (int i = 0; i < files.length; ++i) {
                File srcFile = files[i];
                if (srcFile.isFile()) {
                    File destFile = new File(phone2pc, srcFile.getName());
                    if (srcFile.renameTo(destFile)) {
                        MJMLog.d(TAG,
                                "DailyRoadsVoyager file move from " + srcFile.getAbsolutePath()
                                        + " to " + destFile.getAbsoluteFile());
                    }
                }
            }
        } catch (NullPointerException e) {
        }
    }

    private long phone2pcMaxPartSize() {
        return 8000000;
    }

    private void checkPhone2pcFiles() {
        if (!getPowerConnected()) return;
        MJMLog.d(TAG, "checkPhone2pcFiles()");
        try {
            File sd = Environment.getExternalStorageDirectory();
            File dbmobile = new File(sd, "db-mobile");
            File phone2pc = new File(dbmobile, "phone2pc");
            final long maxPartSize = phone2pcMaxPartSize();
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File directory, String fileName) {
                    File file = new File(directory, fileName);
                    return file.length() > maxPartSize;
                }
            };
            File[] files = phone2pc.listFiles(filter);
            for (int i = 0; i < files.length; ++i) {
                Tools.splitFile(files[i], maxPartSize);
            }
        } catch (NullPointerException e) {
        }
    }
}
