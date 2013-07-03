/**
 * yadf
 * 
 * https://sourceforge.net/projects/yadf
 * 
 * Ben Smith (bensmith87@gmail.com)
 * 
 * yadf is placed under the BSD license.
 * 
 * Copyright (c) 2012-2013, Ben Smith All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * 
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * - Neither the name of the yadf project nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package userinterface.game;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.KeyStroke;

import simulation.Player;
import simulation.Region;
import simulation.character.IGameCharacter;
import simulation.item.Item;
import simulation.item.Stockpile;
import simulation.job.designation.DesignationType;
import simulation.map.MapArea;
import simulation.map.MapIndex;
import simulation.room.Room;
import simulation.workshop.IWorkshop;
import simulation.workshop.Workshop;
import userinterface.game.dwarf.DwarfInterface;
import userinterface.game.item.ItemInterface;
import userinterface.game.room.RoomInterface;
import userinterface.game.stockpile.StockpileInterface;
import userinterface.game.workshop.WorkshopInterface;
import controller.AbstractController;
import controller.command.BuildWorkshopCommand;
import controller.command.CreateFarmCommand;
import controller.command.CreateRoomCommand;
import controller.command.CreateStockpileCommand;
import controller.command.DesignationCommand;
import controller.command.MilitaryMoveCommand;
import controller.command.PlaceItemCommand;

/**
 * The Class WorldPane.
 */
class WorldPane extends JDesktopPane implements ComponentListener, MouseListener, MouseMotionListener {

    /** The serial version UID. */
    private static final long serialVersionUID = 5495536352766170664L;

    /** The world canvas. */
    private final WorldCanvas worldCanvas;

    /** The player. */
    private Player player;

    /** The region. */
    private Region region;

    /** The controller. */
    private AbstractController controller;

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

    /** The main popup menu. */
    private MainPopupMenu mainPopupMenu;

    /** The mouse index. */
    private MapIndex mouseIndex = new MapIndex();

    /** The selection. */
    private final MapArea selection = new MapArea();

    /** The abs selection. */
    private MapArea absSelection = new MapArea();

    /** The selection valid. */
    private boolean selectionValid = true;

    /** The draw selection. */
    private boolean drawSelection = false;

    /** The gui state. */
    private GuiState guiState = GuiState.NORMAL;

    /** The room type. */
    private String roomType = "Bedroom";

    /** The item type. */
    private String placeItemType = "Bed";

    /** The workshop type. */
    private String workshopType = "Kitchen";

    /** The designation type. */
    private DesignationType designationType = DesignationType.CHANNEL;

