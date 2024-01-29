package bk.ltuddd.iotapp.core.component.resource;

import android.graphics.Bitmap;

public interface ResourceService {
    String getString(int key);
    int getColor(int colorId);
    Bitmap getImage(int resourceId);
}

