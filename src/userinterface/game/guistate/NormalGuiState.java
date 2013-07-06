package userinterface.game.guistate;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import simulation.IPlayer;
import simulation.character.IGameCharacter;
import simulation.item.Item;
import simulation.item.Stockpile;
import simulation.map.MapIndex;
import simulation.room.Room;
import simulation.workshop.IWorkshop;
import userinterface.game.IGamePanel;
import userinterface.game.WorldPanelPopupMenu;
import controller.AbstractController;

/**
 * The normal GUI state.
 * <p>
 * Allows game objects to be selected.
 */
public class NormalGuiState extends AbstractGuiState implements MouseListener {

    /** The main popup menu. */
    private WorldPanelPopupMenu mainPopupMenu;

    @Override
    public void setup(final IPlayer playerTmp, final AbstractController controller, final IGamePanel gamePanel) {
        super.setup(playerTmp, controller, gamePanel);
        gamePanel.getWorldPanel().addMouseListener(this);
        mainPopupMenu = new WorldPanelPopupMenu(gamePanel);
        gamePanel.getWorldPanel().setComponentPopupMenu(mainPopupMenu);
    }

    @Override
    public void interrupt() {
        gamePanel.getWorldPanel().removeMouseListener(this);
        gamePanel.getWorldPanel().setComponentPopupMenu(null);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        // TODO: do this smarter
        MapIndex mouseIndex = gamePanel.getWorldPanel().getMouseIndex(e.getX(), e.getY());

        // If click on dwarf, show the dwarf interface
        IGameCharacter dwarf = player.getDwarfManager().getDwarf(mouseIndex);
        if (dwarf != null) {
            gamePanel.getManagementPanel().openDwarfInterface(dwarf, gamePanel.getWorldPanel());
            return;
        }

        // If left click on a item, show the item interface
        Item item = player.getStockManager().getItem(mouseIndex);
        if (item != null) {
            gamePanel.getManagementPanel().openItemInterface(item, gamePanel.getWorldPanel());
            return;
        }

        // If left click on a stockpile, show the stockpile interface
        Stockpile stockpile = player.getStockManager().getStockpile(mouseIndex);
        if (stockpile != null) {
            gamePanel.getManagementPanel().openStockpileInterface(stockpile, gamePanel.getWorldPanel());
            return;
        }

        // If left click on a room, show the room interface
        Room room = player.getRoomManager().getRoom(mouseIndex);
        if (room != null) {
            gamePanel.getManagementPanel().openRoomInterface(room, gamePanel.getWorldPanel());
            return;
        }

        // If left click on a workshop, show the room interface
        IWorkshop workshop = player.getWorkshopManager().getWorkshop(mouseIndex);
        if (workshop != null) {
            gamePanel.getManagementPanel().openWorkshopInterface(workshop, gamePanel.getWorldPanel());
            return;
        }
    }

    @Override
    public void mousePressed(final MouseEvent e) {
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
    }

    @Override
    public void mouseExited(final MouseEvent e) {
    }
}
