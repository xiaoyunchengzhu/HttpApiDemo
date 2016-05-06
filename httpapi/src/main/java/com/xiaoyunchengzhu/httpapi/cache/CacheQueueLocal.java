package com.xiaoyunchengzhu.httpapi.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xiaoyunchengzhu.httpapi.http.LogManger;

/**
 * Created by zhangshiyu on 2016/4/19.
 */
public class CacheQueueLocal extends CacheQueue{



    private String dbFile=null;

    private Context context;
    private String tableName="cacheTable";
    public static String table_id="id";
    public static String table_key="key";
    public static String table_data="data";
    public static String table_lastModified="lastModified";
    public static String table_expiredTime="expiredTime";
    public SQLiteDatabase sqLiteDatabase;
    private CacheOpenHelper cacheOpenHelper;

    public CacheQueueLocal(Context context) {
        super();
        this.context=context;
        cacheOpenHelper=new CacheOpenHelper(context);
        inite();
    }

    private void inite()
    {
         sqLiteDatabase=cacheOpenHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + tableName, null);
        while(cursor.moveToNext())
        {
            String keyInfo = cursor.getString(cursor.getColumnIndex(table_key));
            byte[] dataInfo=cursor.getBlob(cursor.getColumnIndex(table_data));
            long expiredTime=cursor.getLong(cursor.getColumnIndex(table_expiredTime));
            long lastmodifiledTime=cursor.getLong(cursor.getColumnIndex(table_lastModified));
            innerDictionary.put(keyInfo,createCacheItem(dataInfo,keyInfo,lastmodifiledTime,expiredTime));
        }
        sqLiteDatabase.endTransaction();;

    }

    @Override
    public void add(byte[] data, String key, long expired) {
        super.add(data, key, expired);
        boolean isExist=false;


        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + tableName + " where " + table_key + "=?", new String[]{key});
        LogManger.i("key---:" + key);
        if (cursor.moveToNext())
        {
            isExist=true;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(table_key, key);
        contentValues.put(table_data,data);
        contentValues.put(table_lastModified, new Dependency(expired).getLastModified().getTime());
        contentValues.put(table_expiredTime, expired);
        if (isExist)
        {
            sqLiteDatabase.update(tableName,contentValues,table_key+"=?",new String[]{key});
        }else {

            sqLiteDatabase.insert(tableName, null, contentValues);
            Cursor cursor1 = sqLiteDatabase.rawQuery("select * from " + tableName + " where " + table_key + "=?", new String[]{key});
            if (cursor1.moveToNext())
            {
                LogManger.i("已经插入成功");
                LogManger.i("key:"+cursor1.getString(cursor1.getColumnIndex(table_key)));
                LogManger.i("value:"+new String(cursor1.getBlob(cursor1.getColumnIndex(table_data))));


            }else {
                LogManger.i("已经插入失败");
            }
        }

    }

    @Override
    protected CacheItem createCacheItem(byte[] data, String key, long expired) {

        return new CacheItemLocal(data, key, new Dependency(expired),tableName,sqLiteDatabase);

    }
    @Override
    protected CacheItem createCacheItem(byte[] data, String key,long lastModifieldTime, long expired) {

        return new CacheItemLocal(data, key, new Dependency(lastModifieldTime,expired),tableName,sqLiteDatabase);

    }

    @Override
    public void update(byte[] data, String key, long expired) {
        super.update(data, key, expired);

        ContentValues contentValues=new ContentValues();
        if (data!=null)
        {
            contentValues.put(table_data,data);
        }
        if (expired>=0)
        {
            contentValues.put(table_expiredTime,expired);
        }
        sqLiteDatabase.update(tableName,contentValues,table_key+"  =?",new String[]{key} );
    }

    @Override
    public void remove(String key) {
        super.remove(key);

        sqLiteDatabase.delete(tableName,table_key+"=?",new String[]{key});
    }

    @Override
    public void clear() {
        super.clear();
        sqLiteDatabase.execSQL("delete *from "+tableName);

    }


}
