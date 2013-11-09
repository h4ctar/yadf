package yadf.ui.gdx.screen.game;

import yadf.simulation.IGameObject;
import yadf.simulation.character.IGameCharacter;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameCharacter2dController extends AbstractGameObject2dController {

    private TextureAtlas textureAtlas;

    public GameCharacter2dController(TextureAtlas textureAtlasTmp, Stage gameStage) {
        super(gameStage);
        textureAtlas = textureAtlasTmp;
    }

    protected Actor createGameObject2d(IGameObject gameObject) {
        return new GameCharacter2d((IGameCharacter) gameObject, textureAtlas);
    }
}
