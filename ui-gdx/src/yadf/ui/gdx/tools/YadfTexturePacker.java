package yadf.ui.gdx.tools;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

/**
 * The texture packer.
 */
public final class YadfTexturePacker {

    /** Input directory. */
    private static final String INPUT_DIR = "etc/images";

    /** Output directory. */
    private static final String OUTPUT_DIR = "../ui-gdx-android/assets/image-atlases";

    /** Name of the pack file. */
    private static final String PACK_FILE = "images";

    /**
     * Constructor.
     */
    private YadfTexturePacker() {
    }

    /**
     * Main.
     * @param args app args
     */
    public static void main(final String[] args) {
        // create the packing's settings
        Settings settings = new Settings();

        settings.paddingX = 0;
        settings.paddingY = 0;

        // pack the images
        TexturePacker2.process(settings, INPUT_DIR, OUTPUT_DIR, PACK_FILE);
    }
}
