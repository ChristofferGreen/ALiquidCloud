package com.formisk.aneonpath;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import rajawali.BaseObject3D;
import rajawali.Camera;
import rajawali.materials.AMaterial;
import rajawali.materials.SimpleMaterial;
import rajawali.materials.TextureInfo;
import rajawali.parser.ObjParser;
import rajawali.util.ObjectColorPicker.ColorPickerInfo;

public class NeonPathManager {
	private ANeonPathRenderer renderer = null;
	private NeonPathPartCreator neonPathPartCreator = null;
	private List<NeonPath> neonPathList = new LinkedList<NeonPath>();
	private List<NeonPath> neonPathReflectionList = new LinkedList<NeonPath>();
	private List<NeonPathMaterial> materialList = new LinkedList<NeonPathMaterial>();
	
	public NeonPathManager(ANeonPathRenderer renderer, int numberOfPaths, List<Integer> colorList, TextureInfo textureInfo) {
		this.renderer = renderer;
		this.neonPathPartCreator = new NeonPathPartCreator(this.renderer);
	
		this.createPaths(numberOfPaths, colorList, textureInfo);
	}
	
	public void createPaths(int numberOfPaths, List<Integer> colorList, TextureInfo textureInfo) {
        for(int color : colorList) {
        	NeonPathMaterial neonPathMaterial;
        	if(this.renderer.options.pathType == 0)
        		neonPathMaterial = new NeonPathMaterial(color, NeonPathMaterial.mFShaderFlat);
        	else if(this.renderer.options.pathType == 1)
        		neonPathMaterial = new NeonPathMaterial(color, NeonPathMaterial.mFShaderRippleBand);
        	else
        		neonPathMaterial = new NeonPathMaterial(color, NeonPathMaterial.mFShaderRope);
			this.materialList.add(neonPathMaterial);
			neonPathMaterial.addTexture(textureInfo);
		}
		for(int i = 0; i < numberOfPaths; i++) {
			Long seed = NeonPathCreator.getNewSeed();
			this.neonPathList.add(new NeonPath(this.materialList.get(i), this.neonPathPartCreator, seed, false));
			this.neonPathReflectionList.add(new NeonPath(this.materialList.get(i), this.neonPathPartCreator, seed, true));
		}
	}
	
	public void reload(int numberOfPaths, List<Integer> colorList, TextureInfo textureInfo) {
		for(int i = 0; i < this.neonPathList.size(); i++)
			this.renderer.removeChild(this.neonPathList.get(i));
		for(int i = 0; i < this.neonPathReflectionList.size(); i++)
			this.renderer.removeChild(this.neonPathReflectionList.get(i));
		this.neonPathList.clear();
		this.neonPathReflectionList.clear();
		this.materialList.clear();
		this.createPaths(numberOfPaths, colorList, textureInfo);
	}
	
	public void addPathToRenderer() {
		for(int i = 0; i < this.neonPathList.size(); i++)
			this.renderer.addChild(this.neonPathList.get(i));
	}

	public void addPathReflectionToRenderer() {
		for(int i = 0; i < this.neonPathReflectionList.size(); i++)
			this.renderer.addChild(this.neonPathReflectionList.get(i));
	}
	
	public void drawNextFrame(float speed) {
		for(int i = 0; i < this.neonPathList.size(); i++)
			this.neonPathList.get(i).drawNextFrame(speed);
		for(int i = 0; i < this.neonPathReflectionList.size(); i++)
			this.neonPathReflectionList.get(i).drawNextFrame(speed);
	}
}
