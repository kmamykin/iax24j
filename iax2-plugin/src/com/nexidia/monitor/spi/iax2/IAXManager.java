/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nexidia.monitor.spi.iax2;

import iax.audio.AudioFactory;
import iax.protocol.call.Call;
import iax.protocol.peer.Peer;
import iax.protocol.peer.PeerException;
import iax.protocol.peer.PeerListener;
import iax.protocol.user.command.UserCommandFacade;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kmamykin
 */
public class IAXManager implements PeerListener {

    private Peer peer;

    public static void main(String[] args) throws InterruptedException, IOException {
        IAXManager iaxManager = new IAXManager("125", "125", "192.168.1.111", 10);
        IAXManager iaxManager2 = new IAXManager("123", "123", "192.168.1.111", 10);
        IAXManager iaxManager3 = new IAXManager("124", "124", "192.168.1.111", 10);
        iaxManager.start();
        iaxManager2.start();
        iaxManager3.start();
        Thread.sleep(1000);

        iaxManager2.makeCall("125");
        Thread.sleep(1000);
        //iaxManager.answerCall();
        Thread.sleep(1000);
        iaxManager3.makeCall("125");
        Thread.sleep(1000);
        //iaxManager.answerCall();
        Thread.sleep(1000);
        iaxManager2.hangup("125");
        Thread.sleep(100000);
        iaxManager3.hangup("125");

        System.in.read();
        iaxManager.stop();
        iaxManager2.stop();
        iaxManager3.stop();
    }

    public IAXManager(String userName, String userPassword, String host, int maxCalls) {
        AudioFactory audioFactory = new IAXAudioFactory();
        this.peer = new Peer(this, userName, userPassword, host, false, maxCalls, audioFactory);
    }

    public void start() {
        peer.register();
    }

    public void stop() {
        shutdown();
    }

    @Override
    public void recvCall(String callingName, String callingNumber) {
        System.out.println("IAXphone, recvCall: " + callingNumber);
        // This method is called when we receive a call from remote peer
        // We need to handle this event and initiate a recording/scanning session
        answerCall(callingNumber);
    }

    @Override
    public void hungup(String calledNumber) {
        // This method is called when the remote peer hungup.
        // we need to handle this event and close the recording/scanning session
        System.out.println("IAXphone, hungup: " + calledNumber);
//        try {
//            //hungup(calledNumber);
//            Call hangedCall = peer.getCall(calledNumber);
//            hangedCall.endCall();
//        } catch (PeerException ex) {
//            Logger.getLogger(IAXManager.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }

    @Override
    public void answered(String calledNumber) {
        // This method is called when the remote phone answers our call, not applicable for monitoring
        System.out.println("IAXphone, answered: " + calledNumber);
    }

    @Override
    public void exited() {
        System.out.println("IAXphone, exited.");
    }

    @Override
    public void playWaitTones(String calledNumber) {
        System.out.println("IAXphone, playWaitTones" + calledNumber);
    }

    @Override
    public void registered() {
        System.out.println("IAXphone, registered");
    }

    @Override
    public void unregistered() {
        System.out.println("IAXphone, unregistered");
    }

    @Override
    public void waiting() {
        System.out.println("IAXphone, waiting");
    }

    public void makeCall(String calledNumber) {
        System.out.println("IAXphone, Calling " + calledNumber);
        UserCommandFacade.newCall(peer, calledNumber);
    }

//    public void transferCall(String calledNumber) {
//        System.out.println("IAXphone, Transfering call from " + callParticipant + " to " + calledNumber);
//        UserCommandFacade.transferCall(peer, callParticipant, calledNumber);
//    }

//    public void sendDTMF(char tone) {
//        UserCommandFacade.sendDTMF(peer, callParticipant, tone);
//    }

    public void answerCall(String currentCallNo) {
        System.out.println("IAXphone, Answered call from " + currentCallNo);

        UserCommandFacade.answerCall(peer, currentCallNo);
    }

    public void hangup(String callParticipant) {
        System.out.println("IAXphone, Hungup call from " + callParticipant);

        UserCommandFacade.hangupCall(peer, callParticipant);
    }

    public void shutdown() {
        System.out.println("IAXphone, Shutting down...");

        UserCommandFacade.exit(peer);
    }
}
