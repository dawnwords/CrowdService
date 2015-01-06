package service.shcomputer.cs.takepic.interfaces;

import android.graphics.Bitmap;
import android.location.Location;

/**
 * Created by Dawnwords on 2015/1/6.
 */
public interface TakePictureOfURLService {
    Bitmap takeRealPictureOfURL(String url, Location location);
}
