package yadf.ui.gdx.screen.game.object;

import yadf.simulation.character.IGameCharacter;
import yadf.simulation.map.MapIndex;
import yadf.ui.gdx.screen.game.GameScreen;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * A game charactor actor.
 */
public class GameCharacter2d extends Image {

    /** The game character. */
    private IGameCharacter character;

    /**
     * Constructor.
     * @param characterTmp the game character
     * @param atlas the texture atlas
     */
    public GameCharacter2d(final IGameCharacter characterTmp, final TextureAtlas atlas) {
        super(atlas.findRegion("characters/dwarf"));
        character = characterTmp;
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        MapIndex position = character.getPosition();
        setX(position.x * GameScreen.SPRITE_SIZE);
        setY(position.y * GameScreen.SPRITE_SIZE);
        setVisible((int) getStage().getCamera().position.z == position.z);
    }
}
