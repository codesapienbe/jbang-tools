///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.7.5
//DEPS dev.langchain4j:langchain4j:1.0.1
//DEPS dev.langchain4j:langchain4j-open-ai:1.0.1
//DEPS dev.langchain4j:langchain4j-groq:1.0.1
//DEPS dev.langchain4j:langchain4j-ollama:1.0.1
//DEPS dev.langchain4j:langchain4j-perplexity:1.0.1
//FILES ../common/LLMClient.java

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import common.LLMClient;

@Command(name = "apitest", mixinStandardHelpOptions = true, description = "Generate JUnit tests from an OpenAPI spec file")
public class ApiTest implements Callable<Integer> {

    @Option(names = {"-s", "--spec"}, description = "Path to OpenAPI spec file (JSON/YAML)", required = true)
    private Path specFile;

    public static void main(String... args) {
        int exitCode = new CommandLine(new ApiTest()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        String spec = Files.readString(specFile);
        LLMClient client = LLMClient.create();
        String prompt = "Generate Java JUnit test cases for the following OpenAPI spec:\n" + spec;
        String tests = client.generate(prompt);
        System.out.println(tests);
        return 0;
    }
}
