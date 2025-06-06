import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
  
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Write a description of class Raumschiff here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Raumschiff extends Actor
{
    private double geschwindigkeitX;
    private double geschwindigkeitY;
    private int leben;
    private boolean geschossen;
    private boolean gameOver;
    
    public Raumschiff() {
        leben = 3;
        geschossen = false;
        gameOver = false;
    }
    
    public void act()
    {
        setLocation(getX() + (int) geschwindigkeitX, getY() + (int) geschwindigkeitY);
        if (Greenfoot.isKeyDown("up")){
            this.setImage("Raumschiff_powered.png");
            double angle = Math.toRadians(getRotation());
            geschwindigkeitX += 0.2*Math.cos(angle);
            geschwindigkeitY += 0.2*Math.sin(angle);
        } else {
            this.setImage("Raumschiff_unpowered.png");
        }
        geschwindigkeitX = geschwindigkeitX * 0.985;
        geschwindigkeitY = geschwindigkeitY * 0.985;
        
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
        
        if (Greenfoot.isKeyDown("right")) {
            turn(3);
        }
        if (Greenfoot.isKeyDown("left")) {
            turn(-3);
        }
        if (Greenfoot.isKeyDown("down")) { 
            //bremsen
            geschwindigkeitX = geschwindigkeitX * 0.975;
            geschwindigkeitY = geschwindigkeitY * 0.975;
        }
        
        //schiessen
        if (Greenfoot.isKeyDown("space")) {
            if (geschossen == false) {
                Schuss s = new Schuss(10, getRotation(), true);
                getWorld().addObject(s, getX(), getY());
                geschossen = true;
                Greenfoot.playSound("schuss.mp3");
            }
        } else {
            geschossen = false;
        }
        
        // Kollisionserkennung
        Actor k = getOneIntersectingObject(Asteroid.class);
        if (k != null) {
            if (leben > 1) {
                leben -= 1;
                getWorld().removeObject(k);
                Greenfoot.playSound("raumschiffKoll.mp3");
            } else {
                //gameOver
                leben -= 1;
                gameOver = true;
                Greenfoot.playSound("gameOver.mp3");
            }
        }
        
        // Kollisionserkennung Ufo
        k = getOneIntersectingObject(Ufo.class);
        if (k != null) {
            if (leben > 1) {
                leben -= 1;
                getWorld().removeObject(k);
                Greenfoot.playSound("raumschiffKoll.mp3");
            } else {
                //gameOver
                leben -= 1;
                gameOver = true;
                Greenfoot.playSound("gameOver.mp3");
            }
        }
        
        // Kollisionserkennung Ufo-Schuss
        k = getOneIntersectingObject(Schuss.class);
        if (k != null) {
            Schuss s = (Schuss) k;
            if (s.getRaumschiffSchuss() == false) {
                getWorld().removeObject(k);
                if (leben > 1) {
                    leben -= 1;
                    getWorld().removeObject(k);
                    Greenfoot.playSound("raumschiffKoll.mp3");
                } else {
                    //gameOver
                    leben -= 1;
                    gameOver = true;
                    Greenfoot.playSound("gameOver.mp3");
                }
            }
        }
        
        //Ufo-Generierung
        if (Greenfoot.getRandomNumber(400) == 42) {
            Ufo neuesUfo = new Ufo(getGeschwindigkeit(), getGeschwindigkeit());
            //posX, posY = getEdge();
            getWorld().addObject(neuesUfo, 10, 10);
        }
        
        //Asteroid-Generierung
        if (Greenfoot.getRandomNumber(200) == 42) {
            Asteroid neuerAsteroid = new Asteroid(getGeschwindigkeit(), getGeschwindigkeit(), Greenfoot.getRandomNumber(3)+1);
            int[] asteroidSpawn = getAsteroidSpawn();
            getWorld().addObject(neuerAsteroid, asteroidSpawn[0], asteroidSpawn[1]);
        }
        
        if (gameOver) {
            getWorld().showText("Rangliste wird geladen...", 400, 75);
            //webRequest
            try {
                MyWorld mW = (MyWorld) getWorld(); //MyWorld-Objekt erzeugen
                //URL generieren
                String urlString = "https://asteroidspremium.pythonanywhere.com/asteroidsRangliste?name="+mW.getName()+"&score="+String.valueOf(mW.getScore());
                
                //Verbindung aufbauen
                URL url = new URL(urlString);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                
                //lesen
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine).append("\n");
                }
                
                //schließen
                in.close();
                con.disconnect();
                
                //Ausgabe
                getWorld().showText(content.toString(), 400, 300);
            } catch (IOException e) {
                getWorld().showText("Fehler beim Abrufen der Rangliste", 400, 300);
            }
           
            getWorld().showText("Game over!", 400, 100);
            Greenfoot.stop();
        }
    }
    
    
    public int[] getAsteroidSpawn(){
        int[] asteroid_spawn = new int[2];
        asteroid_spawn[0] = 15;
        asteroid_spawn[1] = 15;
        if (Greenfoot.getRandomNumber(2) == 1) {
            asteroid_spawn[0] = Greenfoot.getRandomNumber(780)+10;
        } else {
            asteroid_spawn[1] = Greenfoot.getRandomNumber(580)+10;
        }
        return asteroid_spawn;
    }
    
    
    public int getLeben() {
        return leben;
    }
    
    private int getGeschwindigkeit() {
        int multi = 0;
        if (Greenfoot.getRandomNumber(2) == 1) {
            multi = 1;
        } else {
            multi = -1;
        }
        return (Greenfoot.getRandomNumber(3)+1)*multi;
    }
}
