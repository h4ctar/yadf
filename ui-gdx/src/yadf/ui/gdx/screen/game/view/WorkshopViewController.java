package yadf.ui.gdx.screen.game.view;

import yadf.controller.AbstractController;
import yadf.simulation.IPlayer;
import yadf.simulation.workshop.IWorkshop;
import yadf.ui.gdx.screen.game.window.IDialogWindowManager;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * View Controller for workshops.
 */
public class WorkshopViewController extends AbstractViewController<IWorkshop> {

    /** The texture atlas. */
    private TextureAtlas textureAtlas;

    private IPlayer player;

    private AbstractController controller;

    private IDialogWindowManager dialogWindowManager;

    /**
     * Constructor.
     * @param textureAtlasTmp the texture atlas
     * @param gameStage the stage to add the game object 2Ds to
     * @param dialogWindowManagerTmp
     */
    public WorkshopViewController(final TextureAtlas textureAtlasTmp, final Stage gameStage, final IPlayer playerTmp,
            final AbstractController controllerTmp, IDialogWindowManager dialogWindowManagerTmp) {
        super(gameStage);
        textureAtlas = textureAtlasTmp;
        player = playerTmp;
        controller = controllerTmp;
        dialogWindowManager = dialogWindowManagerTmp;
    }

    @Override
    protected Actor createView(final IWorkshop workshop) {
        return new WorkshopView(workshop, player, controller, textureAtlas, dialogWindowManager);
    }
}
