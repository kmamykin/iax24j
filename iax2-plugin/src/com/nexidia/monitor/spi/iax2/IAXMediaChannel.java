/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nexidia.monitor.spi.iax2;

import iax.audio.AudioListener;
import iax.audio.Player;
import iax.audio.PlayerException;

/**
 *
 * @author kmamykin
 */
public class IAXMediaChannel extends Player {

    int count = 0;

    public IAXMediaChannel() throws PlayerException {
        super(Player.JITTER_BUFFER);
    }

    @Override
    public void play() {
        System.out.println("IAXMediaChannel.play...");
    }

    @Override
    public void stop() {
        System.out.println("IAXMediaChannel.stop...");
    }

    @Override
    public void write(long timestamp, byte[] data, boolean absolute) {
        if (count >= 80) {
            System.out.println("");
            count = 0;
        }
        System.out.print(".");
        count++;
    }

    @Override
    public void run() {
        System.out.println("IAXMediaChannel.run...");
    }
}
