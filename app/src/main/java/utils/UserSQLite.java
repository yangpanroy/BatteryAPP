package utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Bean.Company;
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

    public void addUser(String id, String userName, String password, Company company, String token, int state, String createDate)
    {
        if (!isHasUser(userName))
        {
            deleteAllUser();
            userDb = userSQLiteOpenHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("id", id);
            values.put("userName", userName);
            values.put("password", password);
            values.put("companyJson", new Gson().toJson(company));
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
        User user = new User();
        userDb = userSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = userDb.query("user", null, null, null, null, null, null);
        if (cursor.getCount() != 0)
        {
            cursor.moveToLast();//查询user表并移动到最后一条记录
            String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
            String userName = cursor.getString(cursor.getColumnIndexOrThrow("userName"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            String companyJson = cursor.getString(cursor.getColumnIndexOrThrow("companyJson"));
            String token = cursor.getString(cursor.getColumnIndexOrThrow("token"));
            int state = cursor.getInt(cursor.getColumnIndexOrThrow("state"));
            String createDate = cursor.getString(cursor.getColumnIndexOrThrow("createDate"));
            Company company = new Gson().fromJson(companyJson, new TypeToken<Company>() {}.getType());
            user = new User(id, userName, password, company, token, state, createDate);
        }
        //关闭数据库
        userDb.close();
        cursor.close();
        return user;
    }

    //清空所有的user信息
    public void deleteAllUser() {
        userDb = userSQLiteOpenHelper.getWritableDatabase();
        userDb.execSQL("delete from user");
        userDb.close();
    }

    public void updateUser(String userName, String token)
    {
        userDb = userSQLiteOpenHelper.getWritableDatabase();
        //实例化内容值
        ContentValues values = new ContentValues();
        //在values中添加内容
        values.put("token",token);
        //修改条件
        String whereClause = "userName=?";
        //修改添加参数
        String[] whereArgs={userName};
        //修改
        userDb.update("user",values,whereClause,whereArgs);
    }
}
