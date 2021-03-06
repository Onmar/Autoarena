package master;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

import ablaufsteuerung.Ablaufsteuerung;
import ablaufsteuerung.ZustaendeBall;
import display.Scoreboard;
import mBot.MBotSteuerung;
import java.awt.FlowLayout;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import io.IOHandler;
import io.LagerPosition;
import mBot.ZustaendeSteuerung;
import ablaufsteuerung.ZustaendeSpiel;
import javax.swing.JCheckBox;
import javax.swing.JButton;

public class GUIMain {

	private JFrame frame;

	/** 
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIMain window = new GUIMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
				}
			}
		});
		
		Thread scoreboard = new Thread() {
			public void run() {
				Scoreboard window = new Scoreboard();
				window.setVisible(true);
				while (!Globals.stop) {
				}
			}
		};

		Thread ablaufsteuerung = new Thread() {
			public void run() {
				Ablaufsteuerung.init();
				while(!Globals.stop) {
					Ablaufsteuerung.run();
					try {
						TimeUnit.MILLISECONDS.sleep(20);
					} catch (InterruptedException e) {
					}
				}
				Ablaufsteuerung.close();
				
			}
		};

		Thread mBotSteuerung = new Thread() {
			public void run() {
				MBotSteuerung.init();
				while(!Globals.stop) {
					MBotSteuerung.run();
					try {
						TimeUnit.MILLISECONDS.sleep(20);
					} catch (InterruptedException e) {
					}
				}
				MBotSteuerung.close();
			}
		};

		scoreboard.start();
		ablaufsteuerung.start();
		mBotSteuerung.start();

		while (!Globals.stop) {
		}
		
	}

	/**
	 * Create the application.
	 */
	public GUIMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(
				new FlowLayout(FlowLayout.CENTER, 5, 5));

		JPanel zustaende = new JPanel();
		frame.getContentPane().add(zustaende);

		final JComboBox<LagerPosition> lagerPosition = new JComboBox<LagerPosition>();
		lagerPosition.setEnabled(false);
		lagerPosition.setModel(new DefaultComboBoxModel<LagerPosition>(
				LagerPosition.values()));
		zustaende.add(lagerPosition);

		final JComboBox<ZustaendeSteuerung> zustandSteuerung = new JComboBox<ZustaendeSteuerung>();
		zustandSteuerung.setEnabled(false);
		zustandSteuerung.setModel(new DefaultComboBoxModel<ZustaendeSteuerung>(
				ZustaendeSteuerung.values()));
		zustaende.add(zustandSteuerung);

		final JComboBox<ZustaendeSpiel> zustandSpiel = new JComboBox<ZustaendeSpiel>();
		zustandSpiel.setEnabled(false);
		zustandSpiel.setModel(new DefaultComboBoxModel<ZustaendeSpiel>(
				ZustaendeSpiel.values()));
		zustaende.add(zustandSpiel);

		final JComboBox<ZustaendeBall> zustandBall = new JComboBox<ZustaendeBall>();
		zustandBall.setEnabled(false);
		zustandBall.setModel(new DefaultComboBoxModel<ZustaendeBall>(
				ZustaendeBall.values()));
		zustaende.add(zustandBall);

		JPanel globals = new JPanel();
		frame.getContentPane().add(globals);

		final JCheckBox ausparken = new JCheckBox("Ausparken");
		ausparken.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Globals.ausparken = ausparken.isSelected();
			}
		});
		globals.add(ausparken);

		final JCheckBox einparken = new JCheckBox("Einparken");
		einparken.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Globals.einparken = einparken.isSelected();
			}
		});
		globals.add(einparken);

		final JCheckBox bereit = new JCheckBox("mBotsBereit");
		bereit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Globals.mBotsBereit = bereit.isSelected();
			}
		});
		globals.add(bereit);

		final JCheckBox geparkt = new JCheckBox("mBotsGeparkt");
		geparkt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Globals.mBotsGeparkt = geparkt.isSelected();
			}
		});
		globals.add(geparkt);

		JButton stop = new JButton("StopProgram");
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Globals.einparken = true;
			}
		});
		frame.getContentPane().add(stop);

		ActionListener update = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				lagerPosition.setSelectedItem(IOHandler
						.getLadeBoxSollPosition());
				zustandSteuerung.setSelectedItem(MBotSteuerung
						.getMBotControlState());
				zustandSpiel.setSelectedItem(Ablaufsteuerung.getGameState());
				zustandBall.setSelectedItem(Ablaufsteuerung.getBallState());
				ausparken.setSelected(Globals.ausparken);
				einparken.setSelected(Globals.einparken);
				bereit.setSelected(Globals.mBotsBereit);
				geparkt.setSelected(Globals.mBotsGeparkt);
			}
		};
		Timer updateTimer = new Timer(100, update);
		updateTimer.start();
	}

}
