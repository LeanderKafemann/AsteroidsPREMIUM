import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Counter extends Actor
{
    /**
    * Zählt die Punkte und zeigt sie an
    * 
    * @param lastKnownScore ist zur überprüfung ob sich der Score geändert hat
    * @void update Schreibt den neuen Score ins Spiel
    */
    private int lastKnownScore = 0;
     
    public void act() // Überprüft ob der Score sich geändert hat
    {
        MyWorld mW = (MyWorld) getWorld();
        int score = mW.getScore();
        if (lastKnownScore < score) {
            lastKnownScore = score;
            update();
        }
    }
     
    public Counter()
    {
        setImage(new GreenfootImage(400,120)); //Größe für Bild des Counters
        update();
    }
   
    public void update() // erneuert das Bild nach jedem dazuzählen
    {
        GreenfootImage img = getImage();
        img.clear(); // löscht Bild
        img.setColor(Color.WHITE); // setzt Farbe der Schrift auf Schwarz
        img.drawString("Score: " + lastKnownScore, 20, 60); // addiert den Score
    }
}