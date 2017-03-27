package utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import Bean.User;
import helper.UserSQLiteOpenHelper;

/**
 * Created by Administrator on 2017/3/23.
 */
public class UserSQLite {
    UserSQLiteOpenHelper userSQLiteOpenHelper;
    SQLiteDatabase userDb;

    public UserSQLite(Context context) {
        userSQLiteOpenHelper = new UserSQLiteOpenHelper(context);
    }

    public void addUser(String userName, String password, int companyType, String companyName, String companyId, String token, int state, String createDate)
    {
        if (isHasUser(userName))
        {
            userDb = userSQLiteOpenHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("userName", userName);
            values.put("password", password);
            values.put("companyType", companyType);
            values.put("companyName", companyName);
            values.put("companyId", companyId);
            values.put("token", token);
            values.put("state", state);
            values.put("createDate", createDate);
            //增加搜索的历史记录
            userDb.insert("user", null, values);
            //关闭
            userDb.close();
        }
    }

    private boolean isHasUser(String userName) {
        boolean isHasUser = false;
        userDb = userSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = userDb.query("user", null, null, null, null, null, null);
        while (cursor.moveToNext()){
            if (userName.equals(cursor.getString(cursor.getColumnIndexOrThrow("userName")))){
                isHasUser = true;
            }
        }
        //关闭数据库
        userDb.close();
        cursor.close();
        return isHasUser;
    }

    //获得最近一条User记录
    public User getUser(){
        User user;
        userDb = userSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = userDb.query("user", null, null, null, null, null, null);
        cursor.moveToLast();//查询user表并移动到最后一条记录
        String userName = cursor.getString(cursor.getColumnIndexOrThrow("userName"));
        String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
        int companyType = cursor.getInt(cursor.getColumnIndexOrThrow("companyType"));
        String companyName = cursor.getString(cursor.getColumnIndexOrThrow("companyName"));
        String companyId = cursor.getString(cursor.getColumnIndexOrThrow("companyId"));
        String token = cursor.getString(cursor.getColumnIndexOrThrow("token"));
        int state = cursor.getInt(cursor.getColumnIndexOrThrow("state"));
        String createDate = cursor.getString(cursor.getColumnIndexOrThrow("createDate"));
        user = new User(userName, password, companyType, companyName, companyId, token, state, createDate);
        //关闭数据库
        userDb.close();
        cursor.close();
        return user;
    }
}
