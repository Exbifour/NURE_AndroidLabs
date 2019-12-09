package ua.nure.stanchyk.lab2;

import android.app.Application;

public class ApplicationLab2 extends Application {
    private static ApplicationLab2 instance = null;
    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        databaseHelper = new DatabaseHelper(this);
    }

    public static ApplicationLab2 getInstance() {
        return instance;
    }

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }
}