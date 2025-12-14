from flask import Flask
from app.api.routes import bp
from app.services.index_builder import IndexBuilder
from app.services.qa_service import QAService


def create_app():
    app = Flask(__name__)

    builder = IndexBuilder()

    # Multiple Education & Knowledge Platform URLs
    urls = [
        # freeCodeCamp
        "https://www.freecodecamp.org",
        # "https://www.freecodecamp.org/news/",
        #
        # # Khan Academy
        # "https://www.khanacademy.org/about",
        # "https://www.khanacademy.org/math",
        #
        # # MIT OpenCourseWare
        # "https://ocw.mit.edu",
        # "https://ocw.mit.edu/about/",
    ]

    # Build index only if it doesn't already exist
    if not builder.index.texts:
        index = builder.build_from_urls(urls)
    else:
        index = builder.index

    app.qa_service = QAService(index)
    app.register_blueprint(bp)

    return app


if __name__ == "__main__":
    app = create_app()
    print("Starting Flask app on http://127.0.0.1:5000")
    app.run(debug=True, host="127.0.0.1", port=5000)

