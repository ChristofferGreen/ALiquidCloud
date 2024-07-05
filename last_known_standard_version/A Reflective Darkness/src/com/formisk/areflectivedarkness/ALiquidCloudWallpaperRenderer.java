package com.formisk.areflectivedarkness;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;
import rajawali.renderer.RajawaliRenderer;
import rajawali.primitives.Plane;
import rajawali.primitives.Sphere;
import rajawali.Camera2D;
import rajawali.materials.TextureManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;

public class ALiquidCloudWallpaperRenderer extends RajawaliRenderer {
	private Plane plane;
	private ALiquidCloudMaterial aLiquidCloudMaterial;
	private Camera2D camera2D;
	private SharedPreferences mPrefs;
	public ALiquidCloudWallpaper mWallpaper;
	public Bitmap texture = null;

	public ALiquidCloudWallpaperRenderer(Context context, Bitmap texture) {
		super(context);
		mWallpaper = (ALiquidCloudWallpaper)context;
		camera2D = new Camera2D();
		this.setCamera(camera2D);
		this.setFrameRate(17);
		this.mRed = 0.5f;
		this.mGreen = 0.5f;
		this.mBlue = 0.5f;
		this.texture = texture;
		mPrefs = mWallpaper.getSharedPreferences(ALiquidCloudWallpaper.SHARED_PREFS_NAME, 0);
	}
	
	@Override
	public void onDrawFrame(GL10 glUnused) {
		super.onDrawFrame(glUnused);
		aLiquidCloudMaterial.updateUniform(mWallpaper);
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		super.onSurfaceChanged(gl, width, height);
		if(width != 0.0f && height != 0.0f) {
			float ratio = (float)width/(float)height;
			aLiquidCloudMaterial.setRatio(ratio);
		}
		onDrawFrame(gl);
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl, config);
		
		String theme = mPrefs.getString("themekey", ".");
		if(theme.equals("1") || theme.equals("."))
			aLiquidCloudMaterial = new RedWhiteBlueMaterial();
		else if(theme.equals("2"))
			aLiquidCloudMaterial = new BlackWhiteMaterial();
		else if(theme.equals("3"))
			aLiquidCloudMaterial = new PenguinMaterial();

        aLiquidCloudMaterial.addTexture(mTextureManager.addTexture(texture));
		
		String zoom = mPrefs.getString("zoomkey", ".");
		aLiquidCloudMaterial.initUniform(mWallpaper);
		aLiquidCloudMaterial.initZoom(zoom);

        plane = new Plane(1.0f, 1.0f, 1, 1, 1);
        plane.setMaterial(aLiquidCloudMaterial, true);
        addChild(plane);
        
		String fps = mPrefs.getString("frameratekey", ".");
		if(fps.equals("1"))
			this.setFrameRate(5);
		else if(fps.equals("2"))
			this.setFrameRate(10);
		else if(fps.equals("3") || fps.equals("."))
			this.setFrameRate(15);
		else if(fps.equals("4"))
			this.setFrameRate(20);
		else if(fps.equals("5"))
			this.setFrameRate(25);
		else if(fps.equals("6"))
			this.setFrameRate(30);

		startRendering();
		onDrawFrame(gl);
	}
	
	protected class SettingsUpdater implements SharedPreferences.OnSharedPreferenceChangeListener {
		private ALiquidCloudWallpaperRenderer mRenderer;
		
		public SettingsUpdater(ALiquidCloudWallpaperRenderer renderer) {
			mRenderer = renderer;
		}
		
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		}
	}
}
