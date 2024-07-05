package com.formisk.aneonpath;

import java.nio.FloatBuffer;

import android.graphics.Color;
import android.opengl.GLES20;
import android.os.SystemClock;
import android.util.Log;
import rajawali.materials.AMaterial;

public class ANeonPathMaterial extends AMaterial {
	public ANeonPathWallpaper mWallpaper = null;
	public float time = 0.0f;
	public int timeLoc;
	public long lastTime = 0;
	
	public ANeonPathMaterial(String mVShader, String mFShader, boolean animated) {
		super(mVShader, mFShader, animated);
		this.setShaders();
		timeLoc = GLES20.glGetUniformLocation ( mProgram, "time" );
	}
	
	@Override
	public void reload() {
		super.reload();
		this.lastTime = SystemClock.uptimeMillis();
		updateUniform();
	}
	
	public void printDebug() {
		FloatBuffer fb = FloatBuffer.allocate(3);
		GLES20.glGetUniformfv(mProgram, timeLoc, fb);
		Log.d("time: ", "" + fb.get(0));
	}
	
	public void updateUniform() {
		GLES20.glUseProgram(mProgram); 
		if (lastTime == 0)
			lastTime = SystemClock.uptimeMillis();
		long curTime = SystemClock.uptimeMillis();
		long elapsedTime = curTime - lastTime;
		float deltaTime = elapsedTime / 200000.0f;
		lastTime = curTime;
		if(time >= 1.0f)
			time = 0.0f;
		time += deltaTime;
		GLES20.glUniform1f ( timeLoc, time );
	}
		
}

