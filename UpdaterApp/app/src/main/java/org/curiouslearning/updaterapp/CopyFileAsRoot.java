package org.curiouslearning.updaterapp;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by David Gibbs on 7/10/2015 for Curious Learning.
 * curiouslearning.org
 */
public class CopyFileAsRoot extends ExecuteAsRootBase {
	private static final String TAG = Config.TAG;

	public String fileToCopy = "";
	public String target = "";

	
	@Override
	protected ArrayList<String> getCommandsToExecute() {
		ArrayList<String> stringList = new ArrayList<>();
		if(!fileToCopy.equals( "")) {
			stringList.add("cp " + fileToCopy + " " + target);
			Log.i(TAG, "attempted to copy: " + "cp " + fileToCopy + " " + target);
		}
		
		return stringList;
	}

}