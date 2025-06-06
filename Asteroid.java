import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Asteroid here.
 * 
 * Leander Kafemann 
 * 25.5.7
 */
public class Asteroid extends Actor
{
    /**
     * Act - do whatever the Asteroid wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
  
  private int geschwindigkeitX;
  private int geschwindigkeitY;
  private int groesse;
  private boolean spornAdd;
  
  public Asteroid(int geschwindigkeitX, int geschwindigkeitY, int groesse) {
      this.geschwindigkeitX = geschwindigkeitX;
      this.geschwindigkeitY = geschwindigkeitY;
      this.groesse = groesse;
      
      // korrektes Bild setzen
      String imageName = "Asteroid_";
      if (groesse == 3) {
          imageName += "big_";
      } else if (groesse == 2) {
          imageName += "medium_";
      } else {
          imageName += "small_";
      }
      imageName += String.valueOf(Greenfoot.getRandomNumber(3)+1);
      imageName += ".png";
      this.setImage(imageName);
      
      this.spornAdd = true;
  }
  
  public void act()
  {
    //Asteroiden-Zähler
    if (spornAdd) {
        MyWorld mW = (MyWorld) getWorld();
        mW.addAsteroid();
        spornAdd = false;
    }
    
    //Bewegung
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
  }
  
  public void abgeschossen() {
    if (groesse > 1) {
        int x1 = Greenfoot.getRandomNumber(2)+1;
        int x2 = -x1;
        int y1 = Greenfoot.getRandomNumber(2)+1;
        int y2 = -y1;
        Asteroid a = new Asteroid(x1, y1, groesse-1);
        getWorld().addObject(a, getX(), getY());
        Asteroid a2 = new Asteroid(x2, y2, groesse-1);
        getWorld().addObject(a2, getX(), getY());
    }
    MyWorld mW = (MyWorld) getWorld();
    mW.removeAsteroid();
    getWorld().removeObject(this);
  }
}
