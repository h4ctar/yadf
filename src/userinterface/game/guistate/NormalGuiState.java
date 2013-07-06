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
import userinterface.game.ManagementPanel;
import userinterface.game.WorldCanvas;
import controller.AbstractController;

/**
 * The normal GUI state.
 * <p>
 * Allows game objects to be selected.
 */
public class NormalGuiState extends AbstractGuiState implements MouseListener {

    @Override
    public void setup(final IPlayer playerTmp, final AbstractController controller, final WorldCanvas worldPanelTmp,
            final ManagementPanel managementPanelTmp) {
        super.setup(playerTmp, controller, worldPanelTmp, managementPanelTmp);
        worldPanel.addMouseListener(this);
    }

    @Override
    public void interrupt() {
        worldPanel.removeMouseListener(this);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        // TODO: do this smarter
        MapIndex mouseIndex = worldPanel.getMouseIndex(e.getX(), e.getY());

        // If click on dwarf, show the dwarf interface
        IGameCharacter dwarf = player.getDwarfManager().getDwarf(mouseIndex);
        if (dwarf != null) {
            managementPanel.openDwarfInterface(dwarf, worldPanel);
            return;
        }

        // If left click on a item, show the item interface
        Item item = player.getStockManager().getItem(mouseIndex);
        if (item != null) {
            managementPanel.openItemInterface(item, worldPanel);
            return;
        }

        // If left click on a stockpile, show the stockpile interface
        Stockpile stockpile = player.getStockManager().getStockpile(mouseIndex);
        if (stockpile != null) {
            managementPanel.openStockpileInterface(stockpile, worldPanel);
            return;
        }

        // If left click on a room, show the room interface
        Room room = player.getRoomManager().getRoom(mouseIndex);
        if (room != null) {
            managementPanel.openRoomInterface(room, worldPanel);
            return;
        }

        // If left click on a workshop, show the room interface
        IWorkshop workshop = player.getWorkshopManager().getWorkshop(mouseIndex);
        if (workshop != null) {
            managementPanel.openWorkshopInterface(workshop, worldPanel);
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
