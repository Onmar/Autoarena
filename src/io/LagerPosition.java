package io;

public enum LagerPosition {
	
	UNKNOWN(0),
	M_BOT1(1),
	M_BOT2(2),
	M_BOT3(3),
	TOR(4);
	
	private final static int[] positions = new int[] {0, 5530, 3705, 1890, 60};
	
	private LagerPosition(int position) {
		
	}
	
	public int position() {
		return positions[this.ordinal()];
	}

}
