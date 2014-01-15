package yadf.ui.gdx.screen.game.view;

import yadf.controller.AbstractController;
import yadf.simulation.IPlayer;
import yadf.simulation.workshop.IWorkshop;
import yadf.ui.gdx.screen.game.window.IDialogWindowManager;
import yadf.ui.gdx.screen.game.window.WorkshopWindow;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class WorkshopView extends EntityImageView<IWorkshop> {

    private IPlayer player;

    private AbstractController controller;

    private IDialogWindowManager dialogWindowManager;

    public WorkshopView(IWorkshop workshop, IPlayer playerTmp, AbstractController controllerTmp, TextureAtlas atlas,
            IDialogWindowManager dialogWindowManagerTmp) {
        super(workshop, atlas, "workshop-" + workshop.getType().name.toLowerCase());
        dialogWindowManager = dialogWindowManagerTmp;
        player = playerTmp;
        controller = controllerTmp;
        addListener(new WorkshopClickListener());
    }

    private class WorkshopClickListener extends ClickListener {

        @Override
        public void clicked(InputEvent event, float x, float y) {
            dialogWindowManager.setWindow(new WorkshopWindow(getEntity(), player, controller, dialogWindowManager
                    .getSkin()));
        }
    }
}
