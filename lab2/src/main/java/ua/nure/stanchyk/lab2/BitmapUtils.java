package ua.nure.stanchyk.lab2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class BitmapUtils {
    public static Bitmap loadFromFile(String imagePath){
        File imgFile = new File(imagePath);
        Bitmap resultBitmap = null;
        if (imgFile.exists()) {
            resultBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return resultBitmap;
    }
}
