package ua.nure.stanchyk.lab2;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
    NotesAdapter notesAdapter;
    private Menu menu;
    private Gson gson;
    private boolean isSelected;
    private Note selectedNote;
    private FilterParameters filterParameters;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = ApplicationLab2.getInstance().getDatabaseHelper();
        RecyclerView notesRecyclerView = findViewById(R.id.recyclerViewNotes);
        notesAdapter = new NotesAdapter(this);
        gson = new Gson();
        filterParameters = new FilterParameters(0, null, null);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerView.setAdapter(notesAdapter);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("selectedNote")) {
                isSelected = true;
                selectedNote = gson.fromJson(savedInstanceState.getString("selectedNote"), Note.class);
                selectedNote.setSelected(true);
                dbHelper.updateNote(selectedNote);
            }
            if (savedInstanceState.containsKey("filter")) {
                filterParameters = gson.fromJson(savedInstanceState.getString("filter"), FilterParameters.class);
            }
        }

        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerView.setAdapter(notesAdapter);

        GetNotesTask getNotesTask = new GetNotesTask();
        getNotesTask.execute(filterParameters);
    }

    @Override
    protected void onResume() {
        GetNotesTask getNotesTask = new GetNotesTask();
        getNotesTask.execute(filterParameters);
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
                dbHelper.deleteNote(selectedNote.getId());
                GetNotesTask getNotesTask = new GetNotesTask();
                getNotesTask.execute(filterParameters);
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
              /* todo  || filterParameters.getFilteredImportance() != 0*/) {
            outState.putString("filter", gson.toJson(filterParameters));
        }
        super.onSaveInstanceState(outState);

    }

    private void filter() {
        CharSequence[] options = new CharSequence[]{getString(R.string.text_choice),
                getString(R.string.date_choice)};
       // CharSequence[] options = new CharSequence[]{getString(R.string.text_choice), getString(R.string.date_choice), getString(R.string.importance_choice)}; TODO
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.filter_by));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                if (position == 0) {
                    dialogFilterByText();
                } else if (position == 1) {
                    dialogFilterByDate();
                } else if (position == 2) {
                  //  dialogFilterByImportance(); TODO
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
                GetNotesTask getNotesTask = new GetNotesTask();
                getNotesTask.execute(filterParameters);
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
                        if (dayOfMonth < 10) {
                            filterParameters.setFilteredDate("0" + dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                        } else {
                            filterParameters.setFilteredDate(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                        }
                        GetNotesTask getNotesTask = new GetNotesTask();
                        getNotesTask.execute(filterParameters);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    /* TODO
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
                } else if (position == 1) {
                    filterParameters.setFilteredImportance(2);
                } else if (position == 2) {
                    filterParameters.setFilteredImportance(3);
                }
                GetNotesTask getNotesTask = new GetNotesTask();
                getNotesTask.execute(filterParameters);
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
    */

    @Override
    public void onLongClick(Note selectedNote) {
        dbHelper.updateNote(selectedNote);
        notesAdapter.notifyDataSetChanged();

        isSelected = selectedNote.isSelected();
        setButtonsSelected(isSelected);

        if (isSelected) {
            this.selectedNote = selectedNote;
        } else {
            this.selectedNote = null;
        }
    }

    @Override
    public void onBackPressed() {
        isSelected = false;
        setButtonsSelected(isSelected);
        if (selectedNote != null) {
            selectedNote.setSelected(false);
            dbHelper.updateNote(selectedNote);
        }

        filterParameters = new FilterParameters(0, null, null);
        GetNotesTask getNotesTask = new GetNotesTask();
        getNotesTask.execute(filterParameters);

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

    private class GetNotesTask extends AsyncTask<FilterParameters, Object, List<Note>> {
        DatabaseHelper dbHelper = ApplicationLab2.getInstance().getDatabaseHelper();

        @Override
        protected List<Note> doInBackground(FilterParameters... params) {
            FilterParameters filterParameters = params[0];
            return dbHelper.getAllNotes(filterParameters);
        }

        @Override
        protected void onPostExecute(List<Note> result) {
            notesAdapter.setItems(result);
            notesAdapter.notifyDataSetChanged();
        }
    }

}
