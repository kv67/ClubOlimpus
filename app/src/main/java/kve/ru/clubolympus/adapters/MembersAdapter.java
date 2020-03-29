package kve.ru.clubolympus.adapters;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kve.ru.clubolympus.R;

import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_FIRST_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_GENDER;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_GROUP_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.COLUMN_LAST_NAME;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.GENDER_FEMALE;
import static kve.ru.clubolympus.data.ClubOlympusContract.MemberEntry.GENDER_MALE;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder> {

  private Context context;
  private Cursor cursor;
  private OnMemberClickListener onMemberClickListener;

  public interface OnMemberClickListener {
    void onMemberClick(long id);
  }

  public MembersAdapter(Context context) {
    this.context = context;
  }

  public long getMemberId(int position) {
    if (cursor.moveToPosition(position)) {
      return cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
    }
    return 0;
  }

  public void setOnMemberClickListener(OnMemberClickListener onMemberClickListener) {
    this.onMemberClickListener = onMemberClickListener;
  }

  public void setCursor(Cursor cursor) {
    this.cursor = cursor;
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent,
        false);
    return new MembersViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull MembersViewHolder holder, int position) {
    if (cursor != null && cursor.moveToPosition(position)) {
      holder.textViewFirstName.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)));
      holder.textViewLastName.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)));
      holder.textViewGroup.setText(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GROUP_NAME)));
      int genderId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GENDER));
      switch (genderId) {
        case GENDER_MALE:
          holder.textViewGender.setText(context.getString(R.string.male));
          break;
        case GENDER_FEMALE:
          holder.textViewGender.setText(context.getString(R.string.female));
          break;
        default:
          holder.textViewGender.setText(context.getString(R.string.unknown_gender));
      }
    }
  }

  @Override
  public int getItemCount() {
    if (cursor != null) {
      return cursor.getCount();
    }

    return 0;
  }

  class MembersViewHolder extends RecyclerView.ViewHolder {

    private TextView textViewFirstName;
    private TextView textViewLastName;
    private TextView textViewGender;
    private TextView textViewGroup;

    public MembersViewHolder(@NonNull View itemView) {
      super(itemView);
      textViewFirstName = itemView.findViewById(R.id.textViewMmbFirstName);
      textViewLastName = itemView.findViewById(R.id.textViewMmbLastName);
      textViewGender = itemView.findViewById(R.id.textViewMmbGender);
      textViewGroup = itemView.findViewById(R.id.textViewMmbGroup);
      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (onMemberClickListener != null && cursor != null && cursor.moveToPosition(getAdapterPosition())) {
            onMemberClickListener.onMemberClick(cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)));
          }
        }
      });
    }
  }

}
