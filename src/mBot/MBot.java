package mBot;

import java.io.IOException;

import bluetooth.BluetoothClient;

public class MBot {

    private boolean connected = false;
    private String bluetoothAddress;
    private BluetoothClient serialConn = new BluetoothClient();
    private States lastState = States.STOP;
    private int lastRSpeed = 0;
    private int lastLSpeed = 0;
    private MotorDirection lastDirection = MotorDirection.STOP;

    public MBot(String startBluetoothAddress) {
        bluetoothAddress = startBluetoothAddress;
        try {
            if (!bluetoothAddress.equals("Dummy")) {
                connected = serialConn.connect("Makeblock", bluetoothAddress);
            } else {
                System.out.println("mBot init cancelled because of \"Dummy\" name.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean connect() {
        if (!connected) {
            try {
                if (!bluetoothAddress.equals("Dummy")) {
                    connected = serialConn.connect("Makeblock", bluetoothAddress);
                } else {
                    System.out.println("mBot connect cancelled because of \"Dummy\" name.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return connected;
    }

    public void disconnect() {
        if (connected) {
            try {
                serialConn.disconnect();
                connected = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
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
            serialConn.writeString(resetString);
        }
    }

    public void sendCommand(States state, int rSpeed, int lSpeed, MotorDirection direction) {
        if (connected) {
            char[] command = new char[] { (char) state.ordinal(), (char) rSpeed, (char) lSpeed,
                    (char) direction.ordinal() };
            serialConn.writeChars(command);
        }
    }

    public void changeState(States state) {
        lastState = state;
        this.sendCommand(state, lastRSpeed, lastLSpeed, lastDirection);
    }

    public void changeSpeed(int rSpeed, int lSpeed) {
        lastRSpeed = rSpeed;
        lastLSpeed = lSpeed;
        this.sendCommand(lastState, rSpeed, lSpeed, lastDirection);
    }

    public void changeDirection(MotorDirection direction) {
        lastDirection = direction;
        this.sendCommand(lastState, lastRSpeed, lastLSpeed, direction);
    }
}
