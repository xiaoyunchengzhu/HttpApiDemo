package com.xiaoyunchengzhu.httpapi.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangshiyu on 2016/4/29.
 */
public class DownloadSqlliteHelper extends SQLiteOpenHelper {


    private String tableName="xiaoyunchengzhuDownloadInfo";
    private String table_threadId="threadId";
    private String table_url="url";
    private String table_filPath="filPath";
    private String table_startIndex="startIndex";
    private String table_currentIndedx="currentIndedx";
    private String table_endIndex="endIndex";
    private String table_contentLength="contentLength";
    private String createTable="create  table if not exists " +tableName +"(" + table_threadId +" int primary key ," + table_url + " varchar not null ," +
              table_filPath +" varchar," + table_startIndex + " long,"+table_currentIndedx+" long ,"+table_endIndex+" long,"+table_contentLength+" long)";
    public DownloadSqlliteHelper(Context context) {
        super(context, "xiaoyunchegnzhuDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
               sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insert(DonloadInfo donloadInfo)
    {
        ContentValues contentValues=new ContentValues();
        contentValues.put(table_url,donloadInfo.getUrl());
        contentValues.put(table_filPath,donloadInfo.getFilPath());
        contentValues.put(table_threadId,donloadInfo.getThreadId());
        contentValues.put(table_startIndex,donloadInfo.getStartIndex());
        contentValues.put(table_currentIndedx,donloadInfo.getCurrentIndedx());
        contentValues.put(table_endIndex,donloadInfo.getEndIndex());
        contentValues.put(table_contentLength,donloadInfo.getContentLength());
        getWritableDatabase().insert(tableName,null,contentValues);
    }
    public void delete(DonloadInfo donloadInfo)
    {
        getWritableDatabase().delete(tableName,table_url+"=? and "+table_startIndex+"=?",new String[]{donloadInfo.getUrl(),donloadInfo.getStartIndex()+""});
    }
    public void update(DonloadInfo donloadInfo)
    {
        ContentValues contentValues=new ContentValues();
       contentValues.put(table_url,donloadInfo.getUrl());
        contentValues.put(table_filPath,donloadInfo.getFilPath());
        contentValues.put(table_threadId,donloadInfo.getThreadId());
        contentValues.put(table_startIndex,donloadInfo.getStartIndex());
        contentValues.put(table_currentIndedx,donloadInfo.getCurrentIndedx());
        contentValues.put(table_endIndex,donloadInfo.getEndIndex());
        contentValues.put(table_contentLength,donloadInfo.getContentLength());
        getWritableDatabase().update(tableName,contentValues,table_url + "=" + donloadInfo.getUrl()+"and "+table_startIndex+"="+donloadInfo.getStartIndex()+")",null);
    }
    public List<DonloadInfo> query(String url)
    {

        Cursor cursor = getWritableDatabase().rawQuery("select *from "+tableName+" where "+table_url+"=?", new String[]{url});
        List<DonloadInfo> donloadInfos=new ArrayList<>();
        while (cursor.moveToNext())
        {
            DonloadInfo donloadInfo=new DonloadInfo();
            donloadInfo.setUrl(url);
            donloadInfo.setFilPath(cursor.getString(cursor.getColumnIndex(table_filPath)));
            donloadInfo.setThreadId(cursor.getInt(cursor.getColumnIndex(table_threadId)));
            donloadInfo.setStartIndex(cursor.getLong(cursor.getColumnIndex(table_startIndex)));
            donloadInfo.setCurrentIndedx(cursor.getLong(cursor.getColumnIndex(table_currentIndedx)));
            donloadInfo.setEndIndex(cursor.getLong(cursor.getColumnIndex(table_endIndex)));
            donloadInfo.setContentLength(cursor.getLong(cursor.getColumnIndex(table_contentLength)));
            donloadInfos.add(donloadInfo);
        }
        return donloadInfos;
    }
}
