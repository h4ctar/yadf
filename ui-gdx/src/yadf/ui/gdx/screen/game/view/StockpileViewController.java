package yadf.ui.gdx.screen.game.view;

import yadf.simulation.item.Item;
import yadf.simulation.item.Stockpile;
import yadf.ui.gdx.screen.game.window.IDialogWindowManager;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class StockpileViewController extends AbstractViewController<Stockpile> {

    private IViewController<Item> itemViewController;

    private IDialogWindowManager dialogWindowManager;

    /** The texture atlas. */
    private TextureAtlas textureAtlas;

    public StockpileViewController(final TextureAtlas textureAtlasTmp, Stage gameStage,
            IViewController<Item> itemViewControllerTmp, IDialogWindowManager dialogWindowManagerTmp) {
        super(gameStage);
        textureAtlas = textureAtlasTmp;
        itemViewController = itemViewControllerTmp;
        dialogWindowManager = dialogWindowManagerTmp;
    }

    @Override
    protected Actor createView(Stockpile stockpile) {
        stockpile.addManagerListener(itemViewController);
        return new StockpileView(stockpile, textureAtlas, dialogWindowManager);
    }

    @Override
    public void gameObjectRemoved(Stockpile stockpile) {
        super.gameObjectRemoved(stockpile);
        stockpile.removeManagerListener(itemViewController);
    }
}
