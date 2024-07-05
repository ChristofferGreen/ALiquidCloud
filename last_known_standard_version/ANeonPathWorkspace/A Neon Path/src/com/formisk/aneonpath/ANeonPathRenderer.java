package com.formisk.aneonpath;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.formisk.aneonpath.ANeonPathWallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.opengl.GLES20;
import rajawali.BaseObject3D;
import rajawali.filters.SepiaFilter;
import rajawali.filters.SwirlFilter;
import rajawali.filters.TouchRippleFilter;
import rajawali.materials.SimpleMaterial;
import rajawali.materials.TextureInfo;
import rajawali.parser.ObjParser;
import rajawali.primitives.Plane;
import rajawali.renderer.PostProcessingRenderer.PostProcessingQuality;
import rajawali.renderer.RajawaliRenderer;

public class ANeonPathRenderer extends RajawaliRenderer {
	private String TAG = "ANeonPathRenderer";
	public ANeonPathWallpaper wallpaper = null;
	public GroundManager groundManager = null;
	public SkyObject skyObject = null;
	public SkyObject skyObjectReflection = null;
	public SkyObject skyEffectObject = null;
	public SkyObject skyEffectObjectReflection = null;
	public NeonPathManager neonPathManager = null;
	public NorthernLights northernLights = null;
	public Options options = null;
	public TextureInfo textureInfo = null;
	public TextureInfo textureInfo2 = null;
	public TextureInfo texturePatternInfo = null;
	public TextureInfo texturePatternInfo2 = null;
	public TextureInfo textureNorthernLightsInfo = null;
	private long frameCount;
	private long previousFrameTime = 0;
	public TouchRippleFilter mRippleTouchFilter = new TouchRippleFilter();

	public ANeonPathRenderer(Context context) {
		super(context);
		this.wallpaper = (ANeonPathWallpaper)context;
		this.preferences = this.wallpaper.getSharedPreferences(ANeonPathWallpaper.SHARED_PREFS_NAME, 0);
		this.options = new Options(this, this.wallpaper, this.preferences);
		this.getCamera().setPosition(0.5f, 1.0f, 1.0f);
		this.getCamera().setRotation(-10.0f, 0.0f, 0.0f);
		this.getCamera().setNearPlane(0.3f);
		this.options.readSettings();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glEnable(GLES20.GL_DITHER);
		//GLES20.glDisable(GLES20.GL_DITHER); 
		this.options.setRatio(width, height);
		this.mRippleTouchFilter.setScreenSize(width, height);
		super.onSurfaceChanged(gl, width, height);
	}
	
	@Override
	public void onDrawFrame(GL10 glUnused) {
		super.onDrawFrame(glUnused);
		if(this.previousFrameTime == 0)
			this.previousFrameTime = System.nanoTime();
		long estimatedTime = System.nanoTime() - previousFrameTime;
		float deltaTime = estimatedTime/1000000000.0f;
		float speed = 0.4f*deltaTime;
		if(this.options.cameraSpeed == 0)
			speed = 0.1f*deltaTime;
		else if(this.options.cameraSpeed == 2)
			speed = 1.4f*deltaTime;
		this.groundManager.drawNextFrame(speed);
		this.neonPathManager.drawNextFrame(speed);
		if(this.skyObject != null) {
			this.skyObject.drawNextFrame();
			this.skyObjectReflection.drawNextFrame();
		}
		if(this.skyEffectObject != null) {
			this.skyEffectObject.drawNextFrame();
			this.skyEffectObjectReflection.drawNextFrame();
		}

		if(this.northernLights != null)
			this.northernLights.drawNextFrame();

		if(this.mRippleTouchFilter != null)
			this.mRippleTouchFilter.setTime((float) frameCount++ *.05f);
		
		this.previousFrameTime = System.nanoTime();
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		if(this.options.settingsHasChanged()) {
			System.out.println("Settings have changed");
			this.options.readSettings();
			this.initScene();
			/*this.neonPathManager.reload(this.options.numberOfPaths, this.options.colorList);
			this.removeChild(this.groundManager);
			this.neonPathManager.addPathReflectionToRenderer();
			this.addChild(this.groundManager);
			this.neonPathManager.addPathToRenderer();*/
			this.mRippleTouchFilter.setScreenSize(mViewportWidth, mViewportHeight);
		}
		this.previousFrameTime = 0;
		super.onSurfaceCreated(gl, config);
	}

