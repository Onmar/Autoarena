package ablaufsteuerung;

public enum ZuständeSpiel {
    
    SPIEL_AUS(0),
    SPIEL_INIT(1),
    SPIEL_LÄUFT(2),
    SPIEL_FERTIG(3);
    
    private ZuständeSpiel(int zustand) {
        
    }
    
}
