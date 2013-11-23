package yadf.ui.gdx.screen.game;

import yadf.controller.AbstractController;
import yadf.simulation.IPlayer;
import yadf.ui.gdx.screen.TileCamera;
import yadf.ui.gdx.screen.game.interactor.IInteractorManager;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainControls extends Table {

    private Skin skin;

    private IControlsController controlsController;

    private IInteractorManager interactorManager;

    private IPlayer player;

    private TileCamera camera;

    private AbstractController controller;

    public MainControls(Skin skinTmp, IControlsController controlsControllerTmp,
            IInteractorManager interactorManagerTmp, IPlayer playerTmp, TileCamera cameraTmp,
            AbstractController controllerTmp) {
        skin = skinTmp;
        controlsController = controlsControllerTmp;
        interactorManager = interactorManagerTmp;
        player = playerTmp;
        camera = cameraTmp;
        controller = controllerTmp;

        setFillParent(true);
        align(Align.top | Align.left);
        pad(10);

        TextButton designateButton = new TextButton("Designate", skin);
        designateButton.addListener(new DesignateButtonListener());
        add(designateButton).width(140).spaceBottom(10);

        row();
        TextButton workshopButton = new TextButton("Build Workshop", skin);
        add(workshopButton).width(140).spaceBottom(10);

        row();
        TextButton roomButton = new TextButton("Create Room", skin);
        add(roomButton).width(140).spaceBottom(10);

        row();
        TextButton itemButton = new TextButton("Place Item", skin);
        add(itemButton).width(140).spaceBottom(10);

        row();
        TextButton stockpileButton = new TextButton("Create Stockpile", skin);
        add(stockpileButton).width(140).spaceBottom(10);

        row();
        TextButton farmButton = new TextButton("Build Farm", skin);
        add(farmButton).width(140);
    }

    /**
     * The listener for the designate button.
     */
    private final class DesignateButtonListener extends ClickListener {

        @Override
        public void clicked(InputEvent event, float x, float y) {
            controlsController.setCurrentControls(new DesignateControls(skin, controlsController,
                    interactorManager, player, camera, controller));
        }
    }
}
