package com.pd.contentproviderdemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by 12857 on 2017/9/19.
 */

public class MyContentProvider extends ContentProvider
{
    private static UriMatcher uriMatcher;
    private static final int STUDENTS_DIR = 0;
    private static final int STUDENTS_ITEM = 1;
    private static final String AUTHORITY = "com.pd.contentproviderdemo.provider";
    private MySQLiteOpenHelper helper;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "students", STUDENTS_DIR);
        uriMatcher.addURI(AUTHORITY, "students/#", STUDENTS_ITEM);
    }

    @Override
    public boolean onCreate()
    {
        helper = new MySQLiteOpenHelper(getContext(), "StudentManagementSystem", null, 1);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri))
        {
            case STUDENTS_DIR:
                cursor = db.query("students", projection, selection,selectionArgs,null, null, sortOrder);
                break;
            case STUDENTS_ITEM:
                String studentsName = uri.getPathSegments().get(1);
                cursor = db.query("students", projection, "id = ?", new String[]{studentsName}, null, null, sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri))
        {
            case STUDENTS_DIR:
            case STUDENTS_ITEM:
                long studentsId = db.insert("students", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/students/" + studentsId);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri))
        {
            case STUDENTS_DIR:
                updateRows = db.update("students", values, selection, selectionArgs);
                break;
            case STUDENTS_ITEM:
                String studentsName = uri.getPathSegments().get(1);
                updateRows = db.update("students",values,"id = ?", new String[]{studentsName});
                break;
            default:
                break;
        }
        return updateRows;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        int deleteRows = 0;
        switch (uriMatcher.match(uri))
        {
            case STUDENTS_DIR:
                deleteRows = db.delete("students", selection, selectionArgs);
                break;
            case STUDENTS_ITEM:
                String studentsName = uri.getPathSegments().get(1);
                deleteRows = db.delete("students", "id = ?", new String[]{studentsName});
                break;
            default:
                break;
        }
        return deleteRows;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri)
    {
        switch (uriMatcher.match(uri))
        {
            case STUDENTS_DIR:
                return "vnd.android.cursor.dir/vnd.com.pd.hotfix.contentproviderdemo.students";
            case STUDENTS_ITEM:
                return "vnd.android.cursor.item/vnd.com.pd.hotfix.contentproviderdemo.students";
        }
        return null;
    }
}
