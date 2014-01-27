package yadf.ui.gdx.screen.game.view;

import yadf.simulation.item.Stockpile;
import yadf.ui.gdx.screen.game.window.IDialogWindowManager;
import yadf.ui.gdx.screen.game.window.StockpileWindow;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class StockpileView extends EntityImageView<Stockpile> {

    private IDialogWindowManager dialogWindowManager;

    public StockpileView(Stockpile stockpile, TextureAtlas textureAtlas, IDialogWindowManager dialogWindowManagerTmp) {
        super(stockpile, textureAtlas, "stockpile");
        dialogWindowManager = dialogWindowManagerTmp;
        addListener(new StockpileClickListener());
    }

    private class StockpileClickListener extends ClickListener {

        @Override
        public void clicked(InputEvent event, float x, float y) {
            dialogWindowManager.setWindow(new StockpileWindow(dialogWindowManager.getSkin()));
        }
    }
}
