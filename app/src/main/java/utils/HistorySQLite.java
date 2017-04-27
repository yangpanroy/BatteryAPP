package utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import Bean.Main_Search_History_Item;
import helper.MainHistorySQLiteOpenHelper;

/**
 * Created by Administrator on 2017/3/13.
 */
public class HistorySQLite {

    MainHistorySQLiteOpenHelper mainHistorySQLiteOpenHelper;
    SQLiteDatabase historyDb;

    public HistorySQLite(Context context) {
        mainHistorySQLiteOpenHelper = new MainHistorySQLiteOpenHelper(context);
    }

    //添加主界面历史记录
    public void addHistory(String module_num, String module_date, String module_manufacturer, String latest_date, String latest_place){
        if (!isHasHistory(module_num)){
            historyDb = mainHistorySQLiteOpenHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("module_num", module_num);
            values.put("module_date", module_date);
            values.put("module_manufacturer", module_manufacturer);
            values.put("latest_date", latest_date);
            values.put("latest_place", latest_place);
            //增加搜索的历史记录
            historyDb.insert("history", null, values);
            historyDb.close();
        }
        else {
            historyDb = mainHistorySQLiteOpenHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("module_num", module_num);
            values.put("module_date", module_date);
            values.put("module_manufacturer", module_manufacturer);
            values.put("latest_date", latest_date);
            values.put("latest_place", latest_place);
            //修改条件
            String whereClause = "module_num=?";
            //修改添加参数
            String[] whereArgs={module_num};
            //修改
            historyDb.update("history", values, whereClause, whereArgs);
            historyDb.close();
        }
    }

    //判断搜索的号码是否与数据库中信息重复
    public boolean isHasHistory(String module_num) {
        boolean isHasHistory = false;
        historyDb = mainHistorySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = historyDb.query("history", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            if (module_num.equals(cursor.getString(cursor.getColumnIndexOrThrow("module_num")))){
                isHasHistory = true;
            }
        }
        //关闭数据库
        historyDb.close();
        cursor.close();
        return isHasHistory;
    }

    //获得全部历史记录
    public List<Main_Search_History_Item> getHistoryList(){
        List<Main_Search_History_Item> historyList = new ArrayList<>();
        historyDb = mainHistorySQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = historyDb.query("history", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            String module_num = cursor.getString(cursor.getColumnIndexOrThrow("module_num"));
            String module_date = cursor.getString(cursor.getColumnIndexOrThrow("module_date"));
            String module_manufacturer = cursor.getString(cursor.getColumnIndexOrThrow("module_manufacturer"));
            String latest_date = cursor.getString(cursor.getColumnIndexOrThrow("latest_date"));
            String latest_place = cursor.getString(cursor.getColumnIndexOrThrow("latest_place"));
            Main_Search_History_Item historyItem = new Main_Search_History_Item(module_num, module_date, module_manufacturer, latest_date, latest_place);
            historyList.add(historyItem);
        }
        //关闭数据库
        historyDb.close();
        cursor.close();
        return historyList;
    }

    //清空历史记录
    public void deleteAllHistory(){
        historyDb = mainHistorySQLiteOpenHelper.getWritableDatabase();
        historyDb.execSQL("delete from history");
        historyDb.close();
    }
}
