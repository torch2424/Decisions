package com.torch2424.decisions;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class BackupSettings extends Activity 
{
	
	//our toast
    Toast toasty;
    
    //File Stuff
    String fileDestination;
    File externalSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backupsettings);
		
		//for icon up navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//set up toast
   		toasty = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
   		
   		//The location for our file
   		fileDestination = "/Decisions/DecisionsSave.ser";
   		externalSave = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + fileDestination);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.backup_settings, menu);
		return true;
	}
	
	//Write Backup/Restore Functions
	public void backup(View view) throws IOException
	{
		//check if the external exists
		if(!Environment.getExternalStorageDirectory().canWrite())
		{
			//It cannot so toast the user
			toasty.setText("Error writing to External Storage!");
			toasty.show();
		}
		else
		{
			//Use the Decisions File API to save to the external
			Start.decisions.saveDecisionsToDirectory(externalSave);
			
			
			//Finished
			toasty.setText("Backed up to /Decisions/DecisionsSave.ser");
			toasty.show();
		}
		
		
	}
	
	public void restore(View view)
	{
		//check if the external exists
				if(!Environment.getExternalStorageDirectory().canRead())
				{
					//It cannot so toast the user
					toasty.setText("Error reading from External Storage!");
					toasty.show();
				}
				//check if the file exists
				else if(!externalSave.exists())
				{
					//If it does not exist...
					toasty.setText("No file found at /Decisions/DecisionsSave.ser in Default External Storage!");
					toasty.show();
				}
				else
				{
					//Use the Decisions File API to save to the external
					Start.decisions.getDecisionsFromDirectory(externalSave);
					
					
					//Finished
					toasty.setText("Restored from /Decisions/DecisionsSave.ser");
					toasty.show();
				}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		//The Up Button
		if(id == android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
