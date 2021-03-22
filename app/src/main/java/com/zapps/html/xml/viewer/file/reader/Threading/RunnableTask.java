package com.zapps.html.xml.viewer.file.reader.Threading;

import android.os.Environment;
import android.os.Handler;

import com.zapps.html.xml.viewer.file.reader.models.ModelFiles;
import com.zapps.appsFlowModule.contstants.Constant;

import java.io.File;
import java.util.List;

import static com.zapps.html.xml.viewer.file.reader.appUtils.MyUtils.createMessage;
import static com.zapps.html.xml.viewer.file.reader.appUtils.MyUtils.getFileSize;


public class RunnableTask implements Runnable {

    private final String TAG = this.getClass().getName();
    private Handler mUiHandler;
    private List<ModelFiles> filesList;
    private String fileExtension;


    public RunnableTask(Handler mUiHandler, List<ModelFiles> filesList, String fileExtension) {
        this.mUiHandler = mUiHandler;
        this.filesList = filesList;
        this.fileExtension = fileExtension;
    }

    @Override
    public void run() {
        File parentFile = new File(String.valueOf(Environment.getExternalStorageDirectory()));
        filesList.clear();
        fetchFiles(parentFile);
        mUiHandler.sendMessage(createMessage(Constant.ON_POST_EXECUTE));
    }


    private void fetchFiles(File parentFile) {

        File[] files = parentFile.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {

                if (files[i].isDirectory()) {
                    fetchFiles(files[i]);
                } else {
                    if (files[i].getName().endsWith(fileExtension)) {
                        filesList.add(new ModelFiles(files[i].getAbsolutePath(), files[i].getName(), getFileSize(files[i])));
                    }
                }
            }

        }

    }


}
