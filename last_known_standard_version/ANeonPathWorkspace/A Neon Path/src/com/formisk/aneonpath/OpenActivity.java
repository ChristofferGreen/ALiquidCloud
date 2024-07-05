package com.formisk.aneonpath;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class OpenActivity extends Activity {
	private int REQUEST_CODE = 1;

	@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        Toast toast = Toast.makeText(this, "Choose 'A Neon Path' in the list to start the Live Wallpaper.", Toast.LENGTH_LONG);
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
