package userinterface.game;

import java.awt.Dimension;

import javax.swing.JTabbedPane;

import simulation.IGameObject;
import simulation.IGameObjectListener;
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

/**
 * The south panel that has all the info and stuff in it.
 */
public class ManagementPanel extends JTabbedPane implements IGameObjectListener {

    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

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

    /** The game object that currently has an interface open. */
    private IGameObject gameObject;

    /** The player. */
    private IPlayer player;

    /** The controller. */
    private AbstractController controller;

    /**
     * Constructor.
     */
    public ManagementPanel() {
        setPreferredSize(new Dimension(0, 200));
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

    /**
     * Setup the panel.
     * @param playerTmp the player
     * @param controllerTmp the controller
     * @param worldPanel the world panel
     */
    public void setup(final IPlayer playerTmp, final AbstractController controllerTmp, final WorldPanel worldPanel) {
        player = playerTmp;
        controller = controllerTmp;
        jobsPane.setup(playerTmp.getJobManager(), worldPanel);
        laborsPane.setup(playerTmp, controllerTmp);
        stocksPane.setup(playerTmp);
        militaryPane.setup(playerTmp, controllerTmp);
    }

    /**
     * Open the dwarf interface.
     * @param dwarf the dwarf
     * @param worldCanvas the world canvas
     */
    public void openDwarfInterface(final IGameCharacter dwarf, final WorldPanel worldCanvas) {
        if (getTabCount() == 5) {
            removeTabAt(4);
            gameObject.removeGameObjectListener(this);
        }
        gameObject = dwarf;
        gameObject.addGameObjectListener(this);
        dwarfInterface.setDwarf(dwarf, worldCanvas);
        addTab("Dwarf", null, dwarfInterface, null);
        setSelectedComponent(dwarfInterface);
    }

    /**
     * Open the item interface.
     * @param item the item
     * @param worldCanvas the world canvas
     */
    public void openItemInterface(final Item item, final WorldPanel worldCanvas) {
        if (getTabCount() == 5) {
            removeTabAt(4);
            gameObject.removeGameObjectListener(this);
        }
        gameObject = item;
        gameObject.addGameObjectListener(this);
        itemInterface.setItem(item);
        addTab("Item", null, itemInterface, null);
        setSelectedComponent(itemInterface);
    }

    /**
     * Open the stockpile interface.
     * @param stockpile the stockpile
     * @param worldCanvas the world canvas
     */
    public void openStockpileInterface(final Stockpile stockpile, final WorldPanel worldCanvas) {
        if (getTabCount() == 5) {
            removeTabAt(4);
            gameObject.removeGameObjectListener(this);
        }
        gameObject = stockpile;
        gameObject.addGameObjectListener(this);
        stockpileInterface.setStockpile(stockpile, worldCanvas, player, controller);
        addTab("Stockpile", null, stockpileInterface, null);
        setSelectedComponent(stockpileInterface);
    }

    /**
     * Open the room interface.
     * @param room the room
     * @param worldCanvas the world canvas
     */
    public void openRoomInterface(final Room room, final WorldPanel worldCanvas) {
        if (getTabCount() == 5) {
            removeTabAt(4);
            gameObject.removeGameObjectListener(this);
        }
        gameObject = room;
        gameObject.addGameObjectListener(this);
        roomInterface.setRoom(room, player, controller);
        addTab(room.getType(), null, roomInterface, null);
        setSelectedComponent(roomInterface);
    }

    /**
     * Open the workshop interface.
     * @param workshop the workshop
     * @param worldCanvas the world canvas
     */
    public void openWorkshopInterface(final IWorkshop workshop, final WorldPanel worldCanvas) {
        if (getTabCount() == 5) {
            removeTabAt(4);
            gameObject.removeGameObjectListener(this);
        }
        gameObject = workshop;
        gameObject.addGameObjectListener(this);
        workshopInterface.setWorkshop(workshop, worldCanvas, player, controller);
        addTab(workshop.getType().name, null, workshopInterface, null);
        setSelectedComponent(workshopInterface);
    }

    @Override
    public void gameObjectDeleted(final Object gameObjectTmp) {
        assert getTabCount() == 5;
        removeTabAt(4);
        gameObject.removeGameObjectListener(this);
    }
}
