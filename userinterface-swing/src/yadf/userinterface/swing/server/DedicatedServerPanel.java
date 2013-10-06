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
package yadf.userinterface.swing.server;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import yadf.controller.Connection;
import yadf.controller.command.AbstractCommand;
import yadf.logger.Logger;
import yadf.userinterface.swing.multiplayer.IMainWindow;

/**
 * The Class DedicatedServerPanel.
 */
public class DedicatedServerPanel extends JPanel {

    /** The serial version UID. */
    private static final long serialVersionUID = 524066898610482697L;

    /**
     * The Class ServerThread.
     */
    private class ServerThread implements Runnable {

        /** The connections. */
        private final List<Connection> connections;

        /** The thread. */
        private final Thread thread;

        /** The running. */
        private boolean running = true;

        /**
         * Instantiates a new server thread.
         * @param connectionsTmp the connections
         */
        public ServerThread(final List<Connection> connectionsTmp) {
            connections = connectionsTmp;

            thread = new Thread(this, "DedicatedServerPanel");
            thread.start();
        }

        /**
         * Close.
         */
        public void close() {
            for (Connection connection : connections) {
                connection.close();
            }
        }

        @Override
        public void run() {
            try {
                while (running()) {
                    List<AbstractCommand> commands = new ArrayList<>();
                    // Get commands from all players
                    for (Connection connection : connections) {
                        Object object = connection.readObject();
                        @SuppressWarnings("unchecked")
                        List<AbstractCommand> newCommands = (List<AbstractCommand>) object;

                        for (AbstractCommand command : newCommands) {
                            Logger.getInstance().log(this,
                                    command.getClass().getSimpleName() + " from " + connection.getAddress());
                        }

                        commands.addAll(newCommands);
                    }

                    for (Connection connection : connections) {
                        connection.writeObject(commands);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                disconnect();
            }
        }

        /**
         * Running.
         * @return true, if successful
         */
        public synchronized boolean running() {
            return running;
        }

        /**
         * Stop.
         */
        public synchronized void stop() {
            running = false;
        }
    }

    /** The players table. */
    private JTable playersTable;

    /** The server thread. */
    private final ServerThread serverThread;

    /** The listener. */
    private final IMainWindow mainWindow;

    /**
     * Create the panel.
     * @param connections the connections
     * @param mainWindowTmp the main window
     */
    public DedicatedServerPanel(final List<Connection> connections, final IMainWindow mainWindowTmp) {
        mainWindow = mainWindowTmp;
        serverThread = new ServerThread(connections);
        setupLayout();
    }

    /**
     * Close.
     */
    public void close() {
        serverThread.close();
    }

    /**
     * Disconnect.
     * 
     * Stops and closes the thread then tells the main window.
     */
    public void disconnect() {
        stop();
        close();
        mainWindow.setupMainMenu();
    }

    /**
     * Stop.
     */
    public void stop() {
        serverThread.stop();
    }

    /**
     * Setup layout.
     */
    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));

        JButton stopGameButton = new JButton("Stop Game");
        add(stopGameButton, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);

        playersTable = new JTable();
        playersTable.setFillsViewportHeight(true);
        scrollPane.setViewportView(playersTable);
    }
}
