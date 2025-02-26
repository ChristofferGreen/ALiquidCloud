package rajawali.materials;



public class SkyboxMaterial extends AMaterial {
	protected static final String mVShader = 
			"uniform mat4 uMVPMatrix;\n" +
					
			"attribute vec4 aPosition;\n" +
			"attribute vec3 aTextureCoord;\n" +
			"attribute vec4 aColor;\n" +
			"attribute vec3 aNormal;\n" +

			"varying vec3 vTextureCoord;\n" +
			"varying vec4 vColor;\n" +		

			"void main() {\n" +
			"	gl_Position = uMVPMatrix * aPosition;\n" +
			"	vTextureCoord = aTextureCoord;\n" +
			"	vColor = aColor;\n" +
			"}\n";
		
		protected static final String mFShader = 
			"precision mediump float;\n" +

			"varying vec3 vTextureCoord;\n" +
			"uniform samplerCube uTexture0;\n" +
			"varying vec4 vColor;\n" +

			"uniform bool uUseTexture;\n" +

			"void main() {\n" +
			"	gl_FragColor = textureCube(uTexture0, vTextureCoord);\n" +
			"}\n";
		
	public SkyboxMaterial() {
		super(mVShader, mFShader, false);
		usesCubeMap = true;
	}
}
