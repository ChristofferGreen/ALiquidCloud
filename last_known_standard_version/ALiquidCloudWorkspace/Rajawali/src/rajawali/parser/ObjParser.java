package rajawali.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;

import rajawali.BaseObject3D;
import rajawali.materials.AMaterial;
import rajawali.materials.BumpmapMaterial;
import rajawali.materials.DiffuseMaterial;
import rajawali.materials.PhongMaterial;
import rajawali.materials.TextureManager;
import rajawali.materials.TextureManager.TextureType;
import rajawali.wallpaper.Wallpaper;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

/**
 * The most important thing is that the model should be triangulated. Rajawali doesn�t accept quads, only tris. In Blender, this is an option you can select in the exporter. In a program like MeshLab, this is done automatically.
 * At the moment, Rajawali also doesn�t support per-face textures. This is on the todo list.
 * <p>
 * The options that should be checked when exporting from blender are:
 * <ul>
 * <li>Apply Modifiers
 * <li>Include Normals
 * <li>Include UVs
 * <li>Write Materials (if applicable)
 * <li>Triangulate Faces
 * <li>Objects as OBJ Objects
 * </ul>
 * <p>
 * The files should be written to your �res/raw� folder in your ADT project. Usually you�ll get errors in the console when you do this. The Android SDK ignores file extensions so it�ll regard the .obj and .mtl files as duplicates. The way to fix this is to rename the files. For instance:
 * - myobject.obj > myobject_obj
 * - myobject.mtl > myobject_mtl
 * The parser replaces any dots in file names, so this should be picked up automatically by the parser. Path fragments in front of file names (also texture paths) are discarded so you can leave them as is.
 * <p>
 * The texture file paths in the .mtl files are stripped off periods and path fragments as well. The textures need to be placed in the res/drawable-nodpi folder.
 * <p>
 * If it still throws errors check if there are any funny characters or unsupported texture formats (like bmp).
 * <p>
 * Just as a reminder, here�s the code that takes care of the parsing:
 * <pre>
 * {@code
 * ObjParser objParser = new ObjParser(mContext.getResources(), mTextureManager, R.raw.myobject_obj);
 * objParser.parse();
 * BaseObject3D mObject = objParser.getParsedObject();
 * mObject.setLight(mLight);
 * addChild(mObject);
 * }
 * </pre>
 * 
 * @author dennis.ippel
 *
 */
public class ObjParser extends AParser {
    protected final String VERTEX = "v";
    protected final String FACE = "f";
    protected final String TEXCOORD = "vt";
    protected final String NORMAL = "vn";
    protected final String OBJECT = "o";
    protected final String GROUP = "g";
    protected final String MATERIAL_LIB = "mtllib";
    protected final String USE_MATERIAL = "usemtl";
    protected final String NEW_MATERIAL = "newmtl";
    protected final String DIFFUSE_COLOR = "Kd";
    protected final String DIFFUSE_TEX_MAP = "map_Kd";
	
	public ObjParser(Resources resources, TextureManager textureManager, int resourceId) {
		super(resources, textureManager, resourceId);
	}
	
