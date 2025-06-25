import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
public class Asteroid extends Actor
{
    /**
     * Asteroiden als Gegner des Spielers
     * 
     * @param geschwindigkeitX geschw. in X-Richtung
     * @param geschwindigkeitY geschw. in Y-Richtung
     * @param groesse beschreibt die groesse des Asteroiden (1-4)
     * @param spornAdd existiert da der Zaehler in MyWorld
     *        nicht im Konstrukter aufgerufen werden darf
     * @void act wird jeden tick ausgeführt, handelt Bewegung & co. ab
     * @void abgeschossen entfernt den Asteroiden wenn er abgeschossen wurde
     */
  
  private int geschwindigkeitX;
  private int geschwindigkeitY;
  private int groesse;
  private boolean spornAdd;
  
  //Erstelle den Asteroiden mit Geschw. und Groesse
  public Asteroid(int geschwindigkeitX, int geschwindigkeitY, int groesse) {
      this.geschwindigkeitX = geschwindigkeitX;
      this.geschwindigkeitY = geschwindigkeitY;
      this.groesse = groesse;
      
      // korrektes Bild setzen
      String imageName = "Asteroid_";
      if (groesse == 4) {
          imageName += "XL_";
      } else if (groesse == 3) {
          imageName += "big_";
      } else if (groesse == 2) {
          imageName += "medium_";
      } else {
          imageName += "small_";
      }
      imageName += String.valueOf(Greenfoot.getRandomNumber(3)+1);
      imageName += ".png";
      this.setImage(imageName);
      
      this.spornAdd = true; // Aktiviert den Asteroiden-Zähler
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
  
  public void abgeschossen() { // Wird aktiviert wenn ein Schuss den Asteroiden trifft
    MyWorld mW = (MyWorld) getWorld();
    if (groesse > 1) { //Zerteilt den Asteroiden wenn er ausreichen groß war
        int x1 = Greenfoot.getRandomNumber(2)+1;
        int x2 = -x1;
        int y1 = Greenfoot.getRandomNumber(2)+1;
        int y2 = -y1;
        Asteroid a = new Asteroid(x1, y1, groesse-1);
        getWorld().addObject(a, getX(), getY());
        Asteroid a2 = new Asteroid(x2, y2, groesse-1);
        getWorld().addObject(a2, getX(), getY());
    }
    if (groesse == 4) { //Fügt zusätzliche Asteroiden hinzu,falls Boss
        mW.increaseScore(201); //Bonuspunkte für Boss
        int score = mW.getScore();
        int extra = (int) (score / 3000);
        extra += 2;
        int x = 0;
        int y = 0;
        for (int i = 0; i < extra; i++) {
             x = (Greenfoot.getRandomNumber(3)+1) * (Greenfoot.getRandomNumber(1)*2 - 1);
             y = (Greenfoot.getRandomNumber(3)+1) * (Greenfoot.getRandomNumber(1)*2 - 1);
             Asteroid a = new Asteroid(x, y, Greenfoot.getRandomNumber(3)+1);
             getWorld().addObject(a, getX(), getY());
        }
    }
    // Entfernt den Asteroiden
    mW.removeAsteroid();
    getWorld().removeObject(this);
  }
}
