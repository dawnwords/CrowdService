package edu.fudan.se.crowdservice.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jiahuan on 2015/1/25.
 */
public class IOUtil {

    public static byte[] loadByteArray(String bytePath) {
        FileInputStream fis = null;
        try {
            File file = new File(bytePath);
            long fileSize = file.length();

            byte[] buffer = new byte[(int) fileSize];
            fis = new FileInputStream(file);
            int offset = 0;
            int numRead;
            while (offset < buffer.length
                    && (numRead = fis.read(buffer, offset,
                    buffer.length - offset)) >= 0) {
                offset += numRead;
            }
            return buffer;
        } catch (Exception ignored) {
        } finally {
            close(fis);
        }
        return null;
    }

    public static void saveByteArray(String bytePath, byte[] bytes) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(bytePath);
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fos);
        }
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public static String saveByteArray(byte[] bytes) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        FileOutputStream fos = null;
        String tempFilePath = null;
        try {
            File tempFile = File.createTempFile(imageFileName, ".jpg", storageDir);
            fos = new FileOutputStream(tempFile);
            fos.write(bytes, 0, bytes.length);
            fos.flush();
            tempFilePath = tempFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ignored) {
                }
            }
        }
        return tempFilePath;
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
