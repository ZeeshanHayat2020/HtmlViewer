package com.zapps.html.xml.viewer.file.reader.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.zapps.html.xml.viewer.file.reader.R;
import com.zapps.docsReaderModule.constant.DocsReaderConstant;
import com.zapps.appsFlowModule.contstants.Constant;
import com.zapps.html.xml.viewer.file.reader.Threading.RunnableTask;
import com.zapps.html.xml.viewer.file.reader.Threading.RunnableTaskDataBase;
import com.zapps.html.xml.viewer.file.reader.adapters.AdapterFilesContainer;
import com.zapps.html.xml.viewer.file.reader.appConstant.AppConstants;
import com.zapps.html.xml.viewer.file.reader.databinding.ActivityFilesContainerBinding;
import com.zapps.html.xml.viewer.file.reader.dbs.DataBaseHelper;
import com.zapps.html.xml.viewer.file.reader.interfaces.MyOnClickListener;
import com.zapps.html.xml.viewer.file.reader.interfaces.OnRecyclerItemClickLister;
import com.zapps.docsReaderModule.officereader.ActivityDocumentReader;
import com.zapps.html.xml.viewer.file.reader.models.ModelFiles;
import com.zapps.html.xml.viewer.file.reader.popupWindows.PopUpFloatingMenu;
import com.zapps.html.xml.viewer.file.reader.appUtils.AnimUtils;
import com.zapps.html.xml.viewer.file.reader.appUtils.AppOpenManager;
import com.zapps.html.xml.viewer.file.reader.appUtils.MyUtils;
import com.google.android.gms.ads.AdListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.zapps.html.xml.viewer.file.reader.appUtils.MyUtils.createMessage;

public class ActivityFilesContainer extends ActivityBase implements MenuItem.OnActionExpandListener, MyOnClickListener {

    private final String TAG = this.getClass().getName();
    private ActivityFilesContainerBinding binding;
    private String currentTitle;
    private SearchView searchView;
    private ActivityCall activityCall;
    private List<ModelFiles> filesList;
    private List<ModelFiles> multiSelectedItemList;
    private String fileExtension;
    private Handler mUIHandler;
    private Thread mThread;
    private Runnable mFetchingRunnable;
    private AdapterFilesContainer adapter;
    private static TextView tvNoSearchFound;
    private boolean isSelectAll = true;
    private PopUpFloatingMenu popUpFloatingMenu;

