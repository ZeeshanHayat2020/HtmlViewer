package com.zapps.html.xml.viewer.file.reader.interfaces;

import android.view.View;

import com.zapps.html.xml.viewer.file.reader.adapters.AdapterFilesContainer;
import com.zapps.html.xml.viewer.file.reader.models.ModelFiles;

public interface OnRecyclerItemClickLister {
    void onItemClicked(AdapterFilesContainer.ViewHolder viewHolder, ModelFiles modelFiles);


    boolean onItemLongClicked(ModelFiles modelFiles);

    void onItemCheckBoxClicked(View view, ModelFiles modelFiles);

    void onItemMenuClick(View view, ModelFiles modelFiles);
}
