package com.formisk.aneonpath;

import javax.microedition.khronos.opengles.GL10;

import rajawali.BaseObject3D;
import rajawali.materials.TextureInfo;
import rajawali.parser.ObjParser;

public class NorthernLights extends BaseObject3D {
	public BaseObject3D northernLights = null;
	public BaseObject3D northernLightsReflection = null;
	public NorthernLightsMaterial northernLightsMaterial = null;
	
	public NorthernLights(ANeonPathRenderer renderer, TextureInfo textureInfo, int northernlightscolor) {
		ObjParser parser = new ObjParser(renderer.getContext().getResources(), renderer.getTextureManager(), R.raw.northern_lights);
		parser.parse();
		this.northernLights = parser.getParsedObject();
		
		this.northernLightsMaterial = new NorthernLightsMaterial(northernlightscolor);
		this.northernLightsMaterial.addTexture(textureInfo);
		this.northernLights.setMaterial(this.northernLightsMaterial);
		//this.northernLights.setAdditive(true);
		this.northernLights.setTransparent(true);
		this.northernLights.setBlendingEnabled(true);
		this.northernLights.setBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_COLOR);
		
		float z = 30.0f;
		float scaleX = 1.2f;
		float scaleY = 1.0f;
		float scaleZ = 1.0f;
		float rotX = 0.0f;
		float rotY = 180.0f;
		float rotZ = 0.0f;
		this.northernLights.setZ(z);
		this.northernLights.setRotation(rotX, rotY, rotZ);
		this.northernLights.setScale(scaleX, scaleY, scaleZ);
		this.northernLights.setDoubleSided(true);
		
		this.northernLightsReflection = this.northernLights.clone();
		this.northernLightsReflection.setZ(z);
		this.northernLightsReflection.setRotation(rotX, rotY, rotZ);
		this.northernLightsReflection.setScale(scaleX, -scaleY, scaleZ);
		this.northernLightsReflection.setDoubleSided(true);
		this.northernLightsReflection.setMaterial(this.northernLightsMaterial);
		//this.northernLightsReflection.setAdditive(true);
		this.northernLightsReflection.setTransparent(true);
		this.northernLightsReflection.setBlendingEnabled(true);
		this.northernLightsReflection.setBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_COLOR);

		
		this.addChild(this.northernLights);
		this.addChild(this.northernLightsReflection);
	}
	
	public void drawNextFrame() {
		if(this.northernLightsMaterial != null)
			this.northernLightsMaterial.updateUniform();
	}
	
}
