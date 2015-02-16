package edu.fudan.se.crowdservice.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.*;
import java.util.Date;

/**
 * Created by Jiahuan on 2015/1/28.
 */
public class IOUtil {

    public static String saveByteArray(byte[] imageBytes, Context context) {
        String imageFileName = new Date().getTime() + ".jpg";
        FileOutputStream out = null;
        try {
            out = context.openFileOutput(imageFileName, Context.MODE_PRIVATE);
            out.write(imageBytes, 0, imageBytes.length);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(out);
        }
        return imageFileName;
    }


    public static String compressAndSaveImage(byte[] bytes, Context context) {
        String imageFileName = "" + new Date().getTime();
        FileOutputStream out = null;
        try {
            out = context.openFileOutput(imageFileName, Context.MODE_PRIVATE);
            Bitmap original = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            Bitmap.createScaledBitmap(original, original.getWidth() / 5, original.getHeight() / 5, true).compress(Bitmap.CompressFormat.JPEG, 80, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            imageFileName = "";
        } finally {
            close(out);
        }
        return imageFileName;
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] loadByteArray(String bytePath, Context context) {
        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;
        byte[] result = null;
        try {
            out = new ByteArrayOutputStream();
            in = new BufferedInputStream(context.openFileInput(bytePath));
            byte[] temp = new byte[4096];
            int size;
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            result = out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(out);
            close(in);
        }
        return result;
    }
}
