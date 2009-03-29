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

/**
 *
 * @author kmamykin
 */
class IAXAudioFactory implements AudioFactory {

    public IAXAudioFactory() {
    }

    public Player createPlayer() throws PlayerException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Recorder createRecorder() throws RecorderException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getCodec() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getCodecSubclass() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
