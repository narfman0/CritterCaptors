package com.blastedstudios.crittercaptors.ui.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.blastedstudios.crittercaptors.CritterCaptors;

/**
 * More complex terrain organized in quadtree structure with lod. 
 * @param children indices are:
 *  /-------\
 *  | 0 | 1 |
 *  | 2 | 3 |
 *  \-------/
 */
public class QuadTerrainNode implements ITerrain{
	private static final int DEFAULT_MAX_LOD = (int)(Math.log10(DEFAULT_WIDTH)/Math.log10(4))-1;
	public final Vector3 location;
	public final float scale;
	public final int lod, width;
	private TerrainChunk chunk;
	private Mesh mesh;
	private QuadTerrainNode[] children;
	
	public QuadTerrainNode (final float[] heightMap, final Vector3 location, int scale){
		this(heightMap, location, scale, DEFAULT_MAX_LOD);
	}

	public QuadTerrainNode (final float[] heightMap, final Vector3 location, 
			int scale, int lod) {
		this.location = location;
		this.lod = lod;
		this.scale = scale;
		
		final int width = (int)Math.sqrt(heightMap.length)-1,//#quads 
				stride = (int)Math.pow(2, lod),
				widthOver2 = width/2, widthOver4 = width/4;
		if(lod>0){
			children = new QuadTerrainNode[4];
			Vector3 location0 = new Vector3(location.x-scale*widthOver4,0,location.x+scale*widthOver4),
					location1 = new Vector3(location.x+scale*widthOver4,0,location.x+scale*widthOver4),
					location2 = new Vector3(location.x-scale*widthOver4,0,location.x-scale*widthOver4),
					location3 = new Vector3(location.x+scale*widthOver4,0,location.x-scale*widthOver4);
			float[] heightMap0 = new float[(widthOver2+1)*(widthOver2+1)],
					heightMap1 = new float[(widthOver2+1)*(widthOver2+1)],
					heightMap2 = new float[(widthOver2+1)*(widthOver2+1)],
					heightMap3 = new float[(widthOver2+1)*(widthOver2+1)];
			for(int z=0; z<widthOver2+1; z++)
				for(int x=0; x<widthOver2+1; x++){
					heightMap0[z*widthOver2+x] = heightMap[z*widthOver2+x];
					heightMap1[z*widthOver2+x] = heightMap[z*widthOver2+x+widthOver2+1];
					heightMap2[z*widthOver2+x] = heightMap[(z+widthOver2+1)*widthOver2+x];
					heightMap3[z*widthOver2+x] = heightMap[(z+widthOver2+1)*widthOver2+x+widthOver2+1];
				}
			children[0] = new QuadTerrainNode(heightMap0, location0, scale, lod-1);
			children[1] = new QuadTerrainNode(heightMap1, location1, scale, lod-1);
			children[2] = new QuadTerrainNode(heightMap2, location2, scale, lod-1);
			children[3] = new QuadTerrainNode(heightMap3, location3, scale, lod-1);
		}
		//build own mesh
		this.width = widthOver4+1;
		float[] lodHeightMap = new float[(widthOver4+1)*(widthOver4+1)];
		for(int z=0,i=0; z<(widthOver4+1); z+=stride)
			for(int x=0; x<(widthOver4+1); x+=stride)
				lodHeightMap[i++] = heightMap[z*(widthOver4+1)+x];
		chunk = new TerrainChunk(widthOver4, widthOver4, lodHeightMap, 5, scale);
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
		CritterCaptors.getTexture("grass").bind();
		Gdx.gl10.glPushMatrix();
		//Gdx.gl10.glTranslatef(location.x-width*scaleX/2, location.y, location.z-width*scaleZ/2);
		Gdx.gl10.glTranslatef(location.x, location.y, location.z);
		mesh.render(GL10.GL_TRIANGLES);
		Gdx.gl10.glPopMatrix();
		if(children != null)
			for(QuadTerrainNode child : children)
				child.render();
	}

	public float getHeight(float x, float z) {
		return 3;
	}

	public Vector3 getLocation() {
		return location;
	}

	public float getScale() {
		return scale;
	}
}
