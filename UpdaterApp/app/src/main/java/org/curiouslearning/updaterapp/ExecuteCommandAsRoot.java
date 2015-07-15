package org.curiouslearning.updaterapp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by David Gibbs on 7/10/2015 for Curious Learning.
 * curiouslearning.org
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
