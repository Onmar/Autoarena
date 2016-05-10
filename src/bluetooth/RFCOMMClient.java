package bluetooth;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import master.Globals;

public class RFCOMMClient {

	private FileInputStream in = null;
	private FileOutputStream out = null;
	
	private boolean connected;

	public boolean connect(String deviceName, String bluetoothAddress)
			throws IOException {
		for (int i = 0; i < Globals.mBotAddresses.length; i++) {
			if (Globals.mBotAddresses[i].equals(bluetoothAddress)) {
					in = new FileInputStream("/dev/rfcomm" + i);
					out = new FileOutputStream("/dev/rfcomm" + i);
					connected = true;
					return true;
			}
		}
		return false;
	}

	public void writeString(String message) throws IOException {
		if (connected) {
			out.write(message.getBytes());
		}
	}

	public String readAll() throws IOException {
		if (connected) { // Device connected
			String message = "";
			while (in.available() > 0) {
				message += (char) in.read(); // Read
			}
			return message;
		} else { // No device connected
			return "";
		}
	}

	public boolean isConnected() {
		return connected;
	}

	public void disconnect() throws IOException {
		in.close();
		in = null;
		out.close();
		out = null;
		connected = false;
	}

}
