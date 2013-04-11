/*
 * SpeedMeterActivity.java
 *
 *  (c) 2013 mugcupsoup@gmail.com
 *
 *  Licensed under the MIT license.
 *  http://www.opensource.org/licenses/mit-license
 */

package com.gmail.mugucupsoup.android.LightMeter;

import com.gmail.mugcuposup.android.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;


public class LightMeterActivity extends Activity{

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case R.id.action_settings:
		        Intent intent = new Intent(this,com.gmail.mugucupsoup.android.LightMeter.LightMeterPreferenceActivety.class);
		        startActivity(intent);
		        return true;
	    }
	    return false;
	}
	
	protected void showUsageDialog(int id){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isShowUsage = pref.getBoolean("checkbox_show_usage", true);
        
        if(isShowUsage){
	    	AlertDialog.Builder alert = new AlertDialog.Builder(this);  
	        alert.setTitle(getString(R.string.how_to_use));  
	        alert.setMessage(getString(id));
	        alert.setPositiveButton("OK", null);
	        alert.show();
        }
	}
}
