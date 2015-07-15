package org.curiouslearning.updaterapp;

import android.util.Log;

import java.io.File;
import java.io.InputStream;

/**
 * Created by David Gibbs on 7/10/2015 for Curious Learning.
 * curiouslearning.org
 */
public class Utilities
{
    private final String TAG = Config.TAG;

    /**
     * Execute a command and return the response from the command
     *
     * @param command the command to be executed
     * @return the result of the executed command
     */
    public String executeCommand(String command)
    {
        String output = "";

        try {
            InputStream inputStream = Runtime.getRuntime().exec(command).getInputStream();

            while( inputStream.available() <= 0)
                try { Thread.sleep(500); } catch(Exception ex) {}

            java.util.Scanner s = new java.util.Scanner(inputStream);//.useDelimiter("\\A");
            while(s.hasNext())
                output += s.nextLine() + "\n";

            return output;
        }
        catch (Exception e)
        {
            Log.i(TAG, "Exception when trying to execute command: " + command);
        }
        return output;
    }

    /**
     * Converts a string of permission settings to numberic values that will be
     * saved in a String.  ex: <b>drwxrwx--x</b> will be converted to <b>771</b>.
     *
     * @param directoryLocation the string to be converted
     * @return the converted string in String-numeric format
     */
    public String getDirectoryPermissions(String directoryLocation)
    {
        //Ensure that the directory exists
        File f = new File(directoryLocation);
        if (!f.exists() || !f.isDirectory())
        {
            Log.i(TAG, "Directory doesn't exist");
        }

        String response = executeCommand("ls -ld " + directoryLocation);

        String permissions = response.split(" ")[0];

        //Sanity Check
        if(permissions.length() != 10)
        {
            Log.i(TAG, "Error when trying to get directory settings for " + directoryLocation);
            return null;
        }

        StringBuilder permissionString = new StringBuilder();
        for(int i = 1; i <= 7; i += 3)
            permissionString.append(convertDirectoryPermissions(permissions.substring(i, i+3)));

        return permissionString.toString();
    }

    /**
     * Convert a permission field in the form of "rwx" to 7
     * @param permissionsToConvert the string to be converted
     * @return the numeric representation of the permission
     */
    private String convertDirectoryPermissions(String permissionsToConvert)
    {
        //Sanity check
        if(permissionsToConvert.length() != 3){
            Log.i(TAG, "Trying to convert a wrong string to a permission value");
            return null;
        }

        int permission = 0;
        for(int i = 0; i <= 2; i++)
            if(permissionsToConvert.charAt(i) != '-')
                permission += ((4 >> i));

        return Integer.toString(permission);
    }



}
