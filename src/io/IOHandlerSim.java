package io;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;

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
		IOHandler.init(false);
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

		JCheckBox spieler1_NewGame = new JCheckBox("NewGame");
		spieler1_NewGame.setEnabled(false);
		spieler1.add(spieler1_NewGame);

		JCheckBox spieler1_Tor = new JCheckBox("Tor");
		spieler1_Tor.setEnabled(false);
		spieler1.add(spieler1_Tor);

		JSpinner spieler1_JoystickL = new JSpinner();
		spieler1_JoystickL.setEnabled(false);
		spieler1_JoystickL.setToolTipText("spieler1_JoystickL");
		spieler1.add(spieler1_JoystickL);

		JSpinner spieler1_JoystickR = new JSpinner();
		spieler1_JoystickR.setEnabled(false);
		spieler1_JoystickR.setToolTipText("spieler1_JoystickR");
		spieler1.add(spieler1_JoystickR);

		JPanel spieler2 = new JPanel();
		frame.getContentPane().add(spieler2);

		JLabel spieler2_Title = new JLabel("Spieler2");
		spieler2.add(spieler2_Title);

		JCheckBox spieler2_NewGame = new JCheckBox("NewGame");
		spieler2_NewGame.setEnabled(false);
		spieler2.add(spieler2_NewGame);

		JCheckBox spieler2_Tor = new JCheckBox("Tor");
		spieler2_Tor.setEnabled(false);
		spieler2.add(spieler2_Tor);

		JSpinner spieler2_JoystickL = new JSpinner();
		spieler2_JoystickL.setEnabled(false);
		spieler2_JoystickL.setToolTipText("spieler2_JoystickL");
		spieler2.add(spieler2_JoystickL);

		JSpinner spieler2_JoystickR = new JSpinner();
		spieler2_JoystickR.setEnabled(false);
		spieler2_JoystickR.setToolTipText("spieler2_JoystickR");
		spieler2.add(spieler2_JoystickR);

		JPanel ladeBoxMot = new JPanel();
		frame.getContentPane().add(ladeBoxMot);

		JLabel ladeBoxMotor_Title = new JLabel("LadeBox_Position");
		ladeBoxMot.add(ladeBoxMotor_Title);

		JCheckBox ladeBoxMot_PosReached = new JCheckBox("Position Reached");
		ladeBoxMot_PosReached.setEnabled(false);
		ladeBoxMot.add(ladeBoxMot_PosReached);

		NumberFormat integerFormat = NumberFormat.getIntegerInstance();
		JFormattedTextField ladeBoxMot_Pos = new JFormattedTextField(integerFormat);
		ladeBoxMot_Pos.setColumns(10);
		ladeBoxMot_Pos.setValue(new Integer(0));
		ladeBoxMot.add(ladeBoxMot_Pos);

		JButton ladeBoxMot_GoToPos = new JButton("GoTo Position");
		ladeBoxMot_GoToPos.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				IOHandler.setLadeBoxSollPosition(((Number)ladeBoxMot_Pos.getValue()).intValue());
			}
		});
		ladeBoxMot.add(ladeBoxMot_GoToPos);

		JPanel ladeBoxSensor = new JPanel();
		frame.getContentPane().add(ladeBoxSensor);

		JLabel ladeBoxSensor_Title = new JLabel("LadeBox_SensorHinten");
		ladeBoxSensor.add(ladeBoxSensor_Title);

		JCheckBox ladeBoxSensor_mBot1 = new JCheckBox("mBot1");
		ladeBoxSensor_mBot1.setEnabled(false);
		ladeBoxSensor.add(ladeBoxSensor_mBot1);

		JCheckBox ladeBoxSensor_mBot2 = new JCheckBox("mBot2");
		ladeBoxSensor_mBot2.setEnabled(false);
		ladeBoxSensor.add(ladeBoxSensor_mBot2);

		JCheckBox ladeBoxSensor_mBot3 = new JCheckBox("mBot3");
		ladeBoxSensor_mBot3.setEnabled(false);
		ladeBoxSensor.add(ladeBoxSensor_mBot3);

		JCheckBox ladeBoxSensor_Tor = new JCheckBox("Tor");
		ladeBoxSensor_Tor.setEnabled(false);
		ladeBoxSensor.add(ladeBoxSensor_Tor);

		JPanel ball = new JPanel();
		frame.getContentPane().add(ball);

		JLabel ball_Title = new JLabel("ballRueckfuehrung");
		ball.add(ball_Title);

		JButton ball_Motor = new JButton("Motor");
		ball_Motor.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				IOHandler.startBallMotor();
			}
			
			public void mouseReleased(MouseEvent e) {
				IOHandler.stopBallMotor();
			}
		});
		ball.add(ball_Motor);

		JCheckBox ball_Sensor = new JCheckBox("Sensor");
		ball_Sensor.setEnabled(false);
		ball.add(ball_Sensor);
		
		Thread update = new Thread() {
			@Override
			public void run() {
				spieler1_NewGame.setSelected(IOHandler.spieler1_NeuerBall);
				spieler1_Tor.setSelected(IOHandler.spieler1_Tor);
				spieler2_NewGame.setSelected(IOHandler.spieler2_NeuerBall);
				spieler2_NewGame.setSelected(IOHandler.spieler2_Tor);
				spieler1_JoystickL.setValue(IOHandler.spieler1_JoystickL);
				spieler1_JoystickR.setValue(IOHandler.spieler1_JoystickR);
				spieler2_JoystickL.setValue(IOHandler.spieler2_JoystickL);
				spieler2_JoystickR.setValue(IOHandler.spieler2_JoystickR);
				
				ladeBoxMot_PosReached.setSelected(IOHandler.positionReached());
				
				ladeBoxSensor_mBot1.setSelected(IOHandler.ladeBoxen_Sensoren[0]);
				ladeBoxSensor_mBot2.setSelected(IOHandler.ladeBoxen_Sensoren[1]);
				ladeBoxSensor_mBot3.setSelected(IOHandler.ladeBoxen_Sensoren[2]);
				ladeBoxSensor_Tor.setSelected(IOHandler.ladeBoxen_SensorTor);
				
				ball_Sensor.setSelected(IOHandler.ball_BallEingeworfen);
				
				try {
					TimeUnit.MILLISECONDS.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		update.start();
	}

}
