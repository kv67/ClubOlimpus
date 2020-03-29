package kve.ru.clubolympus;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_FIRST_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_GENDER;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_GROUP_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_LAST_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.CONTENT_URI;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.GENDER_FEMALE;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.GENDER_MALE;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.GENDER_UNKNOWN;

public class AddMemberActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

  private static final int EDIT_LOADER_ID = 333;
  private EditText editTextFirstName;
  private EditText editTextLastName;
  private EditText editTextGroup;
  private Spinner spinnerGender;
  // private List<String> spinnerList = new ArrayList<>();
  private Uri currentMemberUri;
  private int gender = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_member);

    editTextFirstName = findViewById(R.id.editTextFirstName);
    editTextLastName = findViewById(R.id.editTextLastName);
    editTextGroup = findViewById(R.id.editTextGroup);
    spinnerGender = findViewById(R.id.spinnerGender);

    Intent intent = getIntent();
    currentMemberUri = intent.getData();
    if (currentMemberUri == null) {
      setTitle(getString(R.string.add_member_title));
      invalidateOptionsMenu();
    } else {
      setTitle(getString(R.string.edit_member_title));
      LoaderManager.getInstance(this).restartLoader(EDIT_LOADER_ID, new Bundle(), this);
    }

//    spinnerList.add(getString(R.string.unknown_gender));
//    spinnerList.add(getString(R.string.male));
//    spinnerList.add(getString(R.string.female));
//    spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerList);

    ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_gender,
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
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    if (currentMemberUri == null) {
      menu.findItem(R.id.delete_member).setVisible(false);
    }
    return true;
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
        saveMember();
        return true;
      case R.id.delete_member:
        if (currentMemberUri != null) {
          showDeleteMemberDialog();
        }
        return true;
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        return true;
      default:
        break;
    }

    return super.onOptionsItemSelected(item);
  }

  private void saveMember() {
    ContentValues contentValues = new ContentValues();
    contentValues.put(COLUMN_FIRST_NAME, editTextFirstName.getText().toString().trim());
    contentValues.put(COLUMN_LAST_NAME, editTextLastName.getText().toString().trim());
    contentValues.put(COLUMN_GROUP_NAME, editTextGroup.getText().toString().trim());
    contentValues.put(COLUMN_GENDER, gender);

    ContentResolver contentResolver = getContentResolver();
    try {
      if (currentMemberUri == null) {
        Uri uri = contentResolver.insert(CONTENT_URI, contentValues);
        if (uri == null) {
          Toast.makeText(this, R.string.isertion_failed_msg, Toast.LENGTH_LONG).show();
        } else {
          Toast.makeText(this, R.string.data_saved_msg, Toast.LENGTH_LONG).show();
          finish();
        }
      } else {
        int updatedMember = contentResolver.update(currentMemberUri, contentValues, null, null);
        if (updatedMember > 0) {
          Toast.makeText(this, R.string.data_saved_msg, Toast.LENGTH_LONG).show();
          finish();
        } else {
          Toast.makeText(this, R.string.update_failed_msg, Toast.LENGTH_LONG).show();
        }
      }
    } catch (IllegalArgumentException e) {
      Toast.makeText(this, getString(R.string.error_msg) + e.getMessage(), Toast.LENGTH_LONG).show();
    }
  }

  @NonNull
  @Override
  public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
    String[] projection = {BaseColumns._ID, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_GENDER,
        COLUMN_GROUP_NAME};
    return new CursorLoader(this, currentMemberUri, projection, null, null,
        null);

  }

  @Override
  public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
    if (cursor.moveToFirst()) {
      editTextFirstName.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)));
      editTextLastName.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)));
      editTextGroup.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GROUP_NAME)));
      spinnerGender.setSelection(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GENDER)));
    }
  }

  @Override
  public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    // not used
  }

  private void showDeleteMemberDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(R.string.ask_for_delete_msg);
    builder.setPositiveButton(R.string.delete_dialog_btn, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        deleteMember();
      }
    });
    builder.setNegativeButton(R.string.cancel_dialog_btn, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if (dialog != null) {
          dialog.dismiss();
        }
      }
    });

    builder.create().show();
  }

  private void deleteMember() {
    try {
      int deletedMembers = getContentResolver().delete(currentMemberUri, null, null);
      if (deletedMembers > 0) {
        Toast.makeText(this, R.string.member_deleted_msg, Toast.LENGTH_LONG).show();
        finish();
      } else {
        Toast.makeText(this, R.string.delete_failed_msg, Toast.LENGTH_LONG).show();
      }
    } catch (IllegalArgumentException e) {
      Toast.makeText(this, getString(R.string.error_msg) + e.getMessage(), Toast.LENGTH_LONG).show();
    }
  }
}
