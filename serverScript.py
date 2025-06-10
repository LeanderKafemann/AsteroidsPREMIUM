from PyWSGIRef import HELLO_WORLD, ERROR, FieldStorage, makeApplicationObject, setUpServer

HTML = {ord("ü"): "ue", ord("ö"): "oe", ord("ä"): "ae",\
        ord("Ü"): "Ue", ord("Ö"): "Oe", ord("Ä"): "Ae", ord("ß"): "ss"}

def applicationObject(path: str, form: FieldStorage) -> tuple[str, str, str]:
    """
    This function is used to process the request and return the response.
    """
    status = "200 OK"
    if path == "/asteroidsRangliste":
        scores = []
        names = []
        with open("./rangliste.txt", "r", encoding="utf-8") as f:
            for i in f.read().split("#**#"):
                scores.append(int(i.split("#*#")[0]))
                names.append(i.split("#*#")[1])
        newName = form.getvalue("name")
        newScore = int(form.getvalue("score"))
        for i in range(10):
            if newScore > scores[i]:
                scores.insert(i, newScore)
                names.insert(i, newName)
                scores = scores[:-1]
                names = names[:-1]
                break
        ranglistenString = ""
        for i in range(10):
            ranglistenString += str(scores[i])+"#*#"+names[i]
            if i != 9:
                ranglistenString += "#**#"
        with open("./rangliste.txt", "w", encoding="utf-8") as f:
            f.write(ranglistenString)
        content = ranglistenString.replace("#**#", "\n").replace("#*#", " von ")
    elif path == "/test":
        content = "test"
    elif path == "/hello":
        content = HELLO_WORLD
    else:
        status = "404 Not Found"
        content = ERROR
    return content, "text/html", status
application = makeApplicationObject(applicationObject, True, True)

if __name__ == "__main__":
    server = setUpServer(application)
    print("Successfully set up server.")
    server.serve_forever()
    print("Go to asteroidspremium_pythonanywhere_com_wsgi.py...")
