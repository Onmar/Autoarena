package io;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.Timer;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;

public class IOHandlerSim {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IOHandlerSim window = new IOHandlerSim();
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
	public IOHandlerSim() {
		IOHandler.init(true);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 720, 568);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		JPanel spieler1 = new JPanel();
		frame.getContentPane().add(spieler1);

		JLabel spieler1_Title = new JLabel("Spieler1");
		spieler1.add(spieler1_Title);

		final JCheckBox spieler1_NewGame = new JCheckBox("NewGame");
		spieler1_NewGame.setEnabled(false);
		spieler1.add(spieler1_NewGame);

		final JCheckBox spieler1_Tor = new JCheckBox("Tor");
		spieler1_Tor.setEnabled(false);
		spieler1.add(spieler1_Tor);

		final JSpinner spieler1_JoystickL = new JSpinner();
		spieler1_JoystickL.setEnabled(false);
		spieler1_JoystickL.setToolTipText("spieler1_JoystickL");
		spieler1_JoystickL.setSize(300, 20);
		spieler1.add(spieler1_JoystickL);

		final JSpinner spieler1_JoystickR = new JSpinner();
		spieler1_JoystickR.setEnabled(false);
		spieler1_JoystickR.setToolTipText("spieler1_JoystickR");
		spieler1_JoystickR.setSize(300, 20);
		spieler1.add(spieler1_JoystickR);

		final JPanel spieler2 = new JPanel();
		frame.getContentPane().add(spieler2);

		final JLabel spieler2_Title = new JLabel("Spieler2");
		spieler2.add(spieler2_Title);

		final JCheckBox spieler2_NewGame = new JCheckBox("NewGame");
		spieler2_NewGame.setEnabled(false);
		spieler2.add(spieler2_NewGame);

		final JCheckBox spieler2_Tor = new JCheckBox("Tor");
		spieler2_Tor.setEnabled(false);
		spieler2.add(spieler2_Tor);

		final JSpinner spieler2_JoystickL = new JSpinner();
		spieler2_JoystickL.setEnabled(false);
		spieler2_JoystickL.setToolTipText("spieler2_JoystickL");
		spieler2_JoystickL.setSize(300, 20);
		spieler2.add(spieler2_JoystickL);

		final JSpinner spieler2_JoystickR = new JSpinner();
		spieler2_JoystickR.setEnabled(false);
		spieler2_JoystickR.setToolTipText("spieler2_JoystickR");
		spieler2_JoystickR.setSize(300, 20);
		spieler2.add(spieler2_JoystickR);

		final JPanel ladeBoxMot = new JPanel();
		frame.getContentPane().add(ladeBoxMot);

		final JLabel ladeBoxMotor_Title = new JLabel("LadeBox_Position");
		ladeBoxMot.add(ladeBoxMotor_Title);

		final JCheckBox ladeBoxMot_PosReached = new JCheckBox("Position Reached");
		ladeBoxMot_PosReached.setEnabled(false);
		ladeBoxMot.add(ladeBoxMot_PosReached);

		NumberFormat integerFormat = NumberFormat.getIntegerInstance();
		final JFormattedTextField ladeBoxMot_Pos = new JFormattedTextField(integerFormat);
		ladeBoxMot_Pos.setColumns(10);
		ladeBoxMot_Pos.setValue(new Integer(0));
		ladeBoxMot.add(ladeBoxMot_Pos);

		final JButton ladeBoxMot_GoToPos = new JButton("GoTo Position");
		ladeBoxMot_GoToPos.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				IOHandler.setLadeBoxSollPosition(((Number)ladeBoxMot_Pos.getValue()).intValue());
			}
		});
		ladeBoxMot.add(ladeBoxMot_GoToPos);

		final JPanel ladeBoxSensor = new JPanel();
		frame.getContentPane().add(ladeBoxSensor);

		final JLabel ladeBoxSensor_Title = new JLabel("LadeBox_SensorHinten");
		ladeBoxSensor.add(ladeBoxSensor_Title);

		final JCheckBox ladeBoxSensor_mBot1 = new JCheckBox("mBot1");
		ladeBoxSensor_mBot1.setEnabled(false);
		ladeBoxSensor.add(ladeBoxSensor_mBot1);

		final JCheckBox ladeBoxSensor_mBot2 = new JCheckBox("mBot2");
		ladeBoxSensor_mBot2.setEnabled(false);
		ladeBoxSensor.add(ladeBoxSensor_mBot2);

		final JCheckBox ladeBoxSensor_mBot3 = new JCheckBox("mBot3");
		ladeBoxSensor_mBot3.setEnabled(false);
		ladeBoxSensor.add(ladeBoxSensor_mBot3);

		final JCheckBox ladeBoxSensor_Tor = new JCheckBox("Tor");
		ladeBoxSensor_Tor.setEnabled(false);
		ladeBoxSensor.add(ladeBoxSensor_Tor);

		final JPanel ball = new JPanel();
		frame.getContentPane().add(ball);

		final JLabel ball_Title = new JLabel("ballRueckfuehrung");
		ball.add(ball_Title);

		final JButton ball_Motor = new JButton("Motor");
		ball_Motor.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				IOHandler.startBallMotor();
			}
			
			public void mouseReleased(MouseEvent e) {
				IOHandler.stopBallMotor();
			}
		});
		ball.add(ball_Motor);

		final JCheckBox ball_Sensor = new JCheckBox("Sensor");
		ball_Sensor.setEnabled(false);
		ball.add(ball_Sensor);
		
		ActionListener updateAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				spieler1_NewGame.setSelected(IOHandler.spieler1_NeuerBall);
				spieler1_Tor.setSelected(IOHandler.spieler1_Tor);
				spieler2_NewGame.setSelected(IOHandler.spieler2_NeuerBall);
				spieler2_Tor.setSelected(IOHandler.spieler2_Tor);
				spieler1_JoystickL.setValue(IOHandler.spieler1_JoystickL);
				spieler1_JoystickR.setValue(IOHandler.spieler1_JoystickR);
				spieler2_JoystickL.setValue(IOHandler.spieler2_JoystickL);
				spieler2_JoystickR.setValue(IOHandler.spieler2_JoystickR);
				System.out.println(IOHandler.spieler1_JoystickL + " " + IOHandler.spieler1_JoystickR + " " + IOHandler.spieler2_JoystickL + " " + IOHandler.spieler2_JoystickR);
				
				ladeBoxMot_PosReached.setSelected(IOHandler.positionReached());
				
				ladeBoxSensor_mBot1.setSelected(IOHandler.ladeBoxen_Sensoren[0]);
				ladeBoxSensor_mBot2.setSelected(IOHandler.ladeBoxen_Sensoren[1]);
				ladeBoxSensor_mBot3.setSelected(IOHandler.ladeBoxen_Sensoren[2]);
				ladeBoxSensor_Tor.setSelected(IOHandler.ladeBoxen_SensorTor);
				
				ball_Sensor.setSelected(IOHandler.ball_BallEingeworfen);
			}
		};
		Timer update = new Timer(100, updateAction);
		update.start();
	}

}
