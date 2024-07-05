package com.formisk.aneonpath;

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
        
        /*getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tabSettings = getSupportActionBar().newTab();
        tabSettings.setText("Settings");
        tabSettings.setTabListener(this);
        getSupportActionBar().addTab(tabSettings);

        ActionBar.Tab tabCustomCustomize = getSupportActionBar().newTab();
        tabCustomCustomize.setText("Custom Theme");
        tabCustomCustomize.setTabListener(this);
        getSupportActionBar().addTab(tabCustomCustomize);*/
        
        getSupportActionBar().setTitle("A Neon Path Preferences");
        
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Sherlock_Light); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        this.getPreferenceManager().setSharedPreferencesName(ANeonPathWallpaper.SHARED_PREFS_NAME);
        addFreePreferences();
        addPreferences();
        
    	Preference buybutton = (Preference)findPreference("moreappsbutton");
    	buybutton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference arg0) { 
        	   String url = "https://play.google.com/store/apps/developer?id=Christoffer+Green";  
        	   Intent i = new Intent(Intent.ACTION_VIEW);  
        	   i.setData(Uri.parse(url));  
        	   startActivity(i); 
                return true;
            }
        });
    }
    
    public void addFreePreferences() {
	    if(getPackageName().toLowerCase().contains("free")) {
	    	addPreferencesFromResource(R.xml.freepreferences);

	    	Preference buybutton = (Preference)findPreference("buybutton");
	    	buybutton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
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
    
    public void InstructionsDialog() {
    	  AlertDialog.Builder ad = new AlertDialog.Builder(this);
    	  ad.setIcon(R.drawable.ic_launcher);
    	  ad.setTitle("About the 'A Neon Path' wallpaper");
    	  ad.setView(LayoutInflater.from(this).inflate(R.layout.about,null));
    	  ad.setInverseBackgroundForced(true);

    	  ad.setPositiveButton("OK", 
    	    new android.content.DialogInterface.OnClickListener() {
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
        addPreferencesFromResource(R.xml.preferences);

    	Preference aboutbutton = (Preference)findPreference("aboutbutton");
    	aboutbutton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference arg0) { 
            	InstructionsDialog();
            	return true;
            }
        });
    }

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}
