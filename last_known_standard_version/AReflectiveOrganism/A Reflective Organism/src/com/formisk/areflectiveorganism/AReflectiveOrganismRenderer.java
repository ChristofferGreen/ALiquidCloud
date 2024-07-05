package com.formisk.areflectiveorganism;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.formisk.areflectiveorganism.AReflectiveOrganismWallpaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.opengl.GLES20;
import android.view.MotionEvent;
import rajawali.BufferInfo;
import rajawali.Camera2D;
import rajawali.materials.TextureInfo;
import rajawali.materials.TextureManager.TextureType;
import rajawali.primitives.Plane;
import rajawali.renderer.RajawaliRenderer;

public class AReflectiveOrganismRenderer extends RajawaliRenderer implements SensorEventListener {
	public AReflectiveOrganismWallpaper wallpaper = null;
	public Options options = null;
	public OrganicMaterial organicMaterial = null;
	public float prevX = 0;
	public float prevY = 0;
	private float mGravity[] = new float[3];
	private final float ALPHA = 0.8f;
	private final int SENSITIVITY = 5;
	public int rotationUpdates;
	
	public Plane plane = null;
	public Camera2D camera2d = new Camera2D();

	public AReflectiveOrganismRenderer(Context context, SharedPreferences preferences) {
		super(context);
		this.wallpaper = (AReflectiveOrganismWallpaper)context;
		this.preferences = preferences;
		this.options = new Options(this, this.wallpaper, this.preferences);
		this.camera2d.setZ(-2.0f);
		this.setCamera(this.camera2d);
		this.options.readSettings();
		this.setBackgroundColor(0xffffffff);
		//printfps = false;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glEnable(GLES20.GL_DITHER);
		this.options.setRatio(width, height);
		this.organicMaterial.xRatio = this.options.xRatio;
		this.organicMaterial.yRatio = this.options.yRatio;
		super.onSurfaceChanged(gl, width, height);
	}
	
