from flask import Flask
from app.api.routes import qa

app = Flask(__name__)
app.register_blueprint(qa)

if __name__ == "__main__":
    app.run(debug=True)
