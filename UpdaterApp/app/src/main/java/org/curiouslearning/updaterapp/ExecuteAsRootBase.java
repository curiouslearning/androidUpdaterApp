package org.curiouslearning.updaterapp;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by David Gibbs on 7/10/2015 for Curious Learning.
 * curiouslearning.org
 */
public abstract class ExecuteAsRootBase
{
    public final boolean execute()
    {
        boolean retval = false;

        try
        {
            ArrayList<String> commands = getCommandsToExecute();
            if (null != commands && commands.size() > 0)
            {
                Process suProcess = Runtime.getRuntime().exec("su");

                DataOutputStream os =
                        new DataOutputStream(suProcess.getOutputStream());

                // Execute commands that require root access
                for (String currCommand : commands)
                {
                    os.writeBytes(currCommand + "\n");
                    os.flush();
                }

                os.writeBytes("exit\n");
                os.flush();

                try
                {
                    int suProcessRetval = suProcess.waitFor();
                    if (255 != suProcessRetval)
                    {
                        // Root access granted
                        retval = true;
                    }
                    else
                    {
                        // Root access denied
                        retval = false;
                    }
                }
                catch (Exception ex)
                {
                    Log.e("Error executing root action", ex.toString());
                }
            }
        }
        catch (IOException | SecurityException ex)
        {
            Log.w("ROOT", "Can't get root access", ex);
        }
        catch (Exception ex)
        {
            Log.w("ROOT", "Error executing internal operation", ex);
        }

        return retval;
    }
    protected abstract ArrayList<String> getCommandsToExecute();
}
