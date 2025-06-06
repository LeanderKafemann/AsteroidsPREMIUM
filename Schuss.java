import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Schuss here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Schuss extends Actor
{
    /**
     * Act - do whatever the Schuss wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private int geschwindigkeit;
    private int alter;
    private boolean raumschiffSchuss;
    
    public Schuss(int geschwindigkeit, int rotation, boolean raumschiffSchuss) {
        this.geschwindigkeit = geschwindigkeit;
        setRotation(0);
        turn(rotation);
        this.raumschiffSchuss = raumschiffSchuss;
        if (raumschiffSchuss == false) {
            this.setImage("Ufo_Schuss.png");
        }
    }
    public void act()
    {   
        if (getX() > 798) {
            setLocation(2, getY());
        }
        if (getX() < 2) {
            setLocation(798, getY());
        }
        if (getY() > 598) {
            setLocation(getX(), 2);
        }
        if (getY() < 2) {
            setLocation(getX(), 598);
        }
        
        // Kollisionserkennung
        Actor k = getOneIntersectingObject(Asteroid.class);
        if (k != null) {
            Asteroid a = (Asteroid) k;
            a.abgeschossen();
            alter = 200;
            MyWorld mW = (MyWorld) getWorld();
            mW.increaseScore(50);
            Greenfoot.playSound("kollision.mp3");
        }
        
        // Kollisionserkennung Ufo
        if (raumschiffSchuss == true) {
            k = getOneIntersectingObject(Ufo.class);
            if (k != null) {
                getWorld().removeObject(k);
                alter = 200;
                MyWorld mW = (MyWorld) getWorld();
                mW.increaseScore(100);
                Greenfoot.playSound("kollision.mp3");
            }
        }
        
        // Kollisionserkennung Schuss des Ufos
        k = getOneIntersectingObject(Schuss.class);
        if (k != null) {
            Schuss s = (Schuss) k;
            if (s.getRaumschiffSchuss() == false) {
                getWorld().removeObject(k);
                alter = 200;
                MyWorld mW = (MyWorld) getWorld();
                mW.increaseScore(1);
                Greenfoot.playSound("kollision.mp3");
            }
        }
        
        if (alter < 150) {
            alter += 1;
            move(geschwindigkeit);
        } else {
            
            getWorld().removeObject(this);
        }
        
    }
    
    public boolean getRaumschiffSchuss() {
        return raumschiffSchuss;
    }
}
