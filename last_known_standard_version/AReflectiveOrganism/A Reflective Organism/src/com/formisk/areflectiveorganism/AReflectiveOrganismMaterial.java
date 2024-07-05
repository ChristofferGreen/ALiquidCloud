package com.formisk.areflectiveorganism;

import java.nio.FloatBuffer;

import android.graphics.Color;
import android.opengl.GLES20;
import android.os.SystemClock;
import android.util.Log;
import rajawali.materials.AMaterial;

public class AReflectiveOrganismMaterial extends AMaterial {
	public AReflectiveOrganismWallpaper mWallpaper = null;
	public float time = 0.0f;
	public int timeLoc;
	public long lastTime = 0;
	
	public AReflectiveOrganismMaterial(String mVShader, String mFShader, boolean animated) {
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
	}
		
}

