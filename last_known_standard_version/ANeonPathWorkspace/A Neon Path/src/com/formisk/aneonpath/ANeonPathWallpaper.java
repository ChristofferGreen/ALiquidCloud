package com.formisk.aneonpath;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.MotionEvent;
import android.view.View;
import rajawali.wallpaper.Wallpaper;

public class ANeonPathWallpaper extends Wallpaper {
	public static final String SHARED_PREFS_NAME = "aneonpathwallpapersettings";
	public ANeonPathRenderer renderer;
	
	public class ANeonPathWallpaperEngine extends WallpaperEngine {
		public ANeonPathRenderer renderer = null;
		public ANeonPathWallpaperEngine(SharedPreferences preferences, Context context, ANeonPathRenderer renderer) {
			super(preferences, context, renderer);
			this.renderer = renderer;
		}

		@Override
		public void onTouchEvent (MotionEvent event) {
			if(event.getAction() == MotionEvent.ACTION_DOWN && this.renderer != null) {
				this.renderer.setTouch(event.getX() / renderer.getViewportWidth(), 1.0f - (event.getY() / renderer.getViewportHeight()));
			}
		}
	}

	/*@Override
	public void onVisibilityChanged(boolean visible) {
		super.onVisibilityChanged(visible);
		if(mRenderer != null) {
			mRenderer.runAnimation = visible;
		}
		if(mRenderer != null && mRenderer.aLiquidCloudMaterial != null && visible == false) {
			mTime5 = mRenderer.aLiquidCloudMaterial.mTime5;
			mTime = mRenderer.aLiquidCloudMaterial.mTime;
		}
	}*/
	
	@Override
	public Engine onCreateEngine() {
		this.renderer = new ANeonPathRenderer(this);
		return new ANeonPathWallpaperEngine(this.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE), getBaseContext(), renderer);
	}
}