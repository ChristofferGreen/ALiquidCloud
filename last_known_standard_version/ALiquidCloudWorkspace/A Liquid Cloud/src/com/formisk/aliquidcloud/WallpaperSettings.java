package com.formisk.aliquidcloud;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WallpaperSettings extends PreferenceActivity { 
	@Override 
	public void onCreate(Bundle savedInstanceState) { 
	    super.onCreate(savedInstanceState); 
	    getPreferenceManager().setSharedPreferencesName(ALiquidCloudWallpaper.SHARED_PREFS_NAME);
	    if(getPackageName().toLowerCase().contains("free")) {
	    	addPreferencesFromResource(R.xml.freepreferences);

	    	Preference buybutton = (Preference)findPreference("buybutton");
	    	buybutton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
		                    @Override
		                    public boolean onPreferenceClick(Preference arg0) { 
		    	        	   String url = "https://play.google.com/store/apps/details?id=com.formisk.aliquidcloud.full";  
		    	        	   Intent i = new Intent(Intent.ACTION_VIEW);  
		    	        	   i.setData(Uri.parse(url));  
		    	        	   startActivity(i); 
		                        return true;
		                    }
		                });
	    }
	    addPreferencesFromResource(R.xml.preferences);
	} 
}