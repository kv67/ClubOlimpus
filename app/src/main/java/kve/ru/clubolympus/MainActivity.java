package kve.ru.clubolympus;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import kve.ru.clubolympus.adapters.MembersAdapter;

import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_FIRST_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_GENDER;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_GROUP_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_LAST_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

  private static final int LOADER_ID = 123;
  private RecyclerView recyclerViewMembers;
  private MembersAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    recyclerViewMembers = findViewById(R.id.recyclerViewMembers);
    recyclerViewMembers.setLayoutManager(new LinearLayoutManager(this));
    adapter = new MembersAdapter(this);
    adapter.setOnMemberClickListener(new MembersAdapter.OnMemberClickListener() {
      @Override
      public void onMemberClick(long id) {
        Intent intent = new Intent(MainActivity.this, AddMemberActivity.class);
        Uri currentMemberUri = ContentUris.withAppendedId(CONTENT_URI, id);
        intent.setData(currentMemberUri);
        startActivity(intent);
      }
    });
    recyclerViewMembers.setAdapter(adapter);

    FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
    floatingActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, AddMemberActivity.class);
        startActivity(intent);
        // recyclerViewMembers.scrollToPosition(adapter.getItemCount() - 1);  // сдвиг на
        // последнюю позицию
      }
    });

    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

      @Override
      public boolean onMove(@NonNull RecyclerView recyclerView,
          @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
      }

      @Override
      public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        showDeleteMemberDialog(viewHolder.getAdapterPosition());
      }
    });
    itemTouchHelper.attachToRecyclerView(recyclerViewMembers);

    LoaderManager.getInstance(this).restartLoader(LOADER_ID, new Bundle(), this);
  }

  private void deleteMember(int position) {
    long id = adapter.getMemberId(position);
    Uri currentMemberUri = ContentUris.withAppendedId(CONTENT_URI, id);
    try {
      int deletedMembers = getContentResolver().delete(currentMemberUri, null, null);
      if (deletedMembers > 0) {
        Toast.makeText(this, R.string.member_deleted_msg, Toast.LENGTH_LONG).show();
      } else {
        Toast.makeText(this, R.string.delete_failed_msg, Toast.LENGTH_LONG).show();
      }
    } catch (IllegalArgumentException e) {
      Toast.makeText(this, getString(R.string.error_msg) + e.getMessage(), Toast.LENGTH_LONG).show();
    }
  }

  private void showDeleteMemberDialog(int position) {
    final int pos = position;
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(R.string.ask_for_delete_msg);
    builder.setPositiveButton(R.string.delete_dialog_btn, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        deleteMember(pos);
      }
    });

    builder.setNegativeButton(R.string.cancel_dialog_btn, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if (dialog != null) {
          dialog.dismiss();
        }
        adapter.notifyDataSetChanged();
      }
    });

    builder.create().show();
  }

//  @Override
//  protected void onStart() {
//    super.onStart();
//    // displayData();
//  }

  private void displayData() {
//    String[] projection = {BaseColumns._ID, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_GENDER,
//        COLUMN_GROUP_NAME};
//    Cursor cursor = getContentResolver().query(CONTENT_URI, projection, null, null, null);
//    //  MembersCursorAdapter adapter = new MembersCursorAdapter(this, cursor);
//    MembersAdapter adapter = new MembersAdapter(this, cursor);
//    recyclerViewMembers.setAdapter(adapter);
//    adapter.notifyDataSetChanged();
  }

  @NonNull
  @Override
  public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
    String[] projection = {BaseColumns._ID, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_GENDER,
        COLUMN_GROUP_NAME};
    CursorLoader cursorLoader = new CursorLoader(this, CONTENT_URI, projection, null, null, null);

    return cursorLoader;
  }

  @Override
  public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
    adapter.setCursor(data);
  }

  @Override
  public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    // not used
  }
}
