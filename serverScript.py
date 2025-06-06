import cgi

HTML = {ord("ü"): "&uuml;"} #...

def application(environ, start_response):
    status = "200 OK"
    type_ = "text/html"
    if environ["PATH_INFO"] == "/asteroidsRangliste":
        scores = []
        names = []
        with open("./rangliste.txt", "r", encoding="utf-8") as f:
            for i in f.read().split("#**#"):
                scores.append(int(i.split("#*#")[0]))
                names.append(i.split("#*#")[1])
        form = cgi.FieldStorage(fp=environ.get("wsgi.input"), environ=environ, keep_blank_values=True)
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
    elif environ["PATH_INFO"] == "/test":
        content = "test"
    else:
        content = "Error!"
    response_headers = [("Content-Type", type_), ("Content-Length", str(len(content))),\
                        ("Access-Control-Allow-Origin", "*")]
    start_response(status, response_headers)
    content = content.translate(HTML)
    return [content.encode("utf-8")]

if __name__ == "__main__":
    from wsgiref.simple_server import make_server
    port = 8000
    httpd = make_server("", port, application)
    print("Application started...")
    print(f"Serving on port {port}")
    httpd.serve_forever()
