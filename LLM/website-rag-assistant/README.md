
# Website RAG Assistant

A **Retrieval-Augmented Generation (RAG)** based Q&A support bot that answers questions **only using content crawled from a given website**.

This project demonstrates the complete RAG workflow:
**web crawling → text cleaning → chunking → embeddings → vector storage → retrieval → grounded answer generation → API endpoint**.

---

## 🚀 Features

* Crawls and indexes website content automatically
* Token-aware text chunking with overlap
* Semantic search using FAISS vector database
* OpenAI embeddings and LLM for answer generation
* Strict grounding: answers are generated **only from retrieved context**
* REST API for querying the assistant

---

## 🏗️ Architecture Overview

```
Website URLs
   ↓
Web Scraper (BeautifulSoup)
   ↓
Text Cleaning
   ↓
Token-based Chunking
   ↓
OpenAI Embeddings
   ↓
FAISS Vector Store
   ↓
Similarity Retrieval
   ↓
LLM (RAG Prompt)
   ↓
Answer API (/ask)
```

---

## 📁 Project Structure

```
app/
├── api/
│   └── routes.py            # API endpoints
├── core/
│   ├── llm_client.py        # OpenAI LLM wrapper
│   ├── prompt_templates.py # System prompts
│   └── token_utils.py      # Token counting utilities
├── ingestion/
│   ├── web_scraper.py      # Website crawling & scraping
│   └── chunker.py          # Token-based chunking
├── services/
│   ├── index_builder.py    # Index creation & persistence
│   └── qa_service.py       # Retrieval + generation logic
├── vectorstore/
│   ├── embeddings.py       # Embedding generation
│   └── faiss_index.py      # FAISS wrapper
├── config.py               # Environment configuration
└── main.py                 # Application entry point
```

---

## ⚙️ Setup Instructions

### 1️⃣ Clone the Repository

```bash
git clone <repo-url>
cd website-rag-assistant
```

### 2️⃣ Create Virtual Environment

```bash
python -m venv venv
source venv/bin/activate   # Windows: venv\Scripts\activate
```

### 3️⃣ Install Dependencies

```bash
pip install -r requirements.txt
```

### 4️⃣ Set Environment Variables

Create a `.env` file:

```env
OPENAI_API_KEY=your_api_key_here
OPENAI_MODEL=gpt-4.1-mini
EMBEDDING_MODEL=text-embedding-3-small
```

---

## 🌐 Website Ingestion

Edit the base URLs in `main.py`:

```python
urls = [
    # freeCodeCamp
    "https://www.freecodecamp.org",
    "https://www.freecodecamp.org/news/",

    # Khan Academy
    "https://www.khanacademy.org/about",
    "https://www.khanacademy.org/math",

    # MIT OpenCourseWare
    "https://ocw.mit.edu",
    "https://ocw.mit.edu/about/",
]
```

On first startup, the application will:

1. Crawl the websites
2. Clean and chunk text
3. Generate embeddings
4. Build and persist a FAISS index

Subsequent startups reuse the saved index.

---

## ▶️ Running the Application

```bash
python app/main.py
```

The server starts at:

```
http://127.0.0.1:5000
```

---

## 🔍 API Usage

### Endpoint

```
POST /ask
```

### Request Body

```json
{
  "question": "What is this website about?"
}
```

### Response

```json
{
  "answer": "..."
}
```

If the answer is not present in the crawled content:

```json
{
  "answer": "I don't know based on the provided information."
}
```

---

## 🧪 Example Test Questions (with `curl`)

> Assumes Flask app runs at `http://127.0.0.1:5000/ask`

### 🟢 freeCodeCamp

```bash
# What is freeCodeCamp?
curl -X POST http://127.0.0.1:5000/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "What is freeCodeCamp?"}'

# What subjects does freeCodeCamp teach?
curl -X POST http://127.0.0.1:5000/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "What subjects does freeCodeCamp teach?"}'

# Does freeCodeCamp provide certifications?
curl -X POST http://127.0.0.1:5000/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "Does freeCodeCamp provide certifications?"}'

# Negative test
curl -X POST http://127.0.0.1:5000/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "Does freeCodeCamp offer live 1-on-1 mentorship?"}'
```

### 🟢 Khan Academy

```bash
# What subjects does Khan Academy teach?
curl -X POST http://127.0.0.1:5000/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "What subjects does Khan Academy teach?"}'

# Is Khan Academy free to use?
curl -X POST http://127.0.0.1:5000/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "Is Khan Academy free to use?"}'

# Who is the platform designed for?
curl -X POST http://127.0.0.1:5000/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "Who is the platform designed for?"}'

# Negative test
curl -X POST http://127.0.0.1:5000/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "Does Khan Academy offer paid in-person classes?"}'
```

### 🟢 MIT OpenCourseWare

```bash
# What is MIT OpenCourseWare?
curl -X POST http://127.0.0.1:5000/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "What is MIT OpenCourseWare?"}'

# What materials are provided?
curl -X POST http://127.0.0.1:5000/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "What materials are provided by MIT OpenCourseWare?"}'

# Are certificates mentioned?
curl -X POST http://127.0.0.1:5000/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "Does MIT OpenCourseWare provide certificates?"}'

# Negative test
curl -X POST http://127.0.0.1:5000/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "Does MIT OpenCourseWare offer live tutoring?"}'
```

---

## 🔮 Possible Improvements

* Add source citations in API response
* Improve boilerplate removal during scraping
* Add async crawling for large sites
* Build a frontend chat UI
* Separate ingestion into a standalone CLI script

---
