package master;

import java.awt.*;
import javax.swing.*;

import mBot.MBot;
import mBot.MotorDirection;
import mBot.States;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.charset.Charset;

public class MainGUI {

    private JFrame frame;
    /*
     * Bluetooth Addresses of mBots: 
     * mBot1: 000502031DD3 
     * mBot2: 
     * mBot3:
     */
    private static String[] bluetoothAddresses = new String[] { "000502031DD3", "Dummy", "Dummy" };
    private static MBot[] mBots = new MBot[] { null, null, null };

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
    	System.out.println(Charset.defaultCharset().name());
        for (int i = 0; i < mBots.length && i < bluetoothAddresses.length; i++) {
            mBots[i] = new MBot(bluetoothAddresses[i]);
            mBots[i].synch();
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainGUI window = new MainGUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public MainGUI() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame("MainGUI for mBot");
        frame.setBounds(100, 100, 350, 230);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JComboBox<String> comboBox = new JComboBox<String>();

        // Control Panel
        JPanel controlPanel = new JPanel();
        final JButton connect = new JButton("Connect");
        JButton sync = new JButton("Sync");
        JButton lineFollow = new JButton("Line Follow");
        JButton lineSearch = new JButton("Line Search");
        JButton parking = new JButton("Parking");
        JButton stop = new JButton("Stop");

        // Steering Panel
        JPanel steeringPanel = new JPanel();
        JButton up = new JButton("Up");
        JButton left = new JButton("Left");
        JButton right = new JButton("Right");
        JButton down = new JButton("Down");

        // ComboBox to select the current mBot.
        comboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "mBot1", "mBot2", "mBot3" }));
        comboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (mBots[comboBox.getSelectedIndex()].isConnected()) {
                    connect.setText("Disconnect");
                } else {
                    connect.setText("Connect");
                }
            }
        });
        // Set Start Text
        if (mBots[comboBox.getSelectedIndex()].isConnected()) {
            connect.setText("Disconnect");
        } else {
            connect.setText("Connect");
        }

        // Panel with Control Buttons
        FlowLayout fl_controlPanel = (FlowLayout) controlPanel.getLayout();
        fl_controlPanel.setVgap(15);
        fl_controlPanel.setHgap(15);

        // Connect Button
        // Action Performed on Click
        connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (mBots[comboBox.getSelectedIndex()].isConnected()) {
                    mBots[comboBox.getSelectedIndex()].disconnect();
                } else {
                    mBots[comboBox.getSelectedIndex()].connect();
                    mBots[comboBox.getSelectedIndex()].synch();
                }
                if (mBots[comboBox.getSelectedIndex()].isConnected()) {
                    connect.setText("Disconnect");
                } else {
                    connect.setText("Connect");
                }
            }
        });

        // Sync Button
        sync.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mBots[comboBox.getSelectedIndex()].synch();
            }
        });
        // Line Follow
        lineFollow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mBots[comboBox.getSelectedIndex()].sendCommand(States.LINE_FOLLOW, 200, 200, MotorDirection.FORWARD);
            }
        });

        // Line Search
        lineSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mBots[comboBox.getSelectedIndex()].sendCommand(States.LINE_SEARCH, 200, 200, MotorDirection.FORWARD);
            }
        });

        // Parking
        parking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mBots[comboBox.getSelectedIndex()].sendCommand(States.PARKING, 200, 200, MotorDirection.FORWARD);
            }
        });

        // Stop
        stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mBots[comboBox.getSelectedIndex()].sendCommand(States.STOP, 200, 200, MotorDirection.STOP);
            }
        });

        // Panel with Steering Buttons
        FlowLayout flowLayout = (FlowLayout) steeringPanel.getLayout();
        flowLayout.setVgap(15);
        flowLayout.setHgap(20);

        // Up Button
        up.addMouseListener(new MouseAdapter() { // OnClick Action
            public void mousePressed(MouseEvent e) {
                mBots[comboBox.getSelectedIndex()].sendCommand(States.DRIVE, 200, 200, MotorDirection.FORWARD);
            }

            public void mouseReleased(MouseEvent e) {
                mBots[comboBox.getSelectedIndex()].sendCommand(States.STOP, 0, 0, MotorDirection.STOP);
            }
        });
        // Left Button
        left.addMouseListener(new MouseAdapter() { // OnClick Action
            public void mousePressed(MouseEvent e) {
                mBots[comboBox.getSelectedIndex()].sendCommand(States.DRIVE, 100, 100, MotorDirection.LEFT);
            }

            public void mouseReleased(MouseEvent e) {
                mBots[comboBox.getSelectedIndex()].sendCommand(States.STOP, 0, 0, MotorDirection.STOP);
            }
        });

        // Right Button
        right.addMouseListener(new MouseAdapter() { // OnClick Action
            public void mousePressed(MouseEvent e) {
                mBots[comboBox.getSelectedIndex()].sendCommand(States.DRIVE, 100, 100, MotorDirection.RIGHT);
            }

            public void mouseReleased(MouseEvent e) {
                mBots[comboBox.getSelectedIndex()].sendCommand(States.STOP, 0, 0, MotorDirection.STOP);
            }
        });

        // Down Button
        down.addMouseListener(new MouseAdapter() { // OnClick Action
            public void mousePressed(MouseEvent e) {
                mBots[comboBox.getSelectedIndex()].sendCommand(States.DRIVE, 200, 200, MotorDirection.BACKWARD);
            }

            public void mouseReleased(MouseEvent e) {
                mBots[comboBox.getSelectedIndex()].sendCommand(States.STOP, 0, 0, MotorDirection.STOP);
            }
        });

        frame.getContentPane().add(comboBox, BorderLayout.NORTH);
        controlPanel.add(sync);
        controlPanel.add(lineFollow);
        controlPanel.add(lineSearch);
        controlPanel.add(parking);
        controlPanel.add(stop);
        controlPanel.add(connect);
        frame.getContentPane().add(controlPanel, BorderLayout.CENTER);
        steeringPanel.add(up);
        steeringPanel.add(left);
        steeringPanel.add(right);
        steeringPanel.add(down);
        frame.getContentPane().add(steeringPanel, BorderLayout.SOUTH);
    }

}
