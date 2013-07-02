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
package userinterface.multiplayer.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import logger.Logger;
import userinterface.multiplayer.ILobbyPanel;
import controller.Connection;

/**
 * The lobby server class.
 */
public class LobbyServer implements ILobbyServer {

    /** The new client acceptor thread. */
    private final NewClientAcceptorThread newClientAcceptorThread;

    /** The server threads. */
    private final List<ServerThread> serverThreads;

    /** The listener. */
    private final ILobbyPanel lobbyPanel;

    /** The player names. */
    private final List<String> playerNames;

    /** The start games received. */
    private int startGamesReceived;

    /** The player count. */
    private int playerCount;

    /**
     * Instantiates a new lobby server.
     * @param lobbyPanelTmp the lobby panel
     */
    public LobbyServer(final ILobbyPanel lobbyPanelTmp) {
        lobbyPanel = lobbyPanelTmp;

        serverThreads = new ArrayList<>();
        newClientAcceptorThread = new NewClientAcceptorThread(this);
        playerNames = new ArrayList<>();
        startGamesReceived = 0;
    }

    @Override
    public void addNewClient(final Socket socket) {
        Logger.getInstance().log(this, "addNewClient()");
        ServerThread serverThread = new ServerThread(this);
        if (serverThread.init(socket)) {
            serverThread.start();
            serverThreads.add(serverThread);

            playerNames.add("Player " + (++playerCount));
            int playerIndex = playerNames.size() - 1;
            serverThread.sendPlayerIndex(playerIndex);
            for (ServerThread serverThread2 : serverThreads) {
                serverThread2.sendPlayerNames(playerNames);
            }
            lobbyPanel.setPlayerNames(playerNames);
        }
    }

    @Override
    public void close() {
        Logger.getInstance().log(this, "close()");
        newClientAcceptorThread.close();

        for (ServerThread serverThread : serverThreads) {
            serverThread.close();
        }
    }

    @Override
    public void disconnect() {
        Logger.getInstance().log(this, "disconnect()");
        stop();
        close();
        lobbyPanel.disconnect();
    }

    /**
     * Gets the connections.
     * @return the connections
     */
    public List<Connection> getConnections() {
        List<Connection> connections = new ArrayList<>();

        for (ServerThread serverThread : serverThreads) {
            connections.add(serverThread.getConnection());
        }

        return connections;
    }

    @Override
    public boolean init(final String ip, final int port) {
        return newClientAcceptorThread.init(port);
    }

    @Override
    public void receiveChat(final String playerName, final String text) {
        Logger.getInstance().log(this, "receiveChat(" + playerName + ", " + text + ")");
        lobbyPanel.receiveChat(playerName, text);
        sendChat(playerName, text);
    }

    @Override
    public void receiveStartGame() {
        Logger.getInstance().log(this, "receiveStartGame()");
        startGamesReceived++;
        if (startGamesReceived == serverThreads.size()) {
            stop();
            newClientAcceptorThread.close();
            lobbyPanel.startServer();
        }
    }

    @Override
    public void sendChat(final String playerName, final String text) {
        Logger.getInstance().log(this, "sendChat(" + playerName + ", " + text + ")");
        for (ServerThread serverThread : serverThreads) {
            serverThread.sendChat(playerName, text);
        }
    }

    @Override
    public void setPlayerName(final int playerIndex, final String playerName) {
        Logger.getInstance().log(this, "setPlayerName(" + playerIndex + ", " + playerName + ")");
        playerNames.set(playerIndex, playerName);
        lobbyPanel.setPlayerNames(playerNames);

        for (ServerThread serverThread : serverThreads) {
            serverThread.sendPlayerNames(playerNames);
        }
    }

    @Override
    public void start() {
        Logger.getInstance().log(this, "start()");
        newClientAcceptorThread.start();
    }

    /**
     * Start game.
     */
    public void startGame() {
        Logger.getInstance().log(this, "startGame()");
        for (ServerThread serverThread : serverThreads) {
            serverThread.startGame();
        }
    }

    @Override
    public void stop() {
        Logger.getInstance().log(this, "stop()");
        newClientAcceptorThread.stop();

        for (ServerThread serverThread : serverThreads) {
            serverThread.stop();
        }
    }
}
