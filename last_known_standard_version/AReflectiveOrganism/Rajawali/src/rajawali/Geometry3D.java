package rajawali;

import java.nio.Buffer;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import rajawali.animation.mesh.VertexAnimationObject3D;
import rajawali.bounds.BoundingBox;
import rajawali.bounds.BoundingSphere;
import rajawali.renderer.RajawaliRenderer;
import rajawali.util.BufferUtil;
import rajawali.util.RajLog;
import android.graphics.Color;
import android.opengl.GLES20;

/**
 * This is where the vertex, normal, texture coordinate, color and index data is stored.
 * The data is stored in FloatBuffers, IntBuffers and ShortBuffers. The data is uploaded
 * to the graphics card using Vertex Buffer Objects (VBOs). The data in the FloatBuffers
 * is kept in memory in order to restore the VBOs when the OpenGL context needs to be
 * restored (typically when the application regains focus).
 * <p>
 * An object's Geometry3D and its data can be accessed by calling the getGeometry() and its methods:
 * <pre><code> // Get the geometry instance
 * Geometry3D geom = mMyObject3D.getGeometry();
 * // Get vertices (x, y, z)
 * FloatBuffer verts = geom.getVertices();
 * // Get normals (x, y, z)
 * FloatBuffer normals = geom.getNormals();
 * // Get texture coordinates (u, v)
 * FloatBuffer texCoords = geom.getTextureCoords();
 * // Get colors (r, g, b, a)
 * FloatBuffer colors = geom.getColors();
 * // Get indices. This can be either a ShortBuffer or a FloatBuffer. This depends
 * // on the device it runs on. (See RajawaliRenderer.supportsUIntBuffers)
 * FloatBuffer indices = geom.getIndices();
 * ShortBuffer indices = geom.getIndices();
 * </pre></code>
 * 
 * @see RajawaliRenderer.supportsUIntBuffers
 * @author dennis.ippel
 *
 */
public class Geometry3D {
	public static final int FLOAT_SIZE_BYTES = 4;
	public static final int INT_SIZE_BYTES = 4;
	public static final int SHORT_SIZE_BYTES = 2;
	
	/**
	 * FloatBuffer containing vertex data (x, y, z)
	 */
	protected FloatBuffer mVertices;
	/**
	 * FloatBuffer containing normal data (x, y, z) 
	 */
	protected FloatBuffer mNormals;
	/**
	 * FloatBuffer containing texture coordinates (u, v)
	 */
	protected FloatBuffer mTextureCoords;
	/**
	 * FloatBuffer containing color data (r, g, b, a)
	 */
	protected FloatBuffer mColors;
	/**
	 * IntBuffer containing index data. Whether this buffer is used or not depends
	 * on the hardware capabilities. If int buffers aren't supported then short
	 * buffers will be used.
	 * @see RajawaliRenderer.supportsUIntBuffers
	 */
	protected IntBuffer mIndicesInt;
	/**
	 * ShortBuffer containing index data. Whether this buffer is used or not depends
	 * on the hardware capabilities. If int buffers aren't supported then short
	 * buffers will be used.
	 * @see RajawaliRenderer.supportsUIntBuffers
	 */
	protected ShortBuffer mIndicesShort;
	/**
	 * The number of indices currently stored in the index buffer.
	 */
	protected int mNumIndices;
	/**
	 * The number of vertices currently stored in the vertex buffer.
	 */
	protected int mNumVertices;
	/**
	 * A pointer to the original geometry. This is not null when the object has been cloned.
	 * When cloning a BaseObject3D the data isn't copied over, only the handle to the OpenGL
	 * buffers are used.
	 */
	protected Geometry3D mOriginalGeometry;
	/**
	 * Vertex buffer info object.
	 */
	protected BufferInfo mVertexBufferInfo;
	/**
	 * Index buffer info object.
	 */
	protected BufferInfo mIndexBufferInfo;
	/**
	 * Texture coordinate buffer info object.
	 */
	protected BufferInfo mTexCoordBufferInfo;
	/**
	 * Color buffer info object.
	 */
	protected BufferInfo mColorBufferInfo;
	/**
	 * Normal buffer info object.
	 */
	protected BufferInfo mNormalBufferInfo;
	/**
	 * Indices whether only short buffers are supported. Not all devices support
	 * integer buffers.
	 * @see RajawaliRenderer.supportsUIntBuffers
	 */
	protected boolean mOnlyShortBufferSupported = false;
	/**
	 * The bounding box for this geometry. This is used for collision detection. 
	 */
	protected BoundingBox mBoundingBox;
	/**
	 * The bounding sphere for this geometry. This is used for collision detection.
	 */
	protected BoundingSphere mBoundingSphere;
	public enum BufferType {
		FLOAT_BUFFER,
		INT_BUFFER,
		SHORT_BUFFER
	}
	
