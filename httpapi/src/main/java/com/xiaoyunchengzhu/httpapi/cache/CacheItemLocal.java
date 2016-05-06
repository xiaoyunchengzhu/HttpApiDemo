package com.xiaoyunchengzhu.httpapi.cache;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by zhangshiyu on 2016/4/19.
 */
public class CacheItemLocal extends CacheItem {

    private String tableName;
    private SQLiteDatabase database;
    public CacheItemLocal(byte[] data, String key, Dependency dependency,String tableName,SQLiteDatabase database) {
        super(data, key, dependency);
        this.tableName=tableName;
        this.database=database;
    }



    @Override
    public void setData(byte[] data) {
        super.setData(data);
        ContentValues contentValues=new ContentValues();
        contentValues.put(CacheQueueLocal.table_lastModified,getDependency().getLastModified().getTime());
        contentValues.put(CacheQueueLocal.table_expiredTime,getDependency().getExpirationTime());
        contentValues.put(CacheQueueLocal.table_data,data);

        database.update(tableName,contentValues,CacheQueueLocal.table_key+" =? ", new String[]{getKey()});
    }
}
