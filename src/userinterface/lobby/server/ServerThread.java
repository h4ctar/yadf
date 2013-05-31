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
package userinterface.lobby.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import logger.Logger;
import userinterface.Connection;
import userinterface.lobby.LobbyMessage;
import userinterface.lobby.LobbyMessageType;

/**
 * A thread of a server that is listening to one of many clients.
 */
public class ServerThread implements Runnable {

    /** The connection. */
    private Connection connection;

    /** The thread. */
    private Thread thread;

    /** The running. */
    private boolean running;

    /** The listener. */
    private final ILobbyServer lobbyServer;

    /**
     * Instantiates a new server thread.
     * 
     * @param lobbyServerTmp the lobby server
     */
    public ServerThread(final ILobbyServer lobbyServerTmp) {
        lobbyServer = lobbyServerTmp;
    }

    /**
     * Close.
     */
    public void close() {
        Logger.getInstance().log(this, "close()");
        connection.close();
    }

    /**
     * {@inheritDoc}
     */
    public void disconnect() {
        lobbyServer.disconnect();
    }

    /**
     * Gets the connection.
     * 
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Inits the.
     * 
     * @param socket the socket
     * @return true, if successful
     */
    public boolean init(final Socket socket) {
        boolean ok = true;

        try {
            connection = new Connection(socket);
        } catch (Exception e) {
            ok = false;
        }

        return ok;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            while (running()) {
                LobbyMessage message = (LobbyMessage) connection.readObject();

                switch (message.type) {
                case CHAT:
                    lobbyServer.receiveChat(message.playerName, message.text);
                    break;
                case DISCONNECT:
                    break;
                case MY_NAME_IS:
                    receiveMyNameIs(message.playerIndex, message.playerName);
                    break;
                case START_GAME:
                    lobbyServer.receiveStartGame();
                    running = false;
                    break;
                default:
                    break;
                }
            }
        } catch (Exception e) {
            if (running) {
                e.printStackTrace();
                lobbyServer.disconnect();
            }
        }
    }

    /**
     * Send chat.
     * 
     * @param playerName the player name
     * @param text the text
     */
    public void sendChat(final String playerName, final String text) {
        Logger.getInstance().log(this, "sendChat(" + playerName + ", " + text + ")");
        LobbyMessage message = new LobbyMessage(LobbyMessageType.CHAT);
        message.playerName = playerName;
        message.text = text;

        try {
            connection.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
            close();
            lobbyServer.disconnect();
        }
    }

    /**
     * Send player index.
     * 
     * @param playerIndex the player index
     */
    public void sendPlayerIndex(final int playerIndex) {
        Logger.getInstance().log(this, "tellIndex(" + playerIndex + ")");
        LobbyMessage message = new LobbyMessage(LobbyMessageType.THIS_IS_YOUR_INDEX);
        message.playerIndex = playerIndex;

        try {
            connection.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
            close();
            lobbyServer.disconnect();
        }
    }

    /**
     * Send player names.
     * 
     * @param playerNames the player names
     */
    public void sendPlayerNames(final List<String> playerNames) {
        Logger.getInstance().log(this, "sendPlayerNames()");
        LobbyMessage message = new LobbyMessage(LobbyMessageType.ALL_PLAYER_NAMES);
        message.playerNames = new ArrayList<>(playerNames);

        try {
            connection.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
            close();
            lobbyServer.disconnect();
        }
    }

    /**
     * Start.
     */
    public synchronized void start() {
        running = true;
        thread = new Thread(this, "ServerThread");
        thread.start();
    }

    /**
     * Start game.
     */
    public void startGame() {
        Logger.getInstance().log(this, "startGame()");
        LobbyMessage message = new LobbyMessage(LobbyMessageType.START_GAME);

        try {
            connection.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
            close();
            lobbyServer.disconnect();
        }
    }

    /**
     * Stop.
     */
    public synchronized void stop() {
        running = false;
    }

    /**
     * Receive my name is.
     * 
     * @param playerIndex the player index
     * @param playerName the player name
     */
    private void receiveMyNameIs(final int playerIndex, final String playerName) {
        Logger.getInstance().log(this, "receiveMyNameIs(" + playerIndex + ", " + playerName + ")");
        lobbyServer.setPlayerName(playerIndex, playerName);
    }

    /**
     * Running.
     * 
     * @return true, if successful
     */
    private boolean running() {
        return running;
    }
}
