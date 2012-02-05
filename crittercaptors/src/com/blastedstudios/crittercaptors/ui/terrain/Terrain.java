package com.blastedstudios.crittercaptors.ui.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

public class Terrain {
	public static final int DEFAULT_WIDTH = 16,
			DEFAULT_WIDTH_DIV2 = DEFAULT_WIDTH/2,
			DEFAULT_WIDTH_TIM2 = DEFAULT_WIDTH*2;
	public final Vector3 location;
	private TerrainChunk chunk;
	private Mesh mesh;
	private Texture grass;

	public Terrain (final float[] heightMap, final Vector3 location) {
		this.location = location;
		chunk = new TerrainChunk(DEFAULT_WIDTH, DEFAULT_WIDTH, heightMap, 5);
		grass = new Texture(Gdx.files.internal("data/textures/grass1.jpg"), Format.RGB565, true);

		//colorpacked - vertex size + 1
		//for (int i = 3; i < chunk.vertices.length; i += 4)
		//	chunk.vertices[i] = Color.toFloatBits(0, 255, 0, 255);
		
		//texcoords - vertex size + 2
		for (int i = 3, iteration = 0; i < chunk.vertices.length; i += 5, iteration++){
			chunk.vertices[i] = iteration%4>1 ? 0 : 1;
			chunk.vertices[i+1] = iteration%4==1 || iteration%4==2 ? 0 : 1;
		}

		mesh = new Mesh(true, chunk.vertices.length / 3, chunk.indices.length, 
				new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE), 
				new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
		mesh.setVertices(chunk.vertices);
		mesh.setIndices(chunk.indices);
	}

	public void render () {
		Gdx.gl10.glEnable(GL10.GL_DEPTH_TEST);
		Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
		grass.bind();
		Gdx.gl10.glTranslatef(location.x-DEFAULT_WIDTH/2, location.y, location.z-DEFAULT_WIDTH/2);
		mesh.render(GL10.GL_TRIANGLES);
	}

	public float getHeight(int x, int z) {
		return chunk.heightMap[x*chunk.width + z];
	}

	final static class TerrainChunk {
		public final float[] heightMap;
		public final short width;
		public final short height;
		public final float[] vertices;
		public final short[] indices;
		public final int vertexSize;

		public TerrainChunk (final int width, final int height, final float[] heightMap, int vertexSize) {
			if ((width + 1) * (height + 1) > Short.MAX_VALUE)
				throw new IllegalArgumentException("Chunk size too big, (width + 1)*(height+1) must be <= 32767");

			this.heightMap = heightMap;
			this.width = (short)width;
			this.height = (short)height;
			this.vertices = new float[heightMap.length * vertexSize];
			this.indices = new short[width * height * 6];
			this.vertexSize = vertexSize;

			buildIndices();
			buildVertices();
		}

		public void buildVertices () {
			int heightPitch = height + 1;
			int widthPitch = width + 1;

			int idx = 0;
			int hIdx = 0;
			int inc = vertexSize - 3;

			for (int z = 0; z < heightPitch; z++) {
				for (int x = 0; x < widthPitch; x++) {
					vertices[idx++] = x;
					vertices[idx++] = heightMap[hIdx++];
					vertices[idx++] = z;
					idx += inc;
				}
			}
		}

		private void buildIndices () {
			int idx = 0;
			short pitch = (short)(width + 1);
			short i1 = 0;
			short i2 = 1;
			short i3 = (short)(1 + pitch);
			short i4 = pitch;

			short row = 0;

			for (int z = 0; z < height; z++) {
				for (int x = 0; x < width; x++) {
					indices[idx++] = i1;
					indices[idx++] = i2;
					indices[idx++] = i3;

					indices[idx++] = i3;
					indices[idx++] = i4;
					indices[idx++] = i1;

					i1++;
					i2++;
					i3++;
					i4++;
				}

				row += pitch;
				i1 = row;
				i2 = (short)(row + 1);
				i3 = (short)(i2 + pitch);
				i4 = (short)(row + pitch);
			}
		}
	}
}
