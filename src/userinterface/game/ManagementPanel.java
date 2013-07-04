package userinterface.game;

import java.awt.Dimension;

import javax.swing.JTabbedPane;

import simulation.IPlayer;
import simulation.character.IGameCharacter;
import simulation.item.Item;
import simulation.item.Stockpile;
import simulation.room.Room;
import simulation.workshop.IWorkshop;
import userinterface.game.dwarf.DwarfInterface;
import userinterface.game.item.ItemInterface;
import userinterface.game.job.JobsPane;
import userinterface.game.labor.LaborsPane;
import userinterface.game.military.MilitaryPane;
import userinterface.game.room.RoomInterface;
import userinterface.game.stock.StocksPane;
import userinterface.game.stockpile.StockpileInterface;
import userinterface.game.workshop.WorkshopInterface;
import controller.AbstractController;

public class ManagementPanel extends JTabbedPane {

    /** The jobs pane. */
    private JobsPane jobsPane;

    /** The labors pane. */
    private LaborsPane laborsPane;

    /** The stocks pane. */
    private StocksPane stocksPane;

    /** The military pane. */
    private MilitaryPane militaryPane;

    /** The stockpile interface. */
    private StockpileInterface stockpileInterface;

    /** The room interface. */
    private RoomInterface roomInterface;

    /** The workshop interface. */
    private WorkshopInterface workshopInterface;

    /** The dwarf interface. */
    private DwarfInterface dwarfInterface;

    /** The item interface. */
    private ItemInterface itemInterface;

    private IPlayer player;

    private AbstractController controller;

    public ManagementPanel() {
        setPreferredSize(new Dimension(0, 250));
        setRequestFocusEnabled(false);
        setTabPlacement(JTabbedPane.LEFT);
        setBorder(null);
        setOpaque(false);

        jobsPane = new JobsPane();
        laborsPane = new LaborsPane();
        stocksPane = new StocksPane();
        militaryPane = new MilitaryPane();

        addTab("Jobs", null, jobsPane, null);
        addTab("Labors", null, laborsPane, null);
        addTab("Stocks", null, stocksPane, null);
        addTab("Military", null, militaryPane, null);

        roomInterface = new RoomInterface();
        workshopInterface = new WorkshopInterface();
        stockpileInterface = new StockpileInterface();
        dwarfInterface = new DwarfInterface();
        itemInterface = new ItemInterface();
    }

    public void setup(final IPlayer playerTmp, final AbstractController controllerTmp) {
        player = playerTmp;
        controller = controllerTmp;
        jobsPane.setup(playerTmp.getJobManager());
        laborsPane.setup(playerTmp, controllerTmp);
        stocksPane.setup(playerTmp);
        militaryPane.setup(playerTmp, controllerTmp);
    }

    public void openDwarfInterface(IGameCharacter dwarf, WorldCanvas worldCanvas) {
        if (getTabCount() == 5) {
            removeTabAt(4);
        }
        dwarfInterface.setDwarf(dwarf, worldCanvas);
        addTab("Dwarf", null, dwarfInterface, null);
        setSelectedComponent(dwarfInterface);
    }

    public void openItemInterface(Item item, WorldCanvas worldCanvas) {
        if (getTabCount() == 5) {
            removeTabAt(4);
        }
        itemInterface.setItem(item);
        addTab("Item", null, itemInterface, null);
        setSelectedComponent(itemInterface);
    }

    public void openStockpileInterface(Stockpile stockpile, WorldCanvas worldCanvas) {
        if (getTabCount() == 5) {
            removeTabAt(4);
        }
        stockpileInterface.setStockpile(stockpile, worldCanvas, player, controller);
        addTab("Stockpile", null, stockpileInterface, null);
        setSelectedComponent(stockpileInterface);
    }

    public void openRoomInterface(Room room, WorldCanvas worldCanvas) {
        if (getTabCount() == 5) {
            removeTabAt(4);
        }
        roomInterface.setRoom(room, player, controller);
        addTab(room.getType(), null, roomInterface, null);
        setSelectedComponent(roomInterface);
    }

    public void openWorkshopInterface(IWorkshop workshop, WorldCanvas worldCanvas) {
        if (getTabCount() == 5) {
            removeTabAt(4);
        }
        workshopInterface.setWorkshop(workshop, worldCanvas, player, controller);
        addTab(workshop.getType().name, null, workshopInterface, null);
        setSelectedComponent(workshopInterface);
    }
}
