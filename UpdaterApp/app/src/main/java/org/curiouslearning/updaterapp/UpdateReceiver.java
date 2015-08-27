package org.curiouslearning.updaterapp;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.util.Log;

import java.util.List;

/**
 * Created by David Gibbs on 7/10/2015 for Curious Learning.
 * curiouslearning.org
 */
public class UpdateReceiver extends BroadcastReceiver
{
    private static final String TAG = Config.TAG;

    public UpdateReceiver() {
    }

    @Override
    public void onReceive(final Context context, final Intent intent)
    {
        final Runnable primaryProcessor = new Runnable() {
                public void run() {
                    primaryProcessor(context, intent);
                }
            };
            new Thread(primaryProcessor).start();
    }

    synchronized
    private void primaryProcessor(Context context, Intent intent)
    {
        Log.i(TAG, "Starting to update app");
        String packageNameString = "packageName";
        String processIdString = "processId";
        String oldApkNameString = "oldApkName";
        String oldApkPathString = "oldApkPath";
        String newApkPathString = "newApkPath";
        String rebootString = "reboot";

        String oldApkName, newApkPath, oldApkPath, packageName;
        oldApkName = newApkPath = oldApkPath = packageName = null;
        boolean reboot = false;

        int processIdOfAppToUpdate = -1;

        if(intent.hasExtra(oldApkNameString))
        {
            oldApkName = intent.getStringExtra(oldApkNameString);
            Log.i(TAG, "Old APK name: " + oldApkName);
        }
        else
            Log.i(TAG, "Old APK name not present");

        if(intent.hasExtra(packageNameString))
        {
            packageName = intent.getStringExtra(packageNameString);
            Log.i(TAG, "Package name: " + packageName);
        }
        else
            Log.i(TAG, "Package name not present");

        if(intent.hasExtra(oldApkPathString))
        {
            oldApkPath = intent.getStringExtra(oldApkPathString);
            Log.i(TAG, "Old APK to be uninstalled: " + oldApkPath);
        }
        else
            Log.i(TAG, "Old APK to be uninstalled not present");


        if(intent.hasExtra(newApkPathString))
        {
            newApkPath = intent.getStringExtra(newApkPathString);
            Log.i(TAG, "APK to be installed: " + newApkPath);
        }
        else
            Log.i(TAG, "APK to be installed not present");

        if(intent.hasExtra(newApkPathString))
        {
            reboot = intent.getBooleanExtra(rebootString, false);
            Log.i(TAG, "Reboot after successful install: " + reboot);
        }
            Log.i(TAG, "Reboot command is missing");

        if(intent.hasExtra(processIdString)) {
            try
            {
                processIdOfAppToUpdate = Integer.parseInt(intent.getStringExtra("processId"));
                Log.i(TAG, "Process ID of app to update: " + processIdOfAppToUpdate);
            }
            catch (NullPointerException e)
            {
                Log.e(TAG, "Process ID isn't a number: " + intent.getStringExtra("processId"));
            }
        }
        else
            Log.i(TAG, "Process ID of app to be removed not present");

        AppUtility appUtility = new AppUtility();

        //Verify that new APK exists
        if(!FileUtility.fileExists(newApkPath))
        {
            Log.e(TAG, "Unable to find apk to install");
            return;
        }

        //Move current APK to backup location
        if(!appUtility.backupApp(oldApkPath, oldApkName))
        {
            Log.e(TAG, "Unable to find apk to backup");
            return;
        }

        //stop the app from running
        appUtility.killApp(processIdOfAppToUpdate);

        //Uninstall the app
        if(!appUtility.uninstallApp(packageName))
        {
            Log.e(TAG, "Unable to uninstall app: " + packageName);
            return;
        }

        //Install the new APK.  Rollback to previous APK if new APK fails to install
        if(!appUtility.installApp(newApkPath))
        {
            appUtility.rollbackAppInstall();
            launchApp(packageName, context);
            return;
        }

        //Verify installation
        if(!appUtility.isAppInstalled(packageName))
        {
            appUtility.rollbackAppInstall();
            launchApp(packageName, context);
            return;
        }

        //Start the app
        launchApp(packageName, context);

        //Verify that the app is running
        if(!isAppRunning(context, packageName))
            return;
        Log.i(TAG, "App successfully installed and is running");

        String backupApkPath = Config.BACKUP_FILE_LOCATION + oldApkName + ".backup";

        //Delete the apk to save space
        FileUtility.myDeleteFile(newApkPath);
        FileUtility.myDeleteFile(backupApkPath);

        if(reboot)
        {
            //Sleep to let other app initialize after install
            try
            {
                Thread.sleep(5000);
            }
            catch(InterruptedException e) {}
            ExecuteCommandAsRoot executeCommandAsRoot = new ExecuteCommandAsRoot();
            executeCommandAsRoot.addCommand("reboot");
            executeCommandAsRoot.execute();
        }
    }


    public boolean launchApp(String packageName, Context context)
    {
        try
        {
            Intent i = context.getPackageManager().
                    getLaunchIntentForPackage(packageName);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        catch (ActivityNotFoundException | NullPointerException e)
        {
            Log.e(TAG, "Unable to start app: " + packageName);
            return false;
        }
        return true;
    }


    public boolean isAppRunning(Context context, String packageName)
    {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty())
        {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(packageName))
            {
                return false;
            }
        }
        return true;
    }
}
