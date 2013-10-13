package yadf.ui.gdx.screen.game;

import yadf.simulation.map.BlockType;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.RegionMap;

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
    private SpriteBatch spriteBatch;

    /**
     * Constructor.
     * @param mapTmp the map
     * @param atlasTmp the texture atlas
     * @param spriteBatchTmp the sprite batch
     */
    public MapRenderer(RegionMap mapTmp, TextureAtlas atlasTmp, SpriteBatch spriteBatchTmp) {
        map = mapTmp;
        atlas = atlasTmp;
        spriteBatch = spriteBatchTmp;
    }

    /**
     * Render the map.
     * @param viewArea the area to render
     */
    public void render(MapArea viewArea) {
        for (int x = 0; x < viewArea.width; x++) {
            for (int y = 0; y < viewArea.height; y++) {
                BlockType block = map.getBlock(viewArea.pos.add(x, y, -1));
                BlockType blockBelow = map.getBlock(viewArea.pos.add(x, y, -2));
                BlockType blockAbove = map.getBlock(viewArea.pos.add(x, y, 0));

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
                    drawSprite("block/covered", x, y);
                }
            }
        }
    }

    /**
     * Draw a single block.
     * @param blockType the type of block to draw
     * @param x the x position to draw at
     * @param y the y position to draw at
     */
    private void drawBlock(BlockType blockType, int x, int y) {
        String name = "block/" + blockType.name().toLowerCase();
        drawSprite(name, x, y);
    }

    /**
     * Draw a sprite.
     * @param name the name of the region in the texture atlas
     * @param x the x position to draw at
     * @param y the y position to draw at
     */
    private void drawSprite(String name, int x, int y) {
        spriteBatch.draw(atlas.findRegion(name), x * GameScreen.SPRITE_SIZE, y * GameScreen.SPRITE_SIZE);
    }
}
