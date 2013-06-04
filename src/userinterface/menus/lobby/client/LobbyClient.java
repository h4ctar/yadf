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
package userinterface.menus.lobby.client;

import java.util.List;

import controller.Connection;

import logger.Logger;
import userinterface.menus.multiplayer.ILobbyPanel;

/**
 * The lobby client class.
 * 
 */
public class LobbyClient implements ILobbyClient {

    /** The listener. */
    private final ILobbyPanel lobbyPanel;

    /** The client thread. */
    private final ClientThread clientThread;

    /**
     * Instantiates a new lobby client.
     * 
     * @param lobbyPanelTmp the lobby panel
     */
    public LobbyClient(final ILobbyPanel lobbyPanelTmp) {
        this.lobbyPanel = lobbyPanelTmp;

        clientThread = new ClientThread(this);
    }

    @Override
    public void close() {
        clientThread.close();
    }

    @Override
    public void disconnect() {
        lobbyPanel.disconnect();
    }

    /**
     * Gets the connection.
     * 
     * @return the connection
     */
    public Connection getConnection() {
        return clientThread.getConnection();
    }

    @Override
    public boolean init(final String ip, final int port) {
        return clientThread.init(ip, port);
    }

    @Override
    public void receiveChat(final String playerName, final String text) {
        Logger.getInstance().log(this, "receiveChat(" + playerName + ", " + text + ")");
        lobbyPanel.receiveChat(playerName, text);
    }

    @Override
    public void sendChat(final String playerName, final String text) {
        Logger.getInstance().log(this, "sendChat(" + text + ")");
        clientThread.sendChat(playerName, text);
    }

    @Override
    public void setPlayerIndex(final int index) {
        Logger.getInstance().log(this, "setPlayerIndex(" + index + ")");
        lobbyPanel.setPlayerIndex(index);
    }

    @Override
    public void setPlayerNames(final List<String> playerNames) {
        Logger.getInstance().log(this, "setPlayerNames()");
        lobbyPanel.setPlayerNames(playerNames);
    }

    @Override
    public void start() {
        clientThread.start();
    }

    @Override
    public void startGame() {
        Logger.getInstance().log(this, "startGame()");
        lobbyPanel.startGame();
    }

    @Override
    public void stop() {
        clientThread.stop();
    }
}
