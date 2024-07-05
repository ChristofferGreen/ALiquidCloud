package com.formisk.areflectiveorganism;

import rajawali.materials.TextureInfo;
import rajawali.materials.TextureManager;
import rajawali.materials.TextureManager.TextureType;

import com.formisk.areflectiveorganism.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class TextureLoader {
	public Bitmap[] environmentTextures;
    public Bitmap textureDiffuse = null;
    public Bitmap textureNormal = null;
    public AReflectiveOrganismWallpaper wallpaper = null;
    public BitmapFactory.Options options;
    public int loadedTheme = -1;
    public int loadedEnvironment = -1;
    public TextureInfo diffuseTexture = null;
    public TextureInfo normalTexture = null;
    public TextureInfo envTexture = null;
	
	public TextureLoader(AReflectiveOrganismWallpaper wallpaper) {
		this.wallpaper = wallpaper;
		this.options = new BitmapFactory.Options();
		this.options.inDither = true;
		this.options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		this.options.inScaled = false;
		this.environmentTextures = new Bitmap[6];
	}
	
	public void load(int theme, int environment, TextureManager textureManager) {
		//if(theme != this.loadedTheme || environment != this.loadedEnvironment)  {
			textureManager.reset();
			System.out.println("loading diffuse+normal texture");
			if(theme == 0 || theme == 1) {
				this.textureDiffuse = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.base0), new Rect(0, 0, 512, 512), this.options);
				this.textureNormal = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.perturb0), new Rect(0, 0, 512, 512), this.options);
			}
			else if(theme == 2) {
				this.textureDiffuse = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.base1), new Rect(0, 0, 512, 512), this.options);
				this.textureNormal = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.perturb1), new Rect(0, 0, 512, 512), this.options);
			}
			else if(theme == 3) {
				this.textureDiffuse = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.base2), new Rect(0, 0, 512, 512), this.options);
				this.textureNormal = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.perturb2), new Rect(0, 0, 512, 512), this.options);
			}
			else {
				this.textureDiffuse = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.base3), new Rect(0, 0, 512, 512), this.options);
				this.textureNormal = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.perturb3), new Rect(0, 0, 512, 512), this.options);
			}
			
			this.diffuseTexture = textureManager.addTexture(this.textureDiffuse, TextureType.DIFFUSE, false, false);
			this.normalTexture = textureManager.addTexture(this.textureNormal, TextureType.BUMP, false, false);
			System.out.println("loading environment texture");
			if(environment == 1) {
				this.environmentTextures[0] = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.gracepx), new Rect(0, 0, 256, 256), this.options);
				this.environmentTextures[1] = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.gracenx), new Rect(0, 0, 256, 256), this.options);
				this.environmentTextures[2] = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.gracepy), new Rect(0, 0, 256, 256), this.options);
				this.environmentTextures[3] = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.graceny), new Rect(0, 0, 256, 256), this.options);
				this.environmentTextures[4] = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.gracepz), new Rect(0, 0, 256, 256), this.options);
				this.environmentTextures[5] = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.gracenz), new Rect(0, 0, 256, 256), this.options);
			}
			else if(environment == 2) {
				this.environmentTextures[0] = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.entrance_c00), new Rect(0, 0, 256, 256), this.options);
				this.environmentTextures[1] = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.entrance_c01), new Rect(0, 0, 256, 256), this.options);
				this.environmentTextures[2] = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.entrance_c02), new Rect(0, 0, 256, 256), this.options);
				this.environmentTextures[3] = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.entrance_c03), new Rect(0, 0, 256, 256), this.options);
				this.environmentTextures[4] = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.entrance_c04), new Rect(0, 0, 256, 256), this.options);
				this.environmentTextures[5] = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.entrance_c05), new Rect(0, 0, 256, 256), this.options);
			}
			else if(environment == 3) {
				this.environmentTextures[0] = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.emptyroom_c00), new Rect(0, 0, 256, 256), this.options);
				this.environmentTextures[1] = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.emptyroom_c01), new Rect(0, 0, 256, 256), this.options);
				this.environmentTextures[2] = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.emptyroom_c02), new Rect(0, 0, 256, 256), this.options);
				this.environmentTextures[3] = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.emptyroom_c03), new Rect(0, 0, 256, 256), this.options);
				this.environmentTextures[4] = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.emptyroom_c04), new Rect(0, 0, 256, 256), this.options);
				this.environmentTextures[5] = BitmapFactory.decodeStream(wallpaper.getResources().openRawResource(R.raw.emptyroom_c05), new Rect(0, 0, 256, 256), this.options);
			}
			this.envTexture = textureManager.addCubemapTextures(this.environmentTextures, false);
		//}
		this.loadedEnvironment = environment;
		this.loadedTheme = theme;
	}

}
