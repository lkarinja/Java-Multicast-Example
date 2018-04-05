package ninja.karinja.dev;

/*
Copyright © 2018 Leejae Karinja

This file is part of Java Multicast Example.

Java Multicast Example is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Java Multicast Example is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Java Multicast Example.  If not, see <http://www.gnu.org/licenses/>.
*/

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
