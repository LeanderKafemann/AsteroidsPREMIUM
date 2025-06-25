from PyWSGIRef import HELLO_WORLD, ERROR, FieldStorage, addSchablone, makeApplicationObject, setUpServer, BETA, SCHABLONEN, loadFromFile

# aktiviert PyWSGIRef.BETA-Modus für erweiterte PyHTML-Fähigkeiten
BETA.enable()

# lädt Ranglisten-PyHTML code
addSchablone("ranglisteHTML", loadFromFile("./webRangliste.pyhtml"))

HTML = {ord("ü"): "ue", ord("ö"): "oe", ord("ä"): "ae",\
        ord("Ü"): "Ue", ord("Ö"): "Oe", ord("Ä"): "Ae", ord("ß"): "ss"}

def formatScoreboard(scoreboard: str) -> str:
    """
    Gibt für Greenfoot-Anzeige formatiertes Scoreboard zurück.
    """
    return scoreboard.replace("#**#", "\n").replace("#*#", " von ")

def formatScoreboardRead():
    """
    Liest Rangliste und formatiert zu HTML.
    """
    with open("./rangliste.txt", "r", encoding="utf-8") as f:
        return formatScoreboard(f.read()).replace("\n", "<br/>")

def applicationObject(path: str, form: FieldStorage) -> tuple[str, str, str]:
    """
    This function is used to process the request and return the response.
    """
    status = "200 OK"
    if path == "/asteroidsRangliste":
        scores = [] # leere Listen für Punkte und Namen
        names = []
        # lädt gesicherte Punkte und Namen
        with open("./rangliste.txt", "r", encoding="utf-8") as f:
            for i in f.read().split("#**#"):
                scores.append(int(i.split("#*#")[0]))
                names.append(i.split("#*#")[1])
        # liest mit Request gelieferten Namen und Punkte aus
        newName = form.getvalue("name")
        newScore = int(form.getvalue("score"))
        if newScore <= 2 * scores[0] or newScore <= 11111: # Anticheat
            cheat = False
            for i in range(10):
                if newScore > scores[i]:
                    # falls Punkte höher als Wert auf Rangliste sind,
                    # wird die neue Punktzahl der Liste hinzugefügt und der letzte
                    # (niedrigste) Wert entfernt
                    scores.insert(i, newScore)
                    names.insert(i, newName)
                    scores = scores[:-1]
                    names = names[:-1]
                    break
        else:
            cheat = True
        ranglistenString = ""
        for i in range(10):
            ranglistenString += str(scores[i])+"#*#"+names[i]
            if i != 9:
                ranglistenString += "#**#"
        # die neue Rangliste wird gesichert
        with open("./rangliste.txt", "w", encoding="utf-8") as f:
            f.write(ranglistenString)
        # die formatierte Rangliste wird als content gesetzt
        content = formatScoreboard(ranglistenString) + ("\nCHEATVERDACHT!" if cheat else "")
    elif path == "/onlineRanglisteAsteroids":
        # wertet PyHTML-Inhalt aus für Online-Ranglisten-Ansicht
        content = SCHABLONEN["ranglisteHTML"].decodedContext(globals())
    elif path == "/test":
        # Testseite
        content = "test"
    elif path == "/hello":
        # Hallo Welt
        content = HELLO_WORLD
    else:
        # Fehlerseite
        status = "404 Not Found"
        content = ERROR
    # gibt HTML-Inhalt mit entsprechenden Headern zurück
    return content.translate(HTML), "text/html", status
# erstellt WSGI-Applikationsobjekt
application = makeApplicationObject(applicationObject, True, True)

if __name__ == "__main__":
    # erstellt WSGI-Server
    server = setUpServer(application)
    print("Successfully set up server.")
    # startet Server
    server.serve_forever()
    # Go to asteroidspremium_pythonanywhere_com_wsgi.py for more configuration...
