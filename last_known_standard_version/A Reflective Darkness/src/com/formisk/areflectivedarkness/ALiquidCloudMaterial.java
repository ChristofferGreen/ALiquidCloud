package com.formisk.areflectivedarkness;

import android.opengl.GLES20;
import rajawali.materials.AMaterial;
import android.os.SystemClock;

public class ALiquidCloudMaterial extends AMaterial {
	
	protected static final String mFShaderBase = 
			"precision mediump float;\n" +
			"varying vec2 vTextureCoord;\n" +
			"uniform sampler2D uTexture0;\n" +
			"uniform float u_time;\n" +
			"uniform float u_time5;\n" +
			
			"float fbm(vec2 p) {\n" +
			"  return texture2D(uTexture0, p).x;\n" +
			"}\n" +
			
			"float fbmMiddle(vec2 p) {\n" +
			"  return texture2D(uTexture0, p).y;\n" +
			"}\n" +

			"float fbmSmooth(vec2 p) {\n" +
			"  return texture2D(uTexture0, p).z;\n" +
			"}\n" +
			
			"float pattern( in vec2 pNormal, out vec2 q, out float r ) {\n" +
			"  q.x = fbmSmooth( pNormal );\n" +
			"  q.y = fbmSmooth( pNormal );\n" +
			"  r = fbmMiddle( pNormal + (( q * 0.5 ) + u_time5) );\n" +
			"  return fbm( pNormal + r*0.2 + u_time5);\n" +
			"}\n" +
			
			"void main() {\n" +
			"   vec2 q;\n" +
			"	float r;\n" +
			"   float col = pattern(vTextureCoord, q, r);\n";
	
	protected static final String mVShader = 
			"uniform mat4 uMVPMatrix;\n" +
			"attribute vec4 aPosition;\n" +
			"attribute vec2 aTextureCoord;\n" +
			"varying vec2 vTextureCoord;\n" +
			"uniform float u_time;\n" +
			"uniform float u_time5;\n" +
			"uniform float zoom;\n" +
			"uniform float ratio;\n" +

			"void main() {\n" +
			"	gl_Position = uMVPMatrix * aPosition;\n" +
			"	vTextureCoord = aTextureCoord;\n" + // copy texture coordinates
			"	vTextureCoord.y = vTextureCoord.y*ratio;\n" + // correct coordinates for aspect ratio
			"	vTextureCoord = vTextureCoord*zoom;\n" + // correct coordinates for zoom
			"	vTextureCoord = vTextureCoord+u_time;\n" + // apply time based animation
			"}\n"; 

	public ALiquidCloudMaterial(String vertexShader, String fragmentShader) {
		super(vertexShader, fragmentShader);
	}
	
	public void initUniform(ALiquidCloudWallpaper mWallpaper) {
		mTimeLoc = GLES20.glGetUniformLocation ( mProgram, "u_time" );
		mTime5Loc = GLES20.glGetUniformLocation ( mProgram, "u_time5" );
		mZoomLoc = GLES20.glGetUniformLocation ( mProgram, "zoom" );
		mRatioLoc = GLES20.glGetUniformLocation ( mProgram, "ratio" );
		mTime = mWallpaper.mTime;
	}
	
	public void initZoom(String zoom) {
		if(zoom.equals("1"))
			mZoom = 0.4f;
		else if(zoom.equals("2") || zoom.equals("."))
			mZoom = 0.55f;
		else if(zoom.equals("3"))
			mZoom = 0.7f;
	}
	
	public void updateUniform(ALiquidCloudWallpaper mWallpaper) {
		if (mLastTime == 0)
			mLastTime = SystemClock.uptimeMillis();
		long curTime = SystemClock.uptimeMillis();
		long elapsedTime = curTime - mLastTime;
		float deltaTime = elapsedTime / 200000.0f;
		mLastTime = curTime;
		if(mTime >= 1.0f)
			mTime = 0.0f;
		mTime += deltaTime;
		mTime5 = mTime*5.0f;
		GLES20.glUniform1f ( mTimeLoc, mTime );
		GLES20.glUniform1f ( mTime5Loc, mTime5 );
		GLES20.glUniform1f ( mZoomLoc, mZoom );
		GLES20.glUniform1f ( mRatioLoc, mRatio );
		mWallpaper.mTime = mTime;
	}
	
	public void setRatio(float ratio) {
		this.mRatio = ratio;
	}
	
	private float mTime = 0.8f;
	private float mTime5 = 0.0f;
	private float mRatio = 1.0f;
	private long mLastTime = 0;
	private float mZoom = 15.0f;
	private int mRatioLoc;
	private int mTimeLoc;
	private int mTime5Loc;
	private int mZoomLoc;
}
