import requests
from bs4 import BeautifulSoup
from urllib.parse import urljoin, urlparse
from typing import List, Set

def scrape_text(url: str) -> str:
    """Scrape <p> text from a single URL"""
    response = requests.get(url, timeout=10)
    soup = BeautifulSoup(response.text, "lxml")
    text = " ".join(p.get_text() for p in soup.find_all("p"))
    cleaned = " ".join(text.split())
    return cleaned if len(cleaned) > 50 else ""


def get_all_links(base_url: str, max_pages: int = 20) -> List[str]:
    """Crawl internal links from the base_url up to max_pages"""
    visited: Set[str] = set()
    to_visit: List[str] = [base_url]
    links: List[str] = []

    while to_visit and len(links) < max_pages:
        current = to_visit.pop(0)
        if current in visited:
            continue
        visited.add(current)

        try:
            response = requests.get(current, timeout=10)
            soup = BeautifulSoup(response.text, "lxml")
            links.append(current)

            # Only follow internal links
            for a in soup.find_all("a", href=True):
                href = urljoin(base_url, a['href']).split("#")[0].split("?")[0]
                if urlparse(href).netloc == urlparse(base_url).netloc:
                    to_visit.append(href)
        except Exception:
            continue

    return links
