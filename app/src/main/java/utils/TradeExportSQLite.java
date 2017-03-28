package utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import helper.TradeExportSQLiteOpenHelper;

/**
 * Created by Administrator on 2017/3/28.
 */
public class TradeExportSQLite {
    TradeExportSQLiteOpenHelper tradeExportSQLiteOpenHelper;
    SQLiteDatabase tradeDb;

    public TradeExportSQLite(Context context) {
        tradeExportSQLiteOpenHelper = new TradeExportSQLiteOpenHelper(context);
    }

    public void addTrade(String packageList)
    {
        if (!isHasTrade(packageList))
        {
            //若增加新的交易信息，先清空trade表，保证只有一条信息在表内
            deleteAllTrade();
            tradeDb = tradeExportSQLiteOpenHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("packageList", packageList);
            //增加交易信息
            tradeDb.insert("trade", null, values);
            //关闭
            tradeDb.close();
        }
    }

    private boolean isHasTrade(String packageList) {
        boolean isHasTrade = false;
        tradeDb = tradeExportSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = tradeDb.query("trade", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            if (packageList.equals(cursor.getString(cursor.getColumnIndexOrThrow("packageList")))){
                isHasTrade = true;
            }
        }
        //关闭数据库
        tradeDb.close();
        cursor.close();
        return isHasTrade;
    }

    //获得最近一条交易记录
    public String getTrade(){
        String packageList = "";
        tradeDb = tradeExportSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = tradeDb.query("trade", null, null, null, null, null, null);
        if (cursor.getCount() != 0)
        {
            cursor.moveToLast();//查询trade表并移动到最后一条记录
            packageList = cursor.getString(cursor.getColumnIndexOrThrow("packageList"));
        }
        //关闭数据库
        tradeDb.close();
        cursor.close();
        return packageList;
    }

    //清空所有的trade信息
    public void deleteAllTrade(){
        tradeDb = tradeExportSQLiteOpenHelper.getWritableDatabase();
        tradeDb.execSQL("delete from trade");
        tradeDb.close();
    }
}
