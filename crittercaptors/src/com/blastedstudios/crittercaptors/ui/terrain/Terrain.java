package com.blastedstudios.crittercaptors.ui.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.blastedstudios.crittercaptors.util.MathUtil;

public class Terrain implements ITerrain{
	public final Vector3 location;
	public final int scale;
	private TerrainChunk chunk;
	private Mesh mesh;

	/**
	 * Simple terrain sing a single terrain chunk
	 */
	public Terrain (final float[] heightMap, final Vector3 location, int scale) {
		this.location = location;
		this.scale = scale;
		int width = (int)Math.sqrt(heightMap.length)-1;
		chunk = new TerrainChunk(width, width, heightMap, 5, scale);
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

	public void render (Texture texture) {
		Gdx.gl10.glEnable(GL10.GL_DEPTH_TEST);
		Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
		texture.bind();
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glTranslatef(location.x-chunk.width*scale/2, location.y, location.z-chunk.width*scale/2);
		mesh.render(GL10.GL_TRIANGLES);
		Gdx.gl10.glPopMatrix();
	}

	public float getHeight(float x, float z) {
		int left, top;
		left = (int)x / (int)scale;
		top = (int)z / (int)scale;
		float cZ = 0f;
        float rightDiagDifference = chunk.heightMap[left + top*chunk.width] - chunk.heightMap[left + 1 +(top + 1)*chunk.width];
        float leftDiagDifference = chunk.heightMap[left + 1+ top*chunk.width] - chunk.heightMap[left+ (top + 1)*chunk.width];

        if (Math.abs(rightDiagDifference) >= Math.abs(leftDiagDifference))
        {
            if (-((x % scale) / scale) + 1 > (z % scale) / scale)
            {
                float xNormalized = (x % scale) / scale;
                float zNormalized = (z % scale) / scale;

                float topHeight = MathUtil.lerp(
                  chunk.heightMap[left+ top*chunk.width],
                  chunk.heightMap[left + 1 + top*chunk.width],
                  xNormalized);

                float bottomHeight = MathUtil.lerp(
                  chunk.heightMap[left+ (top + 1)*chunk.width],
                  chunk.heightMap[left+ (top + 1)*chunk.width] - (chunk.heightMap[left+ top*chunk.width] - chunk.heightMap[left + 1+ top*chunk.width]),
                  xNormalized);

                cZ = MathUtil.lerp(topHeight, bottomHeight, zNormalized);
            }
            else
            {
                float xNormalized = (x % scale) / scale;
                float zNormalized = (z % scale) / scale;

                float topHeight = MathUtil.lerp(
                  chunk.heightMap[left+ (top + 1)*chunk.width],
                  chunk.heightMap[left + 1+ (top + 1)*chunk.width],
                  xNormalized);

                float bottomHeight = MathUtil.lerp(
                  chunk.heightMap[left + 1 + top*chunk.width] + (chunk.heightMap[left+ (top + 1)*chunk.width] - chunk.heightMap[left + 1+ (top + 1)*chunk.width]),
                  chunk.heightMap[left + 1 + top*chunk.width],
                  xNormalized);

                cZ = MathUtil.lerp(bottomHeight, topHeight, zNormalized);
            }
        }
        else
        {
            if (((x % scale) / scale) > (z % scale) / scale)
            {

                float xNormalized = (x % scale) / scale;
                float zNormalized = (z % scale) / scale;

                float topHeight = MathUtil.lerp(
                  chunk.heightMap[left+ top*chunk.width],
                  chunk.heightMap[left + 1+ top*chunk.width],
                  xNormalized);

                float bottomHeight = MathUtil.lerp(
                  chunk.heightMap[left + 1+ (top + 1)*chunk.width] + (chunk.heightMap[left+ top*chunk.width] - chunk.heightMap[left + 1+ top*chunk.width]),
                  chunk.heightMap[left + 1+ (top + 1)*chunk.width],
                  xNormalized);

                cZ = MathUtil.lerp(topHeight, bottomHeight, zNormalized);
            }
            else
            {
                float xNormalized = (x % scale) / scale;
                float zNormalized = (z % scale) / scale;

                float topHeight = MathUtil.lerp(
                  chunk.heightMap[left+ (top + 1)*chunk.width],
                  chunk.heightMap[left + 1+ (top + 1)*chunk.width],
                  xNormalized);

                float bottomHeight = MathUtil.lerp(
                  chunk.heightMap[left+ top*chunk.width],
                  chunk.heightMap[left+ top*chunk.width] - (chunk.heightMap[left+ (top + 1)*chunk.width] - chunk.heightMap[left + 1+ (top + 1)*chunk.width]),
                  xNormalized);

                cZ = MathUtil.lerp(bottomHeight, topHeight, zNormalized);
            }
        }
		return cZ*scale;
	}

	public Vector3 getLocation() {
		return location;
	}

	public float getScale() {
		return scale;
	}
}
