import os
import pickle
from app.ingestion.web_scraper import scrape_text, get_all_links
from app.ingestion.chunker import chunk_text
from app.vectorstore.embeddings import get_embedding
from app.vectorstore.faiss_index import FaissIndex
from app.config import EMBEDDING_DIM

DATA_DIR = os.path.join(os.path.dirname(__file__), "..", "data")
os.makedirs(DATA_DIR, exist_ok=True)

class IndexBuilder:
    def __init__(self):
        self.index_file = os.path.join(DATA_DIR, "faiss.index")
        self.chunks_file = os.path.join(DATA_DIR, "chunks.pkl")
        self.index = FaissIndex(dim=EMBEDDING_DIM)
        self.load_index()

    def build_from_urls(self, urls: list[str], max_pages_per_site: int = 20) -> FaissIndex:
        all_chunks = set(self.index.texts)  # deduplicate

        for url in urls:
            page_urls = get_all_links(url, max_pages=max_pages_per_site)
            for page_url in page_urls:
                text = scrape_text(page_url)
                chunks = chunk_text(text)
                for chunk in chunks:
                    if chunk not in all_chunks:
                        embedding = get_embedding(chunk)
                        self.index.add([embedding], [chunk])
                        all_chunks.add(chunk)

        self.save_index()
        return self.index

    def save_index(self):
        # Save FAISS index
        self.index.save(self.index_file)

        # Save chunk texts
        with open(self.chunks_file, "wb") as f:
            pickle.dump(self.index.texts, f)

    def load_index(self):
        import faiss
        import pickle
        if os.path.exists(self.index_file) and os.path.exists(self.chunks_file):
            self.index.load(self.index_file)
            with open(self.chunks_file, "rb") as f:
                self.index.texts = pickle.load(f)