	public Geometry3D() {
		super();
		mVertexBufferInfo = new BufferInfo();
		mIndexBufferInfo = new BufferInfo();
		mTexCoordBufferInfo = new BufferInfo();
		mColorBufferInfo = new BufferInfo();
		mNormalBufferInfo = new BufferInfo();
	}
	
	/**
	 * Copies another Geometry3D's BufferInfo objects. This means that it
	 * doesn't copy or clone the actual data. It will just use the pointers
	 * to the other Geometry3D's buffers.
	 * @param geom
	 * @see BufferInfo
	 */
	public void copyFromGeometry3D(Geometry3D geom) {
		this.mNumIndices = geom.getNumIndices();
		this.mNumVertices = geom.getNumVertices();
		this.mVertexBufferInfo = geom.getVertexBufferInfo();
		this.mIndexBufferInfo = geom.getIndexBufferInfo();
		this.mTexCoordBufferInfo = geom.getTexCoordBufferInfo();
		if(mColors == null) this.mColorBufferInfo = geom.getColorBufferInfo();
		this.mNormalBufferInfo = geom.getNormalBufferInfo();
		this.mOriginalGeometry = geom;
	}
	
	/**
	 * Sets the data. This methods takes two BufferInfo objects which means it'll use another
	 * Geometry3D instance's data (vertices and normals). The remaining parameters are arrays
	 * which will be used to create buffers that are unique to this instance.
	 * <p>
	 * This is typically used with VertexAnimationObject3D instances.
	 * 
	 * @param vertexBufferInfo
	 * @param normalBufferInfo
	 * @param textureCoords
	 * @param colors
	 * @param indices
	 * @see VertexAnimationObject3D
	 */
	public void setData(BufferInfo vertexBufferInfo, BufferInfo normalBufferInfo,
			float[] textureCoords, float[] colors, int[] indices) {
		if(textureCoords == null || textureCoords.length == 0)
			textureCoords = new float[(mNumVertices / 3) * 2];
		setTextureCoords(textureCoords);
		if(colors == null || colors.length == 0)
			setColors(0xff000000 + (int)(Math.random() * 0xffffff));
		else
			setColors(colors);	
		setIndices(indices);
		
		mVertexBufferInfo = vertexBufferInfo;
		mNormalBufferInfo = normalBufferInfo;
		
		mOriginalGeometry = null;
		
		createBuffers();
	}
	
	/**
	 * Sets the data. Assumes that the data will never be changed and passes GLES20.GL_STATIC_DRAW
	 * to the OpenGL context when the buffers are created. 
	 * 
	 * @param vertices
	 * @param normals
	 * @param textureCoords
	 * @param colors
	 * @param indices
	 * @see GLES20.GL_STATIC_DRAW
	 */
	public void setData(float[] vertices, float[] normals,
			float[] textureCoords, float[] colors, int[] indices) {
		setData(vertices, GLES20.GL_STATIC_DRAW, normals, GLES20.GL_STATIC_DRAW, textureCoords, GLES20.GL_STATIC_DRAW, colors, GLES20.GL_STATIC_DRAW, indices, GLES20.GL_STATIC_DRAW);
	}
	
