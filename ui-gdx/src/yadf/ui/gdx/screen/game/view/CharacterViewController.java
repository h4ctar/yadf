package yadf.ui.gdx.screen.game.view;

import yadf.simulation.character.IGameCharacter;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * View Controller for game characters.
 */
public class CharacterViewController extends AbstractViewController<IGameCharacter> {

    /** The texture atlas. */
    private TextureAtlas textureAtlas;

    /**
     * Constructor.
     * @param textureAtlasTmp the texture atlas
     * @param gameStage the stage to add the game object 2Ds to
     */
    public CharacterViewController(final TextureAtlas textureAtlasTmp, final Stage gameStage) {
        super(gameStage);
        textureAtlas = textureAtlasTmp;
    }

    @Override
    protected Actor createView(final IGameCharacter gameCharacter) {
        return new EntityImageView<IGameCharacter>(gameCharacter, textureAtlas, "character-dwarf");
    }
}
