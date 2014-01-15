package yadf.ui.gdx.screen.game.window;

import yadf.controller.AbstractController;
import yadf.controller.command.AbstractCommand;
import yadf.controller.command.NewOrderCommand;
import yadf.simulation.IPlayer;
import yadf.simulation.recipe.Recipe;
import yadf.simulation.recipe.RecipeManager;
import yadf.simulation.workshop.IWorkshop;
import yadf.simulation.workshop.IWorkshopListener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Window that is displayed when a workshop is clicked on.
 * <p>
 * Allows the user to view the current orders of the workshop and add new orders.
 */
public class WorkshopWindow extends AbstractWindow implements IWorkshopListener {
    
    private IWorkshop workshop;

    private IPlayer player;
    
    private AbstractController controller;

    private List ordersList;

    private SelectBox recipesCombo;
    
    public WorkshopWindow(IWorkshop workshopTmp, IPlayer playerTmp, AbstractController controllerTmp, Skin skin) {
        super("Workshop", skin);

        workshop = workshopTmp;
        player = playerTmp;
        controller = controllerTmp;
        
        workshop.addListener(this);
        ordersList = new List(workshop.getOrders().toArray(), skin);
        ScrollPane scrollPane = new ScrollPane(ordersList);
        add(scrollPane);
        recipesCombo = new SelectBox(RecipeManager.getInstance().getRecipesForWorkshop(workshop.getType()).toArray(), skin);
        add(recipesCombo);
        TextButton newOrderButton = new TextButton("New Order", skin);
        newOrderButton.addListener(new NewOrderButtonListener());
        add(newOrderButton);
        pack();
    }

    @Override
    public void orderAdded(Recipe recipe) {
        ordersList.setItems(workshop.getOrders().toArray());
    }

    @Override
    public void orderRemoved(Recipe recipe) {
        ordersList.setItems(workshop.getOrders().toArray());
    }
    
    private class NewOrderButtonListener extends ClickListener {
        
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AbstractCommand command = new NewOrderCommand(player, workshop.getId(), recipesCombo.getSelection());
            controller.addCommand(command);
        }
    }
}
