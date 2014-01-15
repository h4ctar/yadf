package yadf.ui.gdx.screen.game;

import yadf.simulation.map.BlockType;
import yadf.simulation.map.MapIndex;
import yadf.simulation.map.RegionMap;
import yadf.ui.gdx.screen.TileCamera;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

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
                BlockType block = map.getBlock(new MapIndex(x, y, (int) (camera.position.z - 1)));
                BlockType blockBelow = map.getBlock(new MapIndex(x, y, (int) (camera.position.z - 2)));
                BlockType blockAbove = map.getBlock(new MapIndex(x, y, (int) camera.position.z));

                if (blockBelow != BlockType.RAMP && blockBelow != BlockType.STAIR) {
                    drawBlock(blockBelow, x, y);
                }

                if (block != BlockType.RAMP && block != BlockType.STAIR) {
                    drawBlock(BlockType.EMPTY, x, y);
                }

                drawBlock(block, x, y);

                if (block == BlockType.RAMP || block == BlockType.STAIR) {
                    drawSprite("block/empty", x, y);
                }

                if (blockAbove == BlockType.RAMP || blockAbove == BlockType.STAIR) {
                    drawBlock(blockAbove, x, y);
                } else if (!blockAbove.isStandIn) {
                    drawSprite("block-covered", x, y);
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
    private void drawBlock(final BlockType blockType, final int x, final int y) {
        String name = "block-" + blockType.name().toLowerCase();
        drawSprite(name, x, y);
    }

    /**
     * Draw a sprite.
     * @param name the name of the region in the texture atlas
     * @param x the x position to draw at
     * @param y the y position to draw at
     */
    private void drawSprite(final String name, final int x, final int y) {
        spriteBatch.draw(atlas.findRegion(name), x * GameScreen.SPRITE_SIZE, y * GameScreen.SPRITE_SIZE,
                GameScreen.SPRITE_SIZE, GameScreen.SPRITE_SIZE);
    }
}
