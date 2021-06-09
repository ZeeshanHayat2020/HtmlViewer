package com.zapps.html.xml.viewer.file.reader.Threading;

import android.os.Handler;

import com.zapps.appsFlowModule.contstants.Constant;
import com.zapps.html.xml.viewer.file.reader.appConstant.AppConstants;
import com.zapps.html.xml.viewer.file.reader.dbs.DataBaseHelper;
import com.zapps.html.xml.viewer.file.reader.models.ModelFiles;
import com.zapps.html.xml.viewer.file.reader.appUtils.MyUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.zapps.html.xml.viewer.file.reader.appUtils.MyUtils.createMessage;

public class RunnableTaskDataBase implements Runnable {

    private final String TAG = this.getClass().getName();
    private Handler mUiHandler;
    private List<ModelFiles> filesList;
    private DataBaseHelper dataBaseHelper;

    public RunnableTaskDataBase(Handler mUiHandler, List<ModelFiles> filesList, DataBaseHelper dataBaseHelper) {
        this.mUiHandler = mUiHandler;
        this.filesList = filesList;
        this.dataBaseHelper = dataBaseHelper;
    }

    @Override
    public void run() {
        filesList.clear();
        fetchRecentFiles();
        mUiHandler.sendMessage(createMessage(Constant.ON_POST_EXECUTE));
    }

    private void fetchRecentFiles() {
        ArrayList<ModelFiles> temList = dataBaseHelper.getAllFiles(AppConstants.TABLE_RECENT_FILES);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        String currentTime = MyUtils.getCurrentTimeInFormat();
        Date openDate;
        Date currentDate;
        long difference;
        for (int i = temList.size(); i > 0; i--) {

            int position = i - 1;
            ModelFiles modelFiles = temList.get(position);
            if (filesList.size() < 9) {
                try {
                    String openTime = modelFiles.getOpenTime();
                    openDate = simpleDateFormat.parse(openTime);
                    currentDate = simpleDateFormat.parse(currentTime);
                    difference = currentDate.getTime() - openDate.getTime();
                    int days = (int) (difference / (1000 * 60 * 60 * 24));
                    int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
//                            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                    if (hours < 5) {
                        filesList.add(new ModelFiles(modelFiles.getFilePath(), modelFiles.getFileName(), modelFiles.getFileSize()));
                    } else {
                        dataBaseHelper.deleteFiles(AppConstants.TABLE_RECENT_FILES, modelFiles.getFilePath());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else break;

        }
    }
}
