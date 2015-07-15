package org.curiouslearning.updaterapp;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


/**
 * Created by David Gibbs on 7/10/2015 for Curious Learning.
 * curiouslearning.org
 */
public class MainActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button getManifestButton = (Button) findViewById(R.id.updateButton);
        getManifestButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String apkName = "org.curiouslearning.managementapp-1.apk";

                Intent intent = new Intent("org.curiouslearning.updaterapp.UpdateReceiver");
                intent.putExtra("apkName", apkName);
                intent.putExtra("packageName", "org.curiouslearning.managementapp");
                intent.putExtra("apkPath", Environment.getExternalStorageDirectory().getPath() + "/" + apkName);
                sendBroadcast(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
