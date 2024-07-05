package com.formisk.aliquidcloud;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLES20;
import rajawali.renderer.RajawaliRenderer;
import rajawali.primitives.Plane;
import rajawali.primitives.Sphere;
import rajawali.Camera2D;
import rajawali.materials.SimpleMaterial;
import rajawali.materials.TextureManager;
import rajawali.materials.TextureManager.TextureType;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;
import android.content.Context;
import android.graphics.Color;

public class ALiquidCloudWallpaperRenderer extends RajawaliRenderer {
	
	
	protected static final String mFShaderBase = 
			"precision highp float;\n" +
			"varying vec2 vTextureCoord;\n" +
			"varying vec2 vTextureCoordPUtime5;\n" +
			"varying vec2 vTextureCoordNormal;\n" +
			"uniform sampler2D uTexture0;\n" +
			"uniform vec3 gradColor1;\n" +
			"uniform vec3 gradColor2;\n" +
			"uniform vec3 gradColor3;\n" +
			"uniform vec3 gradColor4;\n" +
			"uniform vec3 gradColor5;\n" +
			"uniform vec3 gradColor6;\n" +
			"uniform vec2 pokePosition;\n" +
			"uniform float pokeSize;\n" +
			"uniform float qmul;\n" +
			"uniform float rmul;\n" +
			"uniform float u_time;\n" +
			"uniform float ratio;\n" +
			
			"vec3 getGradient1(in float posFloat) {\n" +
			"  float clampPos = clamp(posFloat, 0.01, 0.99);\n" +
			"  return clampPos*gradColor1+(1.0-clampPos)*gradColor2;\n" +
			"}\n" +

			"vec3 getGradient2(in float posFloat) {\n" +
			"  float clampPos = clamp(posFloat, 0.01, 0.99);\n" +
			"  return clampPos*gradColor3+(1.0-clampPos)*gradColor4;\n" +
			"}\n" +

			"vec3 getGradient3(in float posFloat) {\n" +
			"  float clampPos = clamp(posFloat, 0.01, 0.99);\n" +
			"  return clampPos*gradColor5+(1.0-clampPos)*gradColor6;\n" +
			"}\n";
			
	protected static final String mFShaderBasePatternPoke = 
			"float pattern( out float q, out float r ) {\n" +
			"  float dcc = distance(vTextureCoordNormal, pokePosition);\n" +
			"  float x = dcc-pokeSize;\n" +
			"  float qPoke = (sin(x*70.0)/(x*7000.0))*4.0;\n" +
			"  q =    texture2D( uTexture0, vTextureCoord ).z;\n" +
			"  r =    texture2D( uTexture0, vTextureCoordPUtime5 + q*qmul + qPoke ).y;\n" +
			"  return texture2D( uTexture0, vTextureCoordPUtime5 + r*rmul + qPoke ).x;\n" +
			"}\n";	

	protected static final String mFShaderBasePatternBlackPoke = 
			"float pattern( out float q, out float r ) {\n" +
			"  q =    texture2D( uTexture0, vTextureCoord ).z;\n" +
			"  float qPoke = inversesqrt(distance(vTextureCoordNormal, pokePosition))*pokeSize;\n" +
			"  r =    texture2D( uTexture0, vTextureCoordPUtime5 + q*qmul + qPoke ).y;\n" +
			"  return texture2D( uTexture0, vTextureCoordPUtime5 + r*rmul + qPoke ).x;\n" +
			"}\n";	

	protected static final String mFShaderBasePatternInvertedBlackPoke = 
			"float pattern( out float q, out float r ) {\n" +
			"  q =    texture2D( uTexture0, vTextureCoord ).z;\n" +
			"  float qPoke = distance(vTextureCoordNormal, pokePosition)*pokeSize;\n" +
			"  r =    texture2D( uTexture0, vTextureCoordPUtime5 + q*qmul + qPoke ).y;\n" +
			"  return texture2D( uTexture0, vTextureCoordPUtime5 + r*rmul + qPoke ).x;\n" +
			"}\n";	

