from flask import Blueprint, request, jsonify, current_app

bp = Blueprint("api", __name__)

@bp.route("/ask", methods=["POST"])
def ask():
    data = request.get_json()
    question = data.get("question")

    if not question:
        return jsonify({"error": "question is required"}), 400

    answer = current_app.qa_service.answer(question)
    return jsonify({"answer": answer})
