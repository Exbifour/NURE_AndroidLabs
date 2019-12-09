package ua.nure.stanchyk.lab2;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.File;
import java.util.Date;

public class OneNoteActivity extends AppCompatActivity {

    private Gson gson;
    private Note note;

    private EditText etText;
    private EditText etTitle;
    private ImageView ivPhoto;

   // private RadioButton rbOne; TODO
  //  private RadioButton rbTwo;
    private String photoPath;
    private int importance;
    private String title;
    private String text;
    private Date date;
    private DatabaseHelper dbHelper;
    private boolean isNew;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_note);
        dbHelper = ApplicationLab2.getInstance().getDatabaseHelper();

        gson = new Gson();

        etTitle = findViewById(R.id.title);
        etText = findViewById(R.id.noteText);
        Button btnAddPhoto = findViewById(R.id.btn_add_photo);
        ivPhoto = findViewById(R.id.iv_photo);
     //TODO
     //   rbOne = findViewById(R.id.one);
     //   rbTwo = findViewById(R.id.two);
     //   RadioButton rbThree = findViewById(R.id.three);
     //   rbTwo = findViewById(R.id.two);
        TextView tvDate = findViewById(R.id.date);
        Intent intent = getIntent();
        btnAddPhoto.setOnClickListener(onBtnAddPhotoClick);

        String id;
        note = gson.fromJson(intent.getStringExtra("note"), Note.class);
        if (note != null) {
            isNew = false;
            date = note.getDate();
            text = note.getText();
            title = note.getTitle();
            id = note.getId();
            photoPath = note.getPhotoPath();
          //  importance = note.getImportance();
            tvDate.setText(DatabaseHelper.dateFormat.format(date));

            if (id == null) {
                note.generateId();
            }

            //TODO
            /*
            if (importance != 0) {
                if (importance == 1) {
                    rbOne.setChecked(true);
                } else if (importance == 2) {
                    rbTwo.setChecked(true);
                } else {
                    rbThree.setChecked(true);
                }
            }*/
            if (photoPath != null) {
                ivPhoto.setVisibility(View.VISIBLE);
                File imgFile = new File(photoPath);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ivPhoto.setImageBitmap(myBitmap);
                }
            }
            etTitle.setText(title);
            etText.setText(text);
        } else {
            note = new Note();
            isNew = true;
            date = new Date();
            tvDate.setText(DatabaseHelper.dateFormat.format(date));
            note.generateId();
           // rbOne.setChecked(true);
        }

    }

    View.OnClickListener onBtnAddPhotoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, 1);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        ivPhoto.setImageBitmap(selectedImage);
                        ivPhoto.setVisibility(View.VISIBLE);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                photoPath = cursor.getString(columnIndex);
                                ivPhoto.setImageBitmap(BitmapFactory.decodeFile(photoPath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }

    }

    @Override
    public void onBackPressed() {
        text = etText.getText().toString();
        note.setText(text);
        title = etTitle.getText().toString();
        note.setTitle(title);
        if (photoPath != null) {
            note.setPhotoPath(photoPath);
        }
        //TODO
        /*
        if (rbOne.isChecked()) {
            importance = 1;
        } else if (rbTwo.isChecked()) {
            importance = 2;
        } else {
            importance = 3;
        }
        note.setImportance(importance);
        */

        note.setDate(date);

        if (isNew) {
            dbHelper.addNote(note);
        } else {
            dbHelper.updateNote(note);
        }

        OneNoteActivity.super.onBackPressed();
    }


}