	/**
	 * Sets the data. This method takes an additional parameters that specifies the data used for each buffer.
	 * <p>
	 * Usage is a hint to the GL implementation as to how a buffer object's data store will be accessed. This enables the GL implementation to make more intelligent decisions that may significantly impact buffer object performance. It does not, however, constrain the actual usage of the data store. 
	 * <p>
	 * Usage can be broken down into two parts: first, the frequency of access (modification and usage), and second, the nature of that access. The frequency of access may be one of these:
	 * <p>
	 * STREAM
	 * The data store contents will be modified once and used at most a few times.
	 * <p>
	 * STATIC
	 * The data store contents will be modified once and used many times.
	 * <p>
	 * DYNAMIC
	 * The data store contents will be modified repeatedly and used many times.
	 * <p>
	 * The nature of access may be one of these:
	 * <p>
	 * DRAW
	 * The data store contents are modified by the application, and used as the source for GL drawing and image specification commands.
	 * <p>
	 * READ
	 * The data store contents are modified by reading data from the GL, and used to return that data when queried by the application.
	 * <p>
	 * COPY
	 * The data store contents are modified by reading data from the GL, and used as the source for GL drawing and image specification commands.
	 * 
	 * @param vertices
	 * @param verticesUsage
	 * @param normals
	 * @param normalsUsage
	 * @param textureCoords
	 * @param textureCoordsUsage
	 * @param colors
	 * @param colorsUsage
	 * @param indices
	 * @param indicesUsage
	 */
	public void setData(float[] vertices, int verticesUsage, float[] normals, int normalsUsage,
			float[] textureCoords, int textureCoordsUsage, float[] colors, int colorsUsage, 
			int[] indices, int indicesUsage) {
		mVertexBufferInfo.usage = verticesUsage;
		mNormalBufferInfo.usage = normalsUsage;
		mTexCoordBufferInfo.usage = textureCoordsUsage;
		mColorBufferInfo.usage = colorsUsage;
		mIndexBufferInfo.usage = indicesUsage;
		setVertices(vertices);
		setNormals(normals);
		if(textureCoords == null || textureCoords.length == 0)
			textureCoords = new float[(vertices.length / 3) * 2];
		
		setTextureCoords(textureCoords);
		if(colors == null || colors.length == 0)
			setColors(0xff000000 + (int)(Math.random() * 0xffffff));
		else
			setColors(colors);	
		setIndices(indices);

		createBuffers();
	}
	
