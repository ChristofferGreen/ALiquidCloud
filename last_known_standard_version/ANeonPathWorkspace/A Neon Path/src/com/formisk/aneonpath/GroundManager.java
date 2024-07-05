package com.formisk.aneonpath;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import rajawali.BaseObject3D;
import rajawali.materials.SimpleMaterial;
import rajawali.materials.TextureInfo;
import rajawali.primitives.Plane;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class GroundManager extends BaseObject3D {
	public ANeonPathRenderer renderer = null;
	public GroundMaterial groundMaterial = null;
	private Plane ground1 = null;
	private Plane ground2 = null;
	private Plane ground3 = null;
	private Plane ground4 = null;

	private Plane groundl1 = null;
	private Plane groundl2 = null;
	private Plane groundl3 = null;
	private Plane groundl4 = null;

	private Plane groundr1 = null;
	private Plane groundr2 = null;
	private Plane groundr3 = null;
	private Plane groundr4 = null;
	
	public GroundManager(ANeonPathRenderer renderer, TextureInfo textureInfo) {
		super();
		this.renderer = renderer;
		
		if(this.renderer.options.floorTexture == 0)
			return;
		else if(this.renderer.options.floorTexture == 1)
			this.groundMaterial = new GroundMaterial(this.renderer.options.groundColor, GroundMaterial.mFShaderG);
		else if(this.renderer.options.floorTexture == 2)
			this.groundMaterial = new GroundMaterial(this.renderer.options.groundColor, GroundMaterial.mFShaderR);
		else if(this.renderer.options.floorTexture == 3)
			this.groundMaterial = new GroundMaterial(this.renderer.options.groundColor, GroundMaterial.mFShaderB);
		this.groundMaterial.addTexture(textureInfo);

		this.ground1 = new Plane(10.0f, 10.0f, 1, 1);
		this.ground1.setPosition(0.0f, 0.0f, 0.0f);
		this.ground1.setRotation(-90.0f, 0.0f, 0.0f);
		this.ground1.setMaterial(groundMaterial, false);
		this.ground1.setTransparent(true);
        this.addChild(this.ground1);

        this.ground2 = new Plane(10.0f, 10.0f, 1, 1);
		this.ground2.setPosition(0.0f, 0.0f, 10.0f);
		this.ground2.setRotation(-90.0f, 0.0f, 0.0f);
		this.ground2.setMaterial(groundMaterial, false);
		this.ground2.setTransparent(true);
        this.addChild(this.ground2);

        this.ground3 = new Plane(10.0f, 10.0f, 1, 1);
		this.ground3.setPosition(0.0f, 0.0f, 20.0f);
		this.ground3.setRotation(-90.0f, 0.0f, 0.0f);
		this.ground3.setMaterial(groundMaterial, false);
		this.ground3.setTransparent(true);
        this.addChild(this.ground3);

        this.ground4 = new Plane(10.0f, 10.0f, 1, 1);
		this.ground4.setPosition(0.0f, 0.0f, 30.0f);
		this.ground4.setRotation(-90.0f, 0.0f, 0.0f);
		this.ground4.setMaterial(groundMaterial, false);
		this.ground4.setTransparent(true);
        this.addChild(this.ground4);

		this.groundl1 = new Plane(10.0f, 10.0f, 1, 1);
		this.groundl1.setPosition(-10.0f, 0.0f, 0.0f);
		this.groundl1.setRotation(-90.0f, 0.0f, 0.0f);
		this.groundl1.setMaterial(groundMaterial, false);
		this.groundl1.setTransparent(true);
        this.addChild(this.groundl1);

        this.groundl2 = new Plane(10.0f, 10.0f, 1, 1);
		this.groundl2.setPosition(-10.0f, 0.0f, 10.0f);
		this.groundl2.setRotation(-90.0f, 0.0f, 0.0f);
		this.groundl2.setMaterial(groundMaterial, false);
		this.groundl2.setTransparent(true);
        this.addChild(this.groundl2);

        this.groundl3 = new Plane(10.0f, 10.0f, 1, 1);
		this.groundl3.setPosition(-10.0f, 0.0f, 20.0f);
		this.groundl3.setRotation(-90.0f, 0.0f, 0.0f);
		this.groundl3.setMaterial(groundMaterial, false);
		this.groundl3.setTransparent(true);
        this.addChild(this.groundl3);

        this.groundl4 = new Plane(10.0f, 10.0f, 1, 1);
		this.groundl4.setPosition(-10.0f, 0.0f, 30.0f);
		this.groundl4.setRotation(-90.0f, 0.0f, 0.0f);
		this.groundl4.setMaterial(groundMaterial, false);
		this.groundl4.setTransparent(true);
        this.addChild(this.groundl4);
	
		this.groundr1 = new Plane(10.0f, 10.0f, 1, 1);
		this.groundr1.setPosition(10.0f, 0.0f, 0.0f);
		this.groundr1.setRotation(-90.0f, 0.0f, 0.0f);
		this.groundr1.setMaterial(groundMaterial, false);
		this.groundr1.setTransparent(true);
        this.addChild(this.groundr1);

        this.groundr2 = new Plane(10.0f, 10.0f, 1, 1);
		this.groundr2.setPosition(10.0f, 0.0f, 10.0f);
		this.groundr2.setRotation(-90.0f, 0.0f, 0.0f);
		this.groundr2.setMaterial(groundMaterial, false);
		this.groundr2.setTransparent(true);
        this.addChild(this.groundr2);

        this.groundr3 = new Plane(10.0f, 10.0f, 1, 1);
		this.groundr3.setPosition(10.0f, 0.0f, 20.0f);
		this.groundr3.setRotation(-90.0f, 0.0f, 0.0f);
		this.groundr3.setMaterial(groundMaterial, false);
		this.groundr3.setTransparent(true);
        this.addChild(this.groundr3);

        this.groundr4 = new Plane(10.0f, 10.0f, 1, 1);
		this.groundr4.setPosition(10.0f, 0.0f, 30.0f);
		this.groundr4.setRotation(-90.0f, 0.0f, 0.0f);
		this.groundr4.setMaterial(groundMaterial, false);
		this.groundr4.setTransparent(true);
        this.addChild(this.groundr4);
	}
	
	public void drawNextFrame(float speed) {
		if(this.renderer.options.floorTexture == 0)
			return;
		this.ground1.setZ(this.ground1.getZ()-speed);
		this.ground2.setZ(this.ground2.getZ()-speed);
		this.ground3.setZ(this.ground3.getZ()-speed);
		this.ground4.setZ(this.ground4.getZ()-speed);
		if(this.ground1.getZ() <= -10.0f) {
			this.ground1.setZ(0.0f);
			this.ground2.setZ(10.0f);
			this.ground3.setZ(20.0f);
			this.ground4.setZ(30.0f);
		}

		this.groundl1.setZ(this.groundl1.getZ()-speed);
		this.groundl2.setZ(this.groundl2.getZ()-speed);
		this.groundl3.setZ(this.groundl3.getZ()-speed);
		this.groundl4.setZ(this.groundl4.getZ()-speed);
		if(this.groundl1.getZ() <= -10.0f) {
			this.groundl1.setZ(0.0f);
			this.groundl2.setZ(10.0f);
			this.groundl3.setZ(20.0f);
			this.groundl4.setZ(30.0f);
		}

		this.groundr1.setZ(this.groundr1.getZ()-speed);
		this.groundr2.setZ(this.groundr2.getZ()-speed);
		this.groundr3.setZ(this.groundr3.getZ()-speed);
		this.groundr4.setZ(this.groundr4.getZ()-speed);
		if(this.groundr1.getZ() <= -10.0f) {
			this.groundr1.setZ(0.0f);
			this.groundr2.setZ(10.0f);
			this.groundr3.setZ(20.0f);
			this.groundr4.setZ(30.0f);
		}
	}
}
