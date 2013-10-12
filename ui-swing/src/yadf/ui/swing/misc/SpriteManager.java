package yadf.ui.swing.misc;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import yadf.simulation.workshop.Workshop;

/**
 * The sprite manager is a singleton that contains all the sprites.
 */
public final class SpriteManager {
    /** The instance of the singleton. */
    private static SpriteManager instance;

    /** The size of a sprite. */
    public static final int SPRITE_SIZE = 32;

    /** The animal sprite. */
    public static final int ANIMAL_SPRITE = 48;

    /** The dead dwarf sprite. */
    public static final int DEAD_DWARF_SPRITE = 44;

    /** The farm till sprite. */
    public static final int TILL_SPRITE = 64;

    /** The farm plant sprite. */
    public static final int PLANT_SPRITE = 65;

    /** The farm grow sprite. */
    public static final int GROW_SPRITE = 66;

    /** The farm harvest sprite. */
    public static final int HARVEST_SPRITE = 67;

    /** The goblin sprite. */
    public static final int GOBLIN_SPRITE = 80;

    /** The tree sprite. */
    public static final int TREE_SPRITE = 255;

    /**
     * Get an instance of the sprite manager singleton.
     * @return the sprite manager singleton
     */
    public static SpriteManager getInstance() {
        if (instance == null) {
            instance = new SpriteManager();
        }
        return instance;
    }

    /** The block sprites. */
    private Sprite[] blockSprites;

    /** The item sprites. */
    private Sprite[] itemSprites;

    /** The workshop sprites. */
    private Sprite[] workshopSprites;

    /** The unit sprites. */
    private Sprite[] unitSprites;

    /**
     * Private constructor, loads the sprites when the instance is created.
     */
    private SpriteManager() {
        try {
            blockSprites = loadTileSheet("block_sprites.png", 1);
            itemSprites = loadTileSheet("item_sprites.png", 1);
            workshopSprites = loadTileSheet("workshop_sprites.png", Workshop.WORKSHOP_SIZE);
            unitSprites = loadTileSheet("unit_sprites.png", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a block sprite.
     * @param i the index of the sprite to get
     * @return the sprite
     */
    public Sprite getBlockSprite(final int i) {
        return blockSprites[i];
    }

    /**
     * Gets an item sprite.
     * @param i the index of the sprite to get
     * @return the sprite
     */
    public Sprite getItemSprite(final int i) {
        return itemSprites[i];
    }

    /**
     * Gets a workshop sprite.
     * @param i the index of the sprite to get
     * @return the sprite
     */
    public Sprite getWorkshopSprite(final int i) {
        return workshopSprites[i];
    }

    /**
     * Gets a workshop sprite.
     * @param i the index of the sprite to get
     * @return the sprite
     */
    public Sprite getUnitSprite(final int i) {
        return unitSprites[i];
    }

    /**
     * Load tile sheet.
     * @param file the file
     * @param multiplier the multiplier
     * @return the sprite[]
     * @throws Exception Signals that an I/O exception has occurred.
     */
    private Sprite[] loadTileSheet(final String file, final int multiplier) throws Exception {

        URL url = getClass().getClassLoader().getResource(file);
        BufferedImage sourceImage = ImageIO.read(url);
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration();

        int width = sourceImage.getWidth();
        int rows = width / (SPRITE_SIZE * multiplier);

        Sprite[] sprites = new Sprite[rows * rows];

        int size = SPRITE_SIZE * multiplier;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                BufferedImage spriteImage = sourceImage.getSubimage(i * size, j * size, size, size);
                Image image = gc.createCompatibleImage(size, size, Transparency.TRANSLUCENT);
                image.getGraphics().drawImage(spriteImage, 0, 0, null);
                sprites[i + j * rows] = new Sprite(image);
            }
        }

        return sprites;
    }
}
