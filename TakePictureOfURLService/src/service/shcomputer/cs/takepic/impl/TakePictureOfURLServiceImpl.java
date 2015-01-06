package service.shcomputer.cs.takepic.impl;

import android.location.Location;
import android.os.Environment;
import edu.fudan.se.crowdservice.core.ConcreteService;
import service.shcomputer.cs.takepic.interfaces.TakePictureOfURLService;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Dawnwords on 2015/1/6.
 */
public class TakePictureOfURLServiceImpl extends ConcreteService implements TakePictureOfURLService {

    @Override
    public String takeRealPictureOfURL(String url, Location location) {
        url = "http://10.131.252.156:8080/shcm/img/computer.jpg";
        return loadBitmap(url);
    }

    @Override
    protected Class getServiceInterface() {
        return TakePictureOfURLService.class;
    }

    public String loadBitmap(String url) {
        BufferedInputStream in = null;
        FileOutputStream out = null;
        String path = Environment.getExternalStorageDirectory() + File.separator + "computer.jpg";
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            byte[] buffer = new byte[8 * 1024];
            in = new BufferedInputStream(conn.getInputStream());
            out = new FileOutputStream(path);
            while (in.read(buffer) != -1) {
                out.write(buffer);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            path = null;
        } finally {
            close(in);
            close(out);
        }
        return path;
    }


    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
