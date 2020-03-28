package kve.ru.clubolympus;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import kve.ru.clubolympus.adapters.MembersAdapter;

import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_FIRST_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_GENDER;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_GROUP_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_LAST_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.CONTENT_URI;

public class MainActivity extends AppCompatActivity {

  private RecyclerView recyclerViewMembers;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    recyclerViewMembers = findViewById(R.id.recyclerViewMembers);
    recyclerViewMembers.setLayoutManager(new LinearLayoutManager(this));

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

    //  MembersCursorAdapter adapter = new MembersCursorAdapter(this, cursor);

    MembersAdapter adapter = new MembersAdapter(this, cursor);
    recyclerViewMembers.setAdapter(adapter);
    adapter.notifyDataSetChanged();
  }
}