	protected static final String mFShaderBasePatternNoPoke = 
			"float pattern( out float q, out float r ) {\n" +
			"  q =    texture2D( uTexture0, vTextureCoord ).z;\n" +
			"  r =    texture2D( uTexture0, vTextureCoordPUtime5 + q*qmul ).y;\n" +
			"  return texture2D( uTexture0, vTextureCoordPUtime5 + r*rmul ).x;\n" +
			"}\n";	
	
	
	private String TAG = "ALiquidCloudWallpaperRenderer";
	private Plane plane;
	private Plane planePoke;
	public ALiquidCloudMaterial aLiquidCloudMaterial;
	public ALiquidCloudMaterial aLiquidCloudMaterialPoke;
	public ALiquidCloudMaterial aLiquidCloudMaterialNoPoke;
	public boolean runAnimation = true;
	private Camera2D camera2D;
	private SharedPreferences mPrefs;
	public ALiquidCloudWallpaper mWallpaper;
	public Bitmap texture = null;
	public float mWidth = 300;
	public float mHeight = 300;
	protected float mOffsetX = 0.5f;
	protected float mOffsetY = 0.5f;
	public int changeMaterial = 0;
	public float extraZoom = 1.0f;
	public boolean finishedRipple = true;

	public ALiquidCloudWallpaperRenderer(Context context, Bitmap texture) {
		super(context);
		mWallpaper = (ALiquidCloudWallpaper)context;
		camera2D = new Camera2D();
		this.setCamera(camera2D);
		this.setFrameRate(25);
		this.mRed = 0.5f;
		this.mGreen = 0.5f;
		this.mBlue = 0.5f;
		this.texture = texture;
		mPrefs = mWallpaper.getSharedPreferences(ALiquidCloudWallpaper.SHARED_PREFS_NAME, 0);
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
		if(runAnimation) {
			if(changeMaterial == 1) {
				aLiquidCloudMaterialNoPoke.mTime5 = aLiquidCloudMaterialPoke.mTime5;
				aLiquidCloudMaterialNoPoke.mTime = aLiquidCloudMaterialPoke.mTime;
				aLiquidCloudMaterialNoPoke.mLastTime = aLiquidCloudMaterialPoke.mLastTime;
				plane.setPosition(0.0f, 0.0f, -0.1f);
				planePoke.setPosition(0.0f, 0.0f, 0.1f);
				changeMaterial = 0;
				aLiquidCloudMaterial = aLiquidCloudMaterialNoPoke;
			} else if(changeMaterial == 2) {
				aLiquidCloudMaterialPoke.mTime5 = aLiquidCloudMaterialNoPoke.mTime5;
				aLiquidCloudMaterialPoke.mTime = aLiquidCloudMaterialNoPoke.mTime;
				aLiquidCloudMaterialPoke.mLastTime = aLiquidCloudMaterialNoPoke.mLastTime;
				plane.setPosition(0.0f, 0.0f, 0.1f);
				planePoke.setPosition(0.0f, 0.0f, -0.1f);
				changeMaterial = 0;
				aLiquidCloudMaterial = aLiquidCloudMaterialPoke;
			}
			setRatio();
			aLiquidCloudMaterial.setOffsetX(this.getOffsetX());
			aLiquidCloudMaterial.setOffsetY(this.getOffsetY());
			aLiquidCloudMaterial.updateUniform();
		}
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		super.onSurfaceChanged(gl, width, height);
		this.mWidth = width;
		this.mHeight = height;
		setRatio();
		GLES20.glEnable(GLES20.GL_DITHER);
		onDrawFrame(gl);
	}
	
