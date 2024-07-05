package com.formisk.aliquidcloud;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.MotionEvent;
import rajawali.renderer.RajawaliRenderer;
import rajawali.wallpaper.Wallpaper;
import 	android.graphics.Rect;

class PokeRenderTask extends TimerTask {
	public ALiquidCloudWallpaperRenderer mRenderer;
	public float size = 0.00f;
	public float addSize = 0.06f;
	String poketype;
	public void run() {
		if(mRenderer.runAnimation == false) {
			mRenderer.finishedRipple = true;
			cancel();
		}
		if(mRenderer != null && mRenderer.runAnimation) {
			this.mRenderer.aLiquidCloudMaterialPoke.pokeSize = size*0.1f;
			size += addSize;
			//Log.d("size", "size: " + size);
			if(size > 20.0f && poketype.equals("2")) {
				size = 0.0f;
				mRenderer.changeMaterial = 1;
				mRenderer.finishedRipple = true;
				cancel();
			}
			else if(size <= 0.0f && (poketype.equals("3") || poketype.equals("4"))) {
				size = 0.0f;
				addSize = 0.06f;
				mRenderer.changeMaterial = 1;
				mRenderer.finishedRipple = true;
				cancel();
			}
		} else if(size != 0.0f){
			/*size = 0.0f;
			addSize = 0.06f;
			mRenderer.changeMaterial = 1;
			mRenderer.finishedRipple = true;
			cancel();*/
		}
	}
}

public class ALiquidCloudWallpaper extends Wallpaper {
	public ALiquidCloudWallpaperRenderer mRenderer;
	public static final String SHARED_PREFS_NAME="liquidcloudswallpapersettings";
	public Bitmap texture = null;
	public float mTime = 0.8f;
	public float mTime5 = 0.0f;
	float prevX;
	float prevY;
	float offsetX = 0.0f;
	float offsetY = 0.0f;
	private Timer mTimer = null;
	private PokeRenderTask pokeTask = null;
	
	public class MyWallpaperEngine extends WallpaperEngine {
		ALiquidCloudWallpaperRenderer mRenderer = null;
		
		public MyWallpaperEngine(SharedPreferences preferences, Context context, ALiquidCloudWallpaperRenderer renderer) {
			super(preferences, context, renderer);
			this.mRenderer = renderer;
		}
		
		@Override
		public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
			if(getPackageName().toLowerCase().contains("full")) {
				String sliding = getSharedPreferences(ALiquidCloudWallpaper.SHARED_PREFS_NAME, 0).getString("slidingkey", "2");
				if(sliding.equals("2")) {
					if(this.mRenderer != null) {
						this.mRenderer.setOffsetX(xOffset);
					}
				}
			}
		}
		
		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			if(mRenderer != null) {
				mRenderer.runAnimation = visible;
			}
			if(mRenderer != null && mRenderer.aLiquidCloudMaterial != null && visible == false) {
				mTime5 = mRenderer.aLiquidCloudMaterial.mTime5;
				mTime = mRenderer.aLiquidCloudMaterial.mTime;
			}
		}
		
		@Override
		public void onTouchEvent (MotionEvent event) {
			SharedPreferences mPrefs = getSharedPreferences(ALiquidCloudWallpaper.SHARED_PREFS_NAME, 0); 
			if(getPackageName().toLowerCase().contains("full")) {
				String poketype = mPrefs.getString("pokekey", "1");
				if(poketype.equals("2") || poketype.equals("3") || poketype.equals("4")) {
					if(event.getAction() == event.ACTION_DOWN) {
						if(mTimer == null)
							mTimer = new Timer();
						if(mRenderer.finishedRipple == true) {
							pokeTask = new PokeRenderTask();
							pokeTask.mRenderer = this.mRenderer;
							pokeTask.poketype = poketype;
							pokeTask.size = 0.0f;
							pokeTask.addSize = 0.06f;
							mRenderer.finishedRipple = false;
							mTimer.schedule(pokeTask, 0, (long) (1000.0f/60.0f));
							this.mRenderer.aLiquidCloudMaterialPoke.setPoke(1-event.getY()/this.mRenderer.mHeight, event.getX()/this.mRenderer.mWidth);
							mRenderer.changeMaterial = 2;
						}
						if(poketype.equals("2")) {
							this.mRenderer.aLiquidCloudMaterialPoke.setPoke(1-event.getY()/this.mRenderer.mHeight, event.getX()/this.mRenderer.mWidth);
							pokeTask.size = 0.0f;
						}
					}
					if(event.getAction() == event.ACTION_MOVE) {
						if(pokeTask != null)
							this.mRenderer.aLiquidCloudMaterialPoke.setPoke(1-event.getY()/this.mRenderer.mHeight, event.getX()/this.mRenderer.mWidth);
					}
					if(event.getAction() == event.ACTION_UP) {
						if(pokeTask != null && (poketype.equals("3") || poketype.equals("4")) && getPackageName().toLowerCase().contains("full"))
							pokeTask.addSize = -0.1f;
					}
				}
				String sliding = getSharedPreferences(ALiquidCloudWallpaper.SHARED_PREFS_NAME, 0).getString("slidingkey", ".");
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
		mRenderer = new ALiquidCloudWallpaperRenderer(this, texture);
		//this.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE).edit().clear().commit();
		return new MyWallpaperEngine(this.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE), getBaseContext(), mRenderer);
	}
}
