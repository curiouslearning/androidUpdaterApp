package org.curiouslearning.updaterapp;

/**
 * Created by David on 7/21/2015 for Curious Learning.
 * curiouslearning.org
 */

import java.util.ArrayList;

public class DeleteFileAsRoot extends ExecuteAsRootBase {

    public String fileToDelete = "";

    @Override
    protected ArrayList<String> getCommandsToExecute() {
        ArrayList<String> stringList = new ArrayList<String>();
        if(!fileToDelete.equals( "")) {
            stringList.add("rm " + fileToDelete);
        }
        return stringList;
    }

}
