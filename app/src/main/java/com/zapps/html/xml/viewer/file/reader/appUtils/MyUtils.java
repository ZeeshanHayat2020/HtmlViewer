package com.zapps.html.xml.viewer.file.reader.appUtils;

import android.os.Bundle;
import android.os.Message;

import com.zapps.appsFlowModule.contstants.Constant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyUtils {

    public static final String getExt(String filePath) {
        int strLength = filePath.lastIndexOf(".");
        if (strLength > 0)
            return filePath.substring(strLength + 1).toLowerCase();
        return null;
    }

    public static final Message createMessage(String MESSAGE) {
        Bundle bundle = new Bundle();
        Message message = Message.obtain();
        bundle.putString(Constant.ACTION_THREAD_FILES_FETCHING, MESSAGE);
        message.setData(bundle);
        return message;
    }

    public static final Message publishProgress(String MESSAGE) {
        Bundle bundle = new Bundle();
        Message message = Message.obtain();
        bundle.putString(Constant.ACTION_THREAD_FILES_FETCHING, MESSAGE);
        message.setData(bundle);
        return message;
    }

    public static String getFileSize(File file) {
        String modifiedFileSize = null;
        double fileSize;
        if (file.isFile()) {
            fileSize = (double) file.length();//in Bytes

            if (fileSize < 1024) {
                modifiedFileSize = String.valueOf(fileSize).concat("B");
            } else if (fileSize > 1024 && fileSize < (1024 * 1024)) {
                modifiedFileSize = String.valueOf(Math.round((fileSize / 1024 * 100.0)) / 100.0).concat("KB");
            } else {
                modifiedFileSize = String.valueOf(Math.round((fileSize / (1024 * 1204) * 100.0)) / 100.0).concat("MB");
            }
        }

        return modifiedFileSize;
    }

    public static String escapeExtension(String s, String ext) {
        return s != null ? s.replaceAll(ext, "") : null;
    }


    public static String getCurrentTimeInFormat() {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        return df.format(Calendar.getInstance().getTime());
    }

}
