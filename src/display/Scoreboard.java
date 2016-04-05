package display;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import master.Globals;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeUnit;

public class Scoreboard {

	private int height = 576;
	private int width = 720;

	// Window
	private JFrame window = new JFrame();
	// Panes
	private JPanel Pane_Spieler2 = new JPanel();
	private JPanel Pane_Spieler1 = new JPanel();
	private JPanel Pane_General = new JPanel();
	// Labels
	private JLabel titelSpieler1 = new JLabel("Spieler 1");
	private JLabel toreSpieler1 = new JLabel("<Tor1>");
	private JLabel titelSpieler2 = new JLabel("Spieler 2");
	private JLabel toreSpieler2 = new JLabel("<Tor2>");
	private JLabel spielzeit = new JLabel("<Spielzeit>");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Scoreboard window = new Scoreboard();
					window.window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Scoreboard() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Create Window
		window.setTitle("GUI for Car Arena");
		window.setBounds(0, 0, width, height + 40);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(null);
		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Globals.stop = true;
			}
		});

		// Create Pane for Player1
		Pane_Spieler1.setBackground(Colors.getColor(Colors.WHITE));
		Pane_Spieler1.setBounds(0, 0, width / 2, height / 4 * 3);
		window.getContentPane().add(Pane_Spieler1);
		Pane_Spieler1.setLayout(null);

		// Create Title for Player1
		titelSpieler1.setFont(new Font("Tahoma", Font.PLAIN, height / 4 - 80));
		titelSpieler1.setBounds(0, 0, width / 2, height / 4);
		titelSpieler1.setVerticalAlignment(SwingConstants.CENTER);
		titelSpieler1.setHorizontalAlignment(SwingConstants.CENTER);
		Pane_Spieler1.add(titelSpieler1);

		// Create Goal-Count for Player1
		toreSpieler1.setFont(new Font("Tahoma", Font.PLAIN, height / 2 - 120));
		toreSpieler1.setBounds(0, height / 4 /*+ 1*/, width / 2, height / 2);
		toreSpieler1.setVerticalAlignment(SwingConstants.CENTER);
		toreSpieler1.setHorizontalAlignment(SwingConstants.CENTER);
		Pane_Spieler1.add(toreSpieler1);

		// Create Pane for Player2
		Pane_Spieler2.setLayout(null);
		Pane_Spieler2.setBackground(Colors.getColor(Colors.WHITE));
		Pane_Spieler2.setBounds(width / 2/* + 1*/, 0, width / 2, height / 4 * 3);
		window.getContentPane().add(Pane_Spieler2);

		// Create Title for Player2
		titelSpieler2.setFont(new Font("Tahoma", Font.PLAIN, height / 4 - 80));
		titelSpieler2.setBounds(0, 0, width / 2, height / 4);
		titelSpieler2.setVerticalAlignment(SwingConstants.CENTER);
		titelSpieler2.setHorizontalAlignment(SwingConstants.CENTER);
		Pane_Spieler2.add(titelSpieler2);

		// Create Goal-Count for Player2
		toreSpieler2.setFont(new Font("Tahoma", Font.PLAIN, height / 2 - 120));
		toreSpieler2.setBounds(0, height / 4 /*+ 1*/, width / 2, height / 2);
		toreSpieler2.setVerticalAlignment(SwingConstants.CENTER);
		toreSpieler2.setHorizontalAlignment(SwingConstants.CENTER);
		Pane_Spieler2.add(toreSpieler2);

		// Create Pane for Play-Time
		Pane_General.setBackground(Colors.getColor(Colors.WHITE));
		Pane_General.setBounds(0, height / 4 * 3 /*+ 1*/, width, height / 4);
		window.getContentPane().add(Pane_General);
		Pane_General.setLayout(null);

		// Create Label for Play-Time
		spielzeit.setFont(new Font("Tahoma", Font.PLAIN, height / 4 - 60));
		spielzeit.setBounds(0, 0, width, height / 4);
		spielzeit.setVerticalAlignment(SwingConstants.CENTER);
		spielzeit.setHorizontalAlignment(SwingConstants.CENTER);
		Pane_General.add(spielzeit);

		// Update Timer
		ActionListener updateLabels = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				toreSpieler1.setText(Integer.toString(Globals.toreSpieler1));
				toreSpieler2.setText(Integer.toString(Globals.toreSpieler2));
				Pane_Spieler1.setBackground(Colors.getColor(Globals.mBotSpieler1));
				Pane_Spieler2.setBackground(Colors.getColor(Globals.mBotSpieler2));
				if (Globals.spielLaeuft || System.currentTimeMillis() % 1500 > 750) {
					spielzeit.setText(String.format("%02d:%02d",
						TimeUnit.MILLISECONDS.toMinutes(Globals.spielzeit),
						TimeUnit.MILLISECONDS.toSeconds(Globals.spielzeit)
						 - TimeUnit.MINUTES.toSeconds(
						TimeUnit.MILLISECONDS.toMinutes(Globals.spielzeit))));
				} else {
					spielzeit.setText(String.format("%02d %02d",
						TimeUnit.MILLISECONDS.toMinutes(Globals.spielzeit),
						TimeUnit.MILLISECONDS.toSeconds(Globals.spielzeit)
						 - TimeUnit.MINUTES.toSeconds(
						TimeUnit.MILLISECONDS.toMinutes(Globals.spielzeit))));
				};
			}
		};
		Timer updateTimer = new Timer(250, updateLabels);
		updateTimer.start();
		
		window.setVisible(true);
	}
	
	public void closeWindow() {
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}
}
