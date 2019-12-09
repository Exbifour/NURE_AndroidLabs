package ua.nure.stanchyk.lab2;

import java.util.Date;
import java.util.UUID;

public class Note {
    private String title;
    private String text;
    private String id;
    private Date date;
  //  private int importance; //TODO
    private String photoPath;
    private boolean isSelected;

    public Note() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void generateId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

 /*   public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }*/

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
