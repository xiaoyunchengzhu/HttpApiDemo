package com.xiaoyunchengzhu.httpapi.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhangshiyu on 2016/4/23.
 */
public class CacheOpenHelper extends SQLiteOpenHelper {
    private String tableName="cacheTable";
    public static String table_id="id";
    public static String table_key="key";
    public static String table_data="data";
    public static String table_lastModified="lastModified";
    public CacheOpenHelper(Context context ) {
        super(context, context.getPackageName()+"xiaoyunchengzhudb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create  table if not exists " + tableName + " (" + table_id + " int primary key ," + table_key + " text not null ," + table_data + " blob," + table_lastModified + " long)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
