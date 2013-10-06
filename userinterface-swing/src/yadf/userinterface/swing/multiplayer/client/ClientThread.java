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
package yadf.userinterface.swing.multiplayer.client;

import java.net.Socket;

import yadf.controller.Connection;
import yadf.logger.Logger;
import yadf.userinterface.swing.multiplayer.LobbyMessage;
import yadf.userinterface.swing.multiplayer.LobbyMessageType;

/**
 * A thread of a client that is listening to messages from the server.
 */
class ClientThread implements Runnable {

    /** The connection. */
    private Connection connection;

    /** The thread. */
    private Thread thread;

    /** The running. */
    private boolean running;

    /** The listener. */
    private final ILobbyClient lobbyClient;

    /**
     * Instantiates a new client thread.
     * @param lobbyClientTmp the lobby client
     */
    ClientThread(final ILobbyClient lobbyClientTmp) {
        lobbyClient = lobbyClientTmp;
    }

    /**
     * Close.
     */
    void close() {
        Logger.getInstance().log(this, "close()");
        connection.close();
    }

    /**
     * Gets the connection.
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Inits the.
     * @param ip the ip
     * @param port the port
     * @return true, if successful
     */
    boolean init(final String ip, final int port) {
        boolean ok = true;

        try {
            connection = new Connection(new Socket(ip, port));
        } catch (Exception e) {
            ok = false;
        }

        return ok;
    }

    @Override
    public void run() {
        try {
            while (running()) {
                LobbyMessage message = (LobbyMessage) connection.readObject();

                switch (message.type) {
                case ALL_PLAYER_NAMES:
                    lobbyClient.setPlayerNames(message.playerNames);
                    break;
                case CHAT:
                    lobbyClient.receiveChat(message.playerName, message.text);
                    break;
                case DISCONNECT:
                    break;
                case START_GAME:
                    receiveStartGame();
                    break;
                case THIS_IS_YOUR_INDEX:
                    receivePlayerIndex(message.playerIndex);
                    break;
                default:
                    break;
                }
            }
        } catch (Exception e) {
            if (running) {
                e.printStackTrace();
                lobbyClient.disconnect();
            }
        }
    }

    /**
     * Send chat.
     * @param playerName the player name
     * @param text the text
     */
    void sendChat(final String playerName, final String text) {
        Logger.getInstance().log(this, "sendChat(" + playerName + ", " + text + ")");
        LobbyMessage message = new LobbyMessage(LobbyMessageType.CHAT);
        message.playerName = playerName;
        message.text = text;

        try {
            connection.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
            close();
            lobbyClient.disconnect();
        }
    }

    /**
     * Start.
     */
    synchronized void start() {
        Logger.getInstance().log(this, "start()");
        running = true;
        thread = new Thread(this, "ClientThread");
        thread.start();
    }

    /**
     * Stop.
     */
    synchronized void stop() {
        Logger.getInstance().log(this, "stop()");
        running = false;
    }

    /**
     * Receive player index.
     * @param playerIndex the player index
     */
    private void receivePlayerIndex(final int playerIndex) {
        Logger.getInstance().log(this, "receivePlayerIndex(" + playerIndex + ")");
        lobbyClient.setPlayerIndex(playerIndex);
    }

    /**
     * Receive start game.
     */
    private void receiveStartGame() {
        Logger.getInstance().log(this, "receiveStartGame()");
        stop();
        LobbyMessage message = new LobbyMessage(LobbyMessageType.START_GAME);
        try {
            connection.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
            close();
            lobbyClient.disconnect();
        }
        lobbyClient.startGame();
    }

    /**
     * Running.
     * 
     * @return true, if successful
     */
    private synchronized boolean running() {
        return running;
    }
}