	@Override
	public void onDrawFrame(GL10 glUnused) {
		if(this.rotationUpdates >= 10) {
			this.wallpaper.rotator.calcDiff();
			this.wallpaper.rotator.pokeDown();
		}
		FloatBuffer normals = FloatBuffer.allocate(12); 
		normals.put(this.wallpaper.rotator.c1.x);normals.put(this.wallpaper.rotator.c1.y);normals.put(this.wallpaper.rotator.c1.z);
		normals.put(this.wallpaper.rotator.c2.x);normals.put(this.wallpaper.rotator.c2.y);normals.put(this.wallpaper.rotator.c2.z);
		normals.put(this.wallpaper.rotator.c3.x);normals.put(this.wallpaper.rotator.c3.y);normals.put(this.wallpaper.rotator.c3.z);
		normals.put(this.wallpaper.rotator.c4.x);normals.put(this.wallpaper.rotator.c4.y);normals.put(this.wallpaper.rotator.c4.z);
		//this.plane.getGeometry().getNormalBufferInfo().buffer.position(0);
		BufferInfo normalInfo = this.plane.getGeometry().getNormalBufferInfo();
		normals.position(0);
		this.plane.getGeometry().changeBufferData(normalInfo, normals, 0);
		this.organicMaterial.Nrotated[0] = this.wallpaper.rotator.v1.x;this.organicMaterial.Nrotated[1] = this.wallpaper.rotator.v1.y;this.organicMaterial.Nrotated[2] = this.wallpaper.rotator.v1.z;
		this.organicMaterial.cameraPosRotated[0] = this.wallpaper.rotator.v1.x;this.organicMaterial.cameraPosRotated[1] = this.wallpaper.rotator.v1.y;this.organicMaterial.cameraPosRotated[2] = this.wallpaper.rotator.v1.z;
		this.organicMaterial.updateUniform();
		super.onDrawFrame(glUnused);
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		if(this.options.settingsHasChanged()) {
			this.options.readSettings();
			this.initScene();
		}
		super.onSurfaceCreated(gl, config);
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			mGravity[0] = ALPHA * mGravity[0] + (1 - ALPHA) * event.values[0];
			mGravity[1] = ALPHA * mGravity[1] + (1 - ALPHA) * event.values[1];
			//mGravity[2] = ALPHA * mGravity[2] + (1 - ALPHA) * event.values[2];
			this.wallpaper.rotator.deviceRotationX = ((event.values[1] - mGravity[1]* SENSITIVITY)*0.15f)/3.82f;
			this.wallpaper.rotator.deviceRotationY = ((event.values[0] - mGravity[0] * SENSITIVITY)*0.15f)/3.82f;
			if(this.options.rotationInvert == 2)
				this.wallpaper.rotator.deviceRotationX = -this.wallpaper.rotator.deviceRotationX;
			if(this.options.rotationInvert == 3)
				this.wallpaper.rotator.deviceRotationY = -this.wallpaper.rotator.deviceRotationY;
			if(this.options.rotationInvert == 4) {
				this.wallpaper.rotator.deviceRotationX = -this.wallpaper.rotator.deviceRotationX;
				this.wallpaper.rotator.deviceRotationY = -this.wallpaper.rotator.deviceRotationY;
			}
			/*if(this.rotationUpdates < 10) {
				this.wallpaper.rotator.pokeRotationX = this.wallpaper.rotator.prevPokeRotationX;
				this.wallpaper.rotator.pokeRotationY = this.wallpaper.rotator.prevPokeRotationY;
				this.wallpaper.rotator.prevDeviceRotationX = this.wallpaper.rotator.deviceRotationX;
				this.wallpaper.rotator.prevDeviceRotationY = this.wallpaper.rotator.deviceRotationY;
			}*/
		}
		this.rotationUpdates++;
	}

	@Override
	public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
		if((this.options.screenSliding == 2 || this.options.screenSliding == 3) && this.getViewportWidth() != 0)
			this.organicMaterial.sliding = -((float)xPixelOffset)/((float)this.getViewportWidth())*2.0f+0.2f;
	}
	
	@Override
    public void onTouchEvent(MotionEvent ev) {
		if(ev.getActionMasked() == MotionEvent.ACTION_DOWN)
			this.wallpaper.rotator.pokeDown();
		if(this.options.screenSliding > 2) { 
			if(ev.getAction()==ev.ACTION_DOWN) {
				this.prevX = 0;
				this.prevY = 0;
			}
			if(this.organicMaterial != null) {
				float x,y;
				if(prevX == 0 && prevY == 0) {
					x = 0;
					y = 0;
				}
				else {
					x = ev.getX()-prevX;
					y = ev.getY()-prevY;
				}
				this.wallpaper.rotator.pokeRotationX += y;
				this.wallpaper.rotator.pokeRotationY -= x;
				//System.out.println(this.wallpaper.rotator.pokeRotationX + " " + this.wallpaper.rotator.pokeRotationY);
				prevX = ev.getX();
				prevY = ev.getY();
			}
		}
	}
	
	@Override
	public void initScene() {
		this.setFrameRate(this.options.frameRate);
		
		if(this.options.initialized == false || this.options.changed == 1) {
			System.out.println("initializing");
			this.wallpaper.rotator.poke = this.options.screenSliding;
			this.clearChildren();
			this.plane = new Plane(1, 1, 1, 1, 1);
			this.plane.setDoubleSided(true);
			this.addChild(this.plane);

			int environment = 1;
			if(this.options.environmentReflection > 3)
				environment = 2;
			if(this.options.environmentReflection > 6)
				environment = 3;
			this.wallpaper.textureLoader.load(this.options.galleryTheme, environment, mTextureManager);

			this.organicMaterial = new OrganicMaterial();
			while(this.wallpaper.textureLoader.textureDiffuse == null) {System.out.println("loading texture textureDiffuse");}
	        this.organicMaterial.addTexture(this.wallpaper.textureLoader.diffuseTexture);
			while(this.wallpaper.textureLoader.textureNormal == null) {System.out.println("loading texture textureNormal");}
	        this.organicMaterial.addTexture(this.wallpaper.textureLoader.normalTexture);
	        while(this.wallpaper.textureLoader.envTexture == null) {System.out.println("loading env texture");}
	        this.organicMaterial.addTexture(this.wallpaper.textureLoader.envTexture);
	        this.organicMaterial.userReflection = this.options.userReflection/100.0f;
	        this.organicMaterial.userMovement = this.options.userMovement/100.0f;
	        this.organicMaterial.userDisplacement = this.options.userDisplacement/100.0f;

			if(this.options.usecustomtheme == 1) {
				if(this.options.galleryTheme == 0) {
			        this.organicMaterial.color1[0] = 0;
			        this.organicMaterial.color1[1] = 29;
			        this.organicMaterial.color1[2] = 255;
			        this.organicMaterial.color2[0] = 164;
			        this.organicMaterial.color2[1] = 0;
			        this.organicMaterial.color2[2] = 225;
				}
				if(this.options.galleryTheme == 1) {
			        this.organicMaterial.color1[0] = 0;
			        this.organicMaterial.color1[1] = 0;
			        this.organicMaterial.color1[2] = 0;
			        this.organicMaterial.color2[0] = 0;
			        this.organicMaterial.color2[1] = 0;
			        this.organicMaterial.color2[2] = 0;
				}
				if(this.options.galleryTheme == 2) {
			        this.organicMaterial.color1[0] = 2;
			        this.organicMaterial.color1[1] = 251;
			        this.organicMaterial.color1[2] = 39;
			        this.organicMaterial.color2[0] = 23;
			        this.organicMaterial.color2[1] = 173;
			        this.organicMaterial.color2[2] = 230;
				}
				if(this.options.galleryTheme == 3) {
			        this.organicMaterial.color1[0] = 255;
			        this.organicMaterial.color1[1] = 0;
			        this.organicMaterial.color1[2] = 0;
			        this.organicMaterial.color2[0] = 0;
			        this.organicMaterial.color2[1] = 240;
			        this.organicMaterial.color2[2] = 69;
				}
				if(this.options.galleryTheme == 4) {
			        this.organicMaterial.color1[0] = 9;
			        this.organicMaterial.color1[1] = 9;
			        this.organicMaterial.color1[2] = 255;
			        this.organicMaterial.color2[0] = 255;
			        this.organicMaterial.color2[1] = 9;
			        this.organicMaterial.color2[2] = 15;
				}
				
			}
			else {
				this.organicMaterial.color1 = this.options.color1;
				this.organicMaterial.color2 = this.options.color2;
			}

	        this.wallpaper.rotator.clear();    
	        if(this.options.environmentReflection == 1)
	        	this.wallpaper.rotator.setEnvironmentRotation(-357, 45);
	        if(this.options.environmentReflection == 2)
	        	this.wallpaper.rotator.setEnvironmentRotation(431, -319);
	        if(this.options.environmentReflection == 3)
	        	this.wallpaper.rotator.setEnvironmentRotation(188, 280f);
	        if(this.options.environmentReflection == 4)
	        	this.wallpaper.rotator.setEnvironmentRotation(390, -43);
	        if(this.options.environmentReflection == 5)
	        	this.wallpaper.rotator.setEnvironmentRotation(-27, 775f);
	        if(this.options.environmentReflection == 6)
	        	this.wallpaper.rotator.setEnvironmentRotation(296, 635);
	        if(this.options.environmentReflection == 7)
	        	this.wallpaper.rotator.setEnvironmentRotation(-1752, -1874);
	        if(this.options.environmentReflection == 8)
	        	this.wallpaper.rotator.setEnvironmentRotation(-2916, -828);
	        if(this.options.environmentReflection == 9)
	        	this.wallpaper.rotator.setEnvironmentRotation(-1736, -1472);
	        
	        this.wallpaper.rotator.init();

			this.plane.setMaterial(this.organicMaterial);
		}
		
		startRendering();
		this.options.initialized = true;
		this.organicMaterial.time = this.wallpaper.time;
	}

}
