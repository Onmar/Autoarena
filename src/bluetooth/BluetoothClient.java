package bluetooth;

import java.io.*;
import java.util.Vector;
import javax.bluetooth.*;
import javax.microedition.io.*;

public class BluetoothClient implements DiscoveryListener {

    private Object lock = new Object();

    // vector containing the devices discovered
    private Vector<RemoteDevice> vecDevices = new Vector<RemoteDevice>();

    private String connectionURL = null;

    private RemoteDevice remoteDevice = null;
    private StreamConnection streamConnection = null;
    private OutputStream outStream = null;
    private PrintWriter pWriter = null;
    private InputStream inStream = null;
    private BufferedReader bReader = null;

    public boolean connect(String deviceName, String bluetoothAddress) throws IOException {
        if (remoteDevice == null) {
            // Get local bluetooth device
            LocalDevice localDevice = LocalDevice.getLocalDevice();

            // Find bluetooth devices
            DiscoveryAgent agent = localDevice.getDiscoveryAgent();

            System.out.println("Starting device inquiry");
            // Search for bluetooth-devices
            agent.startInquiry(DiscoveryAgent.GIAC, this);

            // Wait for device search to finish
            try {
                synchronized (lock) {
                    lock.wait(); // Lock is triggered in inquiryCompleted Method
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Device Inquiry Completed. ");

            // Get the amount of found devices
            int deviceCount = vecDevices.size();

            System.out.println("Found " + deviceCount + " devices.");

            if (deviceCount <= 0) { // No devices found
                return false;
            } else { // 1 or more devices found
                remoteDevice = searchDeviceList(vecDevices, deviceName, bluetoothAddress);
            }

            vecDevices.clear();

            if (remoteDevice == null) { // Device not found
                System.out.println("Specified device not found.");
                return false;
            }
            // UUID set with SPP-Service
            UUID[] uuidSet = new UUID[1];
            uuidSet[0] = new UUID("1101", true);

            System.out.println("Searching for  service...");
            // Search for the SPP-Service on the Device
            agent.searchServices(null, uuidSet, remoteDevice, this);

            // Wait for service search to be completed
            try

            {
                synchronized (lock) {
                    lock.wait(); // Lock is triggered in serviceSearchCompleted
                                 // Method
                }
            } catch (

            InterruptedException e)

            {
                e.printStackTrace();
            }

            // Service not found
            if (connectionURL == null)

            {
                System.out.println("Device does not support Simple SPP Service.");
                remoteDevice = null;
                return false;
            }
            System.out.println("Found SPP Service");
            // Open Connection to device
            streamConnection = (StreamConnection) Connector.open(connectionURL);

            // Streamer for Output
            outStream = streamConnection.openOutputStream();
            pWriter = new PrintWriter(new OutputStreamWriter(outStream));

            // Streamer for Input
            inStream = streamConnection.openInputStream();
            bReader = new BufferedReader(new InputStreamReader(inStream));

            return true;

        } else

        { // Already connected
            System.out.println("Already Connected.");
            return false;
        }

    }

    public void write(String message) {
        if (pWriter != null) { // Devcie connected
            pWriter.write(message); // Write a String to the Buffer
            pWriter.flush(); // Send Buffer
        }
    }

    public void write(char[] chars) {
        if (pWriter != null && chars != null) { // Device connected
            pWriter.write(chars); // Write an Array of Chars to the Buffer
            pWriter.flush(); // Send Buffer
        }
    }
    
    public void write(int integer) {
    	if (pWriter != null) { // Device connected
    		pWriter.write(integer);
    		pWriter.flush();
    	}
    }

    public String readAll() {
        if (bReader != null) { // Device connected
            String message = "";
            try {
                while (bReader.ready()) {
                    message += (char) bReader.read(); // Read
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return message;
        } else { // No device connected
            return "";
        }
    }

    public boolean isConnected() {
    	if(remoteDevice != null) {
    		return !pWriter.checkError();
    	} else {
    		return false;
    	}
    }
    
    public void disconnect() throws IOException {
        if (pWriter != null) { // Close PrintWriter
            pWriter.close();
            pWriter = null;
        }
        if (inStream != null) { // Close InputStream
            inStream.close();
            inStream = null;
        }
        if (bReader != null) { // Close BufferedReader
            bReader.close();
            bReader = null;
        }
        if (outStream != null) { // Close OutputStream
            outStream.close();
            outStream = null;
        }
        if (streamConnection != null) { // Close StreamConnection
            streamConnection.close();
            streamConnection = null;
        }
        remoteDevice = null; // Destroy RemoteDevice
    }

    @Override
    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
        // add the device to the vector
        if (!vecDevices.contains(btDevice)) {
            vecDevices.addElement(btDevice);
        }
    }

    @Override
    public void inquiryCompleted(int arg0) {
        synchronized (lock) {
            lock.notify();
        }
    }

    @Override
    public void serviceSearchCompleted(int arg0, int arg1) {
        synchronized (lock) {
            lock.notify();
        }
    }

    @Override
    public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
        // Service found
        if (servRecord != null && servRecord.length > 0) {
            connectionURL = servRecord[0].getConnectionURL(0, false);
        }
        synchronized (lock) {
            lock.notify();
        }
    }

    private RemoteDevice searchDeviceList(Vector<RemoteDevice> vecDevices, String deviceName, String bluetoothAddress)
            throws IOException {
        // For every device
        int deviceCount = vecDevices.size();
        for (int i = 0; i < deviceCount; i++) {
            // Get current device from vector
            RemoteDevice currentDevice = (RemoteDevice) vecDevices.elementAt(i);
            // If there is no bluetooth address given
            if (bluetoothAddress == "") {
                String currentDeviceName = currentDevice.getFriendlyName(false);
                System.out.println("Found Device " + currentDeviceName);
                if (currentDeviceName.equals(deviceName)) {
                    System.out.println("Device found");
                    return currentDevice;
                }
            } else { // There is a bluetooth address given
                String currentDeviceBluetoothAddress = currentDevice.getBluetoothAddress();
                System.out.println("Found Device " + currentDeviceBluetoothAddress);
                if (currentDeviceBluetoothAddress.equals(bluetoothAddress)) {
                    System.out.println("Device found");
                    return currentDevice;
                }
            }
        }
        return null;
    }
}
