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
package userinterface.workshop;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import simulation.Player;
import simulation.recipe.Recipe;
import simulation.recipe.RecipeManager;
import simulation.workshop.Workshop;
import controller.Controller;
import controller.command.CancelOrderCommand;
import controller.command.DeleteRoomCommand;
import controller.command.NewOrderCommand;

/**
 * The Class WorkshopInterface.
 */
public class WorkshopInterface extends JInternalFrame {

    /**
     * Action listener for the cancel order button.
     */
    private class CancelOrderActionListener implements ActionListener {

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed(final ActionEvent e) {
            int roomId = workshop.getId();
            int orderIndex = ordersList.getSelectedIndex();
            controller.addCommand(new CancelOrderCommand(player, roomId, orderIndex));
        }
    }

    /**
     * Action listener for the destroy workshop button.
     */
    private class DestroyWorkshopActionListener implements ActionListener {

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed(final ActionEvent e) {
            int roomId = workshop.getId();
            controller.addCommand(new DeleteRoomCommand(player, roomId));
            setVisible(false);
        }
    }

    /**
     * Action listener for the new order button.
     */
    private class NewOrderActionListener implements ActionListener {

        /**
         * {@inheritDoc}
         */
        @Override
        public void actionPerformed(final ActionEvent e) {
            int roomId = workshop.getId();
            Object[] recipes = RecipeManager.getInstance().getRecipesForWorkshop(workshop.getType()).toArray();
            if (recipes.length == 0) {
                JOptionPane.showMessageDialog(WorkshopInterface.this, "No recipes for this workshop type");
                return;
            }
            Recipe recipe = (Recipe) JOptionPane.showInputDialog(WorkshopInterface.this, "Item type", "New Order",
                    JOptionPane.QUESTION_MESSAGE, null, recipes, recipes[0]);
            if (recipe == null) {
                return;
            }
            String quantity = JOptionPane.showInputDialog(WorkshopInterface.this, "How many?", "1");
            if (quantity == null) {
                return;
            }
            for (int i = 0; i < Integer.parseInt(quantity); i++) {
                controller.addCommand(new NewOrderCommand(player, roomId, recipe.name));
            }
        }
    }

    /** The type label. */
    private JLabel typeLabel;

    /** The orders list. */
    private JList<String> ordersList;

    /** The orders list model. */
    private OrdersListModel ordersListModel;

    /** The destroy room button. */
    private JButton destroyWorkshopButton;

    /** The new order button. */
    private JButton newOrderButton;

    /** The cancel order button. */
    private JButton cancelOrderButton;

    /** The controller. */
    private final Controller controller;

    /** The player. */
    private final Player player;

    /** The workshop. */
    private Workshop workshop;

    /**
     * Create the frame.
     * 
     * @param playerTmp the player
     * @param controllerTmp the controller
     */
    public WorkshopInterface(final Player playerTmp, final Controller controllerTmp) {
        player = playerTmp;
        controller = controllerTmp;

        setupLayout();
    }

    /**
     * Sets the workshop that the interface is for.
     * 
     * @param workshopTmp the workshop
     */
    public void setWorkshop(final Workshop workshopTmp) {
        workshop = workshopTmp;
        typeLabel.setText(workshop.getType().name);
        ordersListModel.setWorkshop(workshop);
    }

    /**
     * Setup the layout.
     */
    private void setupLayout() {
        // CHECKSTYLE:OFF
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setResizable(true);
        setClosable(true);
        setTitle("Workshop Interface");
        setBounds(100, 100, 450, 300);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        getContentPane().setLayout(gridBagLayout);

        typeLabel = new JLabel("Workshop Type");
        typeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        typeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints typeLabeConstraints = new GridBagConstraints();
        typeLabeConstraints.fill = GridBagConstraints.HORIZONTAL;
        typeLabeConstraints.insets = new Insets(0, 0, 5, 0);
        typeLabeConstraints.gridx = 0;
        typeLabeConstraints.gridy = 0;
        getContentPane().add(typeLabel, typeLabeConstraints);

        ordersListModel = new OrdersListModel();
        ordersList = new JList<>(ordersListModel);
        GridBagConstraints ordersLisConstraints = new GridBagConstraints();
        ordersLisConstraints.insets = new Insets(0, 0, 5, 0);
        ordersLisConstraints.fill = GridBagConstraints.BOTH;
        ordersLisConstraints.gridx = 0;
        ordersLisConstraints.gridy = 1;
        getContentPane().add(ordersList, ordersLisConstraints);

        newOrderButton = new JButton("New Order");
        GridBagConstraints newOrderButtoConstraints = new GridBagConstraints();
        newOrderButtoConstraints.insets = new Insets(0, 0, 5, 0);
        newOrderButtoConstraints.fill = GridBagConstraints.HORIZONTAL;
        newOrderButtoConstraints.gridx = 0;
        newOrderButtoConstraints.gridy = 2;
        getContentPane().add(newOrderButton, newOrderButtoConstraints);
        newOrderButton.addActionListener(new NewOrderActionListener());

        cancelOrderButton = new JButton("Cancel Order");
        GridBagConstraints cancelOrderButtoConstraints = new GridBagConstraints();
        cancelOrderButtoConstraints.insets = new Insets(0, 0, 5, 0);
        cancelOrderButtoConstraints.fill = GridBagConstraints.HORIZONTAL;
        cancelOrderButtoConstraints.gridx = 0;
        cancelOrderButtoConstraints.gridy = 3;
        getContentPane().add(cancelOrderButton, cancelOrderButtoConstraints);
        cancelOrderButton.addActionListener(new CancelOrderActionListener());

        destroyWorkshopButton = new JButton("Destroy Workshop");
        GridBagConstraints destroyWorkshopButtoConstraints = new GridBagConstraints();
        destroyWorkshopButtoConstraints.fill = GridBagConstraints.HORIZONTAL;
        destroyWorkshopButtoConstraints.gridx = 0;
        destroyWorkshopButtoConstraints.gridy = 4;
        getContentPane().add(destroyWorkshopButton, destroyWorkshopButtoConstraints);
        destroyWorkshopButton.addActionListener(new DestroyWorkshopActionListener());
        // CHECKSTYLE:ON
    }
}
