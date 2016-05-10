package mBot;

import java.io.IOException;

import bluetooth.BluetoothClient;
import bluetooth.RFCOMMClient;

public class MBot {

	private boolean connected = false;
	private String bluetoothAddress;
	private RFCOMMClient serialConn = new RFCOMMClient();

	public MBot(String startBluetoothAddress) {
		bluetoothAddress = startBluetoothAddress;
		try {
			if (!bluetoothAddress.equals("Dummy")) {
				connected = serialConn.connect("Makeblock", bluetoothAddress);
			} else {
				System.out
						.println("mBot init cancelled because of \"Dummy\" name.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			connected = false;
		}
	}

	public boolean connect() {
		if (!connected) {
			try {
				if (!bluetoothAddress.equals("Dummy")) {
					connected = serialConn.connect("Makeblock",
							bluetoothAddress);
				} else {
					System.out
							.println("mBot connect cancelled because of \"Dummy\" name.");
				}
			} catch (IOException e) {
				e.printStackTrace();
				connected = false;
			}
		}
		return connected;
	}

	public void disconnect() {
		try {
			serialConn.disconnect();
			connected = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isConnected(boolean check) {
		if (check) {
			connected = serialConn.isConnected();
		}
		return connected;
	}

	public String getBluetoothAddress() {
		return bluetoothAddress;
	}

	public void synch() {
		if (connected) {
			char resetToken = (char) 0xF0;
			String resetString = "";
			for (int i = 0; i < 10; i++) {
				resetString += resetToken;
			}
			try {
				serialConn.writeString(resetString);
			} catch (IOException e) {
				connected = false;
			}
		}
	}

	public void sendCommand(States state, int lSpeed, int rSpeed,
			MotorDirection direction) {
		if (connected) {
			String command = "";
			command += (char) state.ordinal();
			command += (char) rSpeed;
			command += (char) lSpeed;
			command += (char) direction.ordinal();
			try {
				serialConn.writeString(command);
			} catch (IOException e) {
				connected = false;
			}
		}
	}
}
