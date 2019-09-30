package com.chivesoft.ewavi;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

public class DrawerListModel {

    private Bitmap picture = null;
    private String title = null;

    public DrawerListModel(Bitmap picture, String title) {
        this.picture = picture;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Bitmap getPicture() {
        return picture;
    }


}