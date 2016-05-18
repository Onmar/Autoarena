package display;

import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import ablaufsteuerung.ZustaendeBall;
import ablaufsteuerung.ZustaendeSpiel;
import master.Globals;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Scoreboard {

	private Toolkit tk = Toolkit.getDefaultToolkit();

	private int height = (int) tk.getScreenSize().getHeight();
	private int width = (int) tk.getScreenSize().getWidth();

	private static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
	// Window
	private JFrame window = new JFrame();
	// Layered Pane
	private JLayeredPane lpane = new JLayeredPane();
	// Panes
	private JPanel pane_Spieler2 = new JPanel();
	private JPanel pane_Spieler1 = new JPanel();
	private JPanel pane_General = new JPanel();
	private JPanel pane_GameStart = new JPanel();
	private JPanel pane_NoBall = new JPanel();
	// Labels
	private JLabel titelSpieler1 = new JLabel("Spieler 1");
	private JLabel toreSpieler1 = new JLabel("<Tor1>");
	private JLabel titelSpieler2 = new JLabel("Spieler 2");
	private JLabel toreSpieler2 = new JLabel("<Tor2>");
	private JLabel spielzeit_Minutes = new JLabel("<Spielzeit_Minuten>");
	private JLabel spielzeit_Seconds = new JLabel("<Spielzeit_Sekunden>");
	private JLabel spielzeit_Middle = new JLabel(":");
	private JLabel gameStart_Notice = new JLabel(
			"<html><p align=\"center\">Joystick-Knöpfe drücken um das Spiel zu starten</p></html>");
	private JLabel gameStart_JoystickPicture = new JLabel();
	private JLabel noBall_Notice = new JLabel(
			"<html><p align=\"center\">Joystick-Knöpfe drücken um einen Ball einzuwerfen</p></html>");
	private JLabel noBall_JoystickPicture = new JLabel();

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
		// Read Joystick Picture
		BufferedImage joystickPicture;
		try {
			joystickPicture = ImageIO.read(getClass().getResourceAsStream("/media/pictures/JoystickWithArrow.jpg"));
		} catch (IOException e) {
			joystickPicture = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		}
		
		// Create Window
		window.setTitle("GUI for Car Arena");
		window.setBounds(0, 0, width, height);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(null);
		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Globals.stop = true;
			}
		});

		{
			// Layered Pane settings
			lpane.setBounds(0, 0, width, height);
		}

		{
			// Create Pane for Player1
			pane_Spieler1.setBackground(Colors.getColor(Colors.WHITE));
			pane_Spieler1.setBounds(width / 2/* + 1 */, 0, width / 2, height / 4 * 3);
			pane_Spieler1.setLayout(null);

			// Create Title for Player1
			titelSpieler1.setFont(new Font("Tahoma", Font.PLAIN, height / 4 - 80));
			titelSpieler1.setBounds(0, 0, width / 2, height / 4);
			titelSpieler1.setVerticalAlignment(SwingConstants.CENTER);
			titelSpieler1.setHorizontalAlignment(SwingConstants.CENTER);

			// Create Goal-Count for Player1
			toreSpieler1.setFont(new Font("Tahoma", Font.PLAIN, height / 2 - 120));
			toreSpieler1.setBounds(0, height / 4 /* + 1 */, width / 2, height / 2);
			toreSpieler1.setVerticalAlignment(SwingConstants.CENTER);
			toreSpieler1.setHorizontalAlignment(SwingConstants.CENTER);

			// Add Elements to Pane
			pane_Spieler1.add(titelSpieler1);
			pane_Spieler1.add(toreSpieler1);

		}

		{
			// Create Pane for Player2
			pane_Spieler2.setLayout(null);
			pane_Spieler2.setBackground(Colors.getColor(Colors.WHITE));
			pane_Spieler2.setBounds(0, 0, width / 2, height / 4 * 3);

			// Create Title for Player2
			titelSpieler2.setFont(new Font("Tahoma", Font.PLAIN, height / 4 - 80));
			titelSpieler2.setBounds(0, 0, width / 2, height / 4);
			titelSpieler2.setVerticalAlignment(SwingConstants.CENTER);
			titelSpieler2.setHorizontalAlignment(SwingConstants.CENTER);

			// Create Goal-Count for Player2
			toreSpieler2.setFont(new Font("Tahoma", Font.PLAIN, height / 2 - 120));
			toreSpieler2.setBounds(0, height / 4 /* + 1 */, width / 2, height / 2);
			toreSpieler2.setVerticalAlignment(SwingConstants.CENTER);
			toreSpieler2.setHorizontalAlignment(SwingConstants.CENTER);

			// Add Elements to Pane
			pane_Spieler2.add(titelSpieler2);
			pane_Spieler2.add(toreSpieler2);
		}

		{
			// Create Pane for Play-Time
			pane_General.setBackground(Colors.getColor(Colors.WHITE));
			pane_General.setBounds(0, height / 4 * 3 /* + 1 */, width, height / 4);
			pane_General.setLayout(null);

			// Create Label for Minutes
			spielzeit_Minutes.setFont(new Font("Tahoma", Font.PLAIN, height / 4 - 60));
			spielzeit_Minutes.setBounds(0, 0, (int) (width / 2 * 0.97), height / 4);
			spielzeit_Minutes.setVerticalAlignment(SwingConstants.CENTER);
			spielzeit_Minutes.setHorizontalAlignment(SwingConstants.RIGHT);

			// Create Label for Seconds
			spielzeit_Seconds.setFont(new Font("Tahoma", Font.PLAIN, height / 4 - 60));
			spielzeit_Seconds.setBounds((int) (width * 1.03) / 2, 0, width / 2, height / 4);
			spielzeit_Seconds.setVerticalAlignment(SwingConstants.CENTER);
			spielzeit_Seconds.setHorizontalAlignment(SwingConstants.LEFT);

			// Create Label for Middle
			spielzeit_Middle.setFont(new Font("Tahoma", Font.PLAIN, height / 4 - 60));
			spielzeit_Middle.setBounds(0, 0, width, height / 4);
			spielzeit_Middle.setVerticalAlignment(SwingConstants.CENTER);
			spielzeit_Middle.setHorizontalAlignment(SwingConstants.CENTER);

			// Add Elements to Pane
			pane_General.add(spielzeit_Minutes);
			pane_General.add(spielzeit_Seconds);
			pane_General.add(spielzeit_Middle);
		}

		{
			// Create Pane for Game Start
			pane_GameStart.setBounds(0, 0, width, height);
			pane_GameStart.setBackground(Colors.getColor(Colors.WHITE));
			pane_GameStart.setLayout(null);

			// Label for Game Start
			gameStart_Notice.setFont(new Font("Tahoma", Font.PLAIN, height / 8));
			gameStart_Notice.setBounds(0, 0, width, height / 3);
			gameStart_Notice.setHorizontalAlignment(SwingConstants.CENTER);
			gameStart_Notice.setVerticalAlignment(SwingConstants.CENTER);
			
			gameStart_JoystickPicture.setIcon(new ImageIcon(joystickPicture));
			gameStart_JoystickPicture.setBounds(0, height / 3, width, height * 2 / 3);
			gameStart_JoystickPicture.setHorizontalAlignment(SwingConstants.CENTER);
			gameStart_JoystickPicture.setVerticalAlignment(SwingConstants.CENTER);

			// Add Elements to Pane
			pane_GameStart.add(gameStart_Notice);
			pane_GameStart.add(gameStart_JoystickPicture);
		}

		{
			// Create Pane for No Ball
			pane_NoBall.setBounds(0, 0, width, height);
			pane_NoBall.setBackground(Colors.getColor(Colors.WHITE));
			pane_NoBall.setLayout(null);

			// Label for No Ball
			noBall_Notice.setFont(new Font("Tahoma", Font.PLAIN, height / 8));
			noBall_Notice.setBounds(0, 0, width, height / 3);
			noBall_Notice.setHorizontalTextPosition(SwingConstants.CENTER);
			noBall_Notice.setVerticalAlignment(SwingConstants.CENTER);

			noBall_JoystickPicture.setIcon(new ImageIcon(joystickPicture));
			noBall_JoystickPicture.setBounds(0, height / 3, width, height * 2 / 3);
			noBall_JoystickPicture.setHorizontalAlignment(SwingConstants.CENTER);
			noBall_JoystickPicture.setVerticalAlignment(SwingConstants.CENTER);
			
			// Add Elements to Pane
			pane_NoBall.add(noBall_Notice);
			pane_NoBall.add(noBall_JoystickPicture);
		}

		// Update Timer
		ActionListener updateLabels = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				toreSpieler1.setText(Integer.toString(Globals.toreSpieler1));
				toreSpieler2.setText(Integer.toString(Globals.toreSpieler2));
				pane_Spieler1.setBackground(Colors.getColor(Globals.mBotSpieler1));
				pane_Spieler2.setBackground(Colors.getColor(Globals.mBotSpieler2));
				if (Globals.maxSpielzeit > Globals.spielzeit) {
					spielzeit_Minutes.setText(String.format("%02d",
							TimeUnit.MILLISECONDS.toMinutes(Globals.maxSpielzeit - Globals.spielzeit)));
					if (Globals.spielLaeuft || System.currentTimeMillis() % 1500 > 750) {
						spielzeit_Middle.setText(":");
					} else {
						spielzeit_Middle.setText("");
					}
					spielzeit_Seconds.setText(String.format("%02d",
							TimeUnit.MILLISECONDS.toSeconds(Globals.maxSpielzeit - Globals.spielzeit) % 60));
				} else {
					spielzeit_Middle.setText("Letzter Ball");
					spielzeit_Minutes.setText("");
					spielzeit_Seconds.setText("");
				}
				if (/* Ablaufsteuerung.getGameState() */ Globals.debug_spielZustand == ZustaendeSpiel.SPIEL_AUS) {
					pane_GameStart.setLocation(0, 0);
				} else {
					pane_GameStart.setLocation(width * 2, height * 2);
				}
				if (/* Ablaufsteuerung.getBallState() */ Globals.debug_ballZustand == ZustaendeBall.KEIN_BALL
						&& /* Ablaufsteuerung.getGameState() */ Globals.debug_spielZustand == ZustaendeSpiel.SPIEL_LAEUFT
						&& System.currentTimeMillis() % 10000 < 5000) {
					pane_NoBall.setLocation(0, 0);
				} else {
					pane_NoBall.setLocation(width * 2, height * 2);
				}
			}
		};
		Timer updateTimer = new Timer(250, updateLabels);
		updateTimer.start();

		// Add all Panes
		lpane.add(pane_Spieler1, 0, 0);
		lpane.add(pane_Spieler2, 0, 0);
		lpane.add(pane_General, 0, 0);
		lpane.add(pane_NoBall, 1, 0);
		lpane.add(pane_GameStart, 2, 0);
		window.getContentPane().add(lpane);

		// Hide Cursor
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = tk.createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		window.getContentPane().setCursor(blankCursor);

		// Set Window to Fullscreen
		window.setUndecorated(true);
		window.setExtendedState(Frame.MAXIMIZED_BOTH);
		device.setFullScreenWindow(window);

		// Show Window
		window.setVisible(true);
	}

	public void setVisible(boolean visibility) {
		this.window.setVisible(visibility);
	}

	public void closeWindow() {
		this.window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}
}
