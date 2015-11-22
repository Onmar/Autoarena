package ablaufsteuerung;

public enum ZustaendeBall {

    KEIN_BALL(0),
    BALL_EINWURF(1),
    BALL_VORHANDEN(2),
    TOR_SPIELER1(3),
    TOR_SPIELER2(4);
    
    private ZustaendeBall (int zustand) {
        
    }
    
}