    /**
     * Instantiates a new world pane.
     */
    public WorldPane() {
        worldCanvas = new WorldCanvas();

        addComponentListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, 0, true),
                "SHIFT_UP");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "UP");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "DOWN");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "LEFT");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "RIGHT");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, false), "UP_Z");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, false), "DOWN_Z");

        getActionMap().put("SHIFT_UP", new AbstractAction() {

            /** The serial version UID. */
            private static final long serialVersionUID = -5686631134770314337L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                setState(GuiState.NORMAL);
            }
        });
        getActionMap().put("UP", new AbstractAction() {

            /** The serial version UID. */
            private static final long serialVersionUID = 276268899627961583L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                worldCanvas.moveView(0, -1, 0);
            }
        });
        getActionMap().put("DOWN", new AbstractAction() {

            /** The serial version UID. */
            private static final long serialVersionUID = -8957876323188992119L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                worldCanvas.moveView(0, 1, 0);
            }
        });
        getActionMap().put("LEFT", new AbstractAction() {

            /** The serial version UID. */
            private static final long serialVersionUID = 3496221372222013523L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                worldCanvas.moveView(-1, 0, 0);
            }
        });
        getActionMap().put("RIGHT", new AbstractAction() {

            /** The serial version UID. */
            private static final long serialVersionUID = 4473344779001256351L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                worldCanvas.moveView(1, 0, 0);
            }
        });
        getActionMap().put("UP_Z", new AbstractAction() {

            /** The serial version UID. */
            private static final long serialVersionUID = -7971410086577923706L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                worldCanvas.moveView(0, 0, 1);
            }
        });
        getActionMap().put("DOWN_Z", new AbstractAction() {

            /** The serial version UID. */
            private static final long serialVersionUID = 3537426839175986086L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                worldCanvas.moveView(0, 0, -1);
            }
        });

        add(worldCanvas, BorderLayout.CENTER);
    }

    /**
     * Setup.
     * @param regionTmp the region
     * @param playerTmp the player
     * @param controllerTmp the controller
     */
    public void setup(final Region regionTmp, final Player playerTmp, final AbstractController controllerTmp) {
        region = regionTmp;
        player = playerTmp;
        controller = controllerTmp;
        worldCanvas.setup(player, region);
        mainPopupMenu = new MainPopupMenu(this);
        roomInterface = new RoomInterface(player, controller);
        workshopInterface = new WorkshopInterface(worldCanvas, player, controller);
        stockpileInterface = new StockpileInterface(worldCanvas, player, controller);
        dwarfInterface = new DwarfInterface();
        itemInterface = new ItemInterface();
        add(mainPopupMenu);
        add(roomInterface);
        add(workshopInterface);
        add(stockpileInterface);
        add(dwarfInterface);
        add(itemInterface);
    }

    /**
     * Get the world canvas.
     * @return the world canvas
     */
    public WorldCanvas getWorldCanvas() {
        return worldCanvas;
    }

    @Override
    public void componentHidden(final ComponentEvent e) { // do nothing
    }

    @Override
    public void componentMoved(final ComponentEvent e) { // do nothing
    }

    @Override
    public void componentResized(final ComponentEvent e) {
        worldCanvas.setSize(getSize());
    }

    @Override
    public void componentShown(final ComponentEvent e) { // do nothing
    }

    /**
     * Get a string representing the state of the GUI.
     * @return a state string
     */
    public String getStateString() {
        switch (guiState) {
        case BUILD_FARM:
            return "Build farm";
        case BUILD_STOCKPILE:
            return "Build stockpile";
        case BUILD_WORKSHOP:
            return "Build " + workshopType;
        case CREATE_ROOM:
            return "Create " + roomType;
        case DESIGNATION:
            return "Designate " + designationType.toString();
        case NORMAL:
            return "Normal";
        case PLACE_ITEM:
            return "Place " + placeItemType;
        case MILITARY_STATION:
            return "Military move";
        default:
            return "Normal";
        }
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        switch (guiState) {
        case NORMAL:
            switch (e.getButton()) {
            // Left mouse button
            case MouseEvent.BUTTON1:
                // If click on dwarf, show the dwarf interface
                IGameCharacter dwarf = player.getDwarfManager().getDwarf(mouseIndex);
                if (dwarf != null) {
                    dwarfInterface.setDwarf(dwarf, worldCanvas);
                    dwarfInterface.setVisible(true);
                    return;
                }

                // If left click on a item, show the item interface
                Item item = player.getStockManager().getItem(mouseIndex);
                if (item != null) {
                    itemInterface.setItem(item);
                    itemInterface.setVisible(true);
                    return;
                }

                // If left click on a stockpile, show the stockpile interface
                Stockpile stockpile = player.getStockManager().getStockpile(mouseIndex);
                if (stockpile != null) {
                    stockpileInterface.setStockpile(stockpile);
                    stockpileInterface.setVisible(true);
                    return;
                }

                // If left click on a room, show the room interface
                Room room = player.getRoomManager().getRoom(mouseIndex);
                if (room != null) {
                    roomInterface.setRoom(room);
                    roomInterface.setVisible(true);
                    return;
                }

                // If left click on a workshop, show the room interface
                IWorkshop workshop = player.getWorkshopManager().getWorkshop(mouseIndex);
                if (workshop != null) {
                    workshopInterface.setWorkshop(workshop);
                    workshopInterface.setVisible(true);
                    return;
                }

                break;

            // Middle mouse button
            case MouseEvent.BUTTON2:
                // do nothing
                break;

            // Right mouse button
            case MouseEvent.BUTTON3:
                mainPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                break;

            default:
                break;
            }
            break;

        case BUILD_WORKSHOP:
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (selectionValid) {
                    controller
                            .addCommand(new BuildWorkshopCommand(player, new MapIndex(selection.pos), workshopType));
                }

                if (!e.isShiftDown()) {
                    setState(GuiState.NORMAL);
                    drawSelection = false;
                }
            }
            break;

        case PLACE_ITEM:
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (selectionValid) {
                    controller.addCommand(new PlaceItemCommand(player, placeItemType, new MapIndex(selection.pos)));
                }

                if (!e.isShiftDown()) {
                    setState(GuiState.NORMAL);
                    drawSelection = false;
                }
            }
            break;

        case MILITARY_STATION:
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (selectionValid) {
                    controller.addCommand(new MilitaryMoveCommand(player, new MapIndex(selection.pos)));
                }
                setState(GuiState.NORMAL);
                drawSelection = false;
            }
            break;

        default:
            break;
        }
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        mouseIndex = worldCanvas.getMouseIndex(e.getX(), e.getY());
        selection.width = mouseIndex.x - selection.pos.x + 1;
        selection.height = mouseIndex.y - selection.pos.y + 1;
        absSelection = new MapArea(selection);
        if (selection.width < 1) {
            absSelection.pos.x = selection.pos.x + selection.width - 1;
            absSelection.width = -selection.width + 2;
        }
        if (selection.height < 1) {
            absSelection.pos.y = selection.pos.y + selection.height - 1;
            absSelection.height = -selection.height + 2;
        }

        if (guiState == GuiState.BUILD_STOCKPILE || guiState == GuiState.BUILD_FARM
                || guiState == GuiState.CREATE_ROOM) {
            selectionValid = region.checkAreaValid(absSelection);
        }
    }

    @Override
    public void mouseEntered(final MouseEvent e) { /* nothing to do */
    }

    @Override
    public void mouseExited(final MouseEvent e) { /* nothing to do */
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        MapIndex oldMouseIndex = new MapIndex(mouseIndex);
        mouseIndex = worldCanvas.getMouseIndex(e.getX(), e.getY());

        if (!oldMouseIndex.equals(mouseIndex)) {
            if (guiState == GuiState.PLACE_ITEM || guiState == GuiState.MILITARY_STATION) {
                absSelection.pos = mouseIndex;
                absSelection.width = 1;
                absSelection.height = 1;
                drawSelection = true;
                selectionValid = region.checkAreaValid(absSelection);
            }
            if (guiState == GuiState.BUILD_WORKSHOP) {
                absSelection.pos = mouseIndex;
                absSelection.width = Workshop.WORKSHOP_SIZE;
                absSelection.height = Workshop.WORKSHOP_SIZE;
                drawSelection = true;
                selectionValid = region.checkAreaValid(absSelection);
            }
        }
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        selection.pos = mouseIndex;
        if (guiState == GuiState.BUILD_WORKSHOP) {
            selection.width = Workshop.WORKSHOP_SIZE;
            selection.height = Workshop.WORKSHOP_SIZE;
        } else {
            selection.width = 1;
            selection.height = 1;
        }
        absSelection = new MapArea(selection);

        drawSelection = true;
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        switch (guiState) {
        case NORMAL:
            break;

        case BUILD_STOCKPILE:
            if (e.getButton() == MouseEvent.BUTTON1 && region.checkAreaValid(absSelection)) {
                controller.addCommand(new CreateStockpileCommand(new MapArea(absSelection), player));
            }
            if (!e.isShiftDown()) {
                setState(GuiState.NORMAL);
            }
            break;

        case BUILD_FARM:
            if (e.getButton() == MouseEvent.BUTTON1 && region.checkAreaValid(absSelection)) {
                controller.addCommand(new CreateFarmCommand(new MapArea(absSelection), player));
            }
            if (!e.isShiftDown()) {
                setState(GuiState.NORMAL);
            }
            break;

        case DESIGNATION:
            controller.addCommand(new DesignationCommand(absSelection, designationType,
                    e.getButton() == MouseEvent.BUTTON1, player));
            if (!e.isShiftDown()) {
                setState(GuiState.NORMAL);
            }
            break;

        case CREATE_ROOM:
            if (e.getButton() == MouseEvent.BUTTON1 && region.checkAreaValid(absSelection)) {
                controller.addCommand(new CreateRoomCommand(new MapArea(absSelection), player, roomType));
            }
            if (!e.isShiftDown()) {
                setState(GuiState.NORMAL);
            }
            break;

        default:
            break;
        }

        drawSelection = false;
    }

    /**
     * Sets the designation type.
     * @param designationTypeTmp the new designation type
     */
    public void setDesignationType(final DesignationType designationTypeTmp) {
        designationType = designationTypeTmp;
    }

    /**
     * Sets the item type.
     * @param string the new item type
     */
    public void setItemType(final String string) {
        this.placeItemType = string;
    }

    /**
     * Sets the room type.
     * @param roomTypeTmp the new room type
     */
    public void setRoomType(final String roomTypeTmp) {
        roomType = roomTypeTmp;
    }

    /**
     * Sets the state.
     * @param guiStateTmp the new state
     */
    public void setState(final GuiState guiStateTmp) {
        guiState = guiStateTmp;
        if (guiState == GuiState.NORMAL) {
            drawSelection = false;
        }
    }

    /**
     * Sets the workshop type.
     * @param workshopTypeTmp the new workshop type
     */
    public void setWorkshopType(final String workshopTypeTmp) {
        workshopType = workshopTypeTmp;
    }

    /**
     * Update.
     */
    public void update() {
        if (drawSelection) {
            worldCanvas.setSelection(absSelection, selectionValid);
        } else {
            worldCanvas.setSelection(null, false);
        }
        worldCanvas.repaint();
    }
}
