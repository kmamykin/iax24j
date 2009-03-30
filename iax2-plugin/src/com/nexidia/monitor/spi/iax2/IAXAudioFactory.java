/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nexidia.monitor.spi.iax2;

import iax.audio.AudioFactory;
import iax.audio.Player;
import iax.audio.PlayerException;
import iax.audio.Recorder;
import iax.audio.RecorderException;
import iax.audio.impl.NullRecorder;

/**
 *
 * @author kmamykin
 */
class IAXAudioFactory implements AudioFactory {
    /**
     * ULAW codec.
     */
    public static final long ULAW_V = 4;
    /**
     * Voice frame with codec ULAW
     */
    public static final int ULAW_SC = 4;

    public IAXAudioFactory() {
    }

    @Override
    public Player createPlayer() throws PlayerException {
        return new IAXMediaChannel();
    }

    @Override
    public Recorder createRecorder() throws RecorderException {
        return new NullRecorder();
    }

    @Override
    public long getCodec() {
        return ULAW_V;
    }

    @Override
    public int getCodecSubclass() {
        return ULAW_SC;
    }

}
