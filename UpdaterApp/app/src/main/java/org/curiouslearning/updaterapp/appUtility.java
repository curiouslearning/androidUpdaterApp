package org.curiouslearning.updaterapp;

import android.app.ActivityManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by David on 7/10/2015 for Curious Learning.
 * curiouslearning.org
 */
public class AppUtility
{
    private String TAG = Config.TAG;
    private String apkName = null;
    private String packageName = null;

    private AppUtility(){}

    public AppUtility(String apkName, String packageName)
    {
        this.apkName = apkName;
        this.packageName = packageName;
    }

    public boolean isAppInstalled()
    {
        Utilities utilities = new Utilities();
        String command = "pm list packages";

        String response = utilities.executeCommand(command);

        return (response.toLowerCase().contains(packageName));
    }

    public boolean uninstallApp()
    {

        int numberOfApplications = countInstalledPackages();

        String installCommand = "pm uninstall " + packageName;

        ExecuteCommandAsRoot executeCommandAsRoot = new ExecuteCommandAsRoot();
        executeCommandAsRoot.addCommand(installCommand);
        executeCommandAsRoot.execute();

        int numberOfNewlyInstalledApps = countInstalledPackages() - numberOfApplications;

        if(numberOfNewlyInstalledApps == -1)
            Log.i(TAG, packageName + " Uninstalled correctly");

        return false;
    }


    /**
     * Installs an APK on the device
     *
     * @param apkLocation the location of the application (.apk) to be installed
     * @return true if the application was successfully installed,
     * false otherwise
     */
    public boolean installApp(String apkLocation)
    {
        Utilities utilities = new Utilities();

        //Count the number of installed applications
        int numberOfApplications = countInstalledPackages();

        String installCommand = "pm install " + apkLocation;

        ExecuteCommandAsRoot executeCommandAsRoot = new ExecuteCommandAsRoot();
        executeCommandAsRoot.addCommand(installCommand);
        executeCommandAsRoot.execute();

        int numberOfNewlyInstalledApps = countInstalledPackages() - numberOfApplications;

        if(numberOfNewlyInstalledApps == 1)
            Log.i(TAG, apkLocation + " Installed correctly");

        else
        {
            Log.i(TAG, "Unable to install app.  Taking further actions to install...");

            executeCommandAsRoot = new ExecuteCommandAsRoot();
            String localDirectory = "/data/local";
            String tmpDirectory = "/data/local/tmp";

            //First get the permissions of the directories we are about to change
            String localDirectoryPermissions = utilities.getDirectoryPermissions(localDirectory);
            String tmpDirectoryPermissions = utilities.getDirectoryPermissions(tmpDirectory);

            //Change the permissions of /data/local to allow any user to install the app
            executeCommandAsRoot.addCommand("chmod 777 " + localDirectory);
            executeCommandAsRoot.addCommand("chmod 777 " + tmpDirectory);
            executeCommandAsRoot.execute();

            //Try to install again
            executeCommandAsRoot.addCommand(installCommand);
            executeCommandAsRoot.execute();

            if(countInstalledPackages() == (numberOfApplications + 1))
                Log.i(TAG, apkLocation + " Installed correctly");
            else
                Log.i(TAG, "Unable to install app: " + apkLocation);

            //Change the folder settings back to their original values
            executeCommandAsRoot.addCommand("chmod " + localDirectoryPermissions + " " + localDirectory);
            executeCommandAsRoot.addCommand("chmod " + tmpDirectoryPermissions + " " + tmpDirectory);
            executeCommandAsRoot.execute();
        }
        return (numberOfNewlyInstalledApps == 1);
    }



    /**
     * Counts the number of installed applications
     *
     * @return the number of applications installed on the device
     */
    private int countInstalledPackages()
    {
        String listPackages = "pm list packages";
        Utilities util = new Utilities();
        int numberOfApplications = -1;
        try
        {
            numberOfApplications = util.executeCommand(listPackages).split("\n").length;
        }
        catch (NumberFormatException e)
        {
            Log.i(TAG, "Exception when trying to count the installed packages");
        }
        return numberOfApplications;
    }
}
