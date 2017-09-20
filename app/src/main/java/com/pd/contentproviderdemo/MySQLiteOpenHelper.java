package com.pd.contentproviderdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 12857 on 2017/9/19.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper
{
    private static final String STUDENTS_TABLE = "create table students(id integer primary key autoincrement,name text, age integer)";

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory cursor, int version)
    {
        super(context, name, cursor, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(STUDENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("drop table if exists students");
        onCreate(db);
    }
}
