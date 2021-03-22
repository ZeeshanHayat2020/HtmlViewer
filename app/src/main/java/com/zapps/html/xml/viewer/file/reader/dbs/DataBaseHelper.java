package com.zapps.html.xml.viewer.file.reader.dbs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.zapps.html.xml.viewer.file.reader.appConstant.AppConstants;
import com.zapps.html.xml.viewer.file.reader.models.ModelFiles;

import java.io.File;
import java.util.ArrayList;


public class DataBaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    public static final String LOG = "DatabaseHelper";
    // Database Version
    public static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "com.example.htmlviewer.data_base";
    // Common column names
    public static final String KEY_FILE_ID = "id";
    public static final String KEY_FILE_PATH = "path";
    public static final String KEY_OPEN_TIME = "time";
    public static final String KEY_FILE_SIZE = "size";


    private static final String CREATE_TABLE_RECENT_FILES = "CREATE TABLE IF NOT EXISTS "
            + AppConstants.TABLE_RECENT_FILES + "(" + KEY_FILE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_FILE_PATH + " TEXT ," + KEY_FILE_SIZE + " TEXT ," + KEY_OPEN_TIME + " TEXT " + ")";


    private SQLiteDatabase database;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        openDataBase();
    }

    private void openDataBase() {
        database = this.getWritableDatabase();
    }

    public void closeDataBase() {
        if (database.isOpen()) {
            database.close();
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECENT_FILES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + AppConstants.TABLE_RECENT_FILES);
        onCreate(db);
    }

    public void insertFile(String TABLE_NAME, String filePath, String fileSize, String openTime) {
        ContentValues values = new ContentValues();
        values.put(KEY_FILE_PATH, filePath);
        values.put(KEY_FILE_SIZE, fileSize);
        values.put(KEY_OPEN_TIME, openTime);
        database.insert(TABLE_NAME, null, values);
    }

    public void updateFile(String TABLE_NAME, String newFilePath, String oldFilePath) {
        ContentValues values = new ContentValues();
        values.put(KEY_FILE_PATH, newFilePath);
        database.update(TABLE_NAME, values, KEY_FILE_PATH + " = ?",
                new String[]{oldFilePath});
    }

    public ArrayList<ModelFiles> getAllFiles(String TABLE_NAME) {
        ArrayList<ModelFiles> filesList = new ArrayList<>();
        if (!filesList.isEmpty()) {
            filesList.clear();
        }
        Cursor c = database.query(TABLE_NAME, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                String filePath;
                String openTime;
                String fileSize;
                filePath = c.getString(c.getColumnIndex(KEY_FILE_PATH));
                fileSize = c.getString(c.getColumnIndex(KEY_FILE_SIZE));
                openTime = c.getString(c.getColumnIndex(KEY_OPEN_TIME));
                if (new File(filePath).exists()) {
                    filesList.add(new ModelFiles(filePath, new File(filePath).getName(), fileSize, openTime));
                }
            } while (c.moveToNext());
        }
        c.close();
        return filesList;
    }

    public String getSelectedFilePath(String TABLE_NAME, String filePath) {
        String foundPath = null;
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE "
                + KEY_FILE_PATH + " = '" + filePath + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                foundPath = (c.getString(c.getColumnIndex(KEY_FILE_PATH)));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return foundPath;
    }

    public void deleteFiles(String TABLE_NAME, String columnValue) {
        database.delete(TABLE_NAME, KEY_FILE_PATH + " = ?",
                new String[]{String.valueOf(columnValue)});
    }

    public boolean checkIsRecordExist(String TABLE_NAME, String columnValue) {
        try {
            Cursor cursor = database.query(TABLE_NAME, new String[]{KEY_FILE_PATH}, KEY_FILE_PATH + "=?", new String[]{columnValue}, null, null, null);
            if (cursor.moveToFirst()) {
                return true;
            }
            cursor.close();
        } catch (Exception errorException) {
            Log.d(LOG, "Exception occured" + "Exception occured " + errorException);
        }
        return false;
    }

}
