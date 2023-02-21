import flask

app = flask.Flask(__name__)
app.config["DEBUG"] = True

@app.route("/", methods=["GET"])
def index():
    return "test response"


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)