package yadf.ui.gdx.tools;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

public class YadfTexturePacker {

    private static final String INPUT_DIR = "etc/images";
    private static final String OUTPUT_DIR = "../ui-gdx-android/assets/image-atlases";
    private static final String PACK_FILE = "images";

    public static void main(String[] args) {
        // create the packing's settings
        Settings settings = new Settings();

        settings.paddingX = 0;
        settings.paddingY = 0;

        // pack the images
        TexturePacker2.process(settings, INPUT_DIR, OUTPUT_DIR, PACK_FILE);
    }
}
