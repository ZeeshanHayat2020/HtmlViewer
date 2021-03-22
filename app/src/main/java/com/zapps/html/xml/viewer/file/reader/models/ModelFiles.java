package com.zapps.html.xml.viewer.file.reader.models;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class ModelFiles {

    private String filePath;
    private String fileName;
    private String fileSize;
    private boolean isSelected = false;
    private String openTime;

    public ModelFiles(String filePath, String fileName, String fileSize) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public ModelFiles(String filePath, String fileName, String fileSize, String openTime) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.openTime = openTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    @BindingAdapter("loadFileImage")
    public static void loadImage(ImageView view, int resId) {
        view.setImageResource(resId);
    }
}
