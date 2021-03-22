package com.zapps.html.xml.viewer.file.reader.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.zapps.html.xml.viewer.file.reader.R;
import com.zapps.html.xml.viewer.file.reader.activities.ActivityFilesContainer;
import com.zapps.html.xml.viewer.file.reader.databinding.ItemViewFilesContainerBinding;
import com.zapps.html.xml.viewer.file.reader.interfaces.OnRecyclerItemClickLister;
import com.zapps.html.xml.viewer.file.reader.models.ModelFiles;

import java.util.ArrayList;
import java.util.List;

public class AdapterFilesContainer extends RecyclerView.Adapter<AdapterFilesContainer.ViewHolder> implements Filterable {


    public boolean isContextualMenuOpen;
    private final Activity activity;
    private final List<ModelFiles> filesList;
    private List<ModelFiles> filesListFiltered;
    private OnRecyclerItemClickLister onRecyclerItemClickLister;

    public AdapterFilesContainer(Context context, List<ModelFiles> filesList) {
        this.activity = (Activity) context;
        this.filesList = filesList;
        this.filesListFiltered = filesList;
    }

    public void setOnRecyclerItemClickLister(OnRecyclerItemClickLister onRecyclerItemClickLister) {
        this.onRecyclerItemClickLister = onRecyclerItemClickLister;
    }

    public List<ModelFiles> getFilesList() {
        return filesList;
    }

    public List<ModelFiles> getFilesListFiltered() {
        return filesListFiltered;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewFilesContainerBinding itemRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_view_files_container, parent, false);
        return new ViewHolder(itemRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelFiles currentModel = filesListFiltered.get(position);
        holder.bindViews(currentModel);

        holder.itemRowBinding.setItemClickListener(onRecyclerItemClickLister);
        holder.itemRowBinding.setViewHolder(holder);

        if (isContextualMenuOpen) {
            holder.itemRowBinding.checkBoxMultiSelect.setChecked(filesListFiltered.get(position).isSelected());
            holder.itemRowBinding.btnItemMenu.setVisibility(View.INVISIBLE);
            holder.itemRowBinding.checkBoxMultiSelect.setVisibility(View.VISIBLE);
        } else {
            holder.itemRowBinding.checkBoxMultiSelect.setVisibility(View.INVISIBLE);
            holder.itemRowBinding.btnItemMenu.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return filesListFiltered.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemViewFilesContainerBinding itemRowBinding;

        public ViewHolder(ItemViewFilesContainerBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bindViews(Object obj) {
            itemRowBinding.setVariable(BR.model, obj);
            itemRowBinding.executePendingBindings();
        }

        public ItemViewFilesContainerBinding getItemRowBinding() {
            return itemRowBinding;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filesListFiltered = filesList;
                } else {
                    ArrayList<ModelFiles> filteredList = new ArrayList<>();
                    for (ModelFiles row : filesList) {
                        if (row.getFileName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    filesListFiltered = filteredList;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (filteredList.size() == 0) {
                                ActivityFilesContainer.setTvNoSearchFoundVisibility(View.VISIBLE);
                            } else {
                                ActivityFilesContainer.setTvNoSearchFoundVisibility(View.GONE);
                            }

                        }
                    });

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filesListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filesListFiltered = (List<ModelFiles>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}
