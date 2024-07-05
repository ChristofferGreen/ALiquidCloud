package com.formisk.areflectiveorganism;

import com.badlogic.gdx.math.Vector3;

import rajawali.math.MathUtil;
import rajawali.math.Matrix4;
import rajawali.math.Number3D;
import rajawali.math.Quaternion;


public class Rotator {
	public float pokeRotationX;
	public float pokeRotationY;
	public float pokeRotationZ;
	public float originalPokeRotationX;
	public float originalPokeRotationY;
	public float deviceRotationX;
	public float deviceRotationY;
	public float deviceRotationZ;
	public float prevDeviceRotationX;
	public float prevDeviceRotationY;
	public float prevDeviceRotationZ;
	public float prevPokeRotationX;
	public float prevPokeRotationY;
	public float prevPokeRotationZ;
	public int poke = 0;
	public float diffX;
	public float diffY;
	public float diffZ;
	public float scale = 0.0001f;
	public int init = 0;
	public boolean horizontal = true;
	Vector3 v1 = new Vector3();
	Vector3 v2 = new Vector3();
	Vector3 v3 = new Vector3();
	Vector3 c1 = new Vector3();
	Vector3 c2 = new Vector3();
	Vector3 c3 = new Vector3();
	Vector3 c4 = new Vector3();
	com.badlogic.gdx.math.Quaternion originalRotationQ = new com.badlogic.gdx.math.Quaternion();
	com.badlogic.gdx.math.Quaternion standardRotationQ = new com.badlogic.gdx.math.Quaternion();
	
	public Rotator() {
	}
	
	void calcDiff() {
		diffX =  pokeRotationX-prevPokeRotationX + (deviceRotationX-prevDeviceRotationX)*300.0f;
		diffY =  pokeRotationY-prevPokeRotationY + (deviceRotationY-prevDeviceRotationY)*300.0f;
		diffZ =  pokeRotationZ-prevPokeRotationZ + (deviceRotationZ-prevDeviceRotationZ)*300.0f;
		//System.out.println("diff: " + diffX + " " + diffY);
		//System.out.println("diff: " + pokeRotationX + " " + prevPokeRotationX + " " + deviceRotationX + " " + prevDeviceRotationX + pokeRotationY + " " + prevPokeRotationY + " " + deviceRotationY + " " + prevDeviceRotationY);
		prevPokeRotationX = pokeRotationX;
		prevPokeRotationY = pokeRotationY;
		prevPokeRotationZ = pokeRotationZ;
		prevDeviceRotationX = deviceRotationX;
		prevDeviceRotationY = deviceRotationY;
		prevDeviceRotationZ = deviceRotationZ;
		float rotationX = -diffX*scale;
		float rotationY = diffY*scale;
		float rotationZ = diffZ*scale;
		if(Math.abs(rotationX) < 10.0f && Math.abs(rotationY) < 10.0f) {
			if(horizontal == true) {
				float tmp = rotationX;
				rotationX = rotationY;
				rotationY = tmp;
			}
			
			v1.set(0.0f, 0.0f, -1.0f);
			v2.set(-1.0f, 0.0f, 0.0f);
			v3.set(0.0f, 1.0f, 0.0f);
			standardRotationQ.transform(v1);
			standardRotationQ.transform(v2);
			standardRotationQ.transform(v3);
			c1.set(-0.5f, 0.5f, 0.0f);
			c2.set(-0.5f, -0.5f, 0.0f);
			c3.set(0.5f, 0.5f, 0.0f);
			c4.set(0.5f, -0.5f, 0.0f);
			standardRotationQ.transform(c1);
			standardRotationQ.transform(c2);
			standardRotationQ.transform(c3);
			standardRotationQ.transform(c4);
			
			Vector3 horizontalNormal = v1.cpy().crs(v2);
			Vector3 verticalNormal = v1.cpy().crs(v3);
			Vector3 zNormal = v2.cpy().crs(v3);
			
			com.badlogic.gdx.math.Quaternion tmpQ = new com.badlogic.gdx.math.Quaternion();
			tmpQ.setFromAxis(verticalNormal, rotationX*1000.0f);
			standardRotationQ.mulLeft(tmpQ);
			standardRotationQ.nor();
			tmpQ.setFromAxis(horizontalNormal, rotationY*1000.0f);
			standardRotationQ.mulLeft(tmpQ);
			standardRotationQ.nor();
			tmpQ.setFromAxis(zNormal, rotationZ*1000.0f);
			standardRotationQ.mulLeft(tmpQ);
			standardRotationQ.nor();
		}
		
		if(init == 0 && this.poke <= 2)
			standardRotationQ.slerp(originalRotationQ, 0.0005f);
	}
	
	void init() {
		init = 1;
		calcDiff();
		originalRotationQ = standardRotationQ.cpy();
		init = 0;
	}
	
	void clear() {
		originalRotationQ = new com.badlogic.gdx.math.Quaternion();
		standardRotationQ = new com.badlogic.gdx.math.Quaternion();
		pokeRotationX = 0.0f;
		pokeRotationY = 0.0f;
		pokeRotationZ = 0.0f;
		originalPokeRotationX = 0.0f;
		originalPokeRotationY = 0.0f;
		deviceRotationX = 0.0f;
		deviceRotationY = 0.0f;
		deviceRotationZ = 0.0f;
		prevPokeRotationX = 0.0f;
		prevPokeRotationY = 0.0f;
		prevPokeRotationZ = 0.0f;
		prevDeviceRotationX = 0.0f;
		prevDeviceRotationY = 0.0f;
		prevDeviceRotationZ = 0.0f;
		diffX = 0.0f;
		diffY = 0.0f;
		diffZ = 0.0f;
	}
	
	void pokeDown() {
	}

	void setEnvironmentRotation(float x, float y) {
		this.pokeRotationX = x;
		this.pokeRotationY = y;
		this.originalPokeRotationX = x;
		this.originalPokeRotationX = y;
	}
	
}
