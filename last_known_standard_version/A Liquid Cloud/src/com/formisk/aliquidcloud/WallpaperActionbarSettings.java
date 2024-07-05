package com.formisk.aliquidcloud;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;
import android.view.LayoutInflater;

public class WallpaperActionbarSettings extends SherlockPreferenceActivity implements ActionBar.TabListener {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Used to put dark icons on light action bar

        //menu.add("Save").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tabSettings = getSupportActionBar().newTab();
        tabSettings.setText("Settings");
        tabSettings.setTabListener(this);
        getSupportActionBar().addTab(tabSettings);

        ActionBar.Tab tabCustomCustomize = getSupportActionBar().newTab();
        tabCustomCustomize.setText("Custom Theme");
        tabCustomCustomize.setTabListener(this);
        getSupportActionBar().addTab(tabCustomCustomize);
        
        getSupportActionBar().setTitle("A Liquid Cloud Preferences");
        
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Sherlock_Light); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(ALiquidCloudWallpaper.SHARED_PREFS_NAME);
        addPreferences();
    }
    
    public void InstructionsDialog() {
    	  AlertDialog.Builder ad = new AlertDialog.Builder(this);
    	  ad.setIcon(R.drawable.ic_launcher);
    	  ad.setTitle("About the 'A Liquid Cloud' wallpaper");
    	  ad.setView(LayoutInflater.from(this).inflate(R.layout.about,null));
    	  ad.setInverseBackgroundForced(true);

    	  ad.setPositiveButton("OK", 
    	    new android.content.DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {}
    	    }
    	   );

    	   ad.setOnCancelListener(new DialogInterface.OnCancelListener(){
	    	     public void onCancel(DialogInterface dialog) {}
	    	     }
    	   );

    	  ad.show();
    }    
    
    public void addPreferences() {
        addFreePreferences();
        addPreferencesFromResource(R.xml.preferences);

    	Preference aboutbutton = (Preference)findPreference("aboutbutton");
    	aboutbutton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) { 
            	InstructionsDialog();
            	return true;
            }
        });
    }
    
    public void addFreePreferences() {
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
    }
    
    public void addCustomGUI(String type) {
		PreferenceScreen prefs = getPreferenceScreen();
		prefs.removeAll();			
		if(type.equals("1"))
			addPreferencesFromResource(R.xml.preferencesnocustom);
		if(type.equals("2"))
			addPreferencesFromResource(R.xml.preferencescustomsimple);
		else if(type.equals("3"))
			addPreferencesFromResource(R.xml.preferencescustom);
		
    	Preference customtype = (Preference)findPreference("customthemetypekey");
    	customtype.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				getSharedPreferences(ALiquidCloudWallpaper.SHARED_PREFS_NAME, 0).edit().putString("customthemetypekey", (String)newValue).commit();
				addCustomGUI((String)newValue);
        	    return true;
			}
        });
    }

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if(tab.getPosition() == 0) {
			PreferenceScreen prefs = getPreferenceScreen();
			prefs.removeAll();
			addPreferences();
		}
		if(tab.getPosition() == 1) {
			String type = getSharedPreferences(ALiquidCloudWallpaper.SHARED_PREFS_NAME, 0).getString("customthemetypekey",  "1");
			addCustomGUI(type);
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}
