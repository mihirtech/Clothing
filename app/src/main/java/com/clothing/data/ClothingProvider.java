package com.clothing.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by mihir.shah on 11/8/2015.
 */
public class ClothingProvider extends ContentProvider {

    private static final String TAG = ClothingProvider.class.getSimpleName();

    Context mContext;

    String DATABASE_NAME = "images.db";

    int DATA_BASE_VERSION = 1;

    CallSQLiteHelper mHelper;

    static final String AUTHORITY = ClothingProvider.class.getCanonicalName();

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final int ADDED = 1, DISLIKED = 2, FAVORITE = 3;

    private static final UriMatcher URI_MATCHER;

    public SQLiteDatabase mDatabase;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, AddedImagesColumns.TABLE_NAME, ADDED);
        URI_MATCHER.addURI(AUTHORITY, DislikedImagesColumns.TABLE_NAME, DISLIKED);
        URI_MATCHER.addURI(AUTHORITY, FavoriteImagesColumns.TABLE_NAME, FAVORITE);
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mHelper = new CallSQLiteHelper(mContext, DATABASE_NAME, null,
                DATA_BASE_VERSION);
        try {
            mDatabase = mHelper.getWritableDatabase();
        } catch (Exception ex) {
            mDatabase = mHelper.getReadableDatabase();
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (URI_MATCHER.match(uri)) {
            case ADDED:
                cursor = mDatabase.query(AddedImagesColumns.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case DISLIKED:
                cursor = mDatabase.query(DislikedImagesColumns.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case FAVORITE:
                cursor = mDatabase.query(FavoriteImagesColumns.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                break;
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowId = -1;
        Uri insertUri = null;
        switch (URI_MATCHER.match(uri)) {
            case ADDED:
                rowId = mDatabase.insert(AddedImagesColumns.TABLE_NAME, null, values);
                insertUri = Uri.withAppendedPath(AddedImagesColumns.CONTENT_URI,
                        Long.toString(rowId));
                break;
            case DISLIKED:
                rowId = mDatabase.insert(DislikedImagesColumns.TABLE_NAME, null, values);
                insertUri = Uri.withAppendedPath(DislikedImagesColumns.CONTENT_URI,
                        Long.toString(rowId));
                break;
            case FAVORITE:
                rowId = mDatabase.insert(FavoriteImagesColumns.TABLE_NAME, null, values);
                insertUri = Uri.withAppendedPath(FavoriteImagesColumns.CONTENT_URI,
                        Long.toString(rowId));
                break;
        }
        if (rowId != -1) {
            Log.d(TAG, "Inserted rowId: " + rowId);
            getContext().getContentResolver().notifyChange(uri, null);
        } else {
            insertUri = null;
        }
        return insertUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numRows = 0;
        switch (URI_MATCHER.match(uri)) {
            case ADDED:
                numRows = mDatabase.delete(AddedImagesColumns.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case DISLIKED:
                numRows = mDatabase.delete(DislikedImagesColumns.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case FAVORITE:
                numRows = mDatabase.delete(FavoriteImagesColumns.TABLE_NAME, selection,
                        selectionArgs);
                break;
        }
        return numRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int numRows = 0;
        switch (URI_MATCHER.match(uri)) {
            case ADDED:
                numRows = mDatabase.update(AddedImagesColumns.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case DISLIKED:
                numRows = mDatabase.update(DislikedImagesColumns.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            case FAVORITE:
                numRows = mDatabase.update(FavoriteImagesColumns.TABLE_NAME, values,
                        selection, selectionArgs);
                break;
        }
        Log.d(TAG, "Updated rows: " + numRows);
        return numRows;
    }

    class CallSQLiteHelper extends SQLiteOpenHelper {

        public CallSQLiteHelper(Context context, String name,
                                SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createAddedTable(db);
            createDislikedTable(db);
            createFavoriteTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        void createAddedTable(SQLiteDatabase db) {
            db.execSQL(AddedImagesColumns.CREATE_TABLE);
        }

        void createDislikedTable(SQLiteDatabase db) {
            db.execSQL(DislikedImagesColumns.CREATE_TABLE);
        }

        void createFavoriteTable(SQLiteDatabase db) {
            db.execSQL(FavoriteImagesColumns.CREATE_TABLE);
        }

    }

    public static class AddedImagesColumns {

        public static String TABLE_NAME = "AddedImages";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                ClothingProvider.CONTENT_URI, TABLE_NAME);

        public static final String _ID = "_id", URI = "uri", CLOTH_TYPE = "type";

        public static final int TYPE_SHIRT = 1, TYPE_PANT = 2;

        public static final String[] DEFAULT_PROJECTION = new String[]{_ID, URI, CLOTH_TYPE};

        public static final String CREATE_TABLE = "create table " + TABLE_NAME
                + " (" + _ID + " integer primary key autoincrement"
                + ", " + URI + " text"
                + ", " + CLOTH_TYPE + " int"
                + ");";

        public static final String DEFAULT_SORT_ORDER = _ID  + " DESC";
    }

    public static class DislikedImagesColumns {

        public static String TABLE_NAME = "DislikedSet";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                ClothingProvider.CONTENT_URI, TABLE_NAME);

        public static final String _ID = "_id", URI_SHIRT = "uriShirt", URI_PANT = "uriPant";

        public static final int TYPE_SHIRT = 1, TYPE_PANT = 2;

        public static final String[] DEFAULT_PROJECTION = new String[]{_ID, URI_SHIRT, URI_PANT};

        public static final String CREATE_TABLE = "create table " + TABLE_NAME
                + " (" + _ID + " integer primary key autoincrement"
                + ", " + URI_SHIRT + " text"
                + ", " + URI_PANT + " text"
                + ");";

        public static final String DEFAULT_SORT_ORDER = _ID  + " DESC";
    }

    public static class FavoriteImagesColumns {

        public static String TABLE_NAME = "Favorite";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                ClothingProvider.CONTENT_URI, TABLE_NAME);

        public static final String _ID = "_id", URI_SHIRT = "uriShirt", URI_PANT = "uriPant";

        public static final int TYPE_SHIRT = 1, TYPE_PANT = 2;

        public static final String[] DEFAULT_PROJECTION = new String[]{_ID, URI_SHIRT, URI_PANT};

        public static final String CREATE_TABLE = "create table " + TABLE_NAME
                + " (" + _ID + " integer primary key autoincrement"
                + ", " + URI_SHIRT + " text"
                + ", " + URI_PANT + " text"
                + ");";

        public static final String DEFAULT_SORT_ORDER = _ID  + " DESC";
    }
}
