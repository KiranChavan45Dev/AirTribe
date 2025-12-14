import faiss
import numpy as np
from typing import List

class FaissIndex:
    def __init__(self, dim: int):
        self.index = faiss.IndexFlatL2(dim)
        self.texts: List[str] = []

    def add(self, embeddings: List[np.ndarray], texts: List[str]) -> None:
        self.index.add(np.vstack(embeddings))
        self.texts.extend(texts)

    def search(self, query: np.ndarray, k: int = 5) -> List[str]:
        _, indices = self.index.search(query.reshape(1, -1), k)
        return [self.texts[i] for i in indices[0] if i != -1]

    # New: save/load helpers
    def save(self, filepath: str):
        faiss.write_index(self.index, filepath)

    def load(self, filepath: str):
        self.index = faiss.read_index(filepath)
