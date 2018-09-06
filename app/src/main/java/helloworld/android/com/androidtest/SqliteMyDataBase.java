package helloworld.android.com.androidtest;

import android.content.Context;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteMyDataBase extends SQLiteOpenHelper {
    public SqliteMyDataBase(Context context) {
        super(context, "db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        db.execSQL("CREATE TABLE person("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT DEFAULT  \"\","
                + "age TEXT DEFAULT \"\")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
