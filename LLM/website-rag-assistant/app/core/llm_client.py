from openai import OpenAI
from app.config import OPENAI_API_KEY, OPENAI_MODEL

class LlmClient:
    def __init__(self) -> None:
        self.client = OpenAI(api_key=OPENAI_API_KEY)

    def generate(
        self,
        system_prompt: str,
        user_input: str,
        temperature: float = 0.2,
    ) -> str:
        response = self.client.responses.create(
            model=OPENAI_MODEL,
            input=[
                {"role": "system", "content": system_prompt},
                {"role": "user", "content": user_input},
            ],
            temperature=temperature,
        )
        return response.output_text