    private DataBaseHelper dataBaseHelper;
    private int adCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradient(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_files_container);
        binding.setClickHandler(this);
        reqNewInterstitial(ActivityFilesContainer.this);
        initViews();
        initUiHandler();
        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_view, menu);
        searchView = (androidx.appcompat.widget.SearchView) menu.findItem(R.id.menuBtnSearch).getActionView();
        menu.findItem(R.id.menuBtnSearch).setOnActionExpandListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                if (adapter.getFilesList().size() > 0) {
                    adapter.getFilter().filter(query);
                    if (query.equals("") || TextUtils.isEmpty(query)) {
                        setTvNoSearchFoundVisibility(View.GONE);
                    }
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_contextual_btnSelecAll: {

                if (isSelectAll) {
                    doSelectAll(isSelectAll);
                    isSelectAll = false;
                } else {
                    doSelectAll(isSelectAll);
                    isSelectAll = true;
                }
            }
            break;
            case android.R.id.home: {
                onBackPressed();
            }
            break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return true;
    }

    @Override
    public void onBackPressed() {
        if (adapter != null) {
            if (adapter.isContextualMenuOpen) {
                if (popUpFloatingMenu.getPopupWindow().isShowing()) {
                    popUpFloatingMenu.hideFloatingMenu();
                } else {
                    closeContextualMenu();
                }
            } else {
                super.onBackPressed();

            }
        } else {
            super.onBackPressed();
        }
    }

    private void initViews() {
        Intent intent = getIntent();
        if (intent != null) {
            activityCall = ActivityCall.valueOf(intent.getStringExtra(Constant.KEY_INTENT_ACTIVITY_CALL));
            if (activityCall == ActivityCall.HTML_FILE) {
                fileExtension = ".html";
                currentTitle = getString(R.string.htmViewer);
            } else if (activityCall == ActivityCall.XML_FILES) {
                fileExtension = ".xml";
                currentTitle = getString(R.string.xmlViewer);
            } else if (activityCall == ActivityCall.RECENT_FILES) {
                fileExtension = null;
                currentTitle = getString(R.string.recetnViewer);
            } else {
                fileExtension = null;
                currentTitle = "Failed";
            }
        }
        binding.headerTitle.setText(currentTitle);
        updateToolBarTitle("");
        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.toolBar.setNavigationIcon(null);
        dataBaseHelper = new DataBaseHelper(ActivityFilesContainer.this);
        filesList = new ArrayList<>();
        tvNoSearchFound = binding.tvNoSearchFound;
        popUpFloatingMenu = new PopUpFloatingMenu();
        popUpFloatingMenu.initFloatingMenu(ActivityFilesContainer.this);
        initProgressDialog();
    }

    private void updateToolBarTitle(String toolBarTitle) {
        binding.toolBar.setTitle(toolBarTitle);
    }

    private void initUiHandler() {
        mUIHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Bundle bundle = msg.getData();
                if (bundle != null) {
                    String action = bundle.getString(Constant.ACTION_THREAD_FILES_FETCHING);

                    switch (action) {
                        case Constant.ON_PRE_EXECUTE: {
                            setProgressDialogMessage("Retrieving Files from storage");
                            showProgressDialog();
                        }
                        break;
                        case Constant.ON_POST_EXECUTE: {
                            hideProgressDialog();
                            updateRecyclerView();
                        }
                        break;
                    }
                }
            }
        };
        if (activityCall == ActivityCall.RECENT_FILES) {
            mFetchingRunnable = new RunnableTaskDataBase(mUIHandler, filesList, dataBaseHelper);
        } else {
            if (fileExtension != null) {
                mFetchingRunnable = new RunnableTask(mUIHandler, filesList, fileExtension);
            }
        }
        startFetchingThread();

    }

    private void startFetchingThread() {
        mThread = new Thread(mFetchingRunnable);
        mThread.start();
        mUIHandler.sendMessage(createMessage(Constant.ON_PRE_EXECUTE));
    }

    private void initRecyclerView() {
        adapter = new AdapterFilesContainer(ActivityFilesContainer.this, filesList);
        binding.setMyAdapter(adapter);
        adapter.setOnRecyclerItemClickLister(new OnRecyclerItemClickLister() {
            @Override
            public void onItemClicked(AdapterFilesContainer.ViewHolder viewHolder, ModelFiles modelFiles) {
                if (!adapter.isContextualMenuOpen) {
                    if (mInterstitialAd.isLoaded() && adCounter == 6) {
                        adCounter = 0;
                        mInterstitialAd.show();
                        AppOpenManager.isInterstitialShowing = true;
                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();

                                intentOnItemClick(modelFiles);
                                AppOpenManager.isInterstitialShowing = false;
                                reqNewInterstitial(ActivityFilesContainer.this);
                            }
                        });
                    } else {
                        intentOnItemClick(modelFiles);
                    }
                    adCounter++;
                } else {
                    viewHolder.getItemRowBinding().checkBoxMultiSelect.performClick();
                }
            }

            @Override
            public boolean onItemLongClicked(ModelFiles modelFiles) {
                openContextualMenu();
                return true;
            }

            @Override
            public void onItemCheckBoxClicked(View view, ModelFiles modelFiles) {
                doSingleSelection(view, modelFiles);
            }

            @Override
            public void onItemMenuClick(View view, ModelFiles modelFiles) {
                setItemViewMenu(view, modelFiles);
            }
        });
    }

    private void updateRecyclerView() {
        if (adapter.isContextualMenuOpen) {
            closeContextualMenu();
        }
        if (adapter.getFilesListFiltered().isEmpty()) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.rootViewEmptyFiles.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.rootViewEmptyFiles.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    public static void setTvNoSearchFoundVisibility(int visibility) {
        tvNoSearchFound.setVisibility(visibility);
    }

    private void intentOnItemClick(ModelFiles modelFiles) {
        if (dataBaseHelper.checkIsRecordExist(AppConstants.TABLE_RECENT_FILES,
                modelFiles.getFilePath())) {
            dataBaseHelper.deleteFiles(AppConstants.TABLE_RECENT_FILES, modelFiles.getFilePath());
        }
        dataBaseHelper.insertFile(AppConstants.TABLE_RECENT_FILES, modelFiles.getFilePath(), modelFiles.getFileSize(), MyUtils.getCurrentTimeInFormat());
        Intent intent = new Intent(ActivityFilesContainer.this, ActivityDocumentReader.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(DocsReaderConstant.KEY_INTENT_SELECTED_FILE_PATH, modelFiles.getFilePath());
        ActivityFilesContainer.this.startActivity(intent);
    }

    private void openContextualMenu() {
        adapter.isContextualMenuOpen = true;
        if (binding.btnFabMenu.getVisibility() == View.INVISIBLE) {
            AnimUtils.animCircularShow(binding.btnFabMenu);
        }
        multiSelectedItemList = new ArrayList<>();
        binding.toolBar.getMenu().clear();
        binding.toolBar.inflateMenu(R.menu.menu_ac_files_container_contextual);
        binding.toolBar.setNavigationIcon(R.drawable.ic_cross);
        String text = getString(R.string.files) + " " + multiSelectedItemList.size() + "/" + filesList.size();
        updateToolBarTitle(text);
        adapter.notifyDataSetChanged();
    }

    private void doSingleSelection(View view, ModelFiles modelFiles) {
        try {
            if (((CheckBox) view).isChecked()) {
                modelFiles.setSelected(true);
                multiSelectedItemList.add(modelFiles);
            } else {
                modelFiles.setSelected(false);
                multiSelectedItemList.remove(modelFiles);
            }
            adapter.notifyDataSetChanged();
            String text = getString(R.string.files) + " " + multiSelectedItemList.size() + "/" + filesList.size();
            updateToolBarTitle(text);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onItemCheckBoxClicked: " + e);
        }

    }

    private void doSelectAll(boolean isSelectAll) {
        if (isSelectAll) {
            if (!multiSelectedItemList.isEmpty()) {
                multiSelectedItemList.removeAll(filesList);
                multiSelectedItemList.clear();
            }
            for (int i = 0; i < filesList.size(); i++) {
                filesList.get(i).setSelected(true);
                multiSelectedItemList.add(filesList.get(i));
            }

        } else {
            for (int i = 0; i < filesList.size(); i++) {
                filesList.get(i).setSelected(false);
            }
            multiSelectedItemList.removeAll(filesList);
            multiSelectedItemList.clear();
        }
        adapter.notifyDataSetChanged();
        String text = getString(R.string.files) + " " + multiSelectedItemList.size() + "/" + filesList.size();
        updateToolBarTitle(text);

    }

    private void closeContextualMenu() {
        if (binding.btnFabMenu.getVisibility() == View.VISIBLE) {
            AnimUtils.animCircularHide(binding.btnFabMenu);
        }
        multiSelectedItemList.removeAll(filesList);
        multiSelectedItemList.clear();
        adapter.isContextualMenuOpen = false;
        for (int i = 0; i < filesList.size(); i++) {
            filesList.get(i).setSelected(false);
        }
        updateToolBarTitle("");
        adapter.notifyDataSetChanged();
        binding.toolBar.getMenu().clear();
        onCreateOptionsMenu(binding.toolBar.getMenu());
        binding.toolBar.setNavigationIcon(null);
        adapter.notifyDataSetChanged();
    }

    private void setItemViewMenu(View view, ModelFiles modelFiles) {
        PopupMenu popup = new PopupMenu(ActivityFilesContainer.this, view);
        popup.inflate(R.menu.menu_ac_filescontainer_item_views);
        popup.setGravity(Gravity.RIGHT);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_filesContainer_itemView_rename: {

                        List<ModelFiles> tempList = new ArrayList<>();
                        tempList.clear();
                        tempList.add(modelFiles);
                        dialogRenameFile(tempList);
                    }
                    break;
                    case R.id.menu_filesContainer_itemView_share: {
                        List<ModelFiles> tempList = new ArrayList<>();
                        tempList.clear();
                        tempList.add(modelFiles);
                        shareFileS(tempList);
                    }
                    break;
                    case R.id.menu_filesContainer_itemView_delete: {

                        List<ModelFiles> tempList = new ArrayList<>();
                        tempList.clear();
                        tempList.add(modelFiles);
                        deleteFileDialog(tempList);
                    }
                    break;


                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnFabMenu: {
                if (multiSelectedItemList.size() != 0) {
                    popUpFloatingMenu.showFloatingMenu(binding.btnFabMenu, multiSelectedItemList);
                } else {
                    showToast("Please select any file first.");
                }
            }
            break;
            case R.id.btnBack: {
                onBackPressed();
            }
            break;
        }
    }

    public void dialogRenameFile(List<ModelFiles> tempList) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_dialoge_et_rename, null);

        final EditText editText = (EditText) dialogView.findViewById(R.id.dialog_etFileName);

        AppCompatButton btnRename = dialogView.findViewById(R.id.dialogBtn_rename);
        AppCompatButton btnCancel = dialogView.findViewById(R.id.dialgBtn_Cancel);

        editText.setText(tempList.get(0).getFileName());
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        btnRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (!editText.getText().toString().equals("")) {
                    doRenameFile(tempList, editText.getText().toString());
                    dialogBuilder.dismiss();
                } else {
                    editText.setHint("Enter name first");
                    AnimUtils.animShakeTheView(editText);
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                dialogBuilder.dismiss();

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    AppOpenManager.isInterstitialShowing = true;
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            AppOpenManager.isInterstitialShowing = false;

                        }
                    });
                }

            }
        });
        ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
        int insetValue = (int) this.getResources().getDimension(R.dimen._20sdp);
        InsetDrawable inset = new InsetDrawable(colorDrawable, insetValue);
        dialogBuilder.getWindow().setBackgroundDrawable(inset);
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    public void doRenameFile(List<ModelFiles> temList, String newFileName) {
        setProgressDialogMessage(getString(R.string.dialogMsgRenaming));
        showProgressDialog();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < temList.size(); i++) {
                   /* try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                    String filePath = temList.get(i).getFilePath();
                    String oldFileName = temList.get(i).getFileName();
                    String fileExtension = "." + MyUtils.getExt(oldFileName);

                    String escapedName = "";
                    String completeNewFileName = "";
                    escapedName = MyUtils.escapeExtension(newFileName, fileExtension);
                    completeNewFileName = escapedName + i + fileExtension;
                    File oldPath = new File(filePath);
                    File parentFileName = new File(Objects.requireNonNull(oldPath.getParent()));
                    File from = new File(parentFileName.toString(), oldFileName);
                    File to = new File(parentFileName.toString(), completeNewFileName);
                    from.renameTo(to);
                    if (dataBaseHelper.checkIsRecordExist(AppConstants.TABLE_RECENT_FILES, filePath)) {
                        dataBaseHelper.updateFile(AppConstants.TABLE_RECENT_FILES, to.getAbsolutePath(), filePath);
                    }
                }

                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        showToast(getString(R.string.renameSuccess));
                        startFetchingThread();
                    }
                });
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

    }

    public void deleteFileDialog(List<ModelFiles> tempList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityFilesContainer.this, R.style.mTheme_Dialog);
        builder.setTitle(R.string.deleteAlert);
        if (tempList.size() > 1) {
            builder.setMessage(getString(R.string.mDeleteMsg) + " " + multiSelectedItemList.size() + " " + getString(R.string.selectedFiles));
        } else {
            builder.setMessage(getString(R.string.mDeleteMsg) + ".?");
        }
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doDeleteFiles(tempList);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
              /*  if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    AppOpenManager.isInterstitialShowing = true;
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            dialogInterface.dismiss();
                            AppOpenManager.isInterstitialShowing = false;

                        }
                    });
                } else {
                    dialogInterface.dismiss();
                }*/
            }
        });
        builder.show();


    }

    private void doDeleteFiles(List<ModelFiles> tempList) {
        setProgressDialogMessage(getString(R.string.dialogMsgDeleting));
        showProgressDialog();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < tempList.size(); i++) {
                    File file = new File(tempList.get(i).getFilePath());
                    if (file.exists()) {
                        file.delete();
                    }
                    adapter.getFilesListFiltered().remove(tempList.get(i));

                    if (dataBaseHelper.checkIsRecordExist(AppConstants.TABLE_RECENT_FILES, tempList.get(i).getFilePath())) {
                        dataBaseHelper.deleteFiles(AppConstants.TABLE_RECENT_FILES, tempList.get(i).getFilePath());
                    }
                }


                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressDialog();
                        showToast(getString(R.string.deleteSuccess));
                        startFetchingThread();
                    }
                });

            }

        };
        Thread thread = new Thread(runnable);
        thread.start();

    }

    public void shareFileS(List<ModelFiles> tempList) {
        Intent intentShareFile = new Intent(Intent.ACTION_SEND_MULTIPLE);
        ArrayList<Uri> uris = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
            File file = new File(tempList.get(i).getFilePath());
            if (file.exists()) {
                Uri photoURI = FileProvider.getUriForFile(ActivityFilesContainer.this, getApplicationContext().getPackageName() + ".provider", file);
                uris.add(photoURI);
            }
        }
        intentShareFile.setType("application/*");
        intentShareFile.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Sharing Files...");
        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing Files...");
        startActivity(Intent.createChooser(intentShareFile, "Share File"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataBaseHelper.closeDataBase();
    }
}