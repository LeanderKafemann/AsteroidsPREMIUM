import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends World
{

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    private int score;
    private String name;
    
    public MyWorld()
    {    
        // Create a new world with 800x600 cells with a cell size of 1x1 pixels.
        super(800, 600, 1);
        Raumschiff r = new Raumschiff();
        addObject(r, 400, 300);
        score = 0;
        addObject(new Counter(), 550, 590);
        for (int i = 1; i<4; i++) {
            Leben l = new Leben(i, r);
            addObject(l, 25*i, 580);
        }
        //setName muss hier stattfinden
        name = Greenfoot.ask("Name?");
        this.showText("Name: "+name, 700, 580);
        
        //Soundtrack
        Greenfoot.playSound("soundtrack.mp3");
    }
    
    public int getScore() {
        return score;
    }
    
    public void increaseScore(Integer toIncr) {
        score += toIncr;
    }
    
    public String getName() {
        return name;
    }
}
