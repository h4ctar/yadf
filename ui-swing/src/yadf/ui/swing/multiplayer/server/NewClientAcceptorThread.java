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
package yadf.ui.swing.multiplayer.server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * The Class NewClientAcceptorThread.
 */
class NewClientAcceptorThread implements Runnable {

    /** The server socket. */
    private ServerSocket serverSocket;

    /** The thread. */
    private Thread thread;

    /** The running. */
    private boolean running;

    /** The listener. */
    private final ILobbyServer lobbyServer;

    /**
     * Instantiates a new new client acceptor thread.
     * @param lobbyServerTmp the lobby server
     */
    NewClientAcceptorThread(final ILobbyServer lobbyServerTmp) {
        lobbyServer = lobbyServerTmp;
    }

    /**
     * Close.
     */
    void close() {
        try {
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inits the thread.
     * @param port the port
     * @return true, if successful
     */
    boolean init(final int port) {
        boolean ok = true;

        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
            ok = false;
        }

        return ok;
    }

    @Override
    public void run() {
        try {
            while (running()) {
                Socket socket = serverSocket.accept();
                lobbyServer.addNewClient(socket);
            }
        } catch (Exception e) {
            if (running) {
                e.printStackTrace();
                lobbyServer.disconnect();
            }
        }

        try {
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Running.
     * @return true, if successful
     */
    private synchronized boolean running() {
        return running;
    }

    /**
     * Start.
     */
    synchronized void start() {
        running = true;
        thread = new Thread(this, "NewClientAcceptorThread");
        thread.start();
    }

    /**
     * Stop.
     */
    synchronized void stop() {
        running = false;
    }
}
