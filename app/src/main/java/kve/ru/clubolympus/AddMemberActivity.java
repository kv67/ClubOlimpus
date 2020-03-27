package kve.ru.clubolympus;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_FIRST_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_GENDER;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_GROUP_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_LAST_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.CONTENT_URI;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.GENDER_FEMALE;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.GENDER_MALE;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.GENDER_UNKNOWN;

public class AddMemberActivity extends AppCompatActivity {

  private EditText editTextFirstName;
  private EditText editTextLastName;
  private EditText editTextGroup;
  private Spinner spinnerGender;
  private ArrayAdapter spinnerAdapter;
  // private List<String> spinnerList = new ArrayList<>();

  private int gender = 0;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_member);

    editTextFirstName = findViewById(R.id.editTextFirstName);
    editTextLastName = findViewById(R.id.editTextLastName);
    editTextGroup = findViewById(R.id.editTextGroup);
    spinnerGender = findViewById(R.id.spinnerGender);

//    spinnerList.add(getString(R.string.unknown_gender));
//    spinnerList.add(getString(R.string.male));
//    spinnerList.add(getString(R.string.female));
//    spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerList);

    spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_gender,
        android.R.layout.simple_spinner_item);
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerGender.setAdapter(spinnerAdapter);
    spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        gender = position;
        String genderStr = (String) parent.getItemAtPosition(position);
        if (!genderStr.isEmpty()) {
          if (genderStr.equals(getResources().getString(R.string.male))) {
            gender = GENDER_MALE;
          } else
            if (genderStr.equals(getResources().getString(R.string.female))) {
              gender = GENDER_FEMALE;
            } else {
              gender = GENDER_UNKNOWN;
            }
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        gender = GENDER_UNKNOWN;
      }
    });

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.edit_member_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.save_member:
        insertMember();
        return true;
      case R.id.delete_member:
        return true;
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void insertMember() {
    ContentValues contentValues = new ContentValues();
    contentValues.put(COLUMN_FIRST_NAME, editTextFirstName.getText().toString().trim());
    contentValues.put(COLUMN_LAST_NAME, editTextLastName.getText().toString().trim());
    contentValues.put(COLUMN_GROUP_NAME, editTextGroup.getText().toString().trim());
    contentValues.put(COLUMN_GENDER, gender);

    ContentResolver contentResolver = getContentResolver();
    try {
      Uri uri = contentResolver.insert(CONTENT_URI, contentValues);
      if (uri == null) {
        Toast.makeText(this, R.string.isertion_failed_msg, Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(this, R.string.data_saved_msg, Toast.LENGTH_LONG).show();
      }
    } catch (IllegalArgumentException e) {
      Toast.makeText(this, getString(R.string.error_msg) + e.getMessage(), Toast.LENGTH_LONG).show();
    }
  }

}
