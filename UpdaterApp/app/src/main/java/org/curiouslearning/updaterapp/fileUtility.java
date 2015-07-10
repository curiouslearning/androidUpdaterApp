package org.curiouslearning.updaterapp;

import java.io.File;

/**
 * Created by David on 7/10/2015 for Curious Learning.
 * curiouslearning.org
 */
public class FileUtility {

    private final static String TAG = Config.TAG;

    /**
     * Check if a file exists
     *
     * @param filePath the path of the file to be checked
     * @return true if exists, false otherwise
     */
    public static boolean fileExists(String filePath)
    {
        return new File(filePath).isFile();
    }



}
