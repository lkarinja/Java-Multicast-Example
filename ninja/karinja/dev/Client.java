package ninja.karinja.dev;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Client implements Runnable {
	private InetAddress address;
	private int port;
	private MulticastSocket socket;
	private ClientRequestHandler handler;

	public Client(String address, int port) throws Exception {
		this.address = InetAddress.getByName(address);
		this.port = port;
		this.socket = new MulticastSocket(port);
	}

	public void run() {
		try {
			this.socket.joinGroup(this.address);
			handler = new ClientRequestHandler(this.socket);
			Thread handlerThread = new Thread(handler);
			handlerThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void sendMessage(String message) throws Exception {
		DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, this.address,
				this.port);
		socket.send(packet);
		System.out.println("Client [" + System.identityHashCode(this) + "] Sent: " + message);
	}

	public void finish() {
		this.handler.finish();
		this.socket.close();
	}
}

class ClientRequestHandler implements Runnable {
	private MulticastSocket socket;
	private boolean running;

	public ClientRequestHandler(MulticastSocket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		this.running = true;
		while (this.running) {
			try {
				this.receiveMessage();
			} catch (Exception e) {
				this.running = false;
			}
		}
	}

	private synchronized String receiveMessage() throws Exception {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		this.socket.receive(packet);
		String received = new String(data);
		System.out.println("Client [" + System.identityHashCode(this) + "] Received: " + received);
		return received;
	}

	public void finish() {
		this.running = false;
	}
}