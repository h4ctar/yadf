package yadf.ui.nifty;

import yadf.ui.nifty.ingame.InGame;
import yadf.ui.nifty.mainmenu.MainMenu;
import yadf.ui.nifty.mainmenu.NewSinglePlayerGameMenu;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.batch.BatchRenderDevice;
import de.lessvoid.nifty.examples.LwjglInitHelper;
import de.lessvoid.nifty.renderer.lwjgl.input.LwjglInputSystem;
import de.lessvoid.nifty.renderer.lwjgl.render.batch.LwjglBatchRenderBackendCoreProfile;
import de.lessvoid.nifty.sound.openal.OpenALSoundDevice;
import de.lessvoid.nifty.spi.time.impl.AccurateTimeProvider;

public class Main {

    public static void main(String[] args) {
        LwjglInitHelper.initSubSystems("Nifty 1.2 Tutorial, rendering using native Lwjgl with OpenAL Sound", true);

        BatchRenderDevice renderDevice = new BatchRenderDevice(new LwjglBatchRenderBackendCoreProfile(), 2048,
                2048);

        renderDevice.enableLogFPS();
        Nifty nifty = new Nifty(renderDevice, new OpenALSoundDevice(), new LwjglInputSystem(),
                new AccurateTimeProvider());

        RenderLoop renderLoop = new RenderLoop(nifty);

        nifty.registerScreenController(new MainMenu(renderLoop));
        nifty.registerScreenController(new NewSinglePlayerGameMenu(renderLoop));
        nifty.registerScreenController(new InGame(renderLoop));

        nifty.addXml("main_menu.xml");
        nifty.addXml("new_single_player_game_menu.xml");
        nifty.addXml("in_game.xml");

        nifty.gotoScreen("main-menu");

        LwjglInitHelper.renderLoop(nifty, null);
        LwjglInitHelper.destroy();
    }
}
