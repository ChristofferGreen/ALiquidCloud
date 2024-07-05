package com.formisk.areflectiveorganism;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.cmwmobile.android.samples.GalleryDialogPreference;
import com.formisk.areflectiveorganism.R;

import android.view.LayoutInflater;
import net.robotmedia.billing.BillingController;

public class WallpaperActionbarSettings extends SherlockPreferenceActivity implements ActionBar.TabListener {
	private CustomBilling customBilling = null;
	public static String purchaseName = "com.formisk.areflectiveorganism.unlockall";
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tabSettings = getSupportActionBar().newTab();
        tabSettings.setText("Settings");
        tabSettings.setTabListener(this);
        getSupportActionBar().addTab(tabSettings);

        ActionBar.Tab tabCustomColors = getSupportActionBar().newTab();
        tabCustomColors.setText("Custom Colors");
        tabCustomColors.setTabListener(this);
        getSupportActionBar().addTab(tabCustomColors);

        getSupportActionBar().setTitle("A Reflective Organism Preferences");
        
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    protected void onDestroy() {
        this.customBilling.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Sherlock_Light); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        this.getPreferenceManager().setSharedPreferencesName(AReflectiveOrganismWallpaper.SHARED_PREFS_NAME);
        this.customBilling = new CustomBilling(this, this.getPreferenceManager().getSharedPreferences(), this);
        this.customBilling.onCreate(savedInstanceState);
        addPreferences(false);
    }
    
    public void InstructionsDialog() {
    	  AlertDialog.Builder ad = new AlertDialog.Builder(this);
    	  ad.setIcon(R.drawable.portrait);
    	  ad.setTitle("About the 'A Reflective Organism' wallpaper");
    	  ad.setView(LayoutInflater.from(this).inflate(R.layout.about,null));
    	  ad.setInverseBackgroundForced(true);

    	  ad.setPositiveButton("OK", 
    	    new android.content.DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {}
    	    }
    	   );
    	  
          WallpaperActionbarSettings.this.getPreferenceManager().setSharedPreferencesName(AReflectiveOrganismWallpaper.SHARED_PREFS_NAME);
		  if(WallpaperActionbarSettings.this.getPreferenceManager().getSharedPreferences().getInt("gifted", 0) == 0) {    	  
	    	  ad.setNeutralButton("Recieve Gift", new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						AlertDialog.Builder ad2 = new AlertDialog.Builder(WallpaperActionbarSettings.this);
						ad2.setIcon(R.drawable.secondsmall);
						ad2.setTitle("Recieve Gift");
						ad2.setView(LayoutInflater.from(WallpaperActionbarSettings.this).inflate(R.layout.gift,null));
						ad2.setInverseBackgroundForced(true);
						ad2.setPositiveButton("Accept", new android.content.DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int which) {
							WallpaperActionbarSettings.this.getPreferenceManager().setSharedPreferencesName(AReflectiveOrganismWallpaper.SHARED_PREFS_NAME);
							WallpaperActionbarSettings.this.getPreferenceManager().getSharedPreferences().edit().putInt("gifted", 1).commit();
							WallpaperActionbarSettings.this.addPreferences(true);
						}});
						ad2.setNegativeButton("Decline", new android.content.DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int which) {}});
						ad2.show();
					}
	    	    }
	    	  );
		  }

    	   ad.setOnCancelListener(new DialogInterface.OnCancelListener(){
	    	     public void onCancel(DialogInterface dialog) {}
	    	     }
    	   );

    	  ad.show();
    }    
    
    @SuppressWarnings("deprecation")
	public void addPreferences(boolean prefsTab) {
    	System.out.println("addPreferences " + prefsTab);
		PreferenceScreen prefs = getPreferenceScreen();
		if(prefs != null)
			prefs.removeAll();
		this.getPreferenceManager().setSharedPreferencesName(AReflectiveOrganismWallpaper.SHARED_PREFS_NAME);
		boolean purchased = purchased = BillingController.isPurchased(this, WallpaperActionbarSettings.purchaseName);
		System.out.println("purchased: " + purchased);
		
		
		if(prefsTab) {
	    	if(purchased == true) {
	    		addPreferencesFromResource(R.xml.preferences);
	    	}
	    	else {
	    		addPreferencesFromResource(R.xml.preferencesfree);
	        	Preference buybutton = (Preference)findPreference("buybutton");
	        	buybutton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
	                public boolean onPreferenceClick(Preference arg0) {
	                	System.out.println("onPreferenceClick");
	                	//customBilling.requestPurchase("android.test.purchased");
	                	customBilling.requestPurchase(WallpaperActionbarSettings.purchaseName);
	                	return true;
	                }
	           });
	    	}
	    	GalleryDialogPreference gallery = (GalleryDialogPreference)findPreference("gallery");
	   		gallery.addBitmap(R.drawable.first);
	   		if(purchased == true) {
		   		gallery.addBitmap(R.drawable.second);
		   		gallery.addBitmap(R.drawable.third);
		   		gallery.addBitmap(R.drawable.fourth);
		   		gallery.addBitmap(R.drawable.fifth);
	   		}
	   		else {
				WallpaperActionbarSettings.this.getPreferenceManager().setSharedPreferencesName(AReflectiveOrganismWallpaper.SHARED_PREFS_NAME);
				if(WallpaperActionbarSettings.this.getPreferenceManager().getSharedPreferences().getInt("gifted", 0) == 0)
					gallery.addBitmap(R.drawable.secondfree);
				else
					gallery.addBitmap(R.drawable.second);
		   		gallery.addBitmap(R.drawable.thirdfree);
		   		gallery.addBitmap(R.drawable.fourthfree);
		   		gallery.addBitmap(R.drawable.fifthfree);
	   		}

	    	GalleryDialogPreference reflection = (GalleryDialogPreference)findPreference("environmentkey");
	    	reflection.addBitmap(R.drawable.ref1);
	   		if(purchased == true) {
		    	reflection.addBitmap(R.drawable.ref2);
		    	reflection.addBitmap(R.drawable.ref3);
		    	reflection.addBitmap(R.drawable.ref4);
		    	reflection.addBitmap(R.drawable.ref5);
		    	reflection.addBitmap(R.drawable.ref6);
		    	reflection.addBitmap(R.drawable.ref7);
		    	reflection.addBitmap(R.drawable.ref8);
		    	reflection.addBitmap(R.drawable.ref9);
	   		}
	   		else {
		    	reflection.addBitmap(R.drawable.ref2free);
		    	reflection.addBitmap(R.drawable.ref3free);
		    	reflection.addBitmap(R.drawable.ref4free);
		    	reflection.addBitmap(R.drawable.ref5free);
		    	reflection.addBitmap(R.drawable.ref6free);
		    	reflection.addBitmap(R.drawable.ref7free);
		    	reflection.addBitmap(R.drawable.ref8free);
		    	reflection.addBitmap(R.drawable.ref9free);
	   		}
	   		
	    	Preference aboutbutton = (Preference)findPreference("aboutbutton");
	    	aboutbutton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
	            public boolean onPreferenceClick(Preference arg0) { 
	            	InstructionsDialog();
	            	return true;
	            }
	        });
	    	
		 	Preference moreapps = (Preference)findPreference("moreappsbutton");
		 	moreapps.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
	            public boolean onPreferenceClick(Preference arg0) { 
	        	   String url = "https://play.google.com/store/apps/developer?id=Christoffer+Green";  
	        	   Intent i = new Intent(Intent.ACTION_VIEW);  
	        	   i.setData(Uri.parse(url));  
	        	   startActivity(i); 
	                return true;
	            }
	        });
	    	
		}
		else {
	    	if(purchased == true)
	    		addPreferencesFromResource(R.xml.custompreferences);
	    	else {
	    		addPreferencesFromResource(R.xml.custompreferencesfree);
	        	Preference buybutton = (Preference)findPreference("buybutton");
	        	buybutton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
	                public boolean onPreferenceClick(Preference arg0) {
	                	System.out.println("onPreferenceClick");
	                	//customBilling.requestPurchase("android.test.purchased");
	                	customBilling.requestPurchase(WallpaperActionbarSettings.purchaseName);
	                	return true;
	                }
	           });
	    	}
		}
    }

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		PreferenceScreen prefs = getPreferenceScreen();
		prefs.removeAll();
		if(tab.getPosition() == 0) {
			addPreferences(true);
			this.customBilling.selectedTab = true;
		}
		if(tab.getPosition() == 1) {
			addPreferences(false);
			this.customBilling.selectedTab = false;
		}
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}
