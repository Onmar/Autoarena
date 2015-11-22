package display;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.Color;

public class TestScoreboard {

	private JFrame frmGuiForCararena;
	private int height = 720;
	private int width = 1280;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestScoreboard window = new TestScoreboard();
					window.frmGuiForCararena.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestScoreboard() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGuiForCararena = new JFrame();
		frmGuiForCararena.setTitle("GUI for Car Arena");
		frmGuiForCararena.setBounds(0, 0, width + 2 * 9, height + 9 + 40);
		frmGuiForCararena.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGuiForCararena.getContentPane().setLayout(null);
		
		JPanel Pane_Spieler2 = new JPanel();
		Pane_Spieler2.setLayout(null);
		Pane_Spieler2.setBackground(new Color(0, 204, 0));
		Pane_Spieler2.setBounds(width / 2 + 1, 0, width / 2, height / 4 * 3);
		frmGuiForCararena.getContentPane().add(Pane_Spieler2);
		
		JLabel titelSpieler2 = new JLabel("Spieler 2");
		titelSpieler2.setFont(new Font("Tahoma", Font.PLAIN, height / 4 - 60));
		titelSpieler2.setBounds(0, 0, width / 2, height / 4);
		titelSpieler2.setVerticalAlignment(SwingConstants.CENTER);
		titelSpieler2.setHorizontalAlignment(SwingConstants.CENTER);
		Pane_Spieler2.add(titelSpieler2);
		
		JLabel toreSpieler2 = new JLabel("<Tor2>");
		toreSpieler2.setFont(new Font("Tahoma", Font.PLAIN, height / 2 - 120));
		toreSpieler2.setBounds(0, height / 4 + 1, width / 2, height / 2);
		toreSpieler2.setVerticalAlignment(SwingConstants.CENTER);
		toreSpieler2.setHorizontalAlignment(SwingConstants.CENTER);
		Pane_Spieler2.add(toreSpieler2);
		
		JPanel Pane_Spieler1 = new JPanel();
		Pane_Spieler1.setBackground(new Color(255, 51, 51));
		Pane_Spieler1.setBounds(0, 0, width / 2, height / 4 * 3);
		frmGuiForCararena.getContentPane().add(Pane_Spieler1);
		Pane_Spieler1.setLayout(null);
		
		JLabel titelSpieler1 = new JLabel("Spieler 1");
		titelSpieler1.setFont(new Font("Tahoma", Font.PLAIN, height / 4 - 60));
		titelSpieler1.setBounds(0, 0, width / 2, height / 4);
		titelSpieler1.setVerticalAlignment(SwingConstants.CENTER);
		titelSpieler1.setHorizontalAlignment(SwingConstants.CENTER);
		Pane_Spieler1.add(titelSpieler1);
		
		JLabel toreSpieler1 = new JLabel("<Tor1>");
		toreSpieler1.setFont(new Font("Tahoma", Font.PLAIN, height / 2 - 120));
		toreSpieler1.setBounds(0, height / 4 + 1, width / 2, height / 2);
		toreSpieler1.setVerticalAlignment(SwingConstants.CENTER);
		toreSpieler1.setHorizontalAlignment(SwingConstants.CENTER);
		Pane_Spieler1.add(toreSpieler1);
		
		JPanel Pane_General = new JPanel();
		Pane_General.setBackground(new Color(255, 255, 255));
		Pane_General.setBounds(0, height / 4 * 3 + 1, width, height / 4);
		frmGuiForCararena.getContentPane().add(Pane_General);
		Pane_General.setLayout(null);
		
		JLabel spielzeit = new JLabel("<Spielzeit>");
		spielzeit.setFont(new Font("Tahoma", Font.PLAIN, height / 4 - 60));
		spielzeit.setBounds(0, 0, width, height / 4);
		spielzeit.setVerticalAlignment(SwingConstants.CENTER);
		spielzeit.setHorizontalAlignment(SwingConstants.CENTER);
		Pane_General.add(spielzeit);
	}
}
