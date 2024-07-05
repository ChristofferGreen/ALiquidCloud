package com.formisk.acolorfulstripe;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import rajawali.renderer.RajawaliRenderer;
import rajawali.primitives.Plane;
import rajawali.primitives.Sphere;
import rajawali.Camera2D;
import rajawali.materials.SimpleMaterial;
import rajawali.materials.TextureManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;
import android.content.Context;
import android.graphics.Color;

public class AColorfulStripeWallpaperRenderer extends RajawaliRenderer {
	private String TAG = "ALiquidCloudWallpaperRenderer";
	private Plane plane;
	private AColorfulStripeMaterial aColorfulStripeMaterial;
	private Camera2D camera2D;
	private SharedPreferences mPrefs;
	public AColorfulStripeWallpaper mWallpaper;
	public Bitmap texture = null;
	protected float mOffsetX = 0.5f;
	protected float mOffsetY = 0.5f;

	public AColorfulStripeWallpaperRenderer(Context context, Bitmap texture) {
		super(context);
		mWallpaper = (AColorfulStripeWallpaper)context;
		camera2D = new Camera2D();
		this.setCamera(camera2D);
		this.setFrameRate(17);
		this.mRed = 0.5f;
		this.mGreen = 0.5f;
		this.mBlue = 0.5f;
		this.texture = texture;
		mPrefs = mWallpaper.getSharedPreferences(AColorfulStripeWallpaper.SHARED_PREFS_NAME, 0);
	}
	
	public float getOffsetX() {
		return mOffsetX;
	}

	public void setOffsetX(float offset) {
		this.mOffsetX = offset;
		if(this.mOffsetX > 1.0f)
			this.mOffsetX = this.mOffsetX % 10;
		if(this.mOffsetX < 0.0f)
			this.mOffsetX = this.mOffsetX % 10;
	}

	public float getOffsetY() {
		return mOffsetY;
	}

	public void setOffsetY(float offset) {
		this.mOffsetY = offset;
		if(this.mOffsetY > 1.0f)
			this.mOffsetY = this.mOffsetY % 10;
		if(this.mOffsetY < 0.0f)
			this.mOffsetY = this.mOffsetY % 10;
	}
	
	@Override
	public void onDrawFrame(GL10 glUnused) {
		super.onDrawFrame(glUnused);
		aColorfulStripeMaterial.setOffsetX(this.getOffsetX());
		aColorfulStripeMaterial.setOffsetY(this.getOffsetY());
		aColorfulStripeMaterial.updateUniform();
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		super.onSurfaceChanged(gl, width, height);
		if(width != 0.0f && height != 0.0f) {
			float ratio = (float)width/(float)height;
			aColorfulStripeMaterial.setRatio(ratio);
		}
		onDrawFrame(gl);
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl, config);

		int theme = 1;
		if(mWallpaper.getPackageName().toLowerCase().contains("full"))
			theme = mPrefs.getInt("gallery", 1)+1;
			
		aColorfulStripeMaterial = new BlackWhiteMaterial();
		aColorfulStripeMaterial.addTexture(mTextureManager.addTexture(texture, false, false));
		aColorfulStripeMaterial.setColor1(mPrefs.getInt("color1", Color.rgb(71, 224, 237)));
		aColorfulStripeMaterial.setColor2(mPrefs.getInt("color2", Color.rgb(255, 0, 0)));
		aColorfulStripeMaterial.setColor3(mPrefs.getInt("color3", Color.rgb(44, 94, 205)));
		aColorfulStripeMaterial.setColor4(mPrefs.getInt("color4", Color.rgb(0, 0, 0)));
		
		
		String zoom = ".";
		if(mWallpaper.getPackageName().toLowerCase().contains("full"))
			zoom = mPrefs.getString("zoomkey", ".");
		aColorfulStripeMaterial.mWallpaper = mWallpaper;
		aColorfulStripeMaterial.initUniform();
		aColorfulStripeMaterial.initZoom(zoom);
		aColorfulStripeMaterial.setOffsetX(this.getOffsetX());
		aColorfulStripeMaterial.setOffsetY(this.getOffsetY());

        plane = new Plane(1.0f, 1.0f, 1, 1, 1);
        plane.setMaterial(aColorfulStripeMaterial, true);
        addChild(plane);

		String fps = mPrefs.getString("frameratekey", ".");
		if(fps.equals("1"))
			this.setFrameRate(5);
		else if(fps.equals("2"))
			this.setFrameRate(10);
		else if(fps.equals("3"))
			this.setFrameRate(15);
		else if(fps.equals("4"))
			this.setFrameRate(20);
		else if(fps.equals("5") || fps.equals("."))
			this.setFrameRate(25);
		else if(fps.equals("6"))
			this.setFrameRate(30);

		startRendering();
		onDrawFrame(gl);
	}
	
}
