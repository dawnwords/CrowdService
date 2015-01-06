package service.shcomputer.cs.takepic.interfaces;

import android.location.Location;

/**
 * Created by Dawnwords on 2015/1/6.
 */
public interface TakePictureOfURLService {
    String takeRealPictureOfURL(String url, Location location);
}
