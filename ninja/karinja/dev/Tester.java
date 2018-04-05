package ninja.karinja.dev;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Tester {
	public static void main(String args[]) {
		String address = "224.0.0.127";
		int port = 2468;
		try {
			Server server = new Server(address, port);
			Thread serverThread = new Thread(server);
			serverThread.start();

			Client client1 = new Client(address, port);
			Thread client1Thread = new Thread(client1);
			client1Thread.start();

			Client client2 = new Client(address, port);
			Thread client2Thread = new Thread(client2);
			client2Thread.start();

			BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

			server.sendMessage(r.readLine());
			client1.sendMessage(r.readLine());
			client2.sendMessage(r.readLine());

			client1.finish();
			client2.finish();
			server.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
