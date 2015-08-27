package org.curiouslearning.updaterapp;

import android.util.Log;

import java.io.File;

/**
 * Created by David Gibbs on 7/10/2015 for Curious Learning.
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


    public static void myCopyFile(String fileToCopy, String target) {
        CopyFileAsRoot copyFile = new CopyFileAsRoot();
        copyFile.fileToCopy = fileToCopy;
        copyFile.target = target;
        if(copyFile.execute()) {
            Log.i(TAG, "executed myCopyFile commands ok");
        } else {
            Log.i(TAG, "did not execute myCopyFile commands ok");

        }
    }

    public static void myDeleteFile(String fileToDelete) {
        DeleteFileAsRoot deleteFile = new DeleteFileAsRoot();
        deleteFile.fileToDelete = fileToDelete;
        if(deleteFile.execute()) {
            Log.i(TAG, "executed deletefileasroot commands ok");
        } else {
            Log.i(TAG, "did not execute deletefileasroot commands ok");

        }
    }


}
