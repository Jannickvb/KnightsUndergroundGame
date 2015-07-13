package states;

import handlers.GameContactListener;
import handlers.GameInput;
import handlers.GameStateManager;
import interfaces.B2DVars;
import main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Play extends GameState implements B2DVars {

	private World world;
	private Box2DDebugRenderer b2dr;
	private OrthographicCamera b2dCam;

	private GameContactListener cl;
	private Body playerBody;

	private TiledMap tilemap;
	private float tileSize;
	private OrthogonalTiledMapRenderer tmr;

	BodyDef bdef;
	FixtureDef fdef;
	
	public Play(GameStateManager gsm) {
		super(gsm);

		bdef  = new BodyDef();
		fdef  = new FixtureDef();
		
		world = new World(new Vector2(0, -9.81f), true);
		cl = new GameContactListener();
		world.setContactListener(cl);
		b2dr = new Box2DDebugRenderer();

		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);

		createPlayer();
		
		createTiles();
	}

	@Override
	public void update(float dt) {
		handleInput();

		world.step(dt, 6, 2);
	}

	@Override
	public void render() {
		// clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// render tilemap
		tmr.setView(cam);
		tmr.render();

		// draw world
		b2dr.render(world, b2dCam.combined);

	}

	@Override
	public void dispose() {

	}

	@Override
	public void handleInput() {
		if (GameInput.isPressed(GameInput.VK_SPACE)) {
			if (cl.isPlayerOnGround()) {
				playerBody.setLinearVelocity(0, 500/PPM);
			}
		}
		if (GameInput.isPressed(GameInput.VK_UP)) {
			if (cl.isPlayerOnGround()) {
				playerBody.setLinearVelocity(0, 500/PPM);
			}
		}
		if (GameInput.isDown(GameInput.VK_LEFT)) {
				playerBody.applyForceToCenter(-100/PPM, 0, true);
		}
		if (GameInput.isDown(GameInput.VK_RIGHT)) {
			playerBody.applyForceToCenter(100/PPM, 0, true);
		}
	}

	public void createPlayer(){
		// create falling box
		bdef.position.set(300 / PPM, 200 / PPM);
		bdef.type = BodyType.DynamicBody;
		playerBody = world.createBody(bdef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(5 / PPM, 5 / PPM);
		fdef.shape = shape;
		fdef.filter.categoryBits = BIT_PLAYER;
		fdef.filter.maskBits = BIT_GROUND;
		playerBody.createFixture(fdef).setUserData("player");

		// create foot sensor
		shape.setAsBox(4 / PPM, 2 / PPM, new Vector2(0, -5f / PPM), 0);
		fdef.shape = shape;
		fdef.isSensor = true;
		playerBody.createFixture(fdef).setUserData("foot");
	}
	
	public void createTiles(){
		// load tile map
		tilemap = new TmxMapLoader().load("maps/test.tmx");
		tmr = new OrthogonalTiledMapRenderer(tilemap);
		
		//square blocks
		TiledMapTileLayer layer = (TiledMapTileLayer) tilemap.getLayers().get(
				"solid");
		tileSize = layer.getTileWidth();
		createLayer(layer,BIT_GROUND,0);
		
		//topleftslope
		layer = (TiledMapTileLayer) tilemap.getLayers().get(
				"topleftslope");
		createLayer(layer,BIT_GROUND,1);
		
		//toprightslope
		layer = (TiledMapTileLayer) tilemap.getLayers().get(
				"toprightslope");
		createLayer(layer,BIT_GROUND,2);
		
		//botleftslope
		layer = (TiledMapTileLayer) tilemap.getLayers().get(
				"botleftslope");
		createLayer(layer,BIT_GROUND,3);
		
		//botrightslope
		layer = (TiledMapTileLayer) tilemap.getLayers().get(
				"botrightslope");
		createLayer(layer,BIT_GROUND,4);
	}
	
	public void createLayer(TiledMapTileLayer layer, short bits,int type){
		
		bdef = new BodyDef();
		fdef = new FixtureDef();
		
		for (int row = 0; row < layer.getHeight(); row++) {
			for (int col = 0; col < layer.getWidth(); col++) {
				// get cell
				Cell cell = layer.getCell(col, row);

				if (cell == null)
					continue;
				if (cell.getTile() == null)
					continue;

				bdef.type = BodyType.StaticBody;
				bdef.position.set(
						(col + 0.5f) * tileSize / PPM,
						(row + 0.5f) * tileSize / PPM
				);
				
				ChainShape cs = new ChainShape();
				Vector2[] v = null;
				switch(type){
				case 0:
					v = new Vector2[5];
					v[0] = new Vector2(-tileSize /2 /PPM,-tileSize/2/PPM);
					v[1] = new Vector2(-tileSize /2 /PPM,tileSize/2/PPM);
					v[2] = new Vector2(tileSize /2 /PPM,tileSize/2/PPM);
					v[3] = new Vector2(tileSize /2 /PPM,-tileSize/2/PPM);
					v[4] = new Vector2(-tileSize /2 /PPM,-tileSize/2/PPM);
					break;
				case 1:
					v = new Vector2[4];
					v[0] = new Vector2(-tileSize /2 /PPM,-tileSize/2/PPM);
					v[1] = new Vector2(-tileSize /2 /PPM,tileSize/2/PPM);
					v[2] = new Vector2(tileSize /2 /PPM,tileSize/2/PPM);
					v[3] = new Vector2(-tileSize /2 /PPM,-tileSize/2/PPM);
					break;
				case 2:
					v = new Vector2[4];
					v[0] = new Vector2(-tileSize /2 /PPM,tileSize/2/PPM);
					v[1] = new Vector2(tileSize /2 /PPM,tileSize/2/PPM);
					v[2] = new Vector2(tileSize /2 /PPM,-tileSize/2/PPM);
					v[3] = new Vector2(-tileSize /2 /PPM,tileSize/2/PPM);
					break;
				case 3:
					v = new Vector2[4];
					v[0] = new Vector2(-tileSize /2 /PPM,-tileSize/2/PPM);
					v[1] = new Vector2(-tileSize /2 /PPM,tileSize/2/PPM);
					v[2] = new Vector2(tileSize /2 /PPM,-tileSize/2/PPM);
					v[3] = new Vector2(-tileSize /2 /PPM,-tileSize/2/PPM);
					break;
				case 4:
					v = new Vector2[4];
					v[0] = new Vector2(-tileSize /2 /PPM,-tileSize/2/PPM);
					v[1] = new Vector2(tileSize /2 /PPM,tileSize/2/PPM);
					v[2] = new Vector2(tileSize /2 /PPM,-tileSize/2/PPM);
					v[3] = new Vector2(-tileSize /2 /PPM,-tileSize/2/PPM);
					break;
				}
				if(type>=0&&type<=4)
					cs.createChain(v);
				
				fdef.friction = 1/PPM;
				fdef.shape = cs;
				fdef.filter.categoryBits = BIT_GROUND;
				fdef.filter.maskBits = BIT_PLAYER;
				fdef.isSensor = false;
				world.createBody(bdef).createFixture(fdef);
			}
		}
	}
}
