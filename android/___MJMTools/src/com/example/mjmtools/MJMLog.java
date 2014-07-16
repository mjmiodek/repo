
package com.example.mjmtools;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MJMLog {
    private static File getLogFile() {
        return getLogFileWithOptions(true);
    }

    private static File getLogFileDontCreate() {
        return getLogFileWithOptions(false);
    }

    private static File getLogFileWithOptions(boolean create) {
        if (!MJMToolsApplication.getUseLogFile()) return null;
        File root = Environment.getExternalStorageDirectory();
        String fileName = MJMToolsApplication.getLogFileName();
        File file = new File(root, fileName);
        if (create && !file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private static void appendLog(String type, String tag, String msg) {
        File file = getLogFile();
        if (file == null) return;
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String body = sdf.format(new Date()) + " " + type + " " + tag + " " + msg;
            buf.append(body);
            buf.newLine();
            buf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openLogFile(Activity activity) {
        File file = getLogFile();
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "text/*");
        activity.startActivity(intent);
    }

    public static void clearLogFile() {
        File file = getLogFileDontCreate();
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
        appendLog("info", tag, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
        appendLog("debug", tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
        appendLog("error", tag, msg);
    }
}
