package yadf.ui.gdx.screen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Scaling;

/**
 * The splash screen.
 */
public class SplashScreen extends AbstractScreen {

    /** The image. */
    private Image splashImage;

    /** The texture atlas. */
    private TextureAtlas atlas;

    /**
     * Constructor.
     * @param screenController the screen controller
     */
    public SplashScreen(IScreenController screenController) {
        super(screenController);
    }

    @Override
    public void show() {
        super.show();

        atlas = new TextureAtlas(Gdx.files.internal("image-atlases/images.atlas"));

        TextureRegion splashRegion = atlas.findRegion("splash/splash");

        splashImage = new Image(splashRegion);
        splashImage.setScaling(Scaling.stretch);
        splashImage.setAlign(Align.bottom | Align.left);
        splashImage.getColor().a = 0f;

        Action action = sequence(fadeIn(0.2f), delay(0.4f), fadeOut(0.2f), new StartMainMenuAction());
        splashImage.addAction(action);
        stage.addActor(splashImage);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        splashImage.setSize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    /**
     * The action that starts the main menu when the splash screen is finished.
     */
    private final class StartMainMenuAction extends Action {

        @Override
        public boolean act(float delta) {
            System.out.println("StartMainMenuAction.act");
            screenController.replaceScreen(new MainMenuScreen(screenController));
            return true;
        }
    }
}
