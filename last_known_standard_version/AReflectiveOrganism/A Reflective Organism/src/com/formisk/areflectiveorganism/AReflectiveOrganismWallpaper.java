package com.formisk.areflectiveorganism;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.View;
import rajawali.wallpaper.Wallpaper;

public class AReflectiveOrganismWallpaper extends Wallpaper {
	public static final String SHARED_PREFS_NAME = "aneonpathwallpapersettings";
	public AReflectiveOrganismRenderer renderer = null;
	private SensorManager mSensorManager;
	public TextureLoader textureLoader = null;
	public Rotator rotator = new Rotator();
	public float time = 0.0f;
	
	public class ANeonPathWallpaperEngine extends WallpaperEngine {
		public AReflectiveOrganismRenderer renderer = null;
		public AReflectiveOrganismWallpaper wallpaper = null;
		
		public ANeonPathWallpaperEngine(SharedPreferences preferences, Context context, AReflectiveOrganismRenderer renderer, AReflectiveOrganismWallpaper wallpaper) {
			super(preferences, context, renderer, false);
			this.wallpaper = wallpaper;
			this.renderer = renderer;
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			if(renderer != null && visible == false) {
				System.out.println("stopping tilt");
				this.wallpaper.mSensorManager.unregisterListener((SensorEventListener) this.renderer);
				this.renderer.rotationUpdates = 0;
				if(this.renderer.organicMaterial != null)
					time = this.renderer.organicMaterial.time;
			}
			else if(renderer != null && visible == true) {
				System.out.println("starting tilt");
				this.wallpaper.mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
				this.wallpaper.mSensorManager.registerListener((SensorEventListener) this.renderer, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
			}
			else
				System.out.println("tilt panic");
		}
	}

	@Override
	public Engine onCreateEngine() {
		SharedPreferences preferences = this.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
		this.renderer = new AReflectiveOrganismRenderer(this, preferences);
		this.textureLoader = new TextureLoader(this);
		return new ANeonPathWallpaperEngine(preferences, getBaseContext(), renderer, this);
	}
	
}