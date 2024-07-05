package com.formisk.areflectiveorganism;

import java.nio.FloatBuffer;
import android.util.FloatMath;
import android.graphics.Color;
import android.opengl.GLES20;
import android.os.SystemClock;
import rajawali.materials.AMaterial;
import rajawali.math.Matrix4;
import rajawali.math.Quaternion;
import rajawali.math.Number3D;

public class OrganicMaterial extends AReflectiveOrganismMaterial {
	public AReflectiveOrganismWallpaper mWallpaper = null;
	public int color1[] = new int[3];
	public int color1Loc;
	public int color2[] = new int[3];
	public int color2Loc;
	public float cameraPosRotated[] = new float[3];
	public int cameraPosRotatedLoc;
	public float Nrotated[] = new float[3];
	public int NrotatedLoc;
	public int userReflectionLoc;
	public float userReflection;
	public int userDisplacementLoc;
	public float userDisplacement;
	public int userMovementLoc;
	public float userMovement;
	public int slidingLoc;
	public float sliding = 0.0f;
	public int xRatioLoc;
	public float xRatio;
	public int yRatioLoc;
	public float yRatio;
	FloatBuffer normalRotationMatrix = FloatBuffer.allocate(9);
	public int normalRotationMatrixLoc;
	float prevX = 0.0f;
	float prevY = 0.0f;

	public static final String mVShader =
			"precision highp float;\n" +
			"uniform mat4 uMVPMatrix;\n" +
			"uniform mat4 uMMatrix;\n" +
			"uniform mat3 uNMatrix;\n" +
			"uniform float time;\n" +
			"uniform float sliding;\n" +
			"uniform float xRatio;\n" +
			"uniform float yRatio;\n" +
			"uniform vec3 uCameraPosition;\n" +
			"uniform vec3 cameraPosRotated;\n" +
			"uniform vec3 Nrotated;\n" +
			
			"attribute vec3 aNormal;\n" +
			"attribute vec4 aPosition;\n" +
			"attribute vec2 aTextureCoord;\n" +

			"varying vec2 vTextureCoord;\n" +
			"varying vec3 Vrotated;\n" +

			"void main() {\n" +
			"	gl_Position = uMVPMatrix * aPosition;\n" +
			"	vTextureCoord = aTextureCoord;\n" +
			"	vTextureCoord.y = vTextureCoord.y*yRatio;\n" +
			"	vTextureCoord.y = vTextureCoord.y+sliding;\n" +
			"	vTextureCoord.x = vTextureCoord.x*xRatio;\n" +
			
			"	Vrotated = aNormal;\n" +
			"}\n";
		
		public static final String mFShader = 
			"precision highp float;\n" +

			"varying vec2 vTextureCoord;\n" +
			"varying vec3 Vrotated;\n" +
			
			"uniform sampler2D uDiffuseTexture;\n" +
			"uniform sampler2D uNormalTexture;\n" +
			"uniform samplerCube uCubeMapTexture;\n" +
			"uniform vec3 color1;\n" +
			"uniform vec3 color2;\n" +
			"uniform float time;\n" +
			"uniform float userReflection;\n" +
			"uniform float userMovement;\n" +
			"uniform float userDisplacement;\n" +
			"uniform float sliding;\n" +
			"uniform vec3 cameraPosRotated;\n" +
			"uniform vec3 Nrotated;\n" +

			"void main() {\n" +

			"   float firstLevelColor = texture2D(uNormalTexture, vTextureCoord).r;\n" +
			//"   float secondLevelColor = texture2D(uNormalTexture, vTextureCoord+firstLevelColor+time*userMovement).r;\n" +
			"   float secondLevelColor = texture2D(uNormalTexture, vTextureCoord+firstLevelColor+time).r;\n" +
			"   vec2 modifiedCoords = secondLevelColor*userDisplacement+vTextureCoord;\n" +

			// load textures
			"	vec3 base = texture2D(uDiffuseTexture, modifiedCoords).rgb;\n" +
			"	vec3 perturb = texture2D(uNormalTexture, modifiedCoords).rgb;\n" +

			// decode textures
			"	float bump = perturb.r;\n" +
			"	float ambientOcclusion = perturb.g;\n" +
			"	float colorization = perturb.b;\n" +
			"	vec3  rawNormal = vec3(base.r, base.g, 1.0);\n" +
			"	float diffuseStructure = base.b;\n" +

