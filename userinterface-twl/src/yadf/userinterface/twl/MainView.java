package yadf.userinterface.twl;

import de.matthiasmann.twl.Container;

public abstract class MainView extends Container {

    public abstract void start();

    public abstract void stop();

    public abstract void update();
}
