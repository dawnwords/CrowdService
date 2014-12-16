package edu.fudan.se.crowdservice.felix;

import java.io.File;

/**
 * Created by Dawnwords on 2014/12/15.
 */
public class Parameter {
    private static Parameter ourInstance = new Parameter();

    public static Parameter getInstance() {
        return ourInstance;
    }

    private File initBundleDir;
    private File newBundleDir;
    private File templateDir;
    private File optimizedDir;

    public File getInitBundleDir() {
        return initBundleDir;
    }

    public void setInitBundleDir(File initBundleDir) {
        this.initBundleDir = initBundleDir;
    }

    public File getNewBundleDir() {
        return newBundleDir;
    }

    public void setNewBundleDir(File newBundleDir) {
        this.newBundleDir = newBundleDir;
    }

    public File getTemplateDir() {
        return templateDir;
    }

    public void setTemplateDir(File templateDir) {
        this.templateDir = templateDir;
    }

    public File getOptimizedDir() {
        return optimizedDir;
    }

    public void setOptimizedDir(File optimizedDir) {
        this.optimizedDir = optimizedDir;
    }

}