	/**
	 * Creates the actual Buffer objects. 
	 */
	public void createBuffers() {
		boolean supportsUIntBuffers = RajawaliRenderer.supportsUIntBuffers;
		
		if(mVertices != null) {
			mVertices.compact().position(0);
			createBuffer(mVertexBufferInfo, BufferType.FLOAT_BUFFER, mVertices, GLES20.GL_ARRAY_BUFFER);
		}
		if(mNormals != null) {
			mNormals.compact().position(0);
			createBuffer(mNormalBufferInfo, BufferType.FLOAT_BUFFER, mNormals, GLES20.GL_ARRAY_BUFFER);
		}
		if(mTextureCoords != null) {
			mTextureCoords.compact().position(0);
			createBuffer(mTexCoordBufferInfo, BufferType.FLOAT_BUFFER, mTextureCoords, GLES20.GL_ARRAY_BUFFER);
		}
		if(mColors != null) {
			mColors.compact().position(0);
			createBuffer(mColorBufferInfo, BufferType.FLOAT_BUFFER, mColors, GLES20.GL_ARRAY_BUFFER);
		}
		if(mIndicesInt != null && !mOnlyShortBufferSupported && supportsUIntBuffers) {
			mIndicesInt.compact().position(0);
			createBuffer(mIndexBufferInfo, BufferType.INT_BUFFER, mIndicesInt, GLES20.GL_ELEMENT_ARRAY_BUFFER);
		}
		
		if(mOnlyShortBufferSupported || !supportsUIntBuffers) {
			mOnlyShortBufferSupported = true;
			
			if(mIndicesShort == null && mIndicesInt != null) {
				mIndicesInt.position(0);
				mIndicesShort = ByteBuffer
						.allocateDirect(mNumIndices * SHORT_SIZE_BYTES)
						.order(ByteOrder.nativeOrder()).asShortBuffer();
				
				try {
					for(int i=0; i<mNumIndices; ++i) {
						mIndicesShort.put((short)mIndicesInt.get(i));
					}
				} catch(BufferOverflowException e) {
					RajLog.e("Buffer overflow. Unfortunately your device doesn't supported int type index buffers. The mesh is too big.");
					throw(e);
				}
				
				mIndicesInt.clear();
				mIndicesInt.limit();
				mIndicesInt = null;
			}
			if(mIndicesShort != null) {
				mIndicesShort.compact().position(0);
				createBuffer(mIndexBufferInfo, BufferType.SHORT_BUFFER, mIndicesShort, GLES20.GL_ELEMENT_ARRAY_BUFFER);
			}
		}

		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Reload is typically called whenever the OpenGL context needs to be restored.
	 * All buffer data is re-uploaded and a new handle is obtained.
	 * It is not recommended to call this function manually.
	 */
	public void reload() {
		if(mOriginalGeometry != null) {
			if(!mOriginalGeometry.isValid()) {
				mOriginalGeometry.reload();
			}
			copyFromGeometry3D(mOriginalGeometry);
		}
		createBuffers();
	}
	
	/**
	 * Checks whether the handle to the vertex buffer is still valid or not.
	 * The handle typically becomes invalid whenever the OpenGL context is lost.
	 * This usually happens when the application regains focus.
	 * @return
	 */
	public boolean isValid() {
		return GLES20.glIsBuffer(mVertexBufferInfo.bufferHandle);
	}
	
	/**
	 * Creates the vertex and normal buffers only. This is typically used for a 
	 * VertexAnimationObject3D's frames.
	 * 
	 * @see VertexAnimationObject3D
	 */
	public void createVertexAndNormalBuffersOnly() {
		mVertices.compact().position(0);
		mNormals.compact().position(0);
		
		createBuffer(mVertexBufferInfo, BufferType.FLOAT_BUFFER, mVertices, GLES20.GL_ARRAY_BUFFER);
		createBuffer(mNormalBufferInfo, BufferType.FLOAT_BUFFER, mNormals, GLES20.GL_ARRAY_BUFFER);

		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Creates a buffer and assumes the buffer will be used for static drawing only.
	 * 
	 * @param bufferInfo
	 * @param type
	 * @param buffer
	 * @param target
	 */
	public void createBuffer(BufferInfo bufferInfo, BufferType type, Buffer buffer, int target) {
		createBuffer(bufferInfo, type, buffer, target, GLES20.GL_STATIC_DRAW);
	}
	
	/**
	 * Creates a buffer and uploads it to the GPU.
	 * 
	 * @param bufferInfo
	 * @param type
	 * @param buffer
	 * @param target
	 * @param usage
	 */
	public void createBuffer(BufferInfo bufferInfo, BufferType type, Buffer buffer, int target, int usage) {
		int buff[] = new int[1];
		GLES20.glGenBuffers(1, buff, 0);
		int handle = buff[0];
		int byteSize = FLOAT_SIZE_BYTES;
		if(type == BufferType.SHORT_BUFFER)
			byteSize = SHORT_SIZE_BYTES;
		
		GLES20.glBindBuffer(target, handle);
		GLES20.glBufferData(target, buffer.limit() * byteSize, buffer, usage);
		GLES20.glBindBuffer(target, 0);
		
		bufferInfo.buffer = buffer;
		bufferInfo.bufferHandle = handle;
		bufferInfo.bufferType = type;
		bufferInfo.target = target;
		bufferInfo.byteSize = byteSize;
		bufferInfo.usage = usage;
	}
	
	public void createBuffer(BufferInfo bufferInfo) {
		createBuffer(bufferInfo, bufferInfo.bufferType, bufferInfo.buffer, bufferInfo.target, bufferInfo.usage);
	}
	
	public void validateBuffers() {
		if(mOriginalGeometry != null) return;
		if(mVertexBufferInfo != null && mVertexBufferInfo.bufferHandle == 0)
			createBuffer(mVertexBufferInfo);
		if(mIndexBufferInfo != null && mIndexBufferInfo.bufferHandle == 0)
			createBuffer(mIndexBufferInfo);
		if(mTexCoordBufferInfo != null && mTexCoordBufferInfo.bufferHandle == 0)
			createBuffer(mTexCoordBufferInfo);
		if(mColorBufferInfo != null && mColorBufferInfo.bufferHandle == 0)
			createBuffer(mColorBufferInfo);
		if(mNormalBufferInfo != null && mNormalBufferInfo.bufferHandle == 0)
			createBuffer(mNormalBufferInfo);
	}
	
	/**
	 * Specifies the expected usage pattern of the data store. The symbolic constant must be GLES20.GL_STREAM_DRAW, GLES20.GL_STREAM_READ, GLES20.GL_STREAM_COPY, GLES20.GL_STATIC_DRAW, GLES20.GL_STATIC_READ, GLES20.GL_STATIC_COPY, GLES20.GL_DYNAMIC_DRAW, GLES20.GL_DYNAMIC_READ, or GLES20.GL_DYNAMIC_COPY.
	 * 
	 * usage is a hint to the GL implementation as to how a buffer object's data store will be accessed. This enables the GL implementation to make more intelligent decisions that may significantly impact buffer object performance. It does not, however, constrain the actual usage of the data store. usage can be broken down into two parts: first, the frequency of access (modification and usage), and second, the nature of that access. The frequency of access may be one of these:
	 * <p>
	 * STREAM
	 * The data store contents will be modified once and used at most a few times.
	 * <p>
	 * STATIC
	 * The data store contents will be modified once and used many times.
	 * <p>
	 * DYNAMIC
	 * The data store contents will be modified repeatedly and used many times.
	 * <p>
	 * The nature of access may be one of these:
	 * <p>
	 * DRAW
	 * The data store contents are modified by the application, and used as the source for GL drawing and image specification commands.
	 * <p>
	 * READ
	 * The data store contents are modified by reading data from the GL, and used to return that data when queried by the application.
	 * <p>
	 * COPY
	 * The data store contents are modified by reading data from the GL, and used as the source for GL drawing and image specification commands.
	 * 
	 * @param bufferHandle
	 * @param usage
	 */
	public void changeBufferUsage(BufferInfo bufferInfo, final int usage) {
		GLES20.glDeleteBuffers(1, new int[] { bufferInfo.bufferHandle }, 0);
		createBuffer(bufferInfo, bufferInfo.bufferType, bufferInfo.buffer, bufferInfo.target);
	}
	
	/**
	 * Change a specific buffer's data. Either the whole buffer or a subset.
	 * 
	 * @param bufferInfo
	 * @param newData
	 * @param index
	 */
	public void changeBufferData(BufferInfo bufferInfo, Buffer newData, int index) {
		int num = newData.limit();
		int size = bufferInfo.byteSize * num;
		GLES20.glBindBuffer(bufferInfo.target, bufferInfo.bufferHandle);
		GLES20.glBufferSubData(bufferInfo.target, index * size, size, newData);
		GLES20.glBindBuffer(bufferInfo.target, 0);
	}

	public void setVertices(float[] vertices) {
		setVertices(vertices, false);
	}
	
	public void setVertices(float[] vertices, boolean override) {
		if(mVertices == null || override == true) {
			if(mVertices != null) {
				mVertices.clear();
			}
			mVertices = ByteBuffer
					.allocateDirect(vertices.length * FLOAT_SIZE_BYTES)
					.order(ByteOrder.nativeOrder()).asFloatBuffer();
			BufferUtil.copy(vertices, mVertices, vertices.length, 0);
			mVertices.position(0);
			mNumVertices = vertices.length / 3;
		} else {
			BufferUtil.copy(vertices, mVertices, vertices.length, 0);
		}
	}
	
	public void setVertices(FloatBuffer vertices) {
		vertices.position(0);
		float[] v = new float[vertices.capacity()];
		vertices.get(v);
		setVertices(v);
	}
	
	public FloatBuffer getVertices() {
		if(mOriginalGeometry != null)
			return mOriginalGeometry.getVertices();
		return mVertices;
	}
	
	public void setNormals(float[] normals) {
		if(mNormals == null) {
			mNormals = ByteBuffer.allocateDirect(normals.length * FLOAT_SIZE_BYTES)
					.order(ByteOrder.nativeOrder()).asFloatBuffer();
			BufferUtil.copy(normals, mNormals, normals.length, 0);
			mNormals.position(0);
		} else {
			mNormals.position(0);
			BufferUtil.copy(normals, mNormals, normals.length, 0);
			mNormals.position(0);
		}
	}
	
	public void setNormals(FloatBuffer normals) {
		normals.position(0);
		float[] n = new float[normals.capacity()];
		normals.get(n);
		setNormals(n);
	}

	
	public FloatBuffer getNormals() {
		if(mOriginalGeometry != null)
			return mOriginalGeometry.getNormals();
		return mNormals;
	}
	
	public void setIndices(int[] indices) {
		if(mIndicesInt == null) {
			mIndicesInt = ByteBuffer.allocateDirect(indices.length * INT_SIZE_BYTES)
					.order(ByteOrder.nativeOrder()).asIntBuffer();
			mIndicesInt.put(indices).position(0);
	
			mNumIndices = indices.length;
		} else {
			mIndicesInt.put(indices);
		}
	}
	
	public Buffer getIndices() {
		if(mIndicesInt == null && mOriginalGeometry != null)
			return mOriginalGeometry.getIndices();
		return mOnlyShortBufferSupported ? mIndicesShort : mIndicesInt;
	}
	
	public void setTextureCoords(float[] textureCoords) {
		if(mTextureCoords == null) {
			mTextureCoords = ByteBuffer
					.allocateDirect(textureCoords.length * FLOAT_SIZE_BYTES)
					.order(ByteOrder.nativeOrder()).asFloatBuffer();
			BufferUtil.copy(textureCoords, mTextureCoords, textureCoords.length, 0);
			mTextureCoords.position(0);
		} else {
			BufferUtil.copy(textureCoords, mTextureCoords, textureCoords.length, 0);
		}
	}
	
	public FloatBuffer getTextureCoords() {
		if(mTextureCoords == null && mOriginalGeometry != null)
			return mOriginalGeometry.getTextureCoords();
		return mTextureCoords;
	}
	
	public void setColors(int color) {
		setColor(Color.red(color), Color.green(color), Color.blue(color), Color.alpha(color));
	}
	
	public void setColors(float[] colors) {
		if(mColors == null) {
			mColors = ByteBuffer
					.allocateDirect(colors.length * FLOAT_SIZE_BYTES)
					.order(ByteOrder.nativeOrder()).asFloatBuffer();
			BufferUtil.copy(colors, mColors, colors.length, 0);
			mColors.position(0);
		} else {
			BufferUtil.copy(colors, mColors, colors.length, 0);
			mColors.position(0);
		}
	}
	
	public FloatBuffer getColors() {
		if(mColors == null && mOriginalGeometry != null)
			return mOriginalGeometry.getColors();
		return mColors;
	}
	
	public int getNumIndices() {
		return mNumIndices;
	}

	public int getNumVertices() {
		return mNumVertices;
	}
	
	public void setColor(float r, float g, float b, float a) {
		setColor(r, g, b, a, false);
	}
	
	public void setColor(float r, float g, float b, float a, boolean createNewBuffer) {
		if(mColors == null || mColors.limit() == 0)
		{
			mColorBufferInfo = new BufferInfo();
			mColors = ByteBuffer.allocateDirect(mNumVertices * 4 * FLOAT_SIZE_BYTES)
			.order(ByteOrder.nativeOrder()).asFloatBuffer();
			createNewBuffer = true;
		}
		
		mColors.position(0);
		
		while(mColors.remaining() > 3) {
			mColors.put(r);
			mColors.put(g);
			mColors.put(b);
			mColors.put(a);
		}
		mColors.position(0);
		
		if(createNewBuffer == true) {
			createBuffer(mColorBufferInfo, BufferType.FLOAT_BUFFER, mColors, GLES20.GL_ARRAY_BUFFER);
		} else {
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mColorBufferInfo.bufferHandle);
			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mColors.limit() * FLOAT_SIZE_BYTES, mColors, GLES20.GL_STATIC_DRAW);
		}
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
	}
	
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("Geometry3D indices: ");
		buff.append(mIndicesInt.capacity());
		buff.append(", vertices: ");
		buff.append(mVertices.capacity());
		buff.append(", normals: ");
		buff.append(mNormals.capacity());
		buff.append(", uvs: ");
		buff.append(mTextureCoords.capacity());
		return buff.toString();
	}
	
	public void destroy() {
		int[] buffers  = new int[5];
	    if(mIndexBufferInfo != null) buffers[0] = mIndexBufferInfo.bufferHandle;
	    if(mVertexBufferInfo != null) buffers[1] = mVertexBufferInfo.bufferHandle;
	    if(mNormalBufferInfo != null) buffers[2] = mNormalBufferInfo.bufferHandle;
	    if(mTexCoordBufferInfo != null) buffers[3] = mTexCoordBufferInfo.bufferHandle;
	    if(mColorBufferInfo != null) buffers[4] = mColorBufferInfo.bufferHandle;
	    GLES20.glDeleteBuffers(buffers.length, buffers, 0);

	    if(mVertices != null) mVertices.clear();
	    if(mNormals != null) mNormals.clear();
	    if(mTextureCoords != null) mTextureCoords.clear();
	    if(mColors != null) mColors.clear();
	    if(mIndicesInt != null) mIndicesInt.clear();
	    if(mIndicesShort != null) mIndicesShort.clear();
	    if(mOriginalGeometry != null) mOriginalGeometry.destroy();

	    mVertices=null;
	    mNormals=null;
	    mTextureCoords=null;
	    mColors=null;
	    mIndicesInt=null;
	    mIndicesShort=null;
	    mOriginalGeometry=null;

	    if(mVertexBufferInfo != null && mVertexBufferInfo.buffer != null) { mVertexBufferInfo.buffer.clear(); mVertexBufferInfo.buffer=null; }
	    if(mIndexBufferInfo != null && mIndexBufferInfo.buffer != null) { mIndexBufferInfo.buffer.clear(); mIndexBufferInfo.buffer=null; }
	    if(mColorBufferInfo != null && mColorBufferInfo.buffer != null)  { mColorBufferInfo.buffer.clear(); mColorBufferInfo.buffer=null; }
	    if(mNormalBufferInfo != null && mNormalBufferInfo.buffer != null) { mNormalBufferInfo.buffer.clear(); mNormalBufferInfo.buffer=null; }
	    if(mTexCoordBufferInfo != null && mTexCoordBufferInfo.buffer != null) { mTexCoordBufferInfo.buffer.clear(); mTexCoordBufferInfo.buffer=null; }

	    mVertexBufferInfo=null;
	    mTexCoordBufferInfo=null;
	    mColorBufferInfo=null;
	    mNormalBufferInfo=null;
	    mTexCoordBufferInfo=null;
	}
	
	public boolean hasBoundingBox() {
		return mBoundingBox != null;
	}
	
	/**
	 * Gets the bounding box for this geometry. If there is no current bounding
	 * box it will be calculated. 
	 * 
	 * @return
	 */
	public BoundingBox getBoundingBox() {
		if(mBoundingBox == null)
			mBoundingBox = new BoundingBox(this);
		return mBoundingBox;
	}

	public boolean hasBoundingSphere() {
		return mBoundingSphere != null;
	}
	
	/**
	 * Gets the bounding sphere for this geometry. If there is not current bounding
	 * sphere it will be calculated.
	 * @return
	 */
	public BoundingSphere getBoundingSphere() {
		if(mBoundingSphere == null)
			mBoundingSphere = new BoundingSphere(this);
		return mBoundingSphere;
	}

	public BufferInfo getVertexBufferInfo() {
		return mVertexBufferInfo;
	}

	public void setVertexBufferInfo(BufferInfo vertexBufferInfo) {
		this.mVertexBufferInfo = vertexBufferInfo;
	}

	public BufferInfo getIndexBufferInfo() {
		return mIndexBufferInfo;
	}

	public void setIndexBufferInfo(BufferInfo indexBufferInfo) {
		this.mIndexBufferInfo = indexBufferInfo;
	}

	public BufferInfo getTexCoordBufferInfo() {
		return mTexCoordBufferInfo;
	}

	public void setTexCoordBufferInfo(BufferInfo texCoordBufferInfo) {
		this.mTexCoordBufferInfo = texCoordBufferInfo;
	}

	public BufferInfo getColorBufferInfo() {
		return mColorBufferInfo;
	}

	public void setColorBufferInfo(BufferInfo colorBufferInfo) {
		this.mColorBufferInfo = colorBufferInfo;
	}

	public BufferInfo getNormalBufferInfo() {
		return mNormalBufferInfo;
	}

	public void setNormalBufferInfo(BufferInfo normalBufferInfo) {
		this.mNormalBufferInfo = normalBufferInfo;
	}
	
	/**
	 * Indices whether only short buffers are supported. Not all devices support
	 * integer buffers.
	 * @see RajawaliRenderer.supportsUIntBuffers
	 */
	public boolean areOnlyShortBuffersSupported() {
		return mOnlyShortBufferSupported;
	}
	
	public int getNumTriangles() {
		return mVertices != null ? mVertices.limit() / 9 : 0;
	}
}
