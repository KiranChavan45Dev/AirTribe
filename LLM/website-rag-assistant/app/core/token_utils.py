import tiktoken

def count_tokens(text: str, model: str) -> int:
    encoder = tiktoken.encoding_for_model(model)
    return len(encoder.encode(text))
