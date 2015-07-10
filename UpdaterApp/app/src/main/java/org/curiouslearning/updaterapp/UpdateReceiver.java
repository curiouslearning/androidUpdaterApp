package org.curiouslearning.updaterapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

public class UpdateReceiver extends BroadcastReceiver {
    public UpdateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        //Get APK name/path and package name from intent

        //Verify that new APK exists

        //Move current APK to backup location

        //stop the app from running

        //Uninstall the app

        //Install the new APK

        //Verify installation

        //Start the app

        //Verify that the app is running

        //End


        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean isAppRunning()
    {

    }
}
