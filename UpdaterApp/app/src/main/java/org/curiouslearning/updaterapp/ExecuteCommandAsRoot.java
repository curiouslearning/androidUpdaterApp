package org.curiouslearning.updaterapp;

/**
 * Created by David on 7/10/2015 for Curious Learning.
 * curiouslearning.org
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Curious Learning on 5/4/2015.
 *
 * Accepts a command and adds to a list of commands for later execution
 */
public class ExecuteCommandAsRoot extends ExecuteAsRootBase {

    private List<String> commands = new LinkedList<>();

    public void addCommand(String command) {
        commands.add(command);
    }

    @Override
    protected ArrayList<String> getCommandsToExecute() {
        ArrayList<String> stringList = new ArrayList<String>();
        if (!commands.isEmpty()) {
            for (String cmd : commands)
                stringList.add(cmd);
        }
        return stringList;
    }
}