	public void setRatio() {
		if(mWidth != 0.0f && mHeight != 0.0f) {
			float ratio = (float)mWidth/(float)mHeight;
			if(aLiquidCloudMaterialPoke != null)
				aLiquidCloudMaterialPoke.mRatio = ratio;
			if(aLiquidCloudMaterialNoPoke != null)
				aLiquidCloudMaterialNoPoke.mRatio = ratio;
	        if(mHeight > mWidth) {
				if(aLiquidCloudMaterialNoPoke != null)
					aLiquidCloudMaterialNoPoke.extraZoom = 1.6f;
				if(aLiquidCloudMaterialPoke != null)
					aLiquidCloudMaterialPoke.extraZoom = 1.6f;
	        }
	        else {
				if(aLiquidCloudMaterialNoPoke != null)
					aLiquidCloudMaterialNoPoke.extraZoom = 1.0f;
				if(aLiquidCloudMaterialPoke != null)
					aLiquidCloudMaterialPoke.extraZoom = 1.0f;
	        }
		}
	}
	
	public ALiquidCloudMaterial addMaterial(String fshader) {
		ALiquidCloudMaterial aLiquidCloudMaterial = null;
		int theme = 1;
		//if(mWallpaper.getPackageName().toLowerCase().contains("full")) {
			theme = mPrefs.getInt("gallery", 0)+1;
		
			String customType = mPrefs.getString("customthemetypekey",  "1");
			if(customType.equals("3") && mWallpaper.getPackageName().toLowerCase().contains("full")) {
				boolean custom2 = mPrefs.getBoolean("usesecondgradient",  false);
				boolean custom3 = mPrefs.getBoolean("usethirdgradient",  false);
				if(custom3 && custom2) {
					aLiquidCloudMaterial = new CustomMaterial3(fshader);
					aLiquidCloudMaterial.setColor1(mPrefs.getInt("color1", Color.rgb(71, 224, 237)));
					aLiquidCloudMaterial.setColor2(mPrefs.getInt("color2", Color.rgb(0, 0, 0)));
					aLiquidCloudMaterial.setColor3(mPrefs.getInt("color3", Color.rgb(44, 94, 205)));
					aLiquidCloudMaterial.setColor4(mPrefs.getInt("color4", Color.rgb(0, 0, 0)));
					aLiquidCloudMaterial.setColor5(mPrefs.getInt("color5", Color.rgb(244, 255, 40)));
					aLiquidCloudMaterial.setColor6(mPrefs.getInt("color6", Color.rgb(45, 47, 111)));
				}
				else if(custom2) {
					aLiquidCloudMaterial = new CustomMaterial2(fshader);
					aLiquidCloudMaterial.setColor1(mPrefs.getInt("color1", Color.rgb(71, 224, 237)));
					aLiquidCloudMaterial.setColor2(mPrefs.getInt("color2", Color.rgb(0, 0, 0)));
					aLiquidCloudMaterial.setColor3(mPrefs.getInt("color3", Color.rgb(44, 94, 205)));
					aLiquidCloudMaterial.setColor4(mPrefs.getInt("color4", Color.rgb(0, 0, 0)));
				}
				else {
					aLiquidCloudMaterial = new CustomMaterial1(fshader);
					aLiquidCloudMaterial.setColor1(mPrefs.getInt("color1", Color.rgb(71, 224, 237)));
					aLiquidCloudMaterial.setColor2(mPrefs.getInt("color2", Color.rgb(0, 0, 0)));
				}
			}
			else if(customType.equals("2") && mWallpaper.getPackageName().toLowerCase().contains("full")) {
				aLiquidCloudMaterial = new SimpleCustomAdditiveMaterial(fshader);
				aLiquidCloudMaterial.setColor1(mPrefs.getInt("color7", Color.rgb(71, 224, 237)));
				aLiquidCloudMaterial.setColor2(mPrefs.getInt("color8", Color.rgb(44, 94, 205)));
				aLiquidCloudMaterial.setColor3(mPrefs.getInt("color9", Color.rgb(244, 255, 40)));
			}
			else {
				if(theme == 2)
					aLiquidCloudMaterial = new BlackWhiteMaterial(fshader);
				else if(theme == 3 && mWallpaper.getPackageName().toLowerCase().contains("full"))
					aLiquidCloudMaterial = new PhosphorMaterial(fshader);
				else if(theme == 4)
					aLiquidCloudMaterial = new MetallicWaveMaterial(fshader);
				else if(theme == 5 && mWallpaper.getPackageName().toLowerCase().contains("full"))
					aLiquidCloudMaterial = new MoltenLightMaterial(fshader);
				else if(theme == 6 && mWallpaper.getPackageName().toLowerCase().contains("full"))
					aLiquidCloudMaterial = new PurpleMaterial(fshader);
				else if(theme == 7 && mWallpaper.getPackageName().toLowerCase().contains("full"))
					aLiquidCloudMaterial = new SkyMaterial(fshader);
				else if(theme == 8 && mWallpaper.getPackageName().toLowerCase().contains("full"))
					aLiquidCloudMaterial = new RainbowMaterial(fshader);
				else
					aLiquidCloudMaterial = new LibertyMaterial(fshader);
			}	
		//}
		//else
		//	aLiquidCloudMaterial = new LibertyMaterial(fshader);
        aLiquidCloudMaterial.addTexture(mTextureManager.addTexture(texture, TextureType.DIFFUSE, false, false));
		aLiquidCloudMaterial.mWallpaper = mWallpaper;
		aLiquidCloudMaterial.setOffsetX(this.getOffsetX());
		aLiquidCloudMaterial.setOffsetY(this.getOffsetY());
		String zoom = ".";
		//if(mWallpaper.getPackageName().toLowerCase().contains("full"))
			zoom = mPrefs.getString("zoomkey", ".");
		aLiquidCloudMaterial.initZoom(zoom);
		//if(mWallpaper.getPackageName().toLowerCase().contains("full")) {
			int q = mPrefs.getInt("fluffyness", 50);
			int r = mPrefs.getInt("ringiness", 20);
			aLiquidCloudMaterial.mQMul = (float)q/100.0f;
			aLiquidCloudMaterial.mRMul = (float)r/100.0f;

			String windDirection = mPrefs.getString("winddirectionkey",  "7");
			if(windDirection.equals("1")) {
				aLiquidCloudMaterial.windDirX = 0.0f;
				aLiquidCloudMaterial.windDirY = 0.0f;
			}
			else if(windDirection.equals("2")) {
				aLiquidCloudMaterial.windDirX = 0.0f;
				aLiquidCloudMaterial.windDirY = -8.0f;
			}
			else if(windDirection.equals("3")) {
				aLiquidCloudMaterial.windDirX = -8.0f;
				aLiquidCloudMaterial.windDirY = -8.0f;
			}
			else if(windDirection.equals("4")) {
				aLiquidCloudMaterial.windDirX = -8.0f;
				aLiquidCloudMaterial.windDirY = 0.0f;
			}
			else if(windDirection.equals("5")) {
				aLiquidCloudMaterial.windDirX = -8.0f;
				aLiquidCloudMaterial.windDirY = 1.0f;
			}
			else if(windDirection.equals("6")) {
				aLiquidCloudMaterial.windDirX = 0.0f;
				aLiquidCloudMaterial.windDirY = 1.0f;
			}
			else if(windDirection.equals("7")) {
				aLiquidCloudMaterial.windDirX = 1.0f;
				aLiquidCloudMaterial.windDirY = 1.0f;
			}
			else if(windDirection.equals("8")) {
				aLiquidCloudMaterial.windDirX = 1.0f;
				aLiquidCloudMaterial.windDirY = 0.0f;
			}
			else if(windDirection.equals("9")) {
				aLiquidCloudMaterial.windDirX = 1.0f;
				aLiquidCloudMaterial.windDirY = -8.0f;
			}
			String windSpeed = mPrefs.getString("windspeedkey",  "1");
			if(windSpeed.equals("1"))
				aLiquidCloudMaterial.windSpeed = 1.0f;
			else if(windSpeed.equals("2"))
				aLiquidCloudMaterial.windSpeed = 3.0f;
			else if(windSpeed.equals("3"))
				aLiquidCloudMaterial.windSpeed = 5.0f;
		//}
		
		aLiquidCloudMaterial.updateUniform();
		return aLiquidCloudMaterial;
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl, config);
		
