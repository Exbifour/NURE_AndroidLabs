package ua.nure.stanchyk.lab2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesAdapterViewHolder> {
    private List<Note> notesList;
    private ClickCallback callback;

    public NotesAdapter(ClickCallback callback) {
        notesList = new ArrayList<>();
        this.callback = callback;
    }

    @NonNull
    @Override
    public NotesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NotesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotesAdapterViewHolder holder, final int position) {
        final Note currentNote = notesList.get(position);

        String color = currentNote.isSelected() ? "#ffd8cc" : "#ffffff";
        holder.cvNote.setBackgroundColor(Color.parseColor(color));

        Date date = currentNote.getDate();
        String text = currentNote.getText();
        String title = currentNote.getTitle();
        String photoPath = currentNote.getPhotoPath();
     //   int importance = currentNote.getImportance(); todo

        if (date != null) {
            holder.dateTextView.setText(DatabaseHelper.dateFormat.format(currentNote.getDate()));
        }
        //todo
     //   if (importance != 0) {
     //       Context context = holder.importancePreviewTextView.getContext();
     //       holder.importancePreviewTextView.setText(context.getString(R.string.importance, currentNote.getImportance()));
    //    }
        if (photoPath != null) {
            holder.photoPreviewImageView.setImageBitmap(BitmapUtils.loadFromFile(photoPath));
        }
        if (title != null) {
            holder.titlePreviewTextView.setText(currentNote.getTitle());
        }
        if (text != null) {
            holder.textPreviewTextView.setText(currentNote.getText());
        }

        holder.cvNote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (currentNote.isSelected()) {
                    currentNote.setSelected(false);

                } else {
                    currentNote.setSelected(true);
                }
                callback.onLongClick(currentNote);

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void setItems(List<Note> notes) {
        notesList.clear();
        notesList.addAll(notes);
        notifyDataSetChanged();
    }

    class NotesAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView textPreviewTextView;
     //   private TextView importancePreviewTextView;
        private TextView dateTextView;
        private TextView titlePreviewTextView;
        private ImageView photoPreviewImageView;
        private CardView cvNote;

        NotesAdapterViewHolder(View itemView) {
            super(itemView);
            textPreviewTextView = itemView.findViewById(R.id.tvTextPreview);
            cvNote = itemView.findViewById(R.id.cvNote);
          //  importancePreviewTextView = itemView.findViewById(R.id.tvImportancePreview);
            dateTextView = itemView.findViewById(R.id.tvDatePreview);
            titlePreviewTextView = itemView.findViewById(R.id.tvTitlePreview);
            photoPreviewImageView = itemView.findViewById(R.id.ivImagePreview);
        }
    }

    public interface ClickCallback {
        void onLongClick(Note note);
    }
}

