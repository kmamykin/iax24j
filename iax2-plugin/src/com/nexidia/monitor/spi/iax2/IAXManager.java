/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nexidia.monitor.spi.iax2;

import iax.audio.AudioFactory;
import iax.protocol.peer.Peer;
import iax.protocol.peer.PeerListener;
import iax.protocol.user.command.UserCommandFacade;

/**
 *
 * @author kmamykin
 */
public class IAXManager implements PeerListener {

    private Peer peer;
    private String callParticipant;

    public static void main(String[] args) throws InterruptedException {
        IAXManager iaxManager = new IAXManager("123", "123", "192.168.1.111", 10);
        iaxManager.start();
        Thread.sleep(10000);
        iaxManager.stop();
    }

    public IAXManager(String userName, String userPassword, String host, int maxCalls) {
        AudioFactory audioFactory = new IAXAudioFactory();
        this.peer = new Peer(this, userName, userPassword, host, false, maxCalls, audioFactory);
        this.callParticipant = null;
    }

    public void start() {
        peer.register();
    }

    public void stop() {
        shutdown();
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
    public void hungup(String calledNumber) {
        // This method is called when the remote peer hungup.
        // we need to handle this event and close the recording/scanning session
        System.out.println("IAXphone, hungup: " + calledNumber);
    }

    @Override
    public void playWaitTones(String calledNumber) {
        System.out.println("IAXphone, playWaitTones" + calledNumber);
    }

    @Override
    public void recvCall(String callingName, String callingNumber) {
        // This method is called when we receive a call from remote peer
        // We need to handle this event and initiate a recording/scanning session
        callParticipant = callingNumber;
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

    public void transferCall(String calledNumber) {
        System.out.println("IAXphone, Transfering call from " + callParticipant + " to " + calledNumber);
        UserCommandFacade.transferCall(peer, callParticipant, calledNumber);
    }

    public void sendDTMF(char tone) {
        UserCommandFacade.sendDTMF(peer, callParticipant, tone);
    }

    public void answerCall() {
        System.out.println("IAXphone, Answered call from " + callParticipant);
        System.out.println("Type \"hangup\" to hangup call");
        System.out.println("Type \"transfer <address>\" to transfer call to address");

        UserCommandFacade.answerCall(peer, callParticipant);
    }

    private void hangup() {
        System.out.println("IAXphone, Hungup call from " + callParticipant);

        UserCommandFacade.hangupCall(peer, callParticipant);
    }

    private void shutdown() {
        System.out.println("IAXphone, Shutting down...");

        UserCommandFacade.exit(peer);
    }
}
