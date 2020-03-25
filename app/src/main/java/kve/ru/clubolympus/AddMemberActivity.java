package kve.ru.clubolympus;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

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
            gender = 1;
          } else
            if (genderStr.equals(getResources().getString(R.string.female))) {
              gender = 2;
            } else {
              gender = 0;
            }
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        gender = 0;
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
        return true;
      case R.id.delete_member:
        return true;
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
