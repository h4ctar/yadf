package yadf.ui.gdx.screen.game;

import yadf.simulation.map.BlockType;
import yadf.simulation.map.MapIndex;
import yadf.simulation.map.RegionMap;
import yadf.ui.gdx.screen.TileCamera;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

/**
 * The map renderer.
 */
public class MapRenderer {

    /** The map. */
    private RegionMap map;

    /** The texture atlas. */
    private TextureAtlas atlas;

    /** The sprite batch. */
    private SpriteBatch spriteBatch = new SpriteBatch();

    /**
     * Constructor.
     * @param mapTmp the map
     * @param atlasTmp the texture atlas
     */
    public MapRenderer(final RegionMap mapTmp, final TextureAtlas atlasTmp) {
        map = mapTmp;
        atlas = atlasTmp;
    }

    /**
     * Render the map.
     * @param camera the area to render
     */
    public void draw(final TileCamera camera) {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (int x = 0; x < map.getMapSize().x; x++) {
            for (int y = 0; y < map.getMapSize().y; y++) {
                BlockType block = map.getBlock(new MapIndex(x, y, (int) (camera.position.z)));
                BlockType blockBelow = map.getBlock(new MapIndex(x, y, (int) (camera.position.z - 1)));

                // Draw top of block below
                drawBlock(blockBelow, "-top", x, y);

                // Draw inside of block
                if (block.isStandIn) {
                    drawBlock(block, "-in", x, y);
                }

                // Draw top of block
                else {
                    drawBlock(block, "-top", x, y);
                    if (block.isSolid) {
                        drawSprite(atlas.findRegion("block-covered"), x, y);
                    }
                }
            }
        }
        spriteBatch.end();
    }

    /**
     * Draw a single block.
     * @param blockType the type of block to draw
     * @param x the x position to draw at
     * @param y the y position to draw at
     */
    private void drawBlock(final BlockType blockType, String sufix, final int x, final int y) {
        String name = "block-" + blockType.name().toLowerCase();
        AtlasRegion region = atlas.findRegion(name + sufix);
        if (region == null) {
            region = atlas.findRegion(name);
        }
        if (region != null) {
            drawSprite(region, x, y);
        }
    }

    private void drawSprite(AtlasRegion region, int x, int y) {
        spriteBatch.draw(region, x * GameScreen.SPRITE_SIZE, y * GameScreen.SPRITE_SIZE, GameScreen.SPRITE_SIZE, GameScreen.SPRITE_SIZE);
    }
}
