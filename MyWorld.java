import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/** Programmname: AsteroidsPREMIUM
 * Arcade Spiel "Asteroids" in Greenfoot
 * 
 * @author Leander Kafemann, Florian Liske
 * @version 1.1 / 25.06.2025
 */

public class MyWorld extends World
{

    /**
     * Konstruktor für Objekte der Klasse MyWorld.
     * 
     * @param score Zaehlt die Punktzahl
     * @param name Speichert den eingegeben Namen für die Rangliste
     * @param asteroids zaehlt wie viele Asteroiden auf dem Spielfeld sind
     */
    private int score;
    private String name;
    private int asteroids;
    
    public MyWorld()
    {    
        super(800, 600, 1); // Erstelle eine neue Welt der Groesse 800x600.
        Raumschiff r = new Raumschiff(); 
        addObject(r, 400, 300); // Sporne das Raumschiff
        score = 0;
        addObject(new Counter(), 550, 590);
        for (int i = 1; i<4; i++) {
            Leben l = new Leben(i, r);
            addObject(l, 25*i, 580);
        }
        //Frage den Spieler nach einem Namen
        name = Greenfoot.ask("Name?");
        this.showText("Name: "+name, 650, 580);
        
    }
    
    //Score Counter
    public int getScore() {
        return score;
    }
    
    public void increaseScore(Integer toIncr) {
        score += toIncr;
    }

    
    public String getName() {
        return name;
    }
    
    //Asteroid-Counter
    public void addAsteroid() {
        asteroids += 1;
    }
    
    public void removeAsteroid() {
            asteroids -= 1;
    }
    
    public int getAsteroids() {
        return asteroids;
    }
}
