package com.formisk.aliquidcloud;

import java.nio.FloatBuffer;

import android.graphics.Color;
import android.opengl.GLES20;
import rajawali.materials.AMaterial;
import android.os.SystemClock;
import android.util.Log;

public class ALiquidCloudMaterial extends AMaterial {
	
	protected static final String mVShader = 
			"uniform mat4 uMVPMatrix;\n" +
			"attribute vec4 aPosition;\n" +
			"attribute vec2 aTextureCoord;\n" +
			"varying vec2 vTextureCoordNormal;\n" +
			"varying vec2 vTextureCoord;\n" +
			"varying vec2 vTextureCoordPUtime5;\n" +
			"uniform float u_time;\n" +
			"uniform float u_time5;\n" +
			"uniform float zoom;\n" +
			"uniform float extraZoom;\n" +
			"uniform float ratio;\n" +
			"uniform float offsetX;\n" +
			"uniform float offsetY;\n" +
			"uniform float qmul;\n" +
			"uniform float rmul;\n" +
			"uniform vec2 windDirection;\n" +
			
			"void main() {\n" +
			"	gl_Position = uMVPMatrix * aPosition;\n" +
			"	vTextureCoordNormal = aTextureCoord;\n" + // copy texture coordinates
			"	vTextureCoordNormal.y = vTextureCoordNormal.y*ratio;\n" + 
			"	vTextureCoord = aTextureCoord;\n" + // copy texture coordinates
			"	vTextureCoord.y = vTextureCoord.y*ratio;\n" + // correct coordinates for aspect ratio
			"	vTextureCoord = vTextureCoord*zoom*extraZoom;\n" + // correct coordinates for zoom
			"	vTextureCoord = vTextureCoord+(u_time*windDirection);\n" + // apply time based animation
			"	vTextureCoord.y = vTextureCoord.y+(offsetX/2.0);\n" + // apply wallpaper screen offset based animation
			"	vTextureCoord.x = vTextureCoord.x+(offsetY/2.0);\n" + // apply wallpaper screen offset based animation
			"   vTextureCoordPUtime5 = vTextureCoord+u_time5;\n" +
			"}\n"; 

	public ALiquidCloudMaterial(String vertexShader, String fragmentShader) {
		super(vertexShader, fragmentShader);

		mTimeLoc = GLES20.glGetUniformLocation ( mProgram, "u_time" );
		mTime5Loc = GLES20.glGetUniformLocation ( mProgram, "u_time5" );
		mZoomLoc = GLES20.glGetUniformLocation ( mProgram, "zoom" );
		extraZoomLoc = GLES20.glGetUniformLocation ( mProgram, "extraZoom" );
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
		mPokeLoc = GLES20.glGetUniformLocation ( mProgram, "pokePosition" );
		mPokeSizeLoc = GLES20.glGetUniformLocation ( mProgram, "pokeSize" );
		mWindDirectionLoc = GLES20.glGetUniformLocation ( mProgram, "windDirection" );
	}
	
	public void initZoom(String zoom) {
		if(zoom.equals("1"))
			mZoom = 0.4f;
		else if(zoom.equals("2"))
			mZoom = 0.55f;
		else if(zoom.equals("3") || zoom.equals("."))
			mZoom = 0.7f;
		else if(zoom.equals("4"))
			mZoom = 1.0f;
	}
	
	public void setQMul(float amount) {
		this.mQMul = amount;
		GLES20.glUniform1f ( mQMulLoc, mQMul);
	}

