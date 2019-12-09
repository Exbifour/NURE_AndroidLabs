package ua.nure.stanchyk.lab2;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NotesNURE";

    //todo 1
    private static final int DATABASE_VERSION = 1;
    //todo 2
    //private static final int DATABASE_VERSION = 2;
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private SQLiteDatabase database;

    public final static String NOTES_TABLE = "Notes";

    public final static String NOTE_ID = "id";
    public final static String NOTE_TITLE = "title";
    public final static String NOTE_TEXT = "note_text";
    public final static String NOTE_DATE = "note_date";
    //todo 2
    //public final static String NOTE_IMPORTANCE = "importance";
    public final static String NOTE_PHOTO_PATH = "photo_path";
    public final static String NOTE_IS_SELECTED = "is_selected";


    private static final String DATABASE_CREATE =
            "create table " + NOTES_TABLE + " ( " + NOTE_ID + " text primary key,"
                    + NOTE_TITLE + " text not null, " + NOTE_TEXT + " text not null,"
                    + NOTE_DATE + " text not null, " +
                    //todo 2
                    //NOTE_IMPORTANCE + " integer, "+
                    NOTE_PHOTO_PATH + " text, " + NOTE_IS_SELECTED + " integer );";


    private static final String DATABASE_CREATE2 =
            "create table " + NOTES_TABLE + " ( " + NOTE_ID + " text primary key,"
                    + NOTE_TITLE + " text++ not null, " + NOTE_TEXT + " text not null,"
                    + NOTE_DATE + " text not null, " +
                    //todo 2
                    //NOTE_IMPORTANCE + " integer, "+
                    NOTE_PHOTO_PATH + " text, " + NOTE_IS_SELECTED + " integer );";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        try {
            database = getWritableDatabase();
        }
        catch (SQLiteException ex){
            database = getReadableDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d("db_lab", "onCreate");
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        if(oldVersion == 1 ){

            Log.d("db_lab", "onUpdate old");
            database.execSQL(DATABASE_CREATE);
         }
        else {
             Log.d("db_lab", "onUpdate new");
            database.execSQL(DATABASE_CREATE2);
        }
    }


    public List<Note> getAllNotes(FilterParameters filterParameters) {
        List<Note> noteList = new ArrayList<>();

        String selectQuery = "";
        boolean filterByText = filterParameters.getFilteredText() != null;
        boolean filterByDate = filterParameters.getFilteredDate() != null;
     //   boolean filterByImportance = filterParameters.getFilteredImportance() > 0; //TODO
        Cursor cursor;
        if (!filterByText && !filterByDate /* && !filterByImportance*/) {  //TODO
            selectQuery = "SELECT  * FROM " + NOTES_TABLE;
            cursor = database.rawQuery(selectQuery, null);
        } else {
            if (filterByText) {
                selectQuery = "SELECT  * FROM " + NOTES_TABLE + " WHERE " + NOTE_TEXT + " LIKE ? OR " +
                        NOTE_TITLE + " LIKE ?";
                cursor = database.rawQuery(selectQuery, new String[]{"%"+filterParameters.getFilteredText()+"%",
                        "%"+filterParameters.getFilteredText()+"%"});

            } else /* if (filterByDate)*/ { //TODO
                selectQuery = "SELECT  * FROM " + NOTES_TABLE + " WHERE " + NOTE_DATE + " LIKE ?";
                cursor = database.rawQuery(selectQuery, new String[]{"%"+filterParameters.getFilteredDate()+"%"});

            }
            // todo 2
            // else {
               // selectQuery = "SELECT  * FROM " + NOTES_TABLE + " WHERE " + NOTE_IMPORTANCE + " = ?";
               // cursor = database.rawQuery(selectQuery, new String[]{String.valueOf(filterParameters.getFilteredImportance())});

           // }
        }

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getString(0));
                note.setTitle(cursor.getString(1));
                note.setText(cursor.getString(2));
                try {
                    note.setDate(dateFormat.parse((cursor.getString(3))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //todo 2
                //note.setImportance(Integer.parseInt(cursor.getString(4)));
                // note.setPhotoPath(cursor.getString(5));
                // int isSelected = Integer.parseInt(cursor.getString(6));

                //todo
                note.setPhotoPath(cursor.getString(4));
                int isSelected = Integer.parseInt(cursor.getString(5));
                //
                if (isSelected == 1) {
                    note.setSelected(true);
                } else {
                    note.setSelected(false);
                }
                noteList.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return noteList;
    }

    public void addNote(Note note) {
        ContentValues values = new ContentValues();
        values.put(NOTE_ID, note.getId());
        values.put(NOTE_TITLE, note.getTitle());
        values.put(NOTE_TEXT, note.getText());
        values.put(NOTE_DATE, dateFormat.format(note.getDate()));
        // todo 2
        //values.put(NOTE_IMPORTANCE, note.getImportance());
        values.put(NOTE_PHOTO_PATH, note.getPhotoPath());
        if (note.isSelected()) {
            values.put(NOTE_IS_SELECTED, 1);
        } else {
            values.put(NOTE_IS_SELECTED, 0);
        }
        database.insert(NOTES_TABLE, null, values);
    }


    public void deleteNote(String id) {
        database.delete(NOTES_TABLE, NOTE_ID + " = ?",
                new String[]{id});
    }

    public void updateNote(Note note) {
        ContentValues values = new ContentValues();
        values.put(NOTE_ID, note.getId());
        values.put(NOTE_TITLE, note.getTitle());
        values.put(NOTE_TEXT, note.getText());
        values.put(NOTE_DATE, dateFormat.format(note.getDate()));
        //todo 2
        //values.put(NOTE_IMPORTANCE, note.getImportance());
        values.put(NOTE_PHOTO_PATH, note.getPhotoPath());
        if (note.isSelected()) {
            values.put(NOTE_IS_SELECTED, 1);
        } else {
            values.put(NOTE_IS_SELECTED, 0);
        }
        database.update(NOTES_TABLE, values, NOTE_ID + " = ?",
                new String[]{note.getId()});
    }


}