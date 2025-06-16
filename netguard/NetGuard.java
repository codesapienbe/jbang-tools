///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS dev.langchain4j:langchain4j:1.0.1
//DEPS dev.langchain4j:langchain4j-open-ai:1.0.1
//DEPS dev.langchain4j:langchain4j-groq:1.0.1
//DEPS dev.langchain4j:langchain4j-ollama:1.0.1
//DEPS dev.langchain4j:langchain4j-perplexity:1.0.1
//FILES ../common/LLMClient.java

import common.LLMClient;

public class NetGuard {
    public static void main(String... args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: NetGuard <prompt>");
            System.exit(1);
        }
        String prompt = String.join(" ", args);
        String response = LLMClient.create().generate("Network Security Assistant: " + prompt);
        System.out.println(response);
    }
} 