import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Ufo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Ufo extends Actor
{
    /**
     * Act - do whatever the Ufo wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private int geschwindigkeitX;
    private int geschwindigkeitY;
    
    public Ufo(int geschwindigkeitX, int geschwindigkeitY) {
        this.geschwindigkeitX = geschwindigkeitX;
        this.geschwindigkeitY = geschwindigkeitY;
    }
    
    public void act()
    {
        setLocation(getX()+geschwindigkeitX, getY()+geschwindigkeitY);
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
        
        if (Greenfoot.getRandomNumber(200) == 42) {
            Schuss ufoSchuss = new Schuss(5, Greenfoot.getRandomNumber(360), false);
            getWorld().addObject(ufoSchuss, getX(), getY());
            Greenfoot.playSound("schuss.mp3");
        }
    }
}
