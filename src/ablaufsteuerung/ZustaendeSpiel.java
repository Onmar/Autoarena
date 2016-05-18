package ablaufsteuerung;

public enum ZustaendeSpiel {
    
    SPIEL_AUS(0),
    SPIEL_INIT(1),
    SPIEL_LAEUFT(2),
    SPIEL_FERTIG(3),
    EINPARKEN(4);
    
    
    private ZustaendeSpiel(int zustand) {
        
    }
    
}
