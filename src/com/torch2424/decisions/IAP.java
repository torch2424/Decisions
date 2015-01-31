package com.torch2424.decisions;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import billing.IabHelper;
import billing.IabResult;
import billing.Inventory;
import billing.Purchase;

public class IAP extends Activity 
{
	
	//Dont need to change any views or anything so dont need to reference them
	
	// Helper billing object
	private IabHelper mHelper;
	
	//Booleans to store what has been purchased, and what has not
	boolean fiveBool;
	boolean tenBool;
	boolean infiniteBool;
	
	//Our Buttons
	Button fiveButton;
	Button tenButton;
	Button infiniteButton;
	
	//our toast
    Toast toasty;
    
    //SharedPreferences - to edit when something is purchased
    SharedPreferences prefs;
	
	//our app key, broken up for "safety"
	final private String appKey1 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgK";		
	final private	String appKey2 = "CAQEA0EON51xQhGEAdKBJeGn2RqJT3wlojHN5/l88eCTRNCad61g9" +
			"1MxRh5jLG7l2dxtiT0vtKZ7m2kz4QjFQLWZlu8";
	final private String appKey3 = "88YNAvcu/ns83X2XMhMVnghG//V6GoWFenSZzoktvHsq9r2uv2ejqKTQ0BNE0edkbs" +
			"+V7mhDM8V5nQbO/6gH5UeYq7iOLuSHq";
	final private String appKey4 = "ViWBHdEDVtvfqTbinIXnQGjf4+GyrxfC2OHtYTPQjj/o9Y6OCSOtgXnHFFdQ+ZF+h3602a" +
			"9MSONYVWM8Ead+UunqQGTaIQ3ruzMD42";
	final private String appKey5 = "wQ1i/5AHdAaxT/9LTxkGvPF/OG744F0u/yNvmJPiHGj28R/Z2awhNCuowIDAQAB";
	
	
	//Followed this guide to get purchases working
	//http://developer.android.com/training/in-app-billing/preparing-iab-app.html

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.iap);
		
		//for icon up navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		 String base64EncodedPublicKey = appKey1 + appKey2 + appKey3 + appKey4 + appKey5;
		 
		 //Set up Buttons
		 fiveButton = (Button) findViewById(R.id.fiveButton);
		 tenButton = (Button) findViewById(R.id.tenButton);
		 infiniteButton = (Button) findViewById(R.id.infiniteButton);
		 
		 //Set up prefs
		 prefs = this.getSharedPreferences("MyPrefs", 0);
		 
		//set up toast
	   	toasty = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
		   
		 // compute your public key and store it in base64EncodedPublicKey
		 mHelper = new IabHelper(this, base64EncodedPublicKey);
		 
		 //Start the thingy
		 mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() 
		 {
			   public void onIabSetupFinished(IabResult result) 
			   {
			      if (!result.isSuccess())
			      {
			         // Oh noes, there was a problem.
			         Log.d("Decisions Billing", "Problem setting up In-app Billing: " + result);
			         
			         //Toast error to user
			         toasty.setText("Error preparing In-App Billing!");
				    toasty.show();
			         
			         //Place boolean here that makes buttons not work if can not set up in app purchases
			      }            
			         // Hooray, IAB is fully set up! 
			      
			      
			     //May not need to query at start since we already know what our items are
			      //Just need to query purchased items
			      /*
			      //Since it is now set up, do this!
			      //Get our products and see if they are avalable of purchase
					 List<String> additionalSkuList = new ArrayList<String>();
			         additionalSkuList.add("fivedecisions");
			         additionalSkuList.add("tendecisions");
			         additionalSkuList.add("infinitedecisions");
					 //additionalSkuList.add(SKU_BANANA);
					 mHelper.queryInventoryAsync(true, additionalSkuList,
					    mQueryFinishedListener);
					    */
			      
			      //However we do need to set up our purchased items
			      mHelper.queryInventoryAsync(mGotInventoryListener);
			   }
			});

	}
	
	public void changeButtonText(int id)
	{
		//Function to change a buttons text to purchased
		if(id == R.id.fiveButton)
		{
			fiveButton.setText(getResources().getString(R.string.purchased));
		}
		else if (id == R.id.tenButton)
		{
			tenButton.setText(getResources().getString(R.string.purchased));
		}
		else
		{
			infiniteButton.setText(getResources().getString(R.string.purchased));
		}
	}
	
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener 
	   = new IabHelper.QueryInventoryFinishedListener() 
	{
	   public void onQueryInventoryFinished(IabResult result,
	      Inventory inventory) {

	      if (result.isFailure()) 
	      {
	    	  //toast user
	    	  toasty.setText("Error receiving previous purchases!");
		    	toasty.show();
	      }
	      else 
	      {
	        // does the user have the premium upgrade?
	        fiveBool = inventory.hasPurchase("fivedecisions");
	        tenBool = inventory.hasPurchase("tendecisions");
	        infiniteBool = inventory.hasPurchase("infinitedecisions");
	        // update UI accordingly
	        
	        //Change the buttons to say purchased
	        if(fiveBool)
	        {
	        	changeButtonText(R.id.fiveButton);
	        }
	        if(tenBool)
	        {
	        	changeButtonText(R.id.tenButton);
	        }
	        if(infiniteBool)
	        {
	        	changeButtonText(R.id.infiniteButton);
	        }
	      }
	   }
	};
	
	//Whenever we finish Querying
	 IabHelper.QueryInventoryFinishedListener 
	   mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() 
	 {
	   public void onQueryInventoryFinished(IabResult result, Inventory inventory)   
	   {
	      if (result.isFailure()) 
	      {
	         // handle error
	         return;
	       }
	      
	      //What to do once we query

	      // String fiveDecisionsPrice =
	         // inventory.getSkuDetails("fivedecisions").getPrice();

	       // update the UI 
	   }
	};
	
	
	
	
	//Buy Function, will just use view.getId to determine which button
	//Calls fladEnd Async to stop bug where you can't open dialog twice
	public void buy(View view)
	{
		//get the view id
		int id = view.getId();
		//Need to find which button we pressed
		if(id == R.id.fiveButton)
		{
			if(!fiveBool)
			{
			//Need to check if 5 bool is true or false first though
			//Launch the purchase flow for 5 decisions
			mHelper.flagEndAsync();
			mHelper.launchPurchaseFlow(this, "fivedecisions", 50005,   
					   mPurchaseFinishedListener, "fivedecisionskey");
			}
			else
			{
				toasty.setText("This was already purchsed, it cannot be purchased more than once!");
				toasty.show();
			}
		}
		else if(id == R.id.tenButton)
		{
			if(!tenBool)
			{
			mHelper.flagEndAsync();
			mHelper.launchPurchaseFlow(this, "tendecisions", 1000010,   
					   mPurchaseFinishedListener, "tendecisionskey");
			}
			else
			{
				toasty.setText("This was already purchsed, it cannot be purchased more than once!");
				toasty.show();
			}
		}
		//Then it must be infinite
		else
		{
			if(!infiniteBool)
			{
			mHelper.flagEndAsync();
			mHelper.launchPurchaseFlow(this, "infinitedecisions", 00000,   
					   mPurchaseFinishedListener, "infinitedecisionskey");
			}
			else
			{
				toasty.setText("This was already purchsed, it cannot be purchased more than once!");
				toasty.show();
			}
		}
		
		
	}
	
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener 
	   = new IabHelper.OnIabPurchaseFinishedListener() 
	{
	   public void onIabPurchaseFinished(IabResult result, Purchase purchase) 
	   {
	      if (result.isFailure()) 
	      {
	         Log.d("DECISIONSBILLING", "Error purchasing: " + result);
	       //toast user
	    	  toasty.setText("Error purchasing item!");
		    	toasty.show();
	         return;
	      }      
	      else if (purchase.getSku().equals("fivedecisions")) 
	      {
	         // give user access to premium content and update the UI
	    	  fiveBool = true;
	    	  changeButtonText(R.id.fiveButton);
	    	  toasty.setText("Purchase Successful!");
	    	  toasty.show();
	    	  
	    	  Editor editor = prefs.edit();
	    	  //just change a boolean and add accordingly in start
	    	  editor.putBoolean("FIVEBOOL", true);
	    	  editor.commit();
	      }
	      else if (purchase.getSku().equals("tendecisions")) 
	      {
	         // give user access to premium content and update the UI
	    	  tenBool = true;
	    	  changeButtonText(R.id.tenButton);
	    	  toasty.setText("Purchase Successful!");
	    	  toasty.show();
	    	  
	    	  Editor editor = prefs.edit();
	    	  //change boolean and add accordingly in start
	    	  editor.putBoolean("TENBOOL", true);
	    	  editor.commit();
	      }
	      else if (purchase.getSku().equals("infinitedecisions")) 
	      {
	         // give user access to premium content and update the UI
	    	  infiniteBool = true;
	    	  changeButtonText(R.id.infiniteButton);
	    	  toasty.setText("Purchase Successful!");
	    	  toasty.show();
	    	  
	    	  Editor editor = prefs.edit();
	    	  //Add five to whatever value is in the REMAINING Int slot
	    	  editor.putBoolean("REMAININGBOOL", true);
	    	  editor.commit();
	      }
	   }
	};
	
	public void restorePurchases(View view)
	{
		//Function to restore everything
		//Change the buttons to say purchased
        if(fiveBool)
        {
        	 Editor editor = prefs.edit();
	    	  //just change a boolean and add accordingly in start
	    	  editor.putBoolean("FIVEBOOL", true);
	    	  editor.commit();
        }
        if(tenBool)
        {
        	 Editor editor = prefs.edit();
	    	  //change boolean and add accordingly in start
	    	  editor.putBoolean("TENBOOL", true);
	    	  editor.commit();
        }
        if(infiniteBool)
        {
        	 Editor editor = prefs.edit();
	    	  //make remianing bool true which makes everything infinite
	    	  editor.putBoolean("REMAININGBOOL", true);
	    	  editor.commit();
        }
        
        //Which toast is displayed, depending on if purchases were restored or not
        if(!fiveBool && !tenBool && !infiniteBool)
        {
        	//If nothing was every bought on this account
        	toasty.setText("Could not restore, no purchases found on your Google account linked to this device.");
	    	toasty.show();
        }
        else
        {
        	//Something was bought
        	toasty.setText("Restore Successful!");
	    	toasty.show();
        }
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ia, menu);
		return true;
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
	
	//To fix purchased finished bug
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	 {
	    // Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

	     // Pass on the activity result to the helper for handling
	     if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
	         // not handled, so handle it ourselves (here's where you'd
	         // perform any handling of activity results not related to in-app
	         // billing...
	    	 
	    	 //May Have to do stuff here
	         super.onActivityResult(requestCode, resultCode, data);
	     }
	     else 
	     {
	    	 //Inform User they have to exit app, and then restore purchases
	    	//Something was bought
	    	 
	    	 //Toast was here, but don't need it
	    	 
	         //Log.d(TAG, "onActivityResult handled by IABUtil.");
	     }
	 }
	
	//To dispose of billing stuff
	@Override
	public void onDestroy() 
	{
	   super.onDestroy();
	   if (mHelper != null) mHelper.dispose();
	   mHelper = null;
	}
}
