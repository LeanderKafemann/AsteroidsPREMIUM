import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Leben here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Leben extends Actor
{
    /**
     * Act - do whatever the Leben wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private int lebenNummer;
    private Raumschiff r;
    
    public Leben(int lebenNummer, Raumschiff r) {
        turn(270);
        this.lebenNummer = lebenNummer;
        this.r = r;
    }
    public void act()
    {
        if (r.getLeben() < lebenNummer) {
            getWorld().removeObject(this);
        }
    }
}
