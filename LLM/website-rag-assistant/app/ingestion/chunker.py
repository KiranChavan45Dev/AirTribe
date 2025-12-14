from typing import List
from app.core.token_utils import count_tokens
from app.config import EMBEDDING_MODEL

def chunk_text(
    text: str,
    chunk_size: int = 500,
    overlap: int = 100,
) -> List[str]:
    words = text.split()
    chunks = []
    current = []

    for word in words:
        current.append(word)
        if count_tokens(" ".join(current), EMBEDDING_MODEL) >= chunk_size:
            chunks.append(" ".join(current))
            current = current[-overlap:]

    if current:
        chunks.append(" ".join(current))

    return chunks
