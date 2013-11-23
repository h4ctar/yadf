package yadf.ui.gdx.screen.game.object;

import yadf.simulation.IGameObject;
import yadf.simulation.item.Item;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Item2dController extends AbstractGameObject2dController {

    private TextureAtlas textureAtlas;

    public Item2dController(TextureAtlas textureAtlasTmp, Stage gameStage) {
        super(gameStage);
        textureAtlas = textureAtlasTmp;
    }

    protected Actor createGameObject2d(IGameObject gameObject) {
        System.out.println(((Item) gameObject).getType().name);
        return new Item2d((Item) gameObject, textureAtlas);
    }
}