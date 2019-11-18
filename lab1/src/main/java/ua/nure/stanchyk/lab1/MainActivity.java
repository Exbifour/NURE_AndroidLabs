package ua.nure.stanchyk.lab1;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesAdapter.ClickCallback {
    private NotesAdapter notesAdapter;
    private FileManager fm;
    private Menu menu;
    private Gson gson;
    private boolean isSelected;

    private Note selectedNote;
    private List<Note> notes;

    private FilterParameters filterParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        fm = FileManager.getInstance();
        RecyclerView notesRecyclerView = findViewById(R.id.recyclerViewNotes);
        notesAdapter = new NotesAdapter(this);
        gson = new Gson();
        fm.readFile(this);
        filterParameters = new FilterParameters(0, null, null);

        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerView.setAdapter(notesAdapter);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("selectedNote")) {
                isSelected = true;
                selectedNote = gson.fromJson(savedInstanceState.getString("selectedNote"), Note.class);
                selectedNote.setSelected(true);
                fm.updateNote(selectedNote, this);
            }
            if (savedInstanceState.containsKey("filter")) {
                filterParameters = gson.fromJson(savedInstanceState.getString("filter"), FilterParameters.class);
            }
        }

        notesAdapter.setItems(fm.getNotesList(filterParameters));
        notesAdapter.notifyDataSetChanged();
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerView.setAdapter(notesAdapter);
    }

    @Override
    protected void onResume() {
        notesAdapter.setItems(fm.getNotesList(filterParameters));
        notesAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem delete = menu.findItem(R.id.action_delete);
        delete.setVisible(false);
        MenuItem edit = menu.findItem(R.id.action_edit);
        edit.setVisible(false);
        if (isSelected) {
            setButtonsSelected(true);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, OneNoteActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_filter:
                filter();
                return true;
            case R.id.action_edit:
                selectedNote.setSelected(false);
                isSelected = false;
                setButtonsSelected(false);
                filterParameters = new FilterParameters(0, null, null);
                Intent intentEdt = new Intent(this, OneNoteActivity.class);
                intentEdt.putExtra("note", gson.toJson(selectedNote));
                startActivity(intentEdt);
                return true;
            case R.id.action_delete:
                selectedNote.setSelected(false);
                fm.deleteNote(selectedNote, this);
                notesAdapter.setItems(fm.getNotesList(filterParameters));
                notesAdapter.notifyDataSetChanged();
                setButtonsSelected(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (isSelected) {
            outState.putString("selectedNote", gson.toJson(selectedNote));
        }
        if (filterParameters.getFilteredText() != null
                || filterParameters.getFilteredDate() != null
                || filterParameters.getFilteredImportance() != 0) {
            outState.putString("filter", gson.toJson(filterParameters));
        }
        super.onSaveInstanceState(outState);
    }

    private void filter() {
        CharSequence[] options = new CharSequence[]{getString(R.string.text_choice), getString(R.string.importance_choice),
                getString(R.string.date_choice)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.filter_by));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                if (position == 0) {
                    dialogFilterByText();
                } else if (position == 1) {
                    dialogFilterByImportance();
                } else if (position == 2) {
                    dialogFilterByDate();
                }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    private void dialogFilterByText() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.filter_by_text));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filterParameters.setFilteredText(input.getText().toString().toLowerCase());
                notesAdapter.setItems(fm.getNotesList(filterParameters));
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void dialogFilterByDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        filterParameters.setFilteredDate(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                        notesAdapter.setItems(fm.getNotesList(filterParameters));
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void dialogFilterByImportance() {
        CharSequence[] options = new CharSequence[]{"1", "2", "3"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.filter_by_importance));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                if (position == 0) {
                    filterParameters.setFilteredImportance(1);
                    notesAdapter.setItems(fm.getNotesList(filterParameters));
                } else if (position == 1) {
                    filterParameters.setFilteredImportance(2);
                    notesAdapter.setItems(fm.getNotesList(filterParameters));
                } else if (position == 2) {
                    filterParameters.setFilteredImportance(3);
                    notesAdapter.setItems(fm.getNotesList(filterParameters));
                }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onLongClick(Note note) {
        fm.updateNote(note, this);
        notesAdapter.notifyDataSetChanged();

        isSelected = note.isSelected();
        setButtonsSelected(isSelected);

        if (isSelected) {
            selectedNote = note;
        } else {
            selectedNote = null;
        }
    }

    @Override
    public void onBackPressed() {
        isSelected = false;
        setButtonsSelected(isSelected);
        if (selectedNote != null) {
            selectedNote.setSelected(false);
            fm.updateNote(selectedNote, this);
        }

        filterParameters = new FilterParameters(0, null, null);
        notes = fm.getNotesList(filterParameters);
        notesAdapter.setItems(notes);
        notesAdapter.notifyDataSetChanged();

    }

    public void setButtonsSelected(boolean isSelected) {
        MenuItem add = menu.findItem(R.id.action_add);
        MenuItem filter = menu.findItem(R.id.action_filter);
        MenuItem delete = menu.findItem(R.id.action_delete);
        MenuItem edit = menu.findItem(R.id.action_edit);
        add.setVisible(!isSelected);
        filter.setVisible(!isSelected);
        delete.setVisible(isSelected);
        edit.setVisible(isSelected);
    }
}