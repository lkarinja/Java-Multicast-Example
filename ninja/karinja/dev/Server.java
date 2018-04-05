package ninja.karinja.dev;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server implements Runnable {
	private InetAddress address;
	private int port;
	private DatagramSocket serverSocket;
	private ServerRequestHandler handler;

	public Server(String address, int port) throws Exception {
		this.address = InetAddress.getByName(address);
		this.port = port;
		this.serverSocket = new DatagramSocket();
	}

	public void run() {
		handler = new ServerRequestHandler(this.serverSocket);
		Thread handlerThread = new Thread(handler);
		handlerThread.start();
	}

	public synchronized void sendMessage(String message) throws Exception {
		DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, this.address,
				this.port);
		serverSocket.send(packet);
		System.out.println("Server [" + System.identityHashCode(this) + "] Sent: " + message);
	}

	public void finish() {
		this.handler.finish();
		this.serverSocket.close();
	}
}

class ServerRequestHandler implements Runnable {
	private DatagramSocket serverSocket;
	private boolean running;

	public ServerRequestHandler(DatagramSocket serverSocket) {
		this.serverSocket = serverSocket;
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
		this.serverSocket.receive(packet);
		String received = new String(data);
		System.out.println("Server [" + System.identityHashCode(this) + "] Received: " + received);
		return received;
	}

	public void finish() {
		this.running = false;
	}
}