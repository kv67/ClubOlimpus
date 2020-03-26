package kve.ru.clubolympus;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_FIRST_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_GENDER;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_GROUP_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_LAST_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.CONTENT_URI;

public class MainActivity extends AppCompatActivity {

  private TextView textViewData;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    textViewData = findViewById(R.id.textViewData);

    FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
    floatingActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, AddMemberActivity.class);
        startActivity(intent);
      }
    });
  }

  @Override
  protected void onStart() {
    super.onStart();
    displayData();
  }

  private void displayData() {
    String[] projection = {BaseColumns._ID, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_GENDER,
        COLUMN_GROUP_NAME};
    Cursor cursor = getContentResolver().query(CONTENT_URI, projection, null, null, null);

    textViewData.setText(getString(R.string.all_members) + "\n\n");
    while (cursor.moveToNext()) {
      String str =
          cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)) + " " + cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)) + " " + cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)) + " " + cursor.getInt(cursor.getColumnIndex(COLUMN_GENDER)) + " " + cursor.getString(cursor.getColumnIndex(COLUMN_GROUP_NAME)) + "\n";
      textViewData.append(str);
    }
    cursor.close();
  }
}