	public void setRMul(float amount) {
		this.mRMul = amount;
		GLES20.glUniform1f ( mRMulLoc, mRMul);
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

	public void setPoke(float pokeX, float pokeY) {
		this.mPokeX = pokeX;
		this.mPokeY = pokeY*mRatio;
	}
	
	public void updateUniform() {
		GLES20.glUseProgram(mProgram); 
		if (mLastTime == 0)
			mLastTime = SystemClock.uptimeMillis();
		long curTime = SystemClock.uptimeMillis();
		long elapsedTime = curTime - mLastTime;
		float deltaTime = elapsedTime / 200000.0f;
		mLastTime = curTime;
		if(mTime >= 1.0f)
			mTime = 0.0f;
		mTime += deltaTime*windSpeed;
		mTime5 = (mTime*5.0f);
		mTime5 = mTime5-((int)mTime5);
		//Log.d("time", mTime + " " + mTime5);
		
		//while(GLES20.glGetError() != GLES20.GL_NO_ERROR) {}
		
		GLES20.glUniform1f ( mTimeLoc, mTime );
		/*int error = -1;
		while(error != GLES20.GL_NO_ERROR) {
			error = GLES20.glGetError();
			if(error == GLES20.GL_NO_ERROR)
				Log.d("GL Error", "GL_NO_ERROR");
			else if(error == GLES20.GL_INVALID_ENUM)
				Log.d("GL Error", "GL_INVALID_ENUM");
			else if(error == GLES20.GL_INVALID_VALUE)
				Log.d("GL Error", "GL_INVALID_VALUE");
			else if(error == GLES20.GL_INVALID_OPERATION)
				Log.d("GL Error", "GL_INVALID_OPERATION");
			else if(error == GLES20.GL_OUT_OF_MEMORY)
				Log.d("GL Error", "GL_OUT_OF_MEMORY");
			else
				Log.d("GL Error", "Unknown Error: " + error);
		}*/
		GLES20.glUniform1f ( mTime5Loc, mTime5 );
		GLES20.glUniform1f ( mRatioLoc, mRatio );
		GLES20.glUniform1f ( mZoomLoc, mZoom );
		GLES20.glUniform1f ( extraZoomLoc, extraZoom );
		GLES20.glUniform1f ( mOffsetXLoc, mOffsetX );
		GLES20.glUniform1f ( mOffsetYLoc, mOffsetY );
		GLES20.glUniform1f ( mQMulLoc, mQMul);
		GLES20.glUniform1f ( mRMulLoc, mRMul);
		if(mPokeLoc != -1)
			GLES20.glUniform2f ( mPokeLoc, mPokeX, mPokeY);
		if(mPokeSizeLoc != -1)
			GLES20.glUniform1f ( mPokeSizeLoc, pokeSize );
		GLES20.glUniform3f ( mCol1Loc, (float)Color.red(mColor1)/255.0f, (float)Color.green(mColor1)/255.0f, (float)Color.blue(mColor1)/255.0f );
		GLES20.glUniform3f ( mCol2Loc, (float)Color.red(mColor2)/255.0f, (float)Color.green(mColor2)/255.0f, (float)Color.blue(mColor2)/255.0f );
		GLES20.glUniform3f ( mCol3Loc, (float)Color.red(mColor3)/255.0f, (float)Color.green(mColor3)/255.0f, (float)Color.blue(mColor3)/255.0f );
		GLES20.glUniform3f ( mCol4Loc, (float)Color.red(mColor4)/255.0f, (float)Color.green(mColor4)/255.0f, (float)Color.blue(mColor4)/255.0f );
		GLES20.glUniform3f ( mCol5Loc, (float)Color.red(mColor5)/255.0f, (float)Color.green(mColor5)/255.0f, (float)Color.blue(mColor5)/255.0f );
		GLES20.glUniform3f ( mCol6Loc, (float)Color.red(mColor6)/255.0f, (float)Color.green(mColor6)/255.0f, (float)Color.blue(mColor6)/255.0f );
		GLES20.glUniform2f ( mWindDirectionLoc, windDirY, windDirX );
		
	}
	
	public void printDebug() {
		FloatBuffer fb = FloatBuffer.allocate(3);
		GLES20.glGetUniformfv(mProgram, mTimeLoc, fb);
		Log.d("mTime:"+mProgram+":"+mTimeLoc, ""+fb.get(0));
		GLES20.glGetUniformfv(mProgram, mTime5Loc, fb);
		Log.d("mTime5:"+mProgram+":"+mTime5Loc, ""+fb.get(0));
		GLES20.glGetUniformfv(mProgram, mRatioLoc, fb);
		Log.d("mRatio:"+mProgram+":"+mRatioLoc, ""+fb.get(0));
		GLES20.glGetUniformfv(mProgram, mZoomLoc, fb);
		Log.d("mZoom:"+mProgram+":"+mZoomLoc, ""+fb.get(0));
		GLES20.glGetUniformfv(mProgram, mOffsetXLoc, fb);
		Log.d("mOffsetX:"+mProgram+":"+mOffsetXLoc, ""+fb.get(0));
		GLES20.glGetUniformfv(mProgram, mOffsetYLoc, fb);
		Log.d("mOffsetY:"+mProgram+":"+mOffsetYLoc, ""+fb.get(0));
		GLES20.glGetUniformfv(mProgram, mQMulLoc, fb);
		Log.d("mQMul:"+mProgram+":"+mQMulLoc, ""+fb.get(0));
		GLES20.glGetUniformfv(mProgram, mRMulLoc, fb);
		Log.d("mRMul:"+mProgram+":"+mRMulLoc, ""+fb.get(0));
		GLES20.glGetUniformfv(mProgram, mPokeLoc, fb);
		Log.d("mPoke:"+mProgram+":"+mPokeLoc, ""+fb.get(0)+" "+fb.get(1));
		GLES20.glGetUniformfv(mProgram, mPokeSizeLoc, fb);
		Log.d("mPokeSize:"+mProgram+":"+mPokeSizeLoc, ""+fb.get(0));
	}

	public float mTime = 0.8f;
	public float mTime5 = 0.0f;
	public float mRatio = 1.0f;
	public float mOffsetX = 0.5f;
	public float mOffsetY = 0.5f;
	public float mQMul = 0.5f;
	public float mRMul = 0.2f;
	public long mLastTime = 0;
	private float mZoom = 0.55f;
	private int mRatioLoc;
	private int mQMulLoc;
	private int mRMulLoc;
	private int mPokeLoc;
	private int mTimeLoc;
	private int mTime5Loc;
	private int mZoomLoc;
	private int mPokeSizeLoc;
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
	private int mWindDirectionLoc;
	private int extraZoomLoc;
	public float extraZoom = 1.0f;
	public float windSpeed = 1.0f;
	public float windDirX = 1.0f;
	public float windDirY = 1.0f;
	private float mPokeX = -10.0f;
	private float mPokeY = -10.0f;
	public float pokeSize = -3.0f;
	private int mOffsetXLoc;
	private int mOffsetYLoc;
	public ALiquidCloudWallpaper mWallpaper = null;
}

