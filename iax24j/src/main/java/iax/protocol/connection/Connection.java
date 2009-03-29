package iax.protocol.connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import iax.protocol.peer.Peer;

/**
 * Makes the udp connection with the asterisk host for sending and receiving frames
 */
public class Connection implements Runnable {

	// Maximun length of the udp buffer for receiving frames
	static final int BUFFER_LENGTH = 4096;
    // Port udp of the asterisk host
	static final int IAX_PORT = 4569;
    // Flag to determine if the thread is running or not
	private boolean running = true;
	// Peer that handle the received frames (and send the sending frames)
	private Peer peer;
	// Inet addres of the asterisk host
    private InetAddress hostIAddr;
    // Socket udp to send and receive frames
	private DatagramSocket socket;

	/**
     * Constructor.
	 */
	public Connection() {
	}

    /* (non-Javadoc)
	 * @see iax.protocol.connection.IConnection#start()
     * @param peer peer that handle the received frames (and send the sending frames)
     * @param host ip of the asterisk host
	 */
	public void start(Peer peer,String host) {
		this.peer = peer;
		try {
            hostIAddr = InetAddress.getByName(host);
			socket = new DatagramSocket();
			Thread t = new Thread(this);
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /* (non-Javadoc)
	 * @see iax.protocol.connection.IConnection#stop()
	 */
	public synchronized void stop() {
		this.running = false;
	}

	/* (non-Javadoc)
	 * @see iax.protocol.connection.IConnection#run()
	 */
	public void run() {
		while ( running ) {
			try {
                // Create a packet for receiving the data of a frame
				DatagramPacket packet = new DatagramPacket(new byte[BUFFER_LENGTH], BUFFER_LENGTH);
                // Wait until receiving the data of a packet
				socket.receive( packet );
				recv(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

    /*
     * Handles the packets received by delegating in the peer
     */
	private void recv(DatagramPacket packet) {
        // Data length received (is less or equal than the total length of the packet used for receiving data)
		int length = packet.getLength();
		byte[] buffer = new byte[length];
		System.arraycopy(packet.getData(), 0, buffer, 0, length);
		peer.handleRecvFrame(buffer);
	}

    /* (non-Javadoc)
	 * @see iax.protocol.connection.IConnection#send(byte[])
	 */
    public void send(byte data[]) {
        try {
            DatagramPacket packet = new DatagramPacket(data, data.length, hostIAddr, IAX_PORT);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}