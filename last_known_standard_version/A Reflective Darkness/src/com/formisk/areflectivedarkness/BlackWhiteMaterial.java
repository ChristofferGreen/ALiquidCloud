package com.formisk.areflectivedarkness;

public class BlackWhiteMaterial extends ALiquidCloudMaterial {
	protected static final String mFShader = 
			mFShaderBase +
			"   col=(col*q.y+r*q.x)*q.y;\n" +
			"	gl_FragColor = vec4(col, col, col, 1.0);\n" +
			"}\n";	

	
	public BlackWhiteMaterial() {
		super(mVShader, mFShader);
	}
}
