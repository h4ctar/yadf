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
package userinterface.room;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import simulation.Player;
import simulation.room.Room;
import userinterface.components.ImagePanel;
import userinterface.components.OutlineLabel;
import controller.AbstractController;
import controller.command.DeleteRoomCommand;

/**
 * The Class RoomInterface.
 */
public class RoomInterface extends JInternalFrame implements ActionListener {

    /** The serial version UID. */
    private static final long serialVersionUID = -4042642581073625964L;

    /** The type label. */
    private JLabel typeLabel;

    /** The items list. */
    private JList<String> itemsList;

    /** The items list model. */
    private ItemListModel itemsListModel;

    /** The destroy room button. */
    private JButton destroyRoomButton;

    /** The controller. */
    private final AbstractController controller;

    /** The player. */
    private final Player player;

    /** The room. */
    private Room room;

    /**
     * Create the frame.
     * 
     * @param playerTmp the player
     * @param controllerTmp the controller
     */
    public RoomInterface(final Player playerTmp, final AbstractController controllerTmp) {
        player = playerTmp;
        controller = controllerTmp;

        setupLayout();
    }

    // TODO: move this into action listener internal class
    @Override
    public void actionPerformed(final ActionEvent e) {
        Object source = e.getSource();

        int roomId = room.getId();
        if (source == destroyRoomButton) {
            controller.addCommand(new DeleteRoomCommand(player, roomId));
            setVisible(false);
        }
    }

    /**
     * Sets the room.
     * @param roomTmp the new room
     */
    public void setRoom(final Room roomTmp) {
        room = roomTmp;
        typeLabel.setText(room.getType().toString());
        itemsListModel.setRoom(room);
    }

    /**
     * Update.
     */
    public void update() {
        itemsListModel.update();
    }

    /**
     * Setup the layout.
     */
    private void setupLayout() {
        // CHECKSTYLE:OFF
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setResizable(true);
        setClosable(true);
        setTitle("Room Interface");
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout(5, 5));

        JPanel panel = new ImagePanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
        panel.setLayout(gridBagLayout);
        getContentPane().add(panel, BorderLayout.CENTER);

        typeLabel = new OutlineLabel("Room Type");
        typeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        GridBagConstraints typeLabelConstraints = new GridBagConstraints();
        typeLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        typeLabelConstraints.insets = new Insets(0, 0, 5, 0);
        typeLabelConstraints.gridx = 0;
        typeLabelConstraints.gridy = 0;
        panel.add(typeLabel, typeLabelConstraints);

        itemsListModel = new ItemListModel();
        itemsList = new JList<>(itemsListModel);
        GridBagConstraints itemsListConstraints = new GridBagConstraints();
        itemsListConstraints.insets = new Insets(0, 0, 5, 0);
        itemsListConstraints.fill = GridBagConstraints.BOTH;
        itemsListConstraints.gridx = 0;
        itemsListConstraints.gridy = 1;
        panel.add(itemsList, itemsListConstraints);

        destroyRoomButton = new JButton("Destroy Room");
        GridBagConstraints destroyRoomButtonConstraints = new GridBagConstraints();
        destroyRoomButtonConstraints.gridx = 0;
        destroyRoomButtonConstraints.gridy = 2;
        panel.add(destroyRoomButton, destroyRoomButtonConstraints);
        destroyRoomButton.addActionListener(this);
        // CHECKSTYLE:ON
    }
}
