import java.net.*;
import java.util.Scanner;

import java.io.*;

public class MulticastPeer {

	private static String group_ip = "228.5.6.7";

	public static void main(String args[]) {

		MulticastSocket s = null;
		Scanner scanner = null;

		try {
			InetAddress group = InetAddress.getByName(group_ip);
			s = new MulticastSocket(6789);
			s.joinGroup(group);

			Receiver receiver = new Receiver(s);
			String message = new String("");

			System.out.println("Type your message:");

			do {
				scanner = new Scanner(System.in);
				message = scanner.nextLine();
				byte[] m = message.getBytes();
				DatagramPacket messageOut = new DatagramPacket(m, m.length, group, 6789);
				s.send(messageOut);

			} while (message.compareTo("quit") != 0);

			s.leaveGroup(group);
			receiver.stop();

		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (s != null)
				s.close();
			if (scanner != null)
				scanner.close();
		}
	}

}

class Receiver extends Thread {

	MulticastSocket socket;

	public Receiver(MulticastSocket s) {
		this.socket = s;
		this.start();
	}

	public void run() {
		try {
			String message = new String("");

			do {
				byte[] buffer = new byte[1000];
				DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
				this.socket.receive(messageIn);
				message = new String(messageIn.getData());
				System.out.println("Received:" + message);
			} while (message.compareTo("Quit") != 0);

		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		}
	}

}