		aLiquidCloudMaterialNoPoke = this.addMaterial(mFShaderBase+mFShaderBasePatternNoPoke);
		
		String poketype = mPrefs.getString("pokekey", ".");
		if(poketype.equals("1") || poketype.equals("."))
			aLiquidCloudMaterialPoke = this.addMaterial(mFShaderBase+mFShaderBasePatternNoPoke);
		else if(poketype.equals("2"))
			aLiquidCloudMaterialPoke = this.addMaterial(mFShaderBase+mFShaderBasePatternPoke);
		else if(poketype.equals("3")/* && mWallpaper.getPackageName().toLowerCase().contains("full")*/)
			aLiquidCloudMaterialPoke = this.addMaterial(mFShaderBase+mFShaderBasePatternBlackPoke);
		else if(poketype.equals("4") /*&& mWallpaper.getPackageName().toLowerCase().contains("full")*/)
			aLiquidCloudMaterialPoke = this.addMaterial(mFShaderBase+mFShaderBasePatternInvertedBlackPoke);
		else
			aLiquidCloudMaterialPoke = this.addMaterial(mFShaderBase+mFShaderBasePatternPoke);
		
		aLiquidCloudMaterial = aLiquidCloudMaterialNoPoke;
		aLiquidCloudMaterialPoke.mTime5 = mWallpaper.mTime5;
		aLiquidCloudMaterialPoke.mTime = mWallpaper.mTime;
		aLiquidCloudMaterialNoPoke.mTime5 = mWallpaper.mTime5;
		aLiquidCloudMaterialNoPoke.mTime = mWallpaper.mTime;
		
