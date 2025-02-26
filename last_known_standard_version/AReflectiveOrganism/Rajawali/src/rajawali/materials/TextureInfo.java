package rajawali.materials;

import rajawali.materials.TextureManager.FilterType;
import rajawali.materials.TextureManager.TextureType;
import rajawali.materials.TextureManager.WrapType;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

/**
 * This class contains OpenGL specific texture information.
 * 
 * @author dennis.ippel
 * 
 */
public class TextureInfo {
	/**
	 * This texture's unique id
	 */
	protected int mTextureId;
	/**
	 * The type of texture
	 * 
	 * @see TextureManager.TextureType
	 */
	protected TextureType mTextureType;
	/**
	 * The shader uniform handle for this texture
	 */
	protected int mUniformHandle = -1;
	/**
	 * Texture width
	 */
	protected int mWidth;
	/**
	 * Texture height
	 */
	protected int mHeight;
	protected Bitmap mTexture;
	protected Bitmap[] mTextures;
	protected boolean mMipmap;
	protected Config mBitmapConfig;
	protected boolean mShouldRecycle;
	/**
	 * The type of texture
	 * 
	 * @see TextureManager.WrapType
	 */
	protected WrapType mWrapType;
	/**
	 * The type of texture
	 * 
	 * @see TextureManager.FilterType
	 */
	protected FilterType mFilterType;
	
	protected boolean isCubeMap = false;
	
	/**
	 * OpenGL bitmap format
	 */
	protected int mBitmapFormat;

	public TextureInfo(int textureId) {
		this(textureId, TextureType.DIFFUSE);
	}

	public TextureInfo(int textureId, TextureType textureType) {
		mTextureId = textureId;
		mTextureType = textureType;
	}
	
	public void setFrom(TextureInfo other) {
		mTextureId = other.getTextureId();
		mTextureType = other.getTextureType();
		mUniformHandle = other.getUniformHandle();
		mWidth = other.getWidth();
		mHeight = other.getHeight();
		mTexture = other.getTexture();
		mTextures = other.getTextures();
		mMipmap = other.isMipmap();
		mBitmapConfig = other.getBitmapConfig();
	}
	
	public void setTextureId(int id) {
		mTextureId = id;
	}

	public int getTextureId() {
		return mTextureId;
	}

	public void setUniformHandle(int handle) {
		mUniformHandle = handle;
	}

	public int getUniformHandle() {
		return mUniformHandle;
	}
	
	public boolean isCubeMap() {
		return isCubeMap;
	}
	
	public void setIsCubeMap(boolean cube) {
		isCubeMap = cube;
	}

	public String toString() {
		return "id: " + mTextureId + " handle: " + mUniformHandle + " type: " + mTextureType;
	}

	public TextureType getTextureType() {
		return mTextureType;
	}

	public void setTextureType(TextureType textureType) {
		this.mTextureType = textureType;
	}

	public int getWidth() {
		return mWidth;
	}

	public void setWidth(int width) {
		this.mWidth = width;
	}

	public int getHeight() {
		return mHeight;
	}

	public void setHeight(int height) {
		this.mHeight = height;
	}

	public Bitmap getTexture() {
		return mTexture;
	}

	public void setTexture(Bitmap texture) {
		this.mTexture = texture;
	}

	public Bitmap[] getTextures() {
		return mTextures;
	}

	public void setTextures(Bitmap[] textures) {
		this.mTextures = textures;
	}

	public boolean isMipmap() {
		return mMipmap;
	}

	public void setMipmap(boolean mipmap) {
		this.mMipmap = mipmap;
	}

	public Config getBitmapConfig() {
		return mBitmapConfig;
	}

	public void setBitmapConfig(Config bitmapConfig) {
		this.mBitmapConfig = bitmapConfig;
	}
	
	public void setFilterType(FilterType filterType) {
		this.mFilterType = filterType;
	}
	
	public FilterType getFilterType() {
		return mFilterType;
	}
	
	public void setWrapType(WrapType wrapType) {
		this.mWrapType = wrapType;
	}
	
	public WrapType getWrapType() {
		return mWrapType;
	}
	
	public boolean shouldRecycle() {
		return mShouldRecycle;
	}
	
	public void shouldRecycle(boolean should) {
		mShouldRecycle = should;
	}
}
