package com.blastedstudios.crittercaptors.ui.terrain;

public class TerrainChunk {
	public final float[] heightMap;
	public final short width, height, scale;
	public final float[] vertices;
	public final short[] indices;
	public final int vertexSize;

	public TerrainChunk (final int width, final int height, final float[] heightMap, 
			int vertexSize, int scale) {
		if ((width + 1) * (height + 1) > Short.MAX_VALUE)
			throw new IllegalArgumentException("Chunk size too big, (width + 1)*(height+1) must be <= 32767");
		this.heightMap = heightMap;
		this.width = (short)width;
		this.height = (short)height;
		this.scale = (short)scale;
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
				vertices[idx++] = x*scale;
				vertices[idx++] = heightMap[hIdx++]*scale;
				vertices[idx++] = z*scale;
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
