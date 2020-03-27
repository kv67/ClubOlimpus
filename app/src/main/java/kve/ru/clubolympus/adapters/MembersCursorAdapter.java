package kve.ru.clubolympus.adapters;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import kve.ru.clubolympus.R;

import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_FIRST_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_GENDER;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_GROUP_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_LAST_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.GENDER_FEMALE;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.GENDER_MALE;

public class MembersCursorAdapter extends CursorAdapter {


  public MembersCursorAdapter(Context context, Cursor c) {
    super(context, c, 0);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return LayoutInflater.from(context).inflate(R.layout.members_item, parent, false);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    TextView firstName = view.findViewById(R.id.textViewFirstName);
    TextView lastName = view.findViewById(R.id.textViewLastName);
    TextView gender = view.findViewById(R.id.textViewGender);
    TextView group = view.findViewById(R.id.textViewGroup);

    firstName.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)));
    lastName.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)));
    group.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GROUP_NAME)));
    int genderId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GENDER));
    switch (genderId) {
      case GENDER_MALE:
        gender.setText(context.getString(R.string.male));
        break;
      case GENDER_FEMALE:
        gender.setText(context.getString(R.string.female));
        break;
      default:
        gender.setText(context.getString(R.string.unknown_gender));
    }
  }
}
