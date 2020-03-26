package kve.ru.clubolympus.data;

import android.content.ContentResolver;
import android.net.Uri;

public final class ClubOlympusContract {

  private ClubOlympusContract() {
    throw new IllegalStateException("Utility class");
  }

  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "olympus";

  public static final String SCHEME = "content://";
  public static final String AUTHORITY = "kve.ru.clubolympus";
  public static final String PATH_MEMBERS = "members";

  public static final Uri BASE_CONTENT_URI = Uri.EMPTY.parse(SCHEME + AUTHORITY);


  public static final class MemberEntry {

    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEMBERS);
    public static final String CONTENT_MULTIPLE_ITEMS = ContentResolver.CURSOR_DIR_BASE_TYPE +
        "/" + AUTHORITY + "/" + PATH_MEMBERS;
    public static final String CONTENT_SINGLE_ITEM =
        ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MEMBERS;

    public static final String TABLE_NAME = "members";

    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_GROUP_NAME = "group_name";

    public static final int GENDER_UNKNOWN = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

  }
}
