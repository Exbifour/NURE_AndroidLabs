package ua.nure.stanchyk.lab1;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FileManager {
    private Gson gson = new Gson();
    private Type listType = new TypeToken<ArrayList<Note>>(){}.getType();
    private List<Note> notesList = new ArrayList<>();
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private static FileManager single_instance = null;

    public static FileManager getInstance() {
        if (single_instance == null)
            single_instance = new FileManager();
        return single_instance;
    }

    public List<Note> getNotesList(FilterParameters filterParameters) {
        boolean filterByText = filterParameters.getFilteredText() != null;
        boolean filterByDate = filterParameters.getFilteredDate() != null;
        boolean filterByImportance = filterParameters.getFilteredImportance() > 0;

        if (!filterByText && !filterByDate && !filterByImportance) {
            return notesList;
        } else {
            List<Note> resultList = new ArrayList<>();
            for (Note n : notesList) {
                boolean addNote = true;

                if (filterByText && !(n.getTitle().toLowerCase().contains(filterParameters.getFilteredText()) ||
                        n.getText().toLowerCase().contains(filterParameters.getFilteredText()))) {
                    addNote = false;

                }
                if (addNote && filterByImportance && n.getImportance() != filterParameters.getFilteredImportance()) {
                    addNote = false;
                }
                if (addNote && filterByDate && !dateFormat.format(n.getDate()).contains(filterParameters.getFilteredDate())) {
                    addNote = false;
                }
                if (addNote) {
                    resultList.add(n);
                }
            }
            return resultList;

        }
    }

    public void addNote(Note note, Context context) {
        notesList.add(note);
        writeFile(context);
    }

    public void updateNote(Note note, Context context) {
        notesList.set(getNoteId(note), note);
        writeFile(context);
    }


    public void readFile(Context context) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    context.openFileInput("notes")));
            String str;
            while ((str = br.readLine()) != null) {
                notesList = gson.fromJson(str, listType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void writeFile(Context context) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    context.openFileOutput("notes", MODE_PRIVATE)));
            bw.write(gson.toJson(notesList));
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteNote(Note note, Context context) {
        notesList.remove(note);
        writeFile(context);
    }

    private int getNoteId(Note note) {
        int position = 0;
        Note n;
        for (int i = 0; i < notesList.size(); i++) {
            n = notesList.get(i);
            if (n.getId().equals(note.getId())) {
                position = i;
                break;
            }
        }
        return position;
    }
}
