package com.formisk.areflectivedarkness;

public class RedWhiteBlueMaterial extends ALiquidCloudMaterial {

	protected static final String mFShader = 
			mFShaderBase +
			"   col=col*q.y+r*q.x+r*q.y;\n" +
			"	gl_FragColor = vec4(col*r, col*r*q.x, col*q.y, 1.0);\n" +
			"}\n";	
	
	public RedWhiteBlueMaterial() {
		super(mVShader, mFShader);
	}
	
}
