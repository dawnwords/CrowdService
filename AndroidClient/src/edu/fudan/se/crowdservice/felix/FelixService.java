package edu.fudan.se.crowdservice.felix;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import edu.fudan.se.crowdservice.core.SavedProperty;
import jade.util.Logger;
import org.apache.felix.framework.Felix;

import java.io.File;
import java.util.logging.Level;

public class FelixService extends Service {

    private Felix felix;
    private Logger logger = Logger.getJADELogger(this.getClass().getName());
    private SharedPreferences setting;

    @Override
    public void onCreate() {
        super.onCreate();

        createInitBundleDir();
        createInstallBundleDir();
        createFelixCacheDir();
        createTemplateDir();
        createOptimizedDir();

        felix = new Felix(new FelixConfig(this));
        setting = getSharedPreferences(SavedProperty.CROWD_SERVICE, 0);
    }

    @Override
    public synchronized void onDestroy() {
        shutdownApplication();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        TemplateManager manager = null;
        try {
            felix.start();
            info("Felix Version:" + felix.getVersion());
            String obrIp = setting.getString(SavedProperty.OBR_IP, "");
            int obrPort = setting.getInt(SavedProperty.OBR_PORT, 8080);
            manager = new TemplateManager(felix.getBundleContext(), obrIp, obrPort);
        } catch (Exception ex) {
            info("Could not create framework: " + ex.getMessage());
            ex.printStackTrace();
        }
        return manager;
    }

    private void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    private void shutdownApplication() {
        try {
            felix.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createInitBundleDir() {
        Parameter.getInstance().setInitBundleDir(createClearDir("bundle"));
    }

    private void createInstallBundleDir() {
        Parameter.getInstance().setNewBundleDir(createClearDir("newbundle"));
    }

    private void createFelixCacheDir() {
        createClearDir("cache");
    }

    private void createTemplateDir() {
        Parameter.getInstance().setTemplateDir(createClearDir("template"));
    }

    private void createOptimizedDir() {
        Parameter.getInstance().setOptimizedDir(createClearDir("optimized"));
    }

    private File createClearDir(String name) {
        File dir = new File(getRootPath() + File.separator + "felix" + File.separator + name);
        if (dir.exists()) {
            this.delete(dir);
        }
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException("Unable to create " + name + " dir");
        }
        return dir;
    }

    private String getRootPath() {
        return getFilesDir().getAbsolutePath();
    }

    private void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null)
                for (File f : files) {
                    delete(f);
                }
        }
        file.delete();
    }
}