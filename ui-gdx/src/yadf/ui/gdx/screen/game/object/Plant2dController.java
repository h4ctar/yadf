package yadf.ui.gdx.screen.game.object;

import yadf.simulation.IGameObject;
import yadf.simulation.Tree;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Plant2dController extends AbstractGameObject2dController {

    private TextureAtlas textureAtlas;

    public Plant2dController(TextureAtlas textureAtlasTmp, Stage gameStage) {
        super(gameStage);
        textureAtlas = textureAtlasTmp;
    }

    protected Actor createGameObject2d(IGameObject gameObject) {
        return new Plant2d((Tree) gameObject, textureAtlas);
    }
}
