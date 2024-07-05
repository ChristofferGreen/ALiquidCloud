package com.formisk.aneonpath;

import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import rajawali.BaseObject3D;
import rajawali.Camera;
import rajawali.materials.SimpleMaterial;
import rajawali.materials.TextureInfo;
import rajawali.parser.ObjParser;
import rajawali.util.ObjectColorPicker.ColorPickerInfo;

public class SkyObject extends BaseObject3D {
	public BaseObject3D sky = null;
	private boolean firstRenderPass = true;
	public SkyMaterial skyMaterial = null;
	public SkyEffectMaterial skyEffectMaterial = null;
	public ANeonPathRenderer renderer = null;
	
	public SkyObject(ANeonPathRenderer renderer, boolean inverted, TextureInfo textureInfo, boolean effect) {
		this.renderer = renderer;
		if(effect) {
			this.skyEffectMaterial = new SkyEffectMaterial(this.renderer.options.skyColor);
	        this.skyEffectMaterial.addTexture(textureInfo);
		}
		else {
			this.skyMaterial = new SkyMaterial(this.renderer.options.skyColor);
	        this.skyMaterial.addTexture(textureInfo);
		}
		
		float z = 30.0f;
		float scaleX = 1.2f;
		float scaleY = 1.0f;
		float scaleZ = 1.0f;
		float rotX = 0.0f;
		float rotY = 180.0f;
		float rotZ = 0.0f;
		
		ObjParser parser = new ObjParser(this.renderer.getContext().getResources(), this.renderer.getTextureManager(), R.raw.sky1);
		parser.parse();
		this.sky = parser.getParsedObject();
		if(effect) {
			this.sky.setMaterial(this.skyEffectMaterial);
			//this.sky.setAdditive(true);
			this.sky.setTransparent(true);
			this.sky.setBlendingEnabled(true);
			this.sky.setBlendFunc(GL10.GL_ONE, GL10.GL_ONE);
		}
		else
			this.sky.setMaterial(this.skyMaterial);
		this.sky.setZ(z);
		this.sky.setRotation(rotX, rotY, rotZ);
		if(inverted)
			this.sky.setScale(scaleX, -scaleY, scaleZ);
		else
			this.sky.setScale(scaleX, scaleY, scaleZ);
		this.sky.setDoubleSided(true);

		this.addChild(this.sky);
	}
	
	public void drawNextFrame() {
		if(this.skyMaterial != null)
			this.skyMaterial.updateUniform();
		if(this.skyEffectMaterial != null)
			this.skyEffectMaterial.updateUniform();
	}
	
	@Override
	public void reload() {
		super.reload();
		if(this.skyMaterial != null)
			this.skyMaterial.updateUniform();
		if(this.skyEffectMaterial != null)
			this.skyEffectMaterial.updateUniform();
	}

}
