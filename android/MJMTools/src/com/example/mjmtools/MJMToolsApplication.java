
package com.example.mjmtools;

import android.app.Application;
import android.content.SharedPreferences;

public class MJMToolsApplication extends Application {

    static final String TAG = "MJM:MJMToolsApplication";

    private static MJMToolsApplication mjmToolsApplicationInstance;

    public static MJMToolsApplication getInstance() {
        return mjmToolsApplicationInstance;
    }

    @Override
    public void onCreate() {
        mjmToolsApplicationInstance = this;
        super.onCreate();
    }

    private static SharedPreferences getSP() {
        return mjmToolsApplicationInstance
                .getSharedPreferences("MJMToolsApplication", MODE_PRIVATE);
    }

    /// safe getters and setters

    private static boolean getSafeBoolean(String key, boolean defaultValue) {
        try {
            return getSP().getBoolean(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static boolean setSafeBoolean(String key, boolean value) {
        try {
            getSP().edit().putBoolean(key, value).commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static String getSafeString(String key, String defaultValue) {
        try {
            return getSP().getString(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static boolean setSafeString(String key, String value) {
        try {
            getSP().edit().putString(key, value).commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static long getSafeLong(String key, long defaultValue) {
        try {
            return getSP().getLong(key, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static boolean setSafeLong(String key, long value) {
        try {
            getSP().edit().putLong(key, value).commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /// option powerConnectedEnableWifi

    private static final String powerConnectedEnableWifi = "powerConnectedEnableWifi";

    public static boolean getPowerConnectedEnableWifi() {
        return getSafeBoolean(powerConnectedEnableWifi, false);
    }

    public static void setPowerConnectedEnableWifi(boolean value) {
        boolean ok = setSafeBoolean(powerConnectedEnableWifi, value);
        MJMLog.d(TAG, "setPowerConnectedEnableWifi(" + value + "): " + (ok ? "success" : "error"));
    }

    /// option powerConnectedEnableMobiledata

    private static final String powerConnectedEnableMobiledata = "powerConnectedEnableMobiledata";

    public static boolean getPowerConnectedEnableMobiledata() {
        return getSafeBoolean(powerConnectedEnableMobiledata, false);
    }

    public static void setPowerConnectedEnableMobiledata(boolean value) {
        boolean ok = setSafeBoolean(powerConnectedEnableMobiledata, value);
        MJMLog.d(TAG, "setPowerConnectedEnableMobiledata(" + value + "): "
                + (ok ? "success" : "error"));
    }

    /// option powerConnectedEnableAutosync

    private static final String powerConnectedEnableAutosync = "powerConnectedEnableAutosync";

    public static boolean getPowerConnectedEnableAutosync() {
        return getSafeBoolean(powerConnectedEnableAutosync, false);
    }

    public static void setPowerConnectedEnableAutosync(boolean value) {
        boolean ok = setSafeBoolean(powerConnectedEnableAutosync, value);
        MJMLog.d(TAG, "setPowerConnectedEnableAutosync(" + value + "): "
                + (ok ? "success" : "error"));
    }

    /// option powerDisconnectedPeriodicTime

    private static final String powerDisconnectedPeriodicTime = "powerDisconnectedPeriodicTime";

    public static long getPowerDisconnectedPeriodicTime() {
        return getSafeLong(powerDisconnectedPeriodicTime, 1);
    }

    public static void setPowerDisconnectedPeriodicTime(long value) {
        boolean ok = setSafeLong(powerDisconnectedPeriodicTime, value);
        MJMLog.d(TAG, "setPowerDisconnectedPeriodicTime(" + value + "): "
                + (ok ? "success" : "error"));
    }

    /// option powerDisconnectedEnableWifi

    private static final String powerDisconnectedDisableWifi = "powerDisconnectedDisableWifi";

    public static boolean getPowerDisconnectedDisableWifi() {
        return getSafeBoolean(powerDisconnectedDisableWifi, false);
    }

    public static void setPowerDisconnectedEnableWifi(boolean value) {
        boolean ok = setSafeBoolean(powerDisconnectedDisableWifi, value);
        MJMLog.d(TAG, "setPowerDisconnectedEnableWifi(" + value + "): "
                + (ok ? "success" : "error"));
    }

    /// option powerDisconnectedEnableMobiledata

    private static final String powerDisconnectedDisableMobiledata = "powerDisconnectedDisableMobiledata";

    public static boolean getPowerDisconnectedDisableMobiledata() {
        return getSafeBoolean(powerDisconnectedDisableMobiledata, false);
    }

    public static void setPowerDisconnectedDisableMobiledata(boolean value) {
        boolean ok = setSafeBoolean(powerDisconnectedDisableMobiledata, value);
        MJMLog.d(TAG, "setPowerDisconnectedDisableMobiledata(" + value + "): "
                + (ok ? "success" : "error"));
    }

    /// option powerDisconnectedEnableAutosync

    private static final String powerDisconnectedDisableAutosync = "powerDisconnectedDisableAutosync";

    public static boolean getPowerDisconnectedDisableAutosync() {
        return getSafeBoolean(powerDisconnectedDisableAutosync, false);
    }

    public static void setPowerDisconnectedEnableAutosync(boolean value) {
        boolean ok = setSafeBoolean(powerDisconnectedDisableAutosync, value);
        MJMLog.d(TAG, "setPowerDisconnectedEnableAutosync(" + value + "): "
                + (ok ? "success" : "error"));
    }

    /// option powerOffPeriodicTime

    private static final String powerOffPeriodicTime = "powerOffPeriodicTime";

    public static long getPowerOffPeriodicTime() {
        return getSafeLong(powerOffPeriodicTime, 1);
    }

    public static void setPowerOffPeriodicTime(long value) {
        boolean ok = setSafeLong(powerOffPeriodicTime, value);
        MJMLog.d(TAG, "setPowerOffPeriodicTime(" + value + "): " + (ok ? "success" : "error"));
    }

    /// option powerOffDisableWifi

    private static final String powerOffDisableWifi = "powerOffDisableWifi";

    public static boolean getPowerOffDisableWifi() {
        return getSafeBoolean(powerOffDisableWifi, false);
    }

    public static void setPowerOffDisableWifi(boolean value) {
        boolean ok = setSafeBoolean(powerOffDisableWifi, value);
        MJMLog.d(TAG, "setPowerOffDisableWifi(" + value + "): " + (ok ? "success" : "error"));
    }

    /// option powerOffDisableMobiledata

    private static final String powerOffDisableMobiledata = "powerOffDisableMobiledata";

    public static boolean getPowerOffDisableMobiledata() {
        return getSafeBoolean(powerOffDisableMobiledata, false);
    }

    public static void setPowerOffDisableMobiledata(boolean value) {
        boolean ok = setSafeBoolean(powerOffDisableMobiledata, value);
        MJMLog.d(TAG, "setPowerOffDisableMobiledata(" + value + "): " + (ok ? "success" : "error"));
    }

    /// option useLogFile

    private static final String useLogFile = "useLogFile";

    public static boolean getUseLogFile() {
        return getSafeBoolean(useLogFile, false);
    }

    public static void setUseLogFile(boolean value) {
        boolean ok = setSafeBoolean(useLogFile, value);
        MJMLog.d(TAG, "setUseLogFile(" + value + "): " + (ok ? "success" : "error"));
    }

    /// option logFileName

    private static final String logFileName = "logFileName";

    public static String getLogFileName() {
        return getSafeString(logFileName, "");
    }

    public static void setLogFileName(String value) {
        boolean ok = setSafeString(logFileName, value);
        MJMLog.d(TAG, "setLogFileName(\"" + value + "\"): " + (ok ? "success" : "error"));
    }
}
