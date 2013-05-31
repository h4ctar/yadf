package userinterface;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import simulation.workshop.Workshop;

public class SpriteManager {
    private static SpriteManager instance;

    /** The size of a sprite. */
    public static final int SPRITE_SIZE = 18;

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

    private SpriteManager() {
        try {
            blockSprites = loadTileSheet("block_sprites.png", 1);
            itemSprites = loadTileSheet("item_sprites.png", 1);
            workshopSprites = loadTileSheet("workshop_sprites.png", Workshop.WORKSHOP_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Sprite getBlockSprite(final int i) {
        return blockSprites[i];
    }

    public Sprite getItemSprite(final int i) {
        return itemSprites[i];
    }

    public Sprite getWorkshopSprite(final int i) {
        return workshopSprites[i];
    }

    /**
     * Load tile sheet.
     * 
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
                Image image = gc.createCompatibleImage(size, size, Transparency.BITMASK);
                image.getGraphics().drawImage(spriteImage, 0, 0, null);
                sprites[i + j * rows] = new Sprite(image);
            }
        }

        return sprites;
    }
}
