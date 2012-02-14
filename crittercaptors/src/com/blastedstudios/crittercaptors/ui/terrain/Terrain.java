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
import com.blastedstudios.crittercaptors.util.MathUtil;

public class Terrain implements ITerrain{
	public final Vector3 location;
	public final int scaleX, scaleZ;
	private final Texture grass;
	private TerrainChunk chunk;
	private Mesh mesh;

	/**
	 * Simple terrain sing a single terrain chunk
	 */
	public Terrain (final float[] heightMap, final Vector3 location, 
			int scaleX, int scaleZ) {
		this.location = location;
		this.scaleX = scaleX;
		this.scaleZ = scaleZ;
		chunk = new TerrainChunk(DEFAULT_WIDTH, DEFAULT_WIDTH, heightMap, 5, scaleX, scaleZ);
		grass = new Texture(Gdx.files.internal("data/textures/grass1.jpg"), Format.RGB565, true);
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
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glTranslatef(location.x-DEFAULT_WIDTH_DIV2*scaleX, location.y, location.z-DEFAULT_WIDTH_DIV2*scaleZ);
		mesh.render(GL10.GL_TRIANGLES);
		Gdx.gl10.glPopMatrix();
	}

	public float getHeight(float x, float z) {
		int left, top;
		left = (int)x / (int)scaleX;
		top = (int)z / (int)scaleZ;
		float cZ = 0f;
        float rightDiagDifference = chunk.heightMap[left + top*chunk.width] - chunk.heightMap[left + 1 +(top + 1)*chunk.width];
        float leftDiagDifference = chunk.heightMap[left + 1+ top*chunk.width] - chunk.heightMap[left+ (top + 1)*chunk.width];

        if (Math.abs(rightDiagDifference) >= Math.abs(leftDiagDifference))
        {
            if (-((x % scaleX) / scaleX) + 1 > (z % scaleZ) / scaleZ)
            {
                float xNormalized = (x % scaleX) / scaleX;
                float zNormalized = (z % scaleZ) / scaleZ;

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
                float xNormalized = (x % scaleX) / scaleX;
                float zNormalized = (z % scaleZ) / scaleZ;

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
            if (((x % scaleX) / scaleX) > (z % scaleZ) / scaleZ)
            {

                float xNormalized = (x % scaleX) / scaleX;
                float zNormalized = (z % scaleZ) / scaleZ;

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
                float xNormalized = (x % scaleX) / scaleX;
                float zNormalized = (z % scaleZ) / scaleZ;

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
		return cZ;
	}

	public Vector3 getLocation() {
		return location;
	}

	public float getScaleX() {
		return scaleX;
	}

	public float getScaleZ() {
		return scaleZ;
	}
}
