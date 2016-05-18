package display;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Timer;

import ablaufsteuerung.ZustaendeBall;
import ablaufsteuerung.ZustaendeSpiel;
import master.Globals;

public class TestScoreboard {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestScoreboard window = new TestScoreboard();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					@SuppressWarnings("unused")
					Scoreboard window = new Scoreboard();
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
		frame = new JFrame();
		frame.setBounds(100, 100, 400, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		
		JButton btnAddPlayer1Score = new JButton("Add Player1 Score");
		btnAddPlayer1Score.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                Globals.toreSpieler1++;
            }
        });
		panel.add(btnAddPlayer1Score);
		
		JButton btnAddPlayer2Score = new JButton("AddPlayer2 Score");
		btnAddPlayer2Score.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                Globals.toreSpieler2++;
            }
        });
		panel.add(btnAddPlayer2Score);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		final JComboBox<Colors> player1_Color = new JComboBox<Colors>();
		player1_Color.setModel(new DefaultComboBoxModel<Colors>(Colors.values()));
		player1_Color.setSelectedIndex(0);
		panel_1.add(player1_Color);
		player1_Color.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                Globals.mBotSpieler1 = Colors.values()[player1_Color.getSelectedIndex()];
            }
        });
		
		final JComboBox<Colors> player2_Color = new JComboBox<Colors>();
		player2_Color.setModel(new DefaultComboBoxModel<Colors>(Colors.values()));
		panel_1.add(player2_Color);
		player2_Color.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                Globals.mBotSpieler2 = Colors.values()[player2_Color.getSelectedIndex()];
            }
        });
		
		final JComboBox<ZustaendeSpiel> gameState = new JComboBox<ZustaendeSpiel>();
		gameState.setModel(new DefaultComboBoxModel<ZustaendeSpiel>(ZustaendeSpiel.values()));
		panel_1.add(gameState);
		gameState.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                Globals.debug_spielZustand = ZustaendeSpiel.values()[gameState.getSelectedIndex()];
            }
        });
		
		final JComboBox<ZustaendeBall> ballState = new JComboBox<ZustaendeBall>();
		ballState.setMaximumRowCount(3);
		ballState.setModel(new DefaultComboBoxModel<ZustaendeBall>(ZustaendeBall.values()));
		ballState.setSelectedIndex(0);
		panel_1.add(ballState);
		ballState.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                Globals.debug_ballZustand = ZustaendeBall.values()[ballState.getSelectedIndex()];
            }
        });
		
		JPanel panel_2 = new JPanel();
		frame.getContentPane().add(panel_2, BorderLayout.CENTER);
		
		JButton btnResetGame = new JButton("Reset Game");
		panel_2.add(btnResetGame);
		btnResetGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                Globals.spielzeit = 0;
            }
        });
		
		
		ActionListener update = new ActionListener() {
			private long lastCycle = System.currentTimeMillis();
			
			public void actionPerformed(ActionEvent event) {
				if(Globals.debug_spielZustand == ZustaendeSpiel.SPIEL_LAEUFT) {
					Globals.spielzeit += System.currentTimeMillis() - lastCycle;
				}
				lastCycle = System.currentTimeMillis();
			}
		};
		Timer updateTimer = new Timer(50, update);
		updateTimer.start();
	}

}
