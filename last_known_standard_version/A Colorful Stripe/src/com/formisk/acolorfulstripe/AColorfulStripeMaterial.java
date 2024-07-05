package com.formisk.acolorfulstripe;

import android.graphics.Color;
import android.opengl.GLES20;
import rajawali.materials.AMaterial;
import android.os.SystemClock;
import android.util.Log;

public class AColorfulStripeMaterial extends AMaterial {
	
	protected static final String mFShaderBase = 
			"precision highp float;\n" +
			"varying vec2 vTextureCoord;\n" +
			"varying vec2 vTextureCoordPUtime5;\n" +
			"uniform sampler2D uTexture0;\n" +
			"uniform vec3 gradColor1;\n" +
			"uniform vec3 gradColor2;\n" +
			"uniform vec3 gradColor3;\n" +
			"uniform vec3 gradColor4;\n" +
			"uniform vec3 gradColor5;\n" +
			"uniform vec3 gradColor6;\n" +
			"uniform float qmul;\n" +
			"uniform float rmul;\n" +
			
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
			"}\n" +
			
			"float pattern( out float q, out float r ) {\n" +
			"  q =    texture2D( uTexture0, vTextureCoord ).z;\n" +
			"  r =    texture2D( uTexture0, vTextureCoordPUtime5 + q*qmul ).y;\n" +
			"  return texture2D( uTexture0, vTextureCoordPUtime5 ).x;\n" +
			"}\n";
	
	protected static final String mVShader = 
			"uniform mat4 uMVPMatrix;\n" +
			"attribute vec4 aPosition;\n" +
			"attribute vec2 aTextureCoord;\n" +
			"varying vec2 vTextureCoord;\n" +
			"varying vec2 vTextureCoordPUtime5;\n" +
			"uniform float u_time;\n" +
			"uniform float u_time5;\n" +
			"uniform float zoom;\n" +
			"uniform float ratio;\n" +
			"uniform float offsetX;\n" +
			"uniform float offsetY;\n" +
			"uniform float qmul;\n" +
			"uniform float rmul;\n" +
			
			"void main() {\n" +
			"	gl_Position = uMVPMatrix * aPosition;\n" +
			"	vTextureCoord.x = aTextureCoord.x*0.01;\n" + // copy texture coordinates
			"	vTextureCoord.y = aTextureCoord.y;\n" + // copy texture coordinates
			"	vTextureCoord.y = vTextureCoord.y*ratio;\n" + // correct coordinates for aspect ratio
			"	vTextureCoord = vTextureCoord*zoom;\n" + // correct coordinates for zoom
			"	vTextureCoord = vTextureCoord+u_time;\n" + // apply time based animation
			"	vTextureCoord.y = vTextureCoord.y+(offsetX/2.0);\n" + // apply wallpaper screen offset based animation
			"	vTextureCoord.x = vTextureCoord.x+(offsetY/2.0);\n" + // apply wallpaper screen offset based animation
			"   vTextureCoordPUtime5 = vTextureCoord+u_time5;\n" +
			"}\n"; 

	public AColorfulStripeMaterial(String vertexShader, String fragmentShader) {
		super(vertexShader, fragmentShader);

		mTimeLoc = GLES20.glGetUniformLocation ( mProgram, "u_time" );
		mTime5Loc = GLES20.glGetUniformLocation ( mProgram, "u_time5" );
		mZoomLoc = GLES20.glGetUniformLocation ( mProgram, "zoom" );
		mRatioLoc = GLES20.glGetUniformLocation ( mProgram, "ratio" );
		mOffsetXLoc = GLES20.glGetUniformLocation ( mProgram, "offsetX" );
		mOffsetYLoc = GLES20.glGetUniformLocation ( mProgram, "offsetY" );
		mQMulLoc = GLES20.glGetUniformLocation ( mProgram, "qmul" );
		mRMulLoc = GLES20.glGetUniformLocation ( mProgram, "rmul" );
		mCol1Loc = GLES20.glGetUniformLocation ( mProgram, "gradColor1" );
		mCol2Loc = GLES20.glGetUniformLocation ( mProgram, "gradColor2" );
		mCol3Loc = GLES20.glGetUniformLocation ( mProgram, "gradColor3" );
		mCol4Loc = GLES20.glGetUniformLocation ( mProgram, "gradColor4" );
		mCol5Loc = GLES20.glGetUniformLocation ( mProgram, "gradColor5" );
		mCol6Loc = GLES20.glGetUniformLocation ( mProgram, "gradColor6" );
	}
	
