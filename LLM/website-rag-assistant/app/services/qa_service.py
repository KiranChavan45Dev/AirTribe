from app.core.llm_client import LlmClient
from app.vectorstore.embeddings import get_embedding
from app.vectorstore.faiss_index import FaissIndex
from app.core.prompt_templates import SYSTEM_PROMPT

class QAService:
    def __init__(self, index: FaissIndex):
        self.llm = LlmClient()
        self.index = index

    def answer(self, question: str) -> str:
        query_embedding = get_embedding(question)
        chunks = self.index.search(query_embedding, k=5)

        if not chunks:
            return "I don't know based on the provided information."

        context = "\n\n".join(chunks)
        prompt = f"Context:\n{context}\n\nQuestion:\n{question}"

        return self.llm.generate(SYSTEM_PROMPT, prompt)

