package yadf.userinterface.game;

import yadf.controller.AbstractController;
import yadf.simulation.Region;

/**
 * The Game Loop.
 */
class GameLoop implements Runnable {
    /** How many ms in one second. */
    private static final long MS_IN_A_SECOND = 1000;

    /** The desired period in ms. */
    private static final long DESIRED_PERIOD = 40;

    /** The number of simulation steps to take before sending commands. */
    private static final long SIMULATION_STEPS_SEND_COMMAND = 10;

    /** The region. */
    private final Region region;

    /** The controller. */
    private final AbstractController controller;

    /** The game panel. */
    private final IGamePanel gamePanel;

    /** The thread. */
    private final Thread thread;

    /** The running. */
    private boolean running = true;

    /** The number of frames rendered in the last second. */
    private long frameCount;

    /** The current frames per second. */
    private long fps;

    /**
     * Instantiates a new client runnable.
     * @param regionTmp the region
     * @param controllerTmp the controller
     * @param gamePanelTmp the game panel
     */
    public GameLoop(final Region regionTmp, final AbstractController controllerTmp, final IGamePanel gamePanelTmp) {
        region = regionTmp;
        controller = controllerTmp;
        gamePanel = gamePanelTmp;
        thread = new Thread(this, "GameLoop");
        thread.start();
    }

    @Override
    public void run() {
        try {
            long lastTime = System.currentTimeMillis();
            long lastTimeFps = System.currentTimeMillis();
            int simulationSteps = 0;

            while (running()) {
                long currentTime = System.currentTimeMillis();
                frameCount++;
                if (currentTime - lastTimeFps > MS_IN_A_SECOND) {
                    lastTimeFps = currentTime;
                    fps = frameCount;
                    frameCount = 0;
                }

                long diffTime = currentTime - lastTime;
                long sleepTime = DESIRED_PERIOD - diffTime;
                lastTime = currentTime;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }

                if (simulationSteps++ > SIMULATION_STEPS_SEND_COMMAND) {
                    controller.doCommands(region);
                    simulationSteps = 0;
                }

                region.update();
                gamePanel.update();
            }
        } catch (Exception e) {
            e.printStackTrace();
            gamePanel.disconnect();
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

    /**
     * Get the frames per second.
     * @return the fps
     */
    public long getFps() {
        return fps;
    }
}
