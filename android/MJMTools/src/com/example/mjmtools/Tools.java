
package com.example.mjmtools;

import android.os.AsyncTask;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class Tools {
    final static String TAG = "MJM:Tools";

    public static void setEditTextEditionFinishedAction(EditText edit,
            final RunnableWithString runnable) {
        edit.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edit.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    runnable.run(v.getText().toString());
                    return false;
                }
                return false;
            }
        });
        edit.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) runnable.run(((EditText) v).getText().toString());
            }
        });
    }

    private static class SplitFileAsyncTask extends AsyncTask<Object, Void, Boolean> {

        private String getSplitFileExtension(int nr) {
            if (nr < 10) return "00" + nr;
            if (nr < 100) return "0" + nr;
            return "" + nr;
        }

        private static File getSplitFileFileLock(File file) {
            return new File(file.getParentFile(), file.getName() + ".lock");
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            File file = (File) params[0];
            long maxFileSize = (long) params[1];
            File lockFile = getSplitFileFileLock(file);
            if (lockFile.exists()) return false;
            long fileLength = file.length();
            String fileName = file.getName();
            File dir = file.getParentFile();
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            byte[] buf = new byte[1024];
            CRC32 crc = new CRC32();
            String outFileName = null;
            try {
                int nr = 1;
                bis = new BufferedInputStream(new FileInputStream(file));
                outFileName = fileName + "." + getSplitFileExtension(nr);
                File outFile = new File(dir, outFileName);
                bos = new BufferedOutputStream(new FileOutputStream(outFile));
                int dataSize;
                int curSize = 0;
                while (-1 != (dataSize = bis.read(buf))) {
                    if (curSize + dataSize > maxFileSize) {
                        bos.close();
                        ++nr;
                        outFileName = fileName + "." + getSplitFileExtension(nr);
                        outFile = new File(dir, outFileName);
                        bos = new BufferedOutputStream(new FileOutputStream(outFile));
                        curSize = 0;
                    }
                    bos.write(buf, 0, dataSize);
                    crc.update(buf, 0, dataSize);
                    curSize += dataSize;
                }
            } catch (IOException e) {
                return false;
            } finally {
                try {
                    long crcLong = crc.getValue();
                    String crcString = Long.toHexString(crcLong);
                    while (crcString.length() < 8)
                        crcString = "0" + crcString;
                    String crcContent = "";
                    crcContent += "filename=" + fileName + "\n";
                    crcContent += "size=" + fileLength + "\n";
                    crcContent += "crc32=" + crcString + "\n";
                    String crcFileName = fileName + ".crc";
                    File crcFile = new File(dir, crcFileName);
                    FileOutputStream crcStream = new FileOutputStream(crcFile);
                    crcStream.write(crcContent.getBytes());
                    crcStream.close();
                    if (bis != null) bis.close();
                    if (bos != null) bos.close();
                } catch (IOException e) {
                    return false;
                }
            }
            file.delete();
            lockFile.delete();
            return true;
        }
    }

    public static void splitFile(File file, long maxPartSize) {
        SplitFileAsyncTask task = new SplitFileAsyncTask();
        task.execute(file, maxPartSize);
    }
}
