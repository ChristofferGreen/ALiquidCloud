package com.formisk.areflectiveorganism;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class OpenActivity extends Activity {
	private int REQUEST_CODE = 1;

/*
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 16) {
			intent.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
			String pkg = AReflectiveOrganismWallpaper.class.getPackage().getName();
			String cls = AReflectiveOrganismWallpaper.class.getCanonicalName();
			intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
					new ComponentName(pkg, cls));
		} else {
	        intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
	        startActivityForResult(intent, REQUEST_CODE);
	        Toast toast = Toast.makeText(this, "Choose 'A Reflective Organism' in the list to start the Live Wallpaper.", Toast.LENGTH_LONG);
	        toast.show();
		}
*/	
	@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        Toast toast = Toast.makeText(this, "Choose 'A Reflective Organism' in the list to start the Live Wallpaper.", Toast.LENGTH_LONG);
        toast.show();

        
        Intent intent = new Intent();
        intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        startActivityForResult(intent, REQUEST_CODE);
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE)
            finish();
    }
}
