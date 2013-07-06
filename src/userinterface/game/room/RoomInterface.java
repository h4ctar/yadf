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
package userinterface.game.room;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import simulation.IGameObject;
import simulation.room.Room;
import userinterface.game.AbstractGameObjectInterface;
import controller.command.DeleteRoomCommand;

/**
 * The Class RoomInterface.
 */
public class RoomInterface extends AbstractGameObjectInterface {

    /** The serial version UID. */
    private static final long serialVersionUID = -4042642581073625964L;

    /** The items list. */
    private JList<String> itemsList;

    /** The items list model. */
    private ItemListModel itemsListModel;

    /** The destroy room button. */
    private JButton destroyRoomButton;

    /** The room. */
    private Room room;

    /** The button panel. */
    private JPanel buttonPanel;

    /** The info panel. */
    private JPanel infoPanel;

    /** The scroll pane. */
    private JScrollPane scrollPane;

    /**
     * Create the frame.
     */
    public RoomInterface() {
        setupLayout();
    }

    @Override
    protected void setup(final IGameObject gameObject) {
        room = (Room) gameObject;
        itemsListModel.setRoom(room);
    }

    @Override
    public String getTitle() {
        return room.getType();
    }

    /**
     * Action listener for the destroy room button.
     */
    private class DestroyRoomActionListener implements ActionListener {

        @Override
        public void actionPerformed(final ActionEvent e) {
            int roomId = room.getId();
            controller.addCommand(new DeleteRoomCommand(player, roomId));
            setVisible(false);
        }
    }

    /**
     * Setup the layout.
     */
    private void setupLayout() {
        setOpaque(false);
        setLayout(new BorderLayout(0, 0));

        itemsListModel = new ItemListModel();

        buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        add(buttonPanel, BorderLayout.WEST);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        destroyRoomButton = new JButton("Destroy Room");
        destroyRoomButton.setPreferredSize(new Dimension(150, 23));
        destroyRoomButton.setMinimumSize(new Dimension(150, 23));
        destroyRoomButton.setMaximumSize(new Dimension(150, 23));
        buttonPanel.add(destroyRoomButton);
        destroyRoomButton.addActionListener(new DestroyRoomActionListener());

        infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        add(infoPanel, BorderLayout.CENTER);
        infoPanel.setLayout(new BorderLayout(0, 0));
        GridBagConstraints typeLabelConstraints = new GridBagConstraints();
        typeLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
        typeLabelConstraints.insets = new Insets(0, 0, 5, 0);
        typeLabelConstraints.gridx = 0;
        typeLabelConstraints.gridy = 0;

        scrollPane = new JScrollPane();
        scrollPane.setOpaque(false);
        infoPanel.add(scrollPane, BorderLayout.CENTER);

        itemsList = new JList<>(itemsListModel);
        scrollPane.add(itemsList);
        GridBagConstraints destroyRoomButtonConstraints = new GridBagConstraints();
        destroyRoomButtonConstraints.gridx = 0;
        destroyRoomButtonConstraints.gridy = 2;
    }
}
