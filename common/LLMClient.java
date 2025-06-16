package common;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.groq.GroqChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.perplexity.PerplexityChatModel;

public class LLMClient {
    private final ChatLanguageModel model;

    private LLMClient(ChatLanguageModel model) {
        this.model = model;
    }

    public static LLMClient create() {
        String backend = System.getenv().getOrDefault("LLM_BACKEND", "ollama").toLowerCase();
        ChatLanguageModel model;
        switch (backend) {
            case "openai":
                String openaiKey = System.getenv("OPENAI_API_KEY");
                model = OpenAiChatModel.builder()
                        .apiKey(openaiKey)
                        .build();
                break;
            case "groq":
                String groqKey = System.getenv("GROQ_API_KEY");
                model = GroqChatModel.builder()
                        .apiKey(groqKey)
                        .build();
                break;
            case "perplexity":
                String perKey = System.getenv("PERPLEXITY_API_KEY");
                model = PerplexityChatModel.builder()
                        .apiKey(perKey)
                        .build();
                break;
            case "ollama":
            default:
                String baseUrl = System.getenv().getOrDefault("OLLAMA_BASE_URL", "http://localhost:11434");
                model = OllamaChatModel.builder()
                        .baseUrl(baseUrl)
                        .build();
                break;
        }
        return new LLMClient(model);
    }

    public String generate(String prompt) {
        return model.chat(prompt).getText();
    }
} 