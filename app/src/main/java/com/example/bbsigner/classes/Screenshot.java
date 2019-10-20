package com.example.bbsigner.classes;

import android.graphics.Bitmap;
import android.view.View;

public class Screenshot {

    public static Bitmap takescreesnshot(View v){
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return b;
    }

    public static Bitmap takescreenshotOfRootView(View v){
        return takescreesnshot(v.getRootView());
    }
}
