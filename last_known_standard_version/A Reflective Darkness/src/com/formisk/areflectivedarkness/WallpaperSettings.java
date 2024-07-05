package com.formisk.areflectivedarkness;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class WallpaperSettings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle icicle) {
	    super.onCreate(icicle);
	    if(getPackageName().toLowerCase().contains("full")) {
		    getPreferenceManager().setSharedPreferencesName(ALiquidCloudWallpaper.SHARED_PREFS_NAME);
		    addPreferencesFromResource(R.xml.preferences);
		    getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	    }
	    else {
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setMessage("This is the free version of the 'A liquid Cloud' Live Wallpaper. " +
	    						"The full version includes settings for changing between several themes, " +
	    						"setting of fps and zoom amount. Would you like to go to the page for the " +
	    						"full version on the android market now?")
	    	       .setCancelable(false)
	    	       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	        	   String url = "https://market.android.com/details?id=com.formisk.aliquidcloud.full";  
	    	        	   Intent i = new Intent(Intent.ACTION_VIEW);  
	    	        	   i.setData(Uri.parse(url));  
	    	        	   startActivity(i); 
	    	           }
	    	       })
	    	       .setNegativeButton("No", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	                dialog.cancel();
	    	           }
	    	       });
	    	AlertDialog alert = builder.create();
	    	alert.show();
	    }
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	}
	
	@Override
	protected void onDestroy() {
	    getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	    super.onDestroy();
	}
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	}

}