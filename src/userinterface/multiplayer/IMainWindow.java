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
package userinterface.multiplayer;

import java.util.List;

import simulation.map.MapIndex;
import controller.Connection;

/**
 * Listener to the lobby panel.
 */
public interface IMainWindow {

    /**
     * Load single player game.
     */
    void loadSinglePlayerGame();

    /**
     * Quit.
     */
    void quit();

    /**
     * Setup host multiplayer game.
     */
    void setupHostMultiplayerGame();

    /**
     * Setup join multiplayer game.
     */
    void setupJoinMultiplayerGame();

    /**
     * Setup main menu.
     */
    void setupMainMenu();

    /**
     * Setup single player game.
     */
    void setupSinglePlayerGame();

    /**
     * Start multiplayer game.
     * @param connection the connection
     * @param playerNames the player names
     * @param playerIndex the player index
     * @param regionSize the region size
     */
    void startMultiplayerGame(Connection connection, List<String> playerNames, int playerIndex, MapIndex regionSize);

    /**
     * Start server.
     * @param connections the connections
     */
    void startServer(List<Connection> connections);

    /**
     * Start single player game.
     * @param playerName the player name
     * @param regionSize the region size
     */
    void startSinglePlayerGame(String playerName, MapIndex regionSize);

    /**
     * Show the how to play panel.
     */
    void showHowToPlay();
}
