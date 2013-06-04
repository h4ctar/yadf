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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Set;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import simulation.item.ItemType;
import simulation.item.ItemTypeManager;
import simulation.job.designation.DesignationType;
import simulation.room.RoomTypeManager;
import simulation.workshop.WorkshopType;
import simulation.workshop.WorkshopTypeManager;

/**
 * The main context menu for the world pane.
 * 
 * @author Ben Smith (bensmith87@gmail.com)
 */
public class MainPopupMenu extends JPopupMenu {

    /**
     * Action listener for the build farm menu item.
     */
    private class BuildFarmActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent actionEvent) {
            worldPane.setState(GuiState.BUILD_FARM);
        }
    }

    /**
     * Action listener for the build workshop menu item.
     */
    private class BuildWorkshopActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent actionEvent) {
            worldPane.setState(GuiState.BUILD_WORKSHOP);
            worldPane.setWorkshopType(actionEvent.getActionCommand());
        }
    }

    /**
     * Action listener for the create room menu item.
     */
    private class CreateRoomActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent actionEvent) {
            worldPane.setState(GuiState.CREATE_ROOM);
            worldPane.setRoomType(actionEvent.getActionCommand());
        }
    }

    /**
     * Action listener for the create stockpile menu item.
     */
    private class CreateStockpileActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent actionEvent) {
            worldPane.setState(GuiState.BUILD_STOCKPILE);
        }
    }

    /**
     * Action listener for the designation menu item.
     */
    private class DesignationActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent actionEvent) {
            worldPane.setState(GuiState.DESIGNATION);
            worldPane.setDesignationType(DesignationType.valueOf(actionEvent.getActionCommand()));
        }
    }

    /**
     * Action listener for the place item menu item.
     */
    private class PlaceItemActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent actionEvent) {
            worldPane.setState(GuiState.PLACE_ITEM);
            worldPane.setItemType(actionEvent.getActionCommand());
        }
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The world pane. */
    private final WorldPane worldPane;

    /**
     * Instantiates a new main popup menu.
     * @param worldPaneTmp the world pane
     */
    public MainPopupMenu(final WorldPane worldPaneTmp) {
        super("General Menu");

        worldPane = worldPaneTmp;

        JMenu designateMenu = setupDesignateMenu();
        JMenu createRoomMenu = setupCreateRoomMenu();
        JMenu placeItemMenu = setupPlaceItemMenu();
        JMenu buildWorkshopItemMenu = setupBuildWorkshopItemMenu();

        JMenuItem createStockpile = new JMenuItem("Create stockpile");
        createStockpile.addActionListener(new CreateStockpileActionListener());

        JMenuItem buildFarm = new JMenuItem("Build farm");
        buildFarm.addActionListener(new BuildFarmActionListener());

        add(designateMenu);
        add(buildWorkshopItemMenu);
        add(createRoomMenu);
        add(placeItemMenu);
        add(createStockpile);
        add(buildFarm);
    }

    /**
     * Setup build workshop item menu.
     * @return the j menu
     */
    private JMenu setupBuildWorkshopItemMenu() {
        JMenu buildWorkshopMenu = new JMenu("Build workshop");
        Collection<WorkshopType> workshopTypes = WorkshopTypeManager.getInstance().getWorkshopTypes();
        for (WorkshopType workshopType : workshopTypes) {
            JMenuItem menuItem = new JMenuItem(workshopType.name);
            buildWorkshopMenu.add(menuItem);
            menuItem.addActionListener(new BuildWorkshopActionListener());
        }

        return buildWorkshopMenu;
    }

    /**
     * Setup create room menu.
     * @return the j menu
     */
    private JMenu setupCreateRoomMenu() {
        JMenu createRoomMenu = new JMenu("Create room");
        String[] roomTypes = RoomTypeManager.getInstance().getRoomTypes();
        for (String roomType : roomTypes) {
            JMenuItem menuItem = new JMenuItem(roomType);
            createRoomMenu.add(menuItem);
            menuItem.addActionListener(new CreateRoomActionListener());
        }

        return createRoomMenu;
    }

    /**
     * Setup designate menu.
     * @return the j menu
     */
    private JMenu setupDesignateMenu() {
        JMenu designateMenu = new JMenu("Designation");

        for (int i = 0; i < DesignationType.values().length; i++) {
            JMenuItem menuItem = new JMenuItem(DesignationType.values()[i].toString());
            designateMenu.add(menuItem);
            menuItem.addActionListener(new DesignationActionListener());
        }

        return designateMenu;
    }

    /**
     * Setup place item menu.
     * @return the j menu
     */
    private JMenu setupPlaceItemMenu() {
        JMenu placeItemMenu = new JMenu("Place item");
        Set<ItemType> itemTypes = ItemTypeManager.getInstance().getPlaceableItems();
        for (ItemType itemType : itemTypes) {
            JMenuItem menuItem = new JMenuItem(itemType.name);
            placeItemMenu.add(menuItem);
            menuItem.addActionListener(new PlaceItemActionListener());
        }

        return placeItemMenu;
    }
}