	public void initUniform() {
		if(mWallpaper != null)
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
	
	public void setQMul(float amount) {
		this.mQMul = amount;
		GLES20.glUniform1f ( mQMulLoc, mQMul);
	}

	public void setRMul(float amount) {
		this.mRMul = amount;
		GLES20.glUniform1f ( mRMulLoc, mRMul);
	}

	public void setRatio(float ratio) {
		this.mRatio = ratio;
	}

	public void setOffsetX(float offset) {
		this.mOffsetX = offset;
	}	

	public void setOffsetY(float offset) {
		this.mOffsetY = offset;
	}	
	
	public void setColor1(int color) {
		this.mColor1 = color;
	}

	public void setColor2(int color) {
		this.mColor2 = color;
	}

	public void setColor3(int color) {
		this.mColor3 = color;
	}

	public void setColor4(int color) {
		this.mColor4 = color;
	}

	public void setColor5(int color) {
		this.mColor5 = color;
	}

	public void setColor6(int color) {
		this.mColor6 = color;
	}
	
	public void updateUniform() {
		if (mLastTime == 0)
			mLastTime = SystemClock.uptimeMillis();
		long curTime = SystemClock.uptimeMillis();
		this.elapsedTime = curTime - mLastTime;
		float deltaTime = elapsedTime / 200000.0f;
		mLastTime = curTime;
		if(mTime >= 1.0f)
			mTime = 0.0f;
		mTime += deltaTime/3.0;
		mTime5 = (mTime*5.0f);
		mTime5 = mTime5-((int)mTime5);
		//Log.d("time", mTime + " " + mTime5);
		GLES20.glUniform1f ( mTimeLoc, mTime );
		GLES20.glUniform1f ( mTime5Loc, mTime5 );
		GLES20.glUniform1f ( mRatioLoc, mRatio );
		GLES20.glUniform1f ( mZoomLoc, mZoom );
		GLES20.glUniform1f ( mOffsetXLoc, mOffsetX );
		GLES20.glUniform1f ( mOffsetYLoc, mOffsetY );
		GLES20.glUniform1f ( mQMulLoc, mQMul);
		GLES20.glUniform1f ( mRMulLoc, mRMul);
		if(mWallpaper != null)
			mWallpaper.mTime = mTime;

	
		GLES20.glUniform3f ( mCol1Loc, (float)Color.red(mColor1)/255.0f, (float)Color.green(mColor1)/255.0f, (float)Color.blue(mColor1)/255.0f );
		GLES20.glUniform3f ( mCol2Loc, (float)Color.red(mColor2)/255.0f, (float)Color.green(mColor2)/255.0f, (float)Color.blue(mColor2)/255.0f );
		GLES20.glUniform3f ( mCol3Loc, (float)Color.red(mColor3)/255.0f, (float)Color.green(mColor3)/255.0f, (float)Color.blue(mColor3)/255.0f );
		GLES20.glUniform3f ( mCol4Loc, (float)Color.red(mColor4)/255.0f, (float)Color.green(mColor4)/255.0f, (float)Color.blue(mColor4)/255.0f );
		GLES20.glUniform3f ( mCol5Loc, (float)Color.red(mColor5)/255.0f, (float)Color.green(mColor5)/255.0f, (float)Color.blue(mColor5)/255.0f );
		GLES20.glUniform3f ( mCol6Loc, (float)Color.red(mColor6)/255.0f, (float)Color.green(mColor6)/255.0f, (float)Color.blue(mColor6)/255.0f );
	}

	private float mTime = 0.8f;
	private float mTime5 = 0.0f;
	private float mRatio = 1.0f;
	private float mOffsetX = 0.5f;
	private float mOffsetY = 0.5f;
	public float mQMul = 0.5f;
	public float mRMul = 0.2f;
	private long mLastTime = 0;
	private float mZoom = 0.55f;
	private int mRatioLoc;
	private int mQMulLoc;
	private int mRMulLoc;
	private int mTimeLoc;
	private int mTime5Loc;
	private int mZoomLoc;
	private int mCol1Loc;
	private int mCol2Loc;
	private int mCol3Loc;
	private int mCol4Loc;
	private int mCol5Loc;
	private int mCol6Loc;
	private int mColor1;
	private int mColor2;
	private int mColor3;
	private int mColor4;
	private int mColor5;
	private int mColor6;
	private int mOffsetXLoc;
	private int mOffsetYLoc;
	public long elapsedTime;
	public AColorfulStripeWallpaper mWallpaper = null;
}