		setRatio();
			
		plane = new Plane(1.0f, 1.0f, 1, 1, 1);
        plane.setMaterial(aLiquidCloudMaterialNoPoke, false);
        plane.setPosition(0.0f, 0.0f, -0.1f);
        addChild(plane);

		planePoke = new Plane(1.0f, 1.0f, 1, 1, 1);
		planePoke.setMaterial(aLiquidCloudMaterialPoke, false);
		planePoke.setPosition(0.0f, 0.0f, -0.1f);
        addChild(planePoke);
        
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
		else if(fps.equals("7"))
			this.setFrameRate(40);
		else if(fps.equals("8"))
			this.setFrameRate(50);
		else if(fps.equals("9"))
			this.setFrameRate(60);

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


/*if(custom1) {
Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
int argbCol1 = mPrefs.getInt("color1", Color.rgb(0, 0, 0));
int argbCol2 = mPrefs.getInt("color2", Color.rgb(255, 255, 255));
int color1Blue = Color.blue(argbCol1);
int color1Red = Color.red(argbCol1);
int color1Green = Color.green(argbCol1);
int color2Blue = Color.blue(argbCol2);
int color2Red = Color.red(argbCol2);
int color2Green = Color.green(argbCol2);
for(int i = 0; i < 16; i++) {
	for(int j = 0; j < 255; j++) {        		
		int colorMix1Red = (int)(color1Red*((255.0-i)/255.0)+color2Red*(i/255.0));
		int colorMix1Green = (int)(color1Green*((255.0-i)/255.0)+color2Green*(i/255.0));
		int colorMix1Blue = (int)(color1Blue*((255.0-i)/255.0)+color2Blue*(i/255.0));
		int colorMixed = Color.rgb(colorMix1Red, colorMix1Green, colorMix1Blue);
		bitmap.setPixel(j, i, colorMixed);
	}
}
bitmap.prepareToDraw();
aLiquidCloudMaterial.addTexture(mTextureManager.addTexture(bitmap));
}*/
//    aLiquidCloudMaterial.addTexture(mTextureManager.addTexture(BitmapFactory.decodeResource(mContext.getResources(), R.raw.bluegradient)));