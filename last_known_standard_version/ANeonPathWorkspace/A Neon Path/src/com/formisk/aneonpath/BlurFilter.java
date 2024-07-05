package com.formisk.aneonpath;

import rajawali.filters.IPostProcessingFilter;
import rajawali.materials.AMaterial;

public class BlurFilter extends AMaterial implements IPostProcessingFilter {
	protected static final String mVShader =
		"uniform mat4 uMVPMatrix;\n" +

		"attribute vec4 aPosition;\n" +
		"attribute vec2 aTextureCoord;\n" +
		"attribute vec4 aColor;\n" +

		"varying vec2 vTextureCoord;\n" +

		"void main() {\n" +
		"	gl_Position = uMVPMatrix * aPosition;\n" +
		"	vTextureCoord = aTextureCoord;\n" +
		"}\n";
	
	/**
	 * From the OpenGL Super Bible
	 */
	protected static final String mFShader = 
			"precision mediump float;\n" +
			"varying vec2 vTextureCoord;\n" +
			
			"uniform sampler2D uFrameBufferTexture;\n" +
			"const float blurSize = 1.0/256.0;\n" +

			"void main() {\n" +
			"   vec4 sum = vec4(0.0);\n" +
			"   sum += texture2D(uFrameBufferTexture, vec2(vTextureCoord.x - 4.0*blurSize, vTextureCoord.y)) * 0.05;\n" +
			"   sum += texture2D(uFrameBufferTexture, vec2(vTextureCoord.x - 3.0*blurSize, vTextureCoord.y)) * 0.09;\n" +
			"   sum += texture2D(uFrameBufferTexture, vec2(vTextureCoord.x - 2.0*blurSize, vTextureCoord.y)) * 0.12;\n" +
			"   sum += texture2D(uFrameBufferTexture, vec2(vTextureCoord.x - blurSize, vTextureCoord.y)) * 0.15;\n" +
			"   sum += texture2D(uFrameBufferTexture, vec2(vTextureCoord.x, vTextureCoord.y)) * 0.16;\n" +
			"   sum += texture2D(uFrameBufferTexture, vec2(vTextureCoord.x + blurSize, vTextureCoord.y)) * 0.15;\n" +
			"   sum += texture2D(uFrameBufferTexture, vec2(vTextureCoord.x + 2.0*blurSize, vTextureCoord.y)) * 0.12;\n" +
			"   sum += texture2D(uFrameBufferTexture, vec2(vTextureCoord.x + 3.0*blurSize, vTextureCoord.y)) * 0.09;\n" +
			"   sum += texture2D(uFrameBufferTexture, vec2(vTextureCoord.x + 4.0*blurSize, vTextureCoord.y)) * 0.05;\n" +
			"   gl_FragColor = (sum*0.8)+(0.6*texture2D(uFrameBufferTexture, vTextureCoord));\n" +
			"}\n";
			
	public BlurFilter() {
		super(mVShader, mFShader, false);
	}
	
	public boolean usesDepthBuffer() {
		return false;
	}
	
	@Override
	public void useProgram() {
		super.useProgram();
	}
	
	@Override
	public void setShaders(String vertexShader, String fragmentShader)
	{
		super.setShaders(vertexShader, fragmentShader);
	}
}
