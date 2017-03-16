package helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2017/3/13.
 */
public class MainHistorySQLiteOpenHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "historySQLite.db";
    private final static int DB_VERSION = 1;

    public MainHistorySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("TAG","创建history表");
        String sqlStr = "CREATE TABLE IF NOT EXISTS history (_id INTEGER PRIMARY KEY AUTOINCREMENT, module_num TEXT, module_date TEXT, module_manufacturer TEXT, latest_date TEXT, latest_place TEXT);";
        db.execSQL(sqlStr);
        Log.i("TAG","SQLiteHelper create table history success!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
