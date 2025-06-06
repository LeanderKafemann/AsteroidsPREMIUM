import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Counter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Counter extends Actor
{
  private int lastKnownScore = 0;
     
  public void act()
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