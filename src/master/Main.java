package master;

import mBot.MBot;
import mBot.MotorDirection;
import mBot.States;
import util.ReadConsoleInput;

public class Main {

    public static void main(String[] args) {

        String[] bluetoothAddresses = new String[] { "000502031DD3", "Dummy", "Dummy" };
        MBot[] mBots = new MBot[] { null, null, null };

        for (int i = 0; i < mBots.length && i < bluetoothAddresses.length; i++) {
            mBots[i] = new MBot(bluetoothAddresses[i]);
            boolean tryAgain = false;
            while (!mBots[i].isConnected(true) && tryAgain) {
                System.out.print("mBot " + (i + 1) + " not found. Retry? (Y / N): ");
                tryAgain = ReadConsoleInput.readBoolean(false);
                if (tryAgain) {
                    mBots[i].connect();
                }
            }
            mBots[i].synch();
        }

        String cont;
        do {
            int mBotNr = 0;
            while (mBotNr < 1 || 3 < mBotNr) {
                System.out.print("Select an mBot (1 - 3): ");
                mBotNr = ReadConsoleInput.readInt(false);
                if (mBotNr < 1 || 3 < mBotNr) {
                    System.out.println("Please enter a number between 1 and 3.");
                }
            }
            System.out.print("Enter the State of mBot (0 - 4): ");
            States state = States.values()[ReadConsoleInput.readInt(false)];
            System.out.print("Enter Speed of right Motor: ");
            int rSpeed = ReadConsoleInput.readInt(false);
            System.out.print("Enter Speed of left Motor: ");
            int lSpeed = ReadConsoleInput.readInt(false);
            System.out.print("Enter Direction of Motors (0 - 4): ");
            MotorDirection direction = MotorDirection.values()[ReadConsoleInput.readInt(false)];

            mBots[mBotNr].sendCommand(state, rSpeed, lSpeed, direction);

            System.out.print("Continue (Y / N)? ");
            cont = ReadConsoleInput.readString(false);
        } while (cont.equals("Y") || cont.equals("y"));

        for (int i = 0; i < mBots.length; i++) {
            mBots[i].sendCommand(States.STOP, 0, 0, MotorDirection.STOP);
            mBots[i].disconnect();
        }

    }

}
