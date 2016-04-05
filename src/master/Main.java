package master;

import java.awt.EventQueue;

import ablaufsteuerung.Ablaufsteuerung;
import display.Scoreboard;
import mBot.MBotSteuerung;
import util.ReadConsoleInput;

public class Main {
	
	private static boolean endProgram = false;

    public static void main(String[] args) {

    	Thread ablaufsteuerung = new Thread() {
    		@Override
    		public void run() {
				Ablaufsteuerung.init();
    			while (!endProgram) {
    				Ablaufsteuerung.run();
    			}
    			Ablaufsteuerung.close();
    		}
    	};
    	
    	Thread mBotSteuerung = new Thread() {
    		@Override
    		public void run() {
    			MBotSteuerung.init();
    			while(!endProgram) {
    				MBotSteuerung.run();
    			}
    			MBotSteuerung.close();
    		}
    	};
    	
    	Thread scoreboard = new Thread() {
    		@Override
    		public void run() {
    			EventQueue.invokeLater(new Runnable() {
    				public void run() {
    					try {
    						Scoreboard window = new Scoreboard();
        					while(!endProgram) {
        					}
        					window.closeWindow();
    					} catch (Exception e) {
    						e.printStackTrace();
    					}
    				}
    			});
    		}
    	};
    	
    	ablaufsteuerung.start();
    	mBotSteuerung.start();
    	scoreboard.start();
    	
    	ReadConsoleInput.readString(false);
    	
    	endProgram = true;
    	
    	try {
    	ablaufsteuerung.join();
    	mBotSteuerung.join();
    	scoreboard.join();
    	} catch (InterruptedException e) {
    	}

    }

}