			// calculate normal
			//"	vec3 normal = Nrotated;" +
			"	vec3 normal = normalize(Nrotated + rawNormal*0.4);" +
			//"	vec3 normal = normalize(rawNormal);" +			

			// calculate reflection
			"	vec3 modifiedVertexPosition = vec3(Vrotated.x+modifiedCoords.x, Vrotated.y+modifiedCoords.y-sliding, Vrotated.z);\n" +
			//"	vec3 eyeDir = normalize(modifiedVertexPosition - cameraPosRotated);\n" +
			"	vec3 eyeDir = normalize(Vrotated - cameraPosRotated);\n" +
			"	vec3 vReflectDir = normalize(reflect(eyeDir, normal));\n" +
			"	vec3 reflection = textureCube(uCubeMapTexture, vReflectDir).rgb;\n" +

			// colorize from user supplied colors
			"   vec3 appliedColor = mix(color1, color2, colorization);\n" +
			
			"   vec3 diffuse = appliedColor*diffuseStructure*1.5;\n" + 
			"   float ambient = ambientOcclusion+0.3;\n" + 
			"   vec3 reflectivity = reflection*ambientOcclusion*userReflection;\n" +
			
			"   vec3 color = diffuse*ambient+reflectivity;\n" +
			"   gl_FragColor = vec4(color, 1.0);\n" +
			//"   gl_FragColor = vec4(Vrotated, 1.0);\n" +
			"}\n";
		
	public OrganicMaterial() {
		super(mVShader, mFShader, false);
		usesCubeMap = true;
		userReflectionLoc = GLES20.glGetUniformLocation ( this.mProgram, "userReflection" );
		userMovementLoc = GLES20.glGetUniformLocation ( this.mProgram, "userMovement" );
		userDisplacementLoc = GLES20.glGetUniformLocation ( this.mProgram, "userDisplacement" );
		color1Loc = GLES20.glGetUniformLocation ( this.mProgram, "color1" );
		color2Loc = GLES20.glGetUniformLocation ( this.mProgram, "color2" );
		slidingLoc = GLES20.glGetUniformLocation ( this.mProgram, "sliding" );
		xRatioLoc = GLES20.glGetUniformLocation ( this.mProgram, "xRatio" );
		yRatioLoc = GLES20.glGetUniformLocation ( this.mProgram, "yRatio" );
		cameraPosRotatedLoc = GLES20.glGetUniformLocation ( this.mProgram, "cameraPosRotated" );
		NrotatedLoc = GLES20.glGetUniformLocation ( this.mProgram, "Nrotated" );
	}

	@Override
	public void updateUniform() {
		GLES20.glUseProgram(mProgram); 
		if (lastTime == 0)
			lastTime = SystemClock.uptimeMillis();
		long curTime = SystemClock.uptimeMillis();
		long elapsedTime = curTime - lastTime;
		float deltaTime = (elapsedTime / 20000.0f)*this.userMovement;
		lastTime = curTime;
		if(time >= 1.0f)
			time = 0.0f;
		time += deltaTime;
		GLES20.glUniform1f ( timeLoc, time );
		GLES20.glUniform3f(this.color1Loc, this.color1[0]/255.0f, this.color1[1]/255.0f, this.color1[2]/255.0f);
		GLES20.glUniform3f(this.color2Loc, this.color2[0]/255.0f, this.color2[1]/255.0f, this.color2[2]/255.0f);
		GLES20.glUniform1f(this.userReflectionLoc, this.userReflection);
		GLES20.glUniform1f(this.userMovementLoc, this.userMovement*10.0f);
		GLES20.glUniform1f(this.userDisplacementLoc, this.userDisplacement);
		GLES20.glUniform1f(this.slidingLoc, this.sliding);
		GLES20.glUniform1f(this.xRatioLoc, this.xRatio);
		GLES20.glUniform1f(this.yRatioLoc, this.yRatio);
		GLES20.glUniform3f(this.cameraPosRotatedLoc, this.cameraPosRotated[0], this.cameraPosRotated[1], this.cameraPosRotated[2]);
		GLES20.glUniform3f(this.NrotatedLoc, this.Nrotated[0], this.Nrotated[1], this.Nrotated[2]);
		//System.out.println("time: " + this.time);
	}
}

