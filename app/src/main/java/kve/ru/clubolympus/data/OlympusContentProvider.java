package kve.ru.clubolympus.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import static kve.ru.clubolympus.data.ClubOlympusContract.AUTHORITY;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.CONTENT_MULTIPLE_ITEMS;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.CONTENT_SINGLE_ITEM;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.TABLE_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.PATH_MEMBERS;

public class OlympusContentProvider extends ContentProvider {


  private static final int TABLE_CODE = 111;
  private static final int RECORD_CODE = 222;

  private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

  static {
    uriMatcher.addURI(AUTHORITY, PATH_MEMBERS, TABLE_CODE);
    uriMatcher.addURI(AUTHORITY, PATH_MEMBERS + "/#", RECORD_CODE);
  }

  private OlympusDbOpenHelper dbHelper;


  @Override
  public boolean onCreate() {
    dbHelper = new OlympusDbOpenHelper(getContext());
    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
      String sortOrder) {

    SQLiteDatabase db = dbHelper.getReadableDatabase();
    Cursor cursor;
    int match = uriMatcher.match(uri);
    switch (match) {
      case TABLE_CODE:
        cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        break;
      case RECORD_CODE:
        selection = BaseColumns._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        break;
      default:
        throw new IllegalArgumentException("Incorrect URI: " + uri);
    }

    return cursor;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    int match = uriMatcher.match(uri);
    switch (match) {
      case TABLE_CODE:
        long id = db.insert(TABLE_NAME, null, values);
        if (id == -1) {
          Log.e("insertMethod", "Data insertion failed");
          return null;
        }
        return ContentUris.withAppendedId(uri, id);
      default:
        throw new IllegalArgumentException("Incorrect URI: " + uri);
    }
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    int match = uriMatcher.match(uri);
    switch (match) {
      case TABLE_CODE:
        return db.delete(TABLE_NAME, selection, selectionArgs);
      case RECORD_CODE:
        selection = BaseColumns._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        return db.delete(TABLE_NAME, selection, selectionArgs);
      default:
        throw new IllegalArgumentException("Incorrect URI: " + uri);
    }
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    int match = uriMatcher.match(uri);
    switch (match) {
      case TABLE_CODE:
        return db.update(TABLE_NAME, values, selection, selectionArgs);
      case RECORD_CODE:
        selection = BaseColumns._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        return db.update(TABLE_NAME, values, selection, selectionArgs);
      default:
        throw new IllegalArgumentException("Incorrect URI: " + uri);
    }
  }

  @Override
  public String getType(Uri uri) {
    int match = uriMatcher.match(uri);
    switch (match) {
      case TABLE_CODE:
        return CONTENT_MULTIPLE_ITEMS;
      case RECORD_CODE:
        return CONTENT_SINGLE_ITEM;
      default:
        throw new IllegalArgumentException("Unknown URI: " + uri);
    }
  }
}