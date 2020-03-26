package kve.ru.clubolympus.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static kve.ru.clubolympus.data.ClubOlympusContract.DATABASE_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.DATABASE_VERSION;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_FIRST_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_GENDER;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_GROUP_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_LAST_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.TABLE_NAME;


public class OlympusDbOpenHelper extends SQLiteOpenHelper {

  public OlympusDbOpenHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY, " + COLUMN_FIRST_NAME + " TEXT, " + COLUMN_LAST_NAME + " TEXT, " + COLUMN_GENDER + " INTEGER NOT NULL, " + COLUMN_GROUP_NAME + " TEXT)");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    onCreate(db);
  }
}
