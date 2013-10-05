package userinterface;

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

        BatchRenderDevice renderDevice = new BatchRenderDevice(new LwjglBatchRenderBackendCoreProfile(), 1920,
                1080);

        renderDevice.enableLogFPS();
        Nifty nifty = new Nifty(renderDevice, new OpenALSoundDevice(), new LwjglInputSystem(),
                new AccurateTimeProvider());

        nifty.fromXml("tutorial.xml", "mainmenu");

        LwjglInitHelper.renderLoop(nifty, null);
        LwjglInitHelper.destroy();
    }
}