	@Override
	public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
		if(this.options.screenSliding == 2 && this.getViewportWidth() != 0)
			this.getCamera().setX(-((float)xPixelOffset)/((float)this.getViewportWidth())*2.0f+0.2f);
	}
	
	@Override
	public void initScene() {
		this.clearChildren();
		
		if(this.textureInfo == null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inDither = true;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			options.inScaled = false;
	        InputStream is = this.wallpaper.getResources().openRawResource(R.raw.clouds512);
	        Bitmap texture = BitmapFactory.decodeStream(is, new Rect(0, 0, 512, 512), options);
	        this.textureInfo = this.getTextureManager().addTexture(texture);
	        this.textureInfo2 = this.getTextureManager().addTexture(texture);
		}
		if(this.texturePatternInfo == null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inDither = true;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			options.inScaled = false;
	        InputStream is = this.wallpaper.getResources().openRawResource(R.raw.patterns);
	        Bitmap texture = BitmapFactory.decodeStream(is, new Rect(0, 0, 512, 512), options);
	        this.texturePatternInfo = this.getTextureManager().addTexture(texture);
	        this.texturePatternInfo2 = this.getTextureManager().addTexture(texture);
		}
		if(this.textureNorthernLightsInfo == null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inDither = true;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			options.inScaled = false;
	        InputStream is = this.wallpaper.getResources().openRawResource(R.raw.northernlights);
	        Bitmap texture = BitmapFactory.decodeStream(is, new Rect(0, 0, 512, 512), options);
	        this.textureNorthernLightsInfo = this.getTextureManager().addTexture(texture);
		}

		
		this.neonPathManager = new NeonPathManager(this, this.options.numberOfPaths, this.options.colorList, this.textureInfo);

		if(this.options.quality > 0) {
			if(this.options.skyEffect == 1 || this.options.skyEffect == 3) {
				this.skyObjectReflection = new SkyObject(this, true, this.textureInfo, false);
				this.addChild(this.skyObjectReflection);
				this.skyObject = new SkyObject(this, false, this.textureInfo, false);
				this.addChild(this.skyObject);
			}
			if(this.options.skyEffect > 1) {
				this.skyEffectObjectReflection = new SkyObject(this, true, this.texturePatternInfo, true);
				this.skyEffectObjectReflection.setZ(this.skyEffectObjectReflection.getZ()-0.2f);
				this.addChild(this.skyEffectObjectReflection);
				this.skyEffectObject = new SkyObject(this, false, this.texturePatternInfo, true);
				this.skyEffectObject.setZ(this.skyEffectObject.getZ()-0.2f);
				this.addChild(this.skyEffectObject);
			}

			if(this.options.northernLights == 1) {
				this.northernLights = new NorthernLights(this, this.textureNorthernLightsInfo, this.options.northernlightcolor);
				this.addChild(this.northernLights);
			}
			
			if(this.options.quality > 1)
				this.neonPathManager.addPathReflectionToRenderer();
		}
		
		this.groundManager = new GroundManager(this, this.texturePatternInfo2);
		this.addChild(this.groundManager);

		this.neonPathManager.addPathToRenderer();
		
		this.clearPostProcessingFilters();
		
	
		if(this.options.screenEffect == 1)
			this.addPostProcessingFilter(new SepiaFilter());
		else if(this.options.screenEffect == 2) {
			this.mRippleTouchFilter = new TouchRippleFilter();
			this.mRippleTouchFilter.setRippleSize(80);
			this.mRippleTouchFilter.setDuration(2.0f);
			this.addPostProcessingFilter(this.mRippleTouchFilter);
		}

		if(this.options.screenEffectQuality == 0) {
			this.mPostProcessingRenderer.setQuality(PostProcessingQuality.LOW);
			this.mPostProcessingRenderer.setQuadSegments(20);
		}
		else if(this.options.screenEffectQuality == 1) {
			this.mPostProcessingRenderer.setQuality(PostProcessingQuality.MEDIUM);
			this.mPostProcessingRenderer.setQuadSegments(40);
		}
		else if(this.options.screenEffectQuality == 2) {
			this.mPostProcessingRenderer.setQuality(PostProcessingQuality.HIGH);
			this.mPostProcessingRenderer.setQuadSegments(80);
		}
		
		startRendering();
		this.options.initialized = true;
	}
	
	public void setTouch(float x, float y) {
		System.out.println("Touch");
		this.mRippleTouchFilter.addTouch(x, y, frameCount *.05f);
	}
	
}
