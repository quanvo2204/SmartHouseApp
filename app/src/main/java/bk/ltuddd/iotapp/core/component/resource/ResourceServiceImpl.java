package bk.ltuddd.iotapp.core.component.resource;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

public class ResourceServiceImpl implements ResourceService {

    private Context mContext;

    public ResourceServiceImpl(Context context) {
        mContext = context;
    }

    @Override
    public String getString(int key) {
        return mContext.getResources().getString(key);
    }

    @Override
    public int getColor(int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return mContext.getColor(colorId);
        } else {
            return mContext.getResources().getColor(colorId);
        }
    }

    @Override
    public Bitmap getImage(int resourceId) {
        return BitmapFactory.decodeResource(mContext.getResources(), resourceId);
    }
}
