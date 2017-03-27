package helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2017/3/23.
 */
public class UserSQLiteOpenHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "userSQLite.db";
    private final static int DB_VERSION = 1;

    public UserSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("TAG", "创建user表");
        String sqlStr = "CREATE TABLE IF NOT EXISTS user (_id INTEGER PRIMARY KEY AUTOINCREMENT, userName TEXT, password TEXT, companyType INTEGER, companyName TEXT, companyId TEXT, token TEXT, state INTEGER, createDate TEXT);";
        db.execSQL(sqlStr);
        Log.i("TAG","SQLiteHelper create TABLE user success!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