	@Override
	public void parse() {
		InputStream fileIn = mResources.openRawResource(mResourceId);
		BufferedReader buffer = new BufferedReader(new InputStreamReader(fileIn));
		String line;
		ObjIndexData currObjIndexData = new ObjIndexData(new BaseObject3D());
		ArrayList<ObjIndexData> objIndices = new ArrayList<ObjIndexData>();
				
		ArrayList<Float> vertices = new ArrayList<Float>();
		ArrayList<Float> texCoords = new ArrayList<Float>();
		ArrayList<Float> normals = new ArrayList<Float>();
		MaterialLib matLib = new MaterialLib();
		
		try {
			while((line = buffer.readLine()) != null) {
				StringTokenizer parts = new StringTokenizer(line, " ");
				int numTokens = parts.countTokens();
				
				if(numTokens == 0)
					continue;
				String type = parts.nextToken();
				
				if(type.equals(VERTEX)) {
					vertices.add(Float.parseFloat(parts.nextToken()));
					vertices.add(Float.parseFloat(parts.nextToken()));
					vertices.add(Float.parseFloat(parts.nextToken()));
				} else if(type.equals(FACE)) {
					if(numTokens != 4) {
						throw new RuntimeException("Quads are not allowed. Make sure the model contains only triangles.");
					} else {
                        boolean emptyVt = line.indexOf("//") > -1;
                        if(emptyVt) line = line.replace("//", "/");
                        
                        parts = new StringTokenizer(line);
                        
                        parts.nextToken();
                        StringTokenizer subParts = new StringTokenizer(parts.nextToken(), "/");
                        int partLength = subParts.countTokens();
                        
                        boolean hasuv = partLength >= 2 && !emptyVt;
                        boolean hasn = partLength == 3 || (partLength == 2 && emptyVt);
                        
                        for (int i = 1; i < 4; i++) {
                        	if(i > 1)
                        		subParts = new StringTokenizer(parts.nextToken(), "/");

                            currObjIndexData.vertexIndices.add((short) (Short.parseShort(subParts.nextToken()) - 1));
                            if (hasuv)
                                currObjIndexData.texCoordIndices.add((short) (Short.parseShort(subParts.nextToken()) - 1));
                            if (hasn)
                                currObjIndexData.normalIndices.add((short) (Short.parseShort(subParts.nextToken()) - 1));
                        }
					}
				} else if(type.equals(TEXCOORD)) {
					texCoords.add(Float.parseFloat(parts.nextToken()));
                    texCoords.add(Float.parseFloat(parts.nextToken()) * -1f);
				} else if(type.equals(NORMAL)) {
					normals.add(Float.parseFloat(parts.nextToken()));
                    normals.add(Float.parseFloat(parts.nextToken()));
                    normals.add(Float.parseFloat(parts.nextToken()));
				} else if(type.equals(OBJECT) || type.equals(GROUP)) {
					String objName = parts.hasMoreTokens() ? parts.nextToken() : "Object" + (int)(Math.random() * 10000);
					Log.d(Wallpaper.TAG, "Parsing object: " + objName);
					if(currObjIndexData.targetObj.getName() != null)
						currObjIndexData = new ObjIndexData(new BaseObject3D(objName));
					else
						currObjIndexData.targetObj.setName(objName);
					objIndices.add(currObjIndexData);
				} else if(type.equals(MATERIAL_LIB)) {
					if(!parts.hasMoreTokens()) continue;
					String materialLibPath = parts.nextToken().replace(".", "_");
					Log.d(Wallpaper.TAG, "Found Material Lib: " + materialLibPath);
					matLib.parse(materialLibPath, mResources.getResourceTypeName(mResourceId), mResources.getResourcePackageName(mResourceId));
				} else if(type.equals(USE_MATERIAL)) {
					currObjIndexData.materialName = parts.nextToken();
				}
			}
			buffer.close();
			
			if(objIndices.size() == 0) {
				objIndices.add(currObjIndexData);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		int numObjects = objIndices.size();
		
		for(int j=0; j<numObjects; ++j) {
			ObjIndexData oid = objIndices.get(j);
			
			int i;
			float[] aVertices 	= new float[oid.vertexIndices.size() * 3];
			float[] aTexCoords 	= new float[oid.texCoordIndices.size() * 2];
			float[] aNormals 	= new float[oid.normalIndices.size() * 3];
			float[] aColors		= new float[oid.colorIndices.size() * 4];
			short[] aIndices 	= new short[oid.vertexIndices.size()];
			
			for(i=0; i<oid.vertexIndices.size(); ++i) {
				short faceIndex = (short)(oid.vertexIndices.get(i) * 3);
				int vertexIndex = i * 3;
				aVertices[vertexIndex] = vertices.get(faceIndex);
				aVertices[vertexIndex+1] = vertices.get(faceIndex + 1);
				aVertices[vertexIndex+2] = vertices.get(faceIndex + 2);
				aIndices[i] = (short) i; 
			}
			for(i=0; i<oid.texCoordIndices.size(); ++i) {
				short texCoordIndex = (short)(oid.texCoordIndices.get(i) * 2);
				int ti = i * 2;
				aTexCoords[ti] = texCoords.get(texCoordIndex);
				aTexCoords[ti + 1] = texCoords.get(texCoordIndex + 1);
			}
			for(i=0; i<oid.colorIndices.size(); ++i) {
				short colorIndex = (short)(oid.colorIndices.get(i) * 4);
				int ti = i * 4;
				aTexCoords[ti] = texCoords.get(colorIndex);
				aTexCoords[ti + 1] = texCoords.get(colorIndex + 1);
				aTexCoords[ti + 2] = texCoords.get(colorIndex + 2);
				aTexCoords[ti + 3] = texCoords.get(colorIndex + 3);
			}
			for(i=0; i<oid.normalIndices.size(); ++i){
				short normalIndex = (short)(oid.normalIndices.get(i) * 3);
				int ni = i * 3;
				aNormals[ni] = normals.get(normalIndex);
				aNormals[ni+1] = normals.get(normalIndex + 1);
				aNormals[ni+2] = normals.get(normalIndex + 2);
			}
			
			oid.targetObj.setData(aVertices, aNormals, aTexCoords, aColors, aIndices);
			matLib.setMaterial(oid.targetObj, oid.materialName);
			mRootObject.addChild(oid.targetObj);
		}
		
		if(mRootObject.getNumChildren() == 1)
			mRootObject = mRootObject.getChildAt(0);
	}
	
	protected class ObjIndexData {
		public BaseObject3D targetObj;
		
		public ArrayList<Short> vertexIndices;
		public ArrayList<Short> texCoordIndices;
		public ArrayList<Short> colorIndices;
		public ArrayList<Short> normalIndices;
		
		public String materialName;
		
		public ObjIndexData(BaseObject3D targetObj) {
			this.targetObj = targetObj;
			vertexIndices = new ArrayList<Short>();
			texCoordIndices = new ArrayList<Short>();
			colorIndices = new ArrayList<Short>();
			normalIndices = new ArrayList<Short>();
		}
	}
	
	protected class MaterialLib {
		private final String MATERIAL_NAME = "newmtl";
		private final String AMBIENT_COLOR = "Ka";
		private final String DIFFUSE_COLOR = "Kd";
		private final String SPECULAR_COLOR = "Ks";
		private final String SPECULAR_COEFFICIENT = "Ns";
		private final String ALPHA_1 = "d";
		private final String ALPHA_2 = "Tr";
		private final String AMBIENT_TEXTURE = "map_Ka";
		private final String DIFFUSE_TEXTURE = "map_Kd";
		private final String SPECULAR_COLOR_TEXTURE = "map_Ks";
		private final String SPECULAR_HIGHLIGHT_TEXTURE = "map_Ns";
		private final String ALPHA_TEXTURE_1 = "map_d";
		private final String ALPHA_TEXTURE_2 = "map_Tr";
		private final String BUMP_TEXTURE = "map_Bump";
		
		private Stack<MaterialDef> mMaterials;
		private String mResourcePackage;
		
		public MaterialLib() {
			mMaterials = new Stack<ObjParser.MaterialDef>();
		}
		
		public void parse(String materialLibPath, String resourceType, String resourcePackage) {
			mResourcePackage = resourcePackage;
			int identifier = mResources.getIdentifier(materialLibPath, resourceType, resourcePackage);
			
			InputStream fileIn = mResources.openRawResource(identifier);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(fileIn));
			String line;
			MaterialDef matDef = null;
			
			try {
				while((line = buffer.readLine()) != null) {
					StringTokenizer parts = new StringTokenizer(line, " ");
					int numTokens = parts.countTokens();
					
					if(numTokens == 0)
						continue;
					String type = parts.nextToken();
					type.replace("\t", "");
					
					if(type.equals(MATERIAL_NAME)) {
						if(matDef != null) mMaterials.add(matDef);
						matDef = new MaterialDef();
						matDef.name = parts.hasMoreTokens() ? parts.nextToken() : "";
						Log.d(Wallpaper.TAG, "Parsing material: " + matDef.name);
					} else if(type.equals(DIFFUSE_COLOR)) {
						matDef.diffuseColor = getColorFromParts(parts);
					} else if(type.equals(AMBIENT_COLOR)) {
						matDef.ambientColor = getColorFromParts(parts);
					} else if(type.equals(SPECULAR_COLOR)) {
						matDef.specularColor = getColorFromParts(parts);
					} else if(type.equals(SPECULAR_COEFFICIENT)) {
						matDef.specularCoefficient = Float.parseFloat(parts.nextToken());
					} else if(type.equals(ALPHA_1) || type.equals(ALPHA_2)) {
						matDef.alpha = Float.parseFloat(parts.nextToken());
					} else if(type.equals(AMBIENT_TEXTURE)) {
						matDef.ambientTexture = getFileNameWithoutExtension(parts.nextToken());
					} else if(type.equals(DIFFUSE_TEXTURE)) {
						matDef.diffuseTexture = getFileNameWithoutExtension(parts.nextToken());
					} else if(type.equals(SPECULAR_COLOR_TEXTURE)) {
						matDef.specularColorTexture = getFileNameWithoutExtension(parts.nextToken());
					} else if(type.equals(SPECULAR_HIGHLIGHT_TEXTURE)) {
						matDef.specularHightlightTexture = getFileNameWithoutExtension(parts.nextToken());
					} else if(type.equals(ALPHA_TEXTURE_1) || type.equals(ALPHA_TEXTURE_2)) {
						matDef.alphaTexture = getFileNameWithoutExtension(parts.nextToken());
					} else if(type.equals(BUMP_TEXTURE)) {
						matDef.bumpTexture = getFileNameWithoutExtension(parts.nextToken());
					}
				}
				if(matDef != null) mMaterials.add(matDef);
				buffer.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		public void setMaterial(BaseObject3D object, String materialName) {
			MaterialDef matDef = null;
			
			for(int i=0; i<mMaterials.size(); ++i) {
				if(mMaterials.get(i).name.equals(materialName))
				{
					matDef = mMaterials.get(i);
					break;
				}
			}

			boolean hasTexture = matDef != null && matDef.diffuseTexture != null;
			boolean hasBump = matDef != null && matDef.bumpTexture != null;
			boolean hasSpecular = matDef != null && matDef.specularColor > 0xff000000;
			
			AMaterial mat = null;
			
			if(hasSpecular && !hasBump)
				mat = new PhongMaterial();
			else if(hasBump)
				mat = new BumpmapMaterial();
			else
				mat = new DiffuseMaterial();

			mat.setUseColor(!hasTexture);
			object.setColor(matDef != null ? matDef.diffuseColor : (0xff000000 + ((int)(Math.random() * 0xffffff))));
			object.setMaterial(mat);
			if(hasSpecular && !hasBump) {
				PhongMaterial phong = (PhongMaterial)mat;
				phong.setSpecularColor(matDef.specularColor);
				phong.setShininess(matDef.specularCoefficient);
			}
			
			if(hasTexture) {Log.d("Rajawali", mResourcePackage +" / " + matDef.diffuseTexture);
				int identifier = mResources.getIdentifier(matDef.diffuseTexture, "drawable", mResourcePackage);
				object.addTexture(mTextureManager.addTexture(BitmapFactory.decodeResource(mResources, identifier)));
			}
			if(hasBump) {
				int identifier = mResources.getIdentifier(matDef.bumpTexture, "drawable", mResourcePackage);
				object.addTexture(mTextureManager.addTexture(BitmapFactory.decodeResource(mResources, identifier), TextureType.BUMP));
			}
		}
		
		private int getColorFromParts(StringTokenizer parts) {
			int r = (int)(Float.parseFloat(parts.nextToken()) * 255f);
			int g = (int)(Float.parseFloat(parts.nextToken()) * 255f);
			int b = (int)(Float.parseFloat(parts.nextToken()) * 255f);
			return Color.rgb(r, g, b);
		}
		
		private String getFileNameWithoutExtension(String fileName) {
			String fName = fileName.substring(0, fileName.lastIndexOf("."));
			int indexOf = fName.lastIndexOf("\\");
			if(indexOf > -1)
				fName = fName.substring(indexOf + 1, fName.length());
			indexOf = fName.lastIndexOf("/");
			if(indexOf > -1)
				fName = fName.substring(indexOf, fName.length());
			return fName;
		}
	}
}
