package org.curiouslearning.updaterapp;

import android.os.Environment;

/**
 * Created by David Gibbs on 7/10/2015 for Curious Learning.
 * curiouslearning.org
 */
public class Config {
    public final static String TAG = "org.curiouslearning.updaterapp";
    public final static String BACKUP_FILE_LOCATION =
            Environment.getExternalStorageDirectory().getPath() + "/";
}
