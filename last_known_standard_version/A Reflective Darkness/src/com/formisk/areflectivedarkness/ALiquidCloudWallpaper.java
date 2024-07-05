package com.formisk.areflectivedarkness;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import rajawali.wallpaper.Wallpaper;

public class ALiquidCloudWallpaper extends Wallpaper {
	private ALiquidCloudWallpaperRenderer mRenderer;
	public static final String SHARED_PREFS_NAME="liquidcloudswallpapersettings";
	public float mTime = 0.95f;
	public Bitmap texture = null;
	
	@Override
	public Engine onCreateEngine() {
		if(texture == null || texture.isRecycled())
	        texture = BitmapFactory.decodeResource(getResources(), R.drawable.clouds);
		mRenderer = new ALiquidCloudWallpaperRenderer(this, texture);
		return new WallpaperEngine(this.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE), getBaseContext(), mRenderer);
	}
}
