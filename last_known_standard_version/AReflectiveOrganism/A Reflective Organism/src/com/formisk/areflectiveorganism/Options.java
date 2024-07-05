package com.formisk.areflectiveorganism;

import java.util.LinkedList;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Color;

public class Options {
	public SharedPreferences preferences;
	public AReflectiveOrganismWallpaper wallpaper = null;
	public AReflectiveOrganismRenderer renderer = null;
	public boolean initialized = false;

	public float xRatio = 1.0f;
	public float yRatio = 1.0f;
	
	public int frameRate = 40;
	public int screenSliding = 2;
	public int color1[] = new int[3];
	public int color2[] = new int[3];
	public int purchased = 0;
	public int cameraSpeed = 1;
	public int screenEffect = 0;
	public int screenEffectQuality = 0;
	public int galleryTheme = 0;
	public int environmentReflection = 0;
	public int userReflection = 75;
	public int userMovement = 50;
	public int userDisplacement = 15;
	public int changed = 0;
	public int usecustomtheme = 1;
	public int gifted = 0;
	public int rotationInvert = 1;
	
	public Options(AReflectiveOrganismRenderer renderer, AReflectiveOrganismWallpaper wallpaper, SharedPreferences preferences) {
		this.renderer = renderer;
		this.preferences = preferences;
		this.wallpaper = wallpaper;
		this.color1[0] = Color.red(preferences.getInt("color1", Color.rgb(255, 0, 0)));
		this.color1[1] = Color.green(preferences.getInt("color1", Color.rgb(0, 63, 0)));
		this.color1[2] = Color.blue(preferences.getInt("color1", Color.rgb(0, 0, 6)));
		this.color2[0] = Color.red(preferences.getInt("color2", Color.rgb(255, 0, 0)));
		this.color2[1] = Color.green(preferences.getInt("color2", Color.rgb(0, 242, 0)));
		this.color2[2] = Color.blue(preferences.getInt("color2", Color.rgb(0, 0, 6)));
	}
	
	public void setRatio(int width, int height) {
		//1.7 - horizontal
		//0.65 - vertical
		this.yRatio = ((float)width)/((float)height);
		this.xRatio = ((float)height)/((float)width);
		if(this.yRatio > this.xRatio) {
			this.yRatio = 1.0f;
			this.wallpaper.rotator.horizontal = true;
		}
		else {
			this.xRatio = 1.0f;
			this.wallpaper.rotator.horizontal = false;
		}
	}
	
	public void readSettings() {
		this.screenEffect = Integer.parseInt(this.preferences.getString("screeneffectkey", "0"));
		this.screenEffectQuality = Integer.parseInt(this.preferences.getString("screeneffectqualitykey", "1"));
		this.frameRate = Integer.parseInt(this.preferences.getString("frameratekey", "40"));
		this.screenSliding = Integer.parseInt(this.preferences.getString("slidingkey", "2"));
		this.cameraSpeed = Integer.parseInt(this.preferences.getString("cameraspeedkey", "1"));
		this.usecustomtheme = Integer.parseInt(this.preferences.getString("usecustomthemekey", "1"));
		this.rotationInvert = Integer.parseInt(this.preferences.getString("rotationinvertkey", "1"));
		this.userReflection = this.preferences.getInt("reflectivity", 75);
		this.userMovement = this.preferences.getInt("movement", 20);
		this.userDisplacement = this.preferences.getInt("displacement", 15);
		this.purchased =  this.preferences.getInt("PURCHASED", 0);
		this.gifted = this.preferences.getInt("gifted", 0);
		if(this.purchased == 1) {
			this.galleryTheme = this.preferences.getInt("gallery", 0);
			System.out.println("purchased");
		}
		else if(this.gifted == 1 && this.preferences.getInt("gallery", 0) == 1) {
			this.galleryTheme = 1;
			System.out.println("not purchased, gifted");
		}
		else {
			this.galleryTheme = 0;
			System.out.println("not purchased, gifted");
		}
		if(this.purchased == 1)
			this.environmentReflection = this.preferences.getInt("environmentkey", 0);
		else
			this.environmentReflection = 0;
		this.renderer.setFrameRate(this.frameRate);
		this.color1[0] = Color.red(preferences.getInt("color1", Color.rgb(255, 0, 0)));
		this.color1[1] = Color.green(preferences.getInt("color1", Color.rgb(0, 63, 0)));
		this.color1[2] = Color.blue(preferences.getInt("color1", Color.rgb(0, 0, 6)));
		this.color2[0] = Color.red(preferences.getInt("color2", Color.rgb(255, 0, 0)));
		this.color2[1] = Color.green(preferences.getInt("color2", Color.rgb(0, 242, 0)));
		this.color2[2] = Color.blue(preferences.getInt("color2", Color.rgb(0, 0, 6)));
	}	

	public boolean settingsHasChanged() {
		this.changed = 1;
		if(!this.initialized)
			return false;
		System.out.println("1");
		if(this.frameRate != Integer.parseInt(this.preferences.getString("frameratekey", "40")))
			return true;
		System.out.println("2");
		if(this.usecustomtheme != Integer.parseInt(this.preferences.getString("usecustomthemekey", "1")))
			return true;
		if(this.rotationInvert != Integer.parseInt(this.preferences.getString("rotationinvertkey", "1")))
			return true;
		System.out.println("3");
		if(this.purchased != this.preferences.getInt("PURCHASED", 0))
			return true;
		if(this.gifted != this.preferences.getInt("gifted", 0))
			return true;
		System.out.println("4 " + this.preferences.getInt("gallery", 0));
		if(this.galleryTheme != this.preferences.getInt("gallery", 0))
			return true;
		System.out.println("5");
		if(this.screenSliding != Integer.parseInt(this.preferences.getString("slidingkey", "2")))
			return true;
		System.out.println("6");
		if(this.userReflection != this.preferences.getInt("reflectivity", 75))
			return true;
		System.out.println("7");
		if(this.userMovement != this.preferences.getInt("movement", 20))
			return true;
		System.out.println("8");
		if(this.userDisplacement != this.preferences.getInt("displacement", 15))
			return true;
		System.out.println("9");
		if(this.environmentReflection != this.preferences.getInt("environmentkey", 0))
			return true;
		System.out.println("11");
		if(Color.rgb(this.color1[0], this.color1[1], this.color1[2]) != preferences.getInt("color1", Color.rgb(255, 63, 6)))
			return true;
		System.out.println("12");
		if(Color.rgb(this.color2[0], this.color2[1], this.color2[2]) != preferences.getInt("color2", Color.rgb(255, 242, 6)))
			return true;
		System.out.println("13");
		if(this.screenEffect != Integer.parseInt(this.preferences.getString("screeneffectkey", "0")))
			return true;
		System.out.println("14");
		if(this.cameraSpeed != Integer.parseInt(this.preferences.getString("cameraspeedkey", "1")))
			return true;
		System.out.println("15");
		if(this.screenEffectQuality != Integer.parseInt(this.preferences.getString("screeneffectqualitykey", "1")))
			return true;
		System.out.println("nothing changed");
		this.changed = 0;
		return false;
	}
}

