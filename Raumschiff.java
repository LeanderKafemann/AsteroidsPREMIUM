import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
  
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class Raumschiff extends Actor
{
    /**
     * Raumschiff ist der Spieler
     * 
     * @param geschwindigkeitX geschw. in X-Richtung
     * @param geschwindigkeitY geschw. in Y-Richtung
     * @param leben beschreibt wie viel Leben übrig bleiben (0-3)
     * @param geschossen definiert einen Cooldown fürs Schießen
     * @param gameOver ist zur Überprüfung ob verloren wurde
     * @param track ist zur Steuerung des Soundtracks
     * @param cooldown ist damit nicht zu viele Asteroiden gespornt werden
     * @param bossPoss stellt sicher, dass Bosse nur alle 5000 Punkte spornen
     * 
     * @void generateAsteroid generiert einen Asteroiden
     * @return getSpawn generiert einen zufälligen Spornpunkt für Gegner
     * @void generateUfo generiert ein Ufo
     * @return genGeschwindigkeit generiert eine zufällige Geschwindigkeit für Gegner
     * @return getRangliste holt die Rangliste vom Server
     */
    private double geschwindigkeitX;
    private double geschwindigkeitY;
    private int leben;
    private boolean geschossen;
    private boolean gameOver;
    private GreenfootSound track;
    private int cooldown;
    private boolean bossPoss;
    
    public Raumschiff() {
        leben = 3;
        geschossen = false;
        gameOver = false;
        bossPoss = false;
        cooldown = 10;
        track = new GreenfootSound("soundtrack.mp3");
        track.playLoop(); // Startet den Soundtrack
    }
    
    public void act()
    {   
        //berechnet die neue Position nach einem Tick
        setLocation(getX() + (int) geschwindigkeitX, getY() + (int) geschwindigkeitY);
        //berechnet die Geschwindigkeit je nachdem welche Knöpfe gedrückt werden
        if (Greenfoot.isKeyDown("up")  || Greenfoot.isKeyDown("w")){
            this.setImage("Raumschiff_powered.png");
            double angle = Math.toRadians(getRotation());
            geschwindigkeitX += 0.2*Math.cos(angle);
            geschwindigkeitY += 0.2*Math.sin(angle);
        } else {
            this.setImage("Raumschiff_unpowered.png");
        }
        geschwindigkeitX = geschwindigkeitX * 0.985;
        geschwindigkeitY = geschwindigkeitY * 0.985;
        
        //bewegt über den Rand hinaus
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
        
        //dreht das Raumschiff
        if (Greenfoot.isKeyDown("right") || Greenfoot.isKeyDown("d")) {
            turn(4);
        }
        if (Greenfoot.isKeyDown("left") || Greenfoot.isKeyDown("a")) {
            turn(-4);
        }
        //bremst das Raumschiff
        if (Greenfoot.isKeyDown("down") || Greenfoot.isKeyDown("s")) { 
            geschwindigkeitX = geschwindigkeitX * 0.975;
            geschwindigkeitY = geschwindigkeitY * 0.975;
        }
        // secret cheat mode
        if (Greenfoot.isKeyDown("l") && Greenfoot.isKeyDown("k")) {
            Schuss s = new Schuss(20, getRotation(), true);
            getWorld().addObject(s, getX(), getY());
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
                MyWorld mW = (MyWorld) getWorld();
                mW.removeAsteroid();
                Greenfoot.playSound("raumschiffKoll.mp3");
            } else {
                //gameOver
                leben -= 1;
                gameOver = true;
                track.stop();
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
                Greenfoot.playSound("gameOver.mp3");
                if (leben > 1) {
                    leben -= 1;
                    getWorld().removeObject(k);
                } else {
                    //gameOver
                    leben -= 1;
                    gameOver = true;
                    Greenfoot.playSound("gameOver.mp3");
                }
            }
        }
        
        //Ufo-Generierung
        if (Greenfoot.getRandomNumber(500) == 42) {
            generateUfo();
        }
        
        //neue Asteroiden
        if (Greenfoot.getRandomNumber(200) == 42) {
            generateAsteroid();
        }
        
        //keine Asteroiden -> viele Asteroiden
        MyWorld mW = (MyWorld) getWorld();
        if (mW.getAsteroids() <= 0) {
            if (cooldown == 0){
                generateAsteroid(1);
                generateAsteroid(2);
                for(int i = 1; i <=3; i++){
                    if(Greenfoot.getRandomNumber(2) == 0){
                        generateAsteroid(i);
                    }
                }
                cooldown = 10;
            } else {cooldown -= 1;}
        }
        
        // viele Punkte -> Spezial-Asteroid
        if (mW.getScore() % 5000 < 1000 && bossPoss) {
            generateAsteroid(4);
        }
        if (mW.getScore() % 5000 > 4000){ 
            bossPoss = true;
        } else { 
            bossPoss = false;
        }
        
        //Spiel beenden falls verloren
        if (gameOver) {

            //webRequest
            CompletableFuture<String> future = getRangliste(mW.getName(), String.valueOf(mW.getScore()));
            future.thenAccept(result -> {
                //System.out.println("Test!");
            });
            getWorld().showText("Rangliste wird geladen...", 400, 75);
            
            String toPrint = future.join();
            getWorld().showText(toPrint, 400, 300);
            
            getWorld().showText("Game over!", 400, 100);
            track.stop();
            Greenfoot.stop();
        }
    }
    
    //Asteroid-Generierung
    public void generateAsteroid() {
        Asteroid neuerAsteroid = new Asteroid(genGeschwindigkeit(), genGeschwindigkeit(), Greenfoot.getRandomNumber(3)+1);
            int[] asteroidSpawn = getSpawn();
            getWorld().addObject(neuerAsteroid, asteroidSpawn[0], asteroidSpawn[1]);
        }
    //Asteroid-Generierung mit vorgegebener Größe
    public void generateAsteroid(int size) {
        Asteroid neuerAsteroid = new Asteroid(genGeschwindigkeit(), genGeschwindigkeit(), size);
            int[] asteroidSpawn = getSpawn();
            getWorld().addObject(neuerAsteroid, asteroidSpawn[0], asteroidSpawn[1]);
        }
    //Spornpunkt-Generierung
    public int[] getSpawn(){
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
    
    //Ufo-Generieriung
    public void generateUfo(){
        Ufo neuesUfo = new Ufo(genGeschwindigkeit(), genGeschwindigkeit());
            //posX, posY = getEdge();
            int[] UfoSpawn = getSpawn();
            getWorld().addObject(neuesUfo, UfoSpawn[0], UfoSpawn[1]);
    }
    
    public int getLeben() {
        return leben;
    }
    //Geschwindigkeits-Generierung
    private int genGeschwindigkeit() {
        int multi = 0;
        if (Greenfoot.getRandomNumber(2) == 1) {
            multi = 1;
        } else {
            multi = -1;
        }
        return (Greenfoot.getRandomNumber(3)+1)*multi;
    }
    //Ranglisten-Abfrage
    public static CompletableFuture<String> getRangliste(String name, String score) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                //URL generieren
                String urlString = "https://asteroidspremium.pythonanywhere.com/asteroidsRangliste?name="+name+"&score="+score;
                    
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
                return content.toString();
            } catch (IOException e) {
                return "Fehler beim Abrufen der Rangliste";
            }
        });
    }
}
