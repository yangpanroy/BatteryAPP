package helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2017/3/28.
 */
public class TradeExportSQLiteOpenHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "tradeSQLite.db";
    private final static int DB_VERSION = 1;

    public TradeExportSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("TAG", "创建trade表");
        String sqlStr = "CREATE TABLE IF NOT EXISTS trade (_id INTEGER PRIMARY KEY AUTOINCREMENT, packageList TEXT);";
        db.execSQL(sqlStr);
        Log.i("TAG","SQLiteHelper create TABLE trade success!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
