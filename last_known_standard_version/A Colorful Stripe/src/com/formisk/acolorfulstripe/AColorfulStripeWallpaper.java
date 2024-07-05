package com.formisk.acolorfulstripe;

import java.io.InputStream;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.MotionEvent;
import rajawali.renderer.RajawaliRenderer;
import rajawali.wallpaper.Wallpaper;
import 	android.graphics.Rect;

public class AColorfulStripeWallpaper extends Wallpaper {
	private AColorfulStripeWallpaperRenderer mRenderer;
	public static final String SHARED_PREFS_NAME="acolorfullstripewallpapersettings";
	public float mTime = 0.95f;
	public Bitmap texture = null;
	float prevX;
	float prevY;
	float offsetX = 0.0f;
	float offsetY = 0.0f;
	
	public class MyWallpaperEngine extends WallpaperEngine {
		AColorfulStripeWallpaperRenderer mRenderer = null;
		
		public MyWallpaperEngine(SharedPreferences preferences, Context context, AColorfulStripeWallpaperRenderer renderer) {
			super(preferences, context, renderer);
			this.mRenderer = renderer;
		}
		
		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
			if(getPackageName().toLowerCase().contains("full")) {
				String sliding = getSharedPreferences(AColorfulStripeWallpaper.SHARED_PREFS_NAME, 0).getString("slidingkey", "1");
				if(sliding.equals("2")) {
					if(this.mRenderer != null) {
						this.mRenderer.setOffsetX(xOffset);
					}
				}
			}
		}

		@Override
		public void onTouchEvent (MotionEvent event) {
			if(getPackageName().toLowerCase().contains("full")) {
				String sliding = getSharedPreferences(AColorfulStripeWallpaper.SHARED_PREFS_NAME, 0).getString("slidingkey", "1");
				if(sliding.equals("3")) {
					if(event.getAction() == event.ACTION_DOWN) {
						prevX = event.getX();
						prevY = event.getY();
						offsetX = this.mRenderer.getOffsetX();
						offsetY = this.mRenderer.getOffsetY();
					}
					if(event.getAction() == event.ACTION_MOVE) {
						offsetX = offsetX-((event.getX()-prevX)/1200.0f);
						offsetY = offsetY+((event.getY()-prevY)/1200.0f);
						this.mRenderer.setOffsetX(offsetX);
						this.mRenderer.setOffsetY(offsetY);
						prevX = event.getX();
						prevY = event.getY();
					}
					if(event.getAction() == event.ACTION_UP) {
						offsetX = this.mRenderer.getOffsetX();
						offsetY = this.mRenderer.getOffsetY();
					}
				}
			}
			//Log.d("moo", "MotionEvent " + event.ACTION_MOVE + " x: " + event.getX() + " y: " + event.getY() + " toolMajor: " + event.PointerCoords.toolMajor);
		}
	}
	
	@Override
	public Engine onCreateEngine() {
		if(texture == null || texture.isRecycled()) {
	        //texture = BitmapFactory.decodeResource(getResources(), R.raw.clouds);
	        //texture = BitmapFactory.decodeResource(getResources(), R.raw.clouds512);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inDither = true;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			options.inScaled = false;
	        InputStream is = getResources().openRawResource(R.raw.clouds512);
	        texture = BitmapFactory.decodeStream(is, new Rect(0, 0, 512, 512), options);
		}
		mRenderer = new AColorfulStripeWallpaperRenderer(this, texture);
		return new MyWallpaperEngine(this.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE), getBaseContext(), mRenderer);
	}
}
