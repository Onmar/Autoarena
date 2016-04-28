package io;

public enum LagerPosition {
	
	UNKNOWN(0),
	M_BOT1(1),
	M_BOT2(2),
	M_BOT3(3),
	TOR(4);
	
	private final static int[] positions = new int[] {0, 800, 1600, 2400, 3200};
	
	private LagerPosition(int position) {
		
	}
	
	public int position() {
		return positions[this.ordinal()];
	}

}
