package io;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.BoxLayout;

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

		JButton spieler1_NewGame = new JButton("NewGame");
		spieler1.add(spieler1_NewGame);

		JButton spieler1_Tor = new JButton("Tor");
		spieler1.add(spieler1_Tor);

		JSpinner spieler1_JoystickL = new JSpinner();
		spieler1_JoystickL.setToolTipText("spieler1_JoystickL");
		spieler1_JoystickL.setModel(
				new SpinnerNumberModel(0.0, /* -IOHandler.joystickAmplitude / 2 */ -512.0, /* IOHandler.joystickAmplitude / 2 */ 512.0, 1));
		spieler1.add(spieler1_JoystickL);

		JSpinner spieler1_JoystickR = new JSpinner();
		spieler1_JoystickR.setToolTipText("spieler1_JoystickR");
		spieler1_JoystickR.setModel(
				new SpinnerNumberModel(0.0, /* -IOHandler.joystickAmplitude / 2 */ -512.9, /* IOHandler.joystickAmplitude / 2 */ 512.0, 1));
		spieler1.add(spieler1_JoystickR);

		JPanel spieler2 = new JPanel();
		frame.getContentPane().add(spieler2);

		JLabel spieler2_Title = new JLabel("Spieler2");
		spieler2.add(spieler2_Title);

		JButton spieler2_NewGame = new JButton("NewGame");
		spieler2.add(spieler2_NewGame);

		JButton spieler2_Tor = new JButton("Tor");
		spieler2.add(spieler2_Tor);

		JSpinner spieler2_JoystickL = new JSpinner();
		spieler2_JoystickL.setToolTipText("spieler2_JoystickL");
		spieler2_JoystickL.setModel(
				new SpinnerNumberModel(0.0, /* -IOHandler.joystickAmplitude / 2 */ -512.0, /* IOHandler.joystickAmplitude / 2 */ 512.0, 1.0));
		spieler2.add(spieler2_JoystickL);

		JSpinner spieler2_JoystickR = new JSpinner();
		spieler2_JoystickR.setToolTipText("spieler2_JoystickR");
		spieler2_JoystickR.setModel(
				new SpinnerNumberModel(0.0, /* -IOHandler.joystickAmplitude / 2 */ -512.0, /* IOHandler.joystickAmplitude / 2 */ 512.0, 1.0));
		spieler2.add(spieler2_JoystickR);

		JPanel ladeBoxPos = new JPanel();
		frame.getContentPane().add(ladeBoxPos);

		JLabel ladeBoxPos_Title = new JLabel("LadeBox_Position");
		ladeBoxPos.add(ladeBoxPos_Title);

		JComboBox<LagerPosition> ladeBoxPos_SollPosition = new JComboBox<LagerPosition>();
		ladeBoxPos_SollPosition.setModel(new DefaultComboBoxModel<LagerPosition>(LagerPosition.values()));
		ladeBoxPos_SollPosition.setSelectedIndex(0);
		ladeBoxPos.add(ladeBoxPos_SollPosition);

		JComboBox<LagerPosition> ladeBoxPos_IstPositition = new JComboBox<LagerPosition>();
		ladeBoxPos_IstPositition.setEnabled(false);
		ladeBoxPos_IstPositition.setModel(new DefaultComboBoxModel<LagerPosition>(LagerPosition.values()));
		ladeBoxPos_IstPositition.setSelectedIndex(0);
		ladeBoxPos.add(ladeBoxPos_IstPositition);
		
		JCheckBox ladeBoxPos_MotorL = new JCheckBox("MotorL");
		ladeBoxPos_MotorL.setEnabled(false);
		ladeBoxPos.add(ladeBoxPos_MotorL);
		
		JCheckBox ladeBoxPos_MotorR = new JCheckBox("MotorR");
		ladeBoxPos_MotorR.setEnabled(false);
		ladeBoxPos.add(ladeBoxPos_MotorR);

		JPanel ladeBoxSensor = new JPanel();
		frame.getContentPane().add(ladeBoxSensor);

		JLabel ladeBoxSensor_Title = new JLabel("LadeBox_SensorHinten");
		ladeBoxSensor.add(ladeBoxSensor_Title);

		JButton ladeBoxSensor_mBot1 = new JButton("mBot1");
		ladeBoxSensor.add(ladeBoxSensor_mBot1);

		JButton ladeBoxSensor_mBot2 = new JButton("mBot2");
		ladeBoxSensor.add(ladeBoxSensor_mBot2);

		JButton ladeBoxSensor_mBot3 = new JButton("mBot3");
		ladeBoxSensor.add(ladeBoxSensor_mBot3);

		JPanel ladeBoxTor = new JPanel();
		frame.getContentPane().add(ladeBoxTor);

		JLabel ladeBoxTor_Title = new JLabel("LadeBox_TorSensor");
		ladeBoxTor.add(ladeBoxTor_Title);

		JButton ladeBoxTor_mBot1 = new JButton("mBot1");
		ladeBoxTor.add(ladeBoxTor_mBot1);

		JButton ladeBoxTor_mBot2 = new JButton("mBot2");
		ladeBoxTor.add(ladeBoxTor_mBot2);

		JButton ladeBoxTor_mBot3 = new JButton("mBot3");
		ladeBoxTor.add(ladeBoxTor_mBot3);

		JPanel ball = new JPanel();
		frame.getContentPane().add(ball);

		JLabel ball_Title = new JLabel("ballRueckfuehrung");
		ball.add(ball_Title);

		JCheckBox ball_Motor = new JCheckBox("Motor");
		ball_Motor.setEnabled(false);
		ball.add(ball_Motor);

		JButton ball_BallEingeworfen = new JButton("BallEingeworfen");
		ball.add(ball_BallEingeworfen);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
	}

}
