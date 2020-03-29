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
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_FIRST_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_GENDER;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_GROUP_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_LAST_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.CONTENT_MULTIPLE_ITEMS;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.CONTENT_SINGLE_ITEM;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.GENDER_FEMALE;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.GENDER_MALE;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.GENDER_UNKNOWN;
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

    cursor.setNotificationUri(getContext().getContentResolver(), uri);
    return cursor;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    if (values.getAsString(COLUMN_FIRST_NAME) == null || values.getAsString(COLUMN_FIRST_NAME).isEmpty()) {
      throw new IllegalArgumentException("First name is expected");
    }

    if (values.getAsString(COLUMN_LAST_NAME) == null || values.getAsString(COLUMN_LAST_NAME).isEmpty()) {
      throw new IllegalArgumentException("Last name is expected");
    }

    if (values.getAsInteger(COLUMN_GENDER) == null && !(values.getAsInteger(COLUMN_GENDER) == GENDER_UNKNOWN || values.getAsInteger(COLUMN_GENDER) == GENDER_MALE || values.getAsInteger(COLUMN_GENDER) == GENDER_FEMALE)) {
      throw new IllegalArgumentException("Gender is expected");
    }

    if (values.getAsString(COLUMN_GROUP_NAME) == null || values.getAsString(COLUMN_GROUP_NAME).isEmpty()) {
      throw new IllegalArgumentException("Group name is expected");
    }

    SQLiteDatabase db = dbHelper.getWritableDatabase();
    int match = uriMatcher.match(uri);
    switch (match) {
      case TABLE_CODE:
        long id = db.insert(TABLE_NAME, null, values);
        if (id == -1) {
          Log.e("insertMethod", "Data insertion failed");
          return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
      default:
        throw new IllegalArgumentException("Incorrect URI: " + uri);
    }
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    int match = uriMatcher.match(uri);
    int rowsDeleted = 0;
    switch (match) {
      case TABLE_CODE:
        rowsDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
        break;
      case RECORD_CODE:
        selection = BaseColumns._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        rowsDeleted = db.delete(TABLE_NAME, selection, selectionArgs);
        break;
      default:
        throw new IllegalArgumentException("Incorrect URI: " + uri);
    }

    if (rowsDeleted > 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return rowsDeleted;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    if (values.containsKey(COLUMN_FIRST_NAME) && (values.getAsString(COLUMN_FIRST_NAME) == null) || values.getAsString(COLUMN_FIRST_NAME).isEmpty()) {
      throw new IllegalArgumentException("First name is expected");
    }

    if (values.containsKey(COLUMN_LAST_NAME) && (values.getAsString(COLUMN_LAST_NAME) == null || values.getAsString(COLUMN_LAST_NAME).isEmpty())) {
      throw new IllegalArgumentException("Last name is expected");
    }

    if (values.containsKey(COLUMN_GENDER) && values.getAsInteger(COLUMN_GENDER) == null && !(values.getAsInteger(COLUMN_GENDER) == GENDER_UNKNOWN || values.getAsInteger(COLUMN_GENDER) == GENDER_MALE || values.getAsInteger(COLUMN_GENDER) == GENDER_FEMALE)) {
      throw new IllegalArgumentException("Gender is expected");
    }

    if (values.containsKey(COLUMN_GROUP_NAME) && (values.getAsString(COLUMN_GROUP_NAME) == null || values.getAsString(COLUMN_GROUP_NAME).isEmpty())) {
      throw new IllegalArgumentException("Group name is expected");
    }

    SQLiteDatabase db = dbHelper.getWritableDatabase();
    int match = uriMatcher.match(uri);
    int rowsUpdated = 0;
    switch (match) {
      case TABLE_CODE:
        rowsUpdated = db.update(TABLE_NAME, values, selection, selectionArgs);
        break;
      case RECORD_CODE:
        selection = BaseColumns._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        rowsUpdated = db.update(TABLE_NAME, values, selection, selectionArgs);
        break;
      default:
        throw new IllegalArgumentException("Incorrect URI: " + uri);
    }
    if (rowsUpdated > 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return rowsUpdated;
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
