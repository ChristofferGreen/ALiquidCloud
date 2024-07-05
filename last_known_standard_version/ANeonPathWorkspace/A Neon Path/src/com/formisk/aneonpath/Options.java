package com.formisk.aneonpath;

import java.util.LinkedList;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Color;

public class Options {
	public SharedPreferences preferences;
	public ANeonPathWallpaper wallpaper = null;
	public ANeonPathRenderer renderer = null;
	public boolean initialized = false;

	public boolean isFullVersion = true;
	public float ratio = 1.0f;
	
	public int numberOfPaths = 3;
	public int frameRate = 20;
	public int screenSliding = 2;
	public List<Integer> colorList = new LinkedList<Integer>();
	public int skyColor;
	public int groundColor;
	public int northernlightcolor;
	public int quality = 2;
	public int skyEffect = 1;
	public int cameraSpeed = 1;
	public int floorTexture = 1;
	public int northernLights = 0;
	public int screenEffect = 0;
	public int pathType = 0;
	public int screenEffectQuality = 0;
	
	public Options(ANeonPathRenderer renderer, ANeonPathWallpaper wallpaper, SharedPreferences preferences) {
		this.renderer = renderer;
		this.preferences = preferences;
		this.wallpaper = wallpaper;
	}
	public void setRatio(int width, int height) {
		//1.7 - horizontal
		//0.65 - vertical
		this.ratio = ((float)width)/((float)height);
		if(this.ratio > 1.0)
			this.renderer.getCamera().setFieldOfView(45f);
		else
			this.renderer.getCamera().setFieldOfView(65f);
	}
	
	public void readSettings() {
		if(this.wallpaper.getPackageName().toLowerCase().contains("free"))
			this.isFullVersion =  false;
		this.numberOfPaths = Integer.parseInt(this.preferences.getString("numberofpathskey", "3"));
		if(!this.isFullVersion)
			this.numberOfPaths = 1;
		
		this.screenEffect = Integer.parseInt(this.preferences.getString("screeneffectkey", "0"));
		this.pathType = Integer.parseInt(this.preferences.getString("pathtypekey", "0"));
		this.screenEffectQuality = Integer.parseInt(this.preferences.getString("screeneffectqualitykey", "1"));
		this.frameRate = Integer.parseInt(this.preferences.getString("frameratekey", "20"));
		this.screenSliding = Integer.parseInt(this.preferences.getString("slidingkey", "2"));
		this.quality = Integer.parseInt(this.preferences.getString("quality2key", "2"));
		this.skyEffect = Integer.parseInt(this.preferences.getString("skyeffectkey", "1"));
		this.floorTexture = Integer.parseInt(this.preferences.getString("floortexturekey", "1"));
		this.northernLights = Integer.parseInt(this.preferences.getString("northernlightskey", "0"));
		this.cameraSpeed = Integer.parseInt(this.preferences.getString("cameraspeedkey", "1"));
		this.renderer.setFrameRate(this.frameRate);
		this.colorList.clear();
		this.colorList.add(preferences.getInt("color1", Color.rgb(255, 63, 6)));
		this.colorList.add(preferences.getInt("color2", Color.rgb(255, 242, 6)));
		this.colorList.add(preferences.getInt("color3", Color.rgb(6, 255, 249)));
		this.colorList.add(preferences.getInt("color4", Color.rgb(238, 0, 202)));
		this.colorList.add(preferences.getInt("color5", Color.rgb(0, 238, 149)));
		this.skyColor = preferences.getInt("skycolor", Color.rgb(13, 66, 249));
		this.northernlightcolor = preferences.getInt("northernlightcolor", Color.rgb(255, 0, 233));
		if(this.renderer.skyObject != null) {
			this.renderer.skyObject.skyMaterial.skyColor = this.skyColor;
			this.renderer.skyObject.skyMaterial.updateUniform();
			this.renderer.skyObjectReflection.skyMaterial.skyColor = this.skyColor;
			this.renderer.skyObjectReflection.skyMaterial.updateUniform();
		}
		this.groundColor = preferences.getInt("groundcolor", Color.rgb(33, 82, 250));
		if(this.renderer.groundManager != null && this.renderer.groundManager.groundMaterial != null) {
			this.renderer.groundManager.groundMaterial.groundColor = this.groundColor;
			this.renderer.groundManager.groundMaterial.updateUniform();
		}
	}	

	public boolean settingsHasChanged() {
		if(!this.initialized)
			return false;
		if(this.frameRate != Integer.parseInt(this.preferences.getString("frameratekey", "20")))
			return true;
		if(this.quality != Integer.parseInt(this.preferences.getString("quality2key", "2")))
			return true;
		if(this.isFullVersion) {
			System.out.println("skyEffect");
			if(this.skyEffect != Integer.parseInt(this.preferences.getString("skyeffectkey", "1")))
				return true;
			System.out.println("screenSliding");
			if(this.screenSliding != Integer.parseInt(this.preferences.getString("slidingkey", "2")))
				return true;
			System.out.println("numberOfPaths");
			if(this.numberOfPaths != Integer.parseInt(this.preferences.getString("numberofpathskey", "3")))
				return true;
			System.out.println("color1");
			if(this.colorList.get(0) != preferences.getInt("color1", Color.rgb(255, 63, 6)))
				return true;
			System.out.println("color2");
			if(this.colorList.get(1) != preferences.getInt("color2", Color.rgb(255, 242, 6)))
				return true;
			System.out.println("color3");
			if(this.colorList.get(2) != preferences.getInt("color3", Color.rgb(6, 255, 249)))
				return true;
			System.out.println("color4");
			if(this.colorList.get(3) != preferences.getInt("color4", Color.rgb(238, 0, 202)))
				return true;
			System.out.println("color5");
			if(this.colorList.get(4) != preferences.getInt("color5", Color.rgb(0, 238, 149)))
				return true;
			System.out.println("skycolor");
			if(this.skyColor != preferences.getInt("skycolor", Color.rgb(13, 66, 249)))
				return true;
			System.out.println("groundcolor");
			if(this.groundColor != preferences.getInt("groundcolor", Color.rgb(33, 82, 250)))
				return true;
			if(this.northernlightcolor != preferences.getInt("northernlightcolor", Color.rgb(255, 0, 233)))
				return true;
			System.out.println("screenEffect");
			if(this.screenEffect != Integer.parseInt(this.preferences.getString("screeneffectkey", "0")))
				return true;
			System.out.println("cameraSpeed");
			if(this.cameraSpeed != Integer.parseInt(this.preferences.getString("cameraspeedkey", "1")))
				return true;
			System.out.println("northernLights");
			if(this.northernLights != Integer.parseInt(this.preferences.getString("northernlightskey", "0")))
				return true;
			System.out.println("pathType");
			if(this.pathType != Integer.parseInt(this.preferences.getString("pathtypekey", "0")))
				return true;
			System.out.println("screenEffectQuality");
			if(this.screenEffectQuality != Integer.parseInt(this.preferences.getString("screeneffectqualitykey", "1")))
				return true;
			System.out.println("floorTexture");
			if(this.floorTexture != Integer.parseInt(this.preferences.getString("floortexturekey", "1")))
				return true;
		}
		return false;
	}
}

