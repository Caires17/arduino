package test1;

import com.fazecast.jSerialComm.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author José Pedro Caires
 *
 * NOTE: ON WINDOWS' ENVIRONMENT SerialPort = SerialComm
 */
public class Main {

	private static final String FILENAME = "filename.txt";

	public static void main(String[] args) {
		// Setup
		BufferedWriter bw = null;
		FileWriter fw = null;
		SerialPort ports[] = SerialPort.getCommPorts();

		System.out.println("Choose a port:");
		int i = 1;

		// Loop throw all available ports
		for (SerialPort port : ports) {
			System.out.println(i++ + "," + port.getSystemPortName());
		}

		// Gets the port the user chooses
		Scanner s = new Scanner(System.in);

		int chosenPort = s.nextInt();

		SerialPort port = ports[chosenPort - 1];

		if (port.openPort()) {
			System.out.println("Successfully opened the port.");
		} else {
			System.out.println("Failed to open port.");
			return;
		}

		// Timeouts needed on Windows
		port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		Scanner data = new Scanner(port.getInputStream());

		while (true) {
			while (data.hasNextLine()) {
				System.out.println(data.nextLine());
				try {

					fw = new FileWriter(FILENAME, true);
					bw = new BufferedWriter(fw);
					bw.write(data.nextLine());

					System.out.println("Done");

				} catch (IOException e) {

					e.printStackTrace();

				} finally {

					try {

						if (bw != null) {
							bw.close();
						}

						if (fw != null) {
							fw.close();
						}

					} catch (IOException ex) {

						ex.printStackTrace();

					}

				}
			}
		}

	}

}
