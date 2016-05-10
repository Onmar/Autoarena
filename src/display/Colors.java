package display;

import java.awt.Color;

public enum Colors {
	
	WHITE(0),
	RED(1),
	GREEN(2),
	BLUE(3);
	private static Color[] colorArray = {
			new Color(255, 255, 255),
			new Color(255, 51, 51),
			new Color(0, 204, 0),
			new Color(51, 51, 255)
	};
	
	private Colors(int index) {
		
	}
	
	public static Color getColor(Colors color) {
		return colorArray[color.ordinal()];
	}

}
