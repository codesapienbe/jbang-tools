package jobops;
///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.7.5
//DEPS com.fasterxml.jackson.core:jackson-databind:2.16.1
//DEPS org.jsoup:jsoup:1.17.2
//DEPS org.apache.pdfbox:pdfbox:2.0.29
//DEPS org.apache.poi:poi:5.2.3
//DEPS org.apache.poi:poi-ooxml:5.2.3
//DEPS org.commonmark:commonmark:0.21.0
//DEPS org.apache.logging.log4j:log4j-core:2.22.1
//DEPS com.microsoft.playwright:playwright:1.52.0


import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import java.time.Duration;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.Clipboard;
import java.awt.Toolkit;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.awt.datatransfer.StringSelection;
import java.nio.file.attribute.FileTime;

import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.BrowserType;

@Command(name = "jobops", mixinStandardHelpOptions = true, version = "1.0",
         description = "AI Motivation Letter Daemon")
public class JobOps implements Callable<Integer> {
    
    private static final Logger logger = LogManager.getLogger(JobOps.class);
    
    private static final String DEFAULT_BACKEND = "ollama";
    private static final String OPENAPI_MODEL = "gpt-4o-mini";
    private static final String GROQ_MODEL = "llama-3.3-70b-versatile";
    private static final String OLLAMA_MODEL = "qwen3:1.7b";
    private static final String OLLAMA_BASE_URL = "http://localhost:11434";
    private static final String OPENAI_BASE_URL = "https://api.openai.com/v1";
    private static final String GROQ_BASE_URL = "https://api.groq.com/openai/v1";
    private static final int CRAWL_DEPTH = 1; // Only crawl the main page

    
    @Option(names = {"-b", "--backend"}, description = "LLM Backend (ollama, openai, groq)", defaultValue = DEFAULT_BACKEND)
    private String backend;
    
    @Option(names = {"--openai-key"}, description = "OpenAI API Key")
    private String openaiKey;
    
    @Option(names = {"--groq-key"}, description = "Groq API Key")
    private String groqKey;

    @Option(names = {"-w", "--watch"}, description = "Directory to watch for job applications", defaultValue = ".")
    private Path watchDir;

    @Option(names = {"--resume"}, description = "Path to resume file (PDF, DOCX, or MD)", defaultValue = "resume.md")
    private Path resumePath;

    @Option(names = {"--export"}, description = "Directory to export generated documents", defaultValue = ".")
    private Path exportDir;
    
    private static final Path USER_HOME = Paths.get(System.getProperty("user.home"));
    private static final Path CONFIG_DIR = USER_HOME.resolve(".jobops");
    
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(30))
        .executor(Executors.newVirtualThreadPerTaskExecutor())
        .build();
    
    // Predefined backend configurations
    private static final Map<String, BackendConfig> BACKEND_CONFIGS = Map.of(
        "ollama", new BackendConfig(OLLAMA_MODEL, OLLAMA_BASE_URL),
        "openai", new BackendConfig(OPENAPI_MODEL, OPENAI_BASE_URL),
        "groq", new BackendConfig(GROQ_MODEL, GROQ_BASE_URL)
    );

    // Thread-safe state management
    private static class ApplicationState {
        private final Path processedUrlsFile;
        private final Set<String> processedUrls;
        private final AtomicReference<String> lastResumeContent = new AtomicReference<>();
        private final AtomicReference<Path> lastResumePath = new AtomicReference<>();
        private final AtomicReference<FileTime> lastResumeModifiedTime = new AtomicReference<>();
        
        public ApplicationState() {
            this.processedUrlsFile = CONFIG_DIR.resolve("processed.txt");
            this.processedUrls = new HashSet<>();
            loadProcessedUrls();
        }
        
        private void loadProcessedUrls() {
            try {
                Files.createDirectories(CONFIG_DIR);
                if (Files.exists(processedUrlsFile)) {
                    processedUrls.addAll(Files.readAllLines(processedUrlsFile));
                }
            } catch (IOException e) {
                logger.error("Error loading processed URLs: " + e.getMessage(), e);
            }
        }
        
        public boolean isUrlProcessed(String url) {
            return processedUrls.contains(url);
        }
        
        public void markUrlProcessed(String url) {
            if (processedUrls.add(url)) {
                try {
                    Files.write(processedUrlsFile, 
                        (url + System.lineSeparator()).getBytes(),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND);
                } catch (IOException e) {
                    logger.error("Error saving processed URL: " + e.getMessage(), e);
                }
            }
        }
        
        public void setLastResume(String content, Path path, FileTime modifiedTime) {
            lastResumeContent.set(content);
            lastResumePath.set(path);
            lastResumeModifiedTime.set(modifiedTime);
        }
        
        public String getLastResumeContent() {
            return lastResumeContent.get();
        }
        
        public Path getLastResumePath() {
            return lastResumePath.get();
        }
        
        public void cleanupOldEntries() {
            // No need to clean up old entries since we're using a file-based approach
        }
    }
    
    private final ApplicationState state = new ApplicationState();
    
    private TrayIcon trayIcon;
    private volatile boolean running = true;
    private volatile boolean watching = true;
    private ScheduledExecutorService scheduler;
    private BackendConfig currentBackend;
    private static final String CLIPBOARD_MONITOR_THREAD = "ClipboardMonitor";
    private Clipboard clipboard;
    private String lastClipboardContent = "";

    public JobOps() {
        // No changes to constructor
    }

    private void loadEnvFile() {
        // First try .env file
        Path envFile = watchDir.resolve(".env");
        if (Files.exists(envFile)) {
            try {
                Map<String, String> env = Files.lines(envFile)
                    .filter(line -> !line.trim().isEmpty() && !line.trim().startsWith("#"))
                    .map(line -> line.split("=", 2))
                    .collect(Collectors.toMap(
                        parts -> parts[0].trim(),
                        parts -> parts.length > 1 ? parts[1].trim() : "",
                        (v1, v2) -> v1
                    ));
                
                // Only set API keys if they're not already set via command line
                if (openaiKey == null || openaiKey.trim().isEmpty()) {
                    openaiKey = env.get("OPENAI_API_KEY");
                    if (openaiKey != null && !openaiKey.trim().isEmpty()) {
                        logger.info("Loaded OpenAI API key from .env file");
                    }
                }
                if (groqKey == null || groqKey.trim().isEmpty()) {
                    groqKey = env.get("GROQ_API_KEY");
                    if (groqKey != null && !groqKey.trim().isEmpty()) {
                        logger.info("Loaded Groq API key from .env file");
                    }
                }
            } catch (IOException e) {
                logger.error("Error reading .env file: " + e.getMessage(), e);
            }
        }

        // If keys are still not set, try config.json
        if ((openaiKey == null || openaiKey.trim().isEmpty() || 
             groqKey == null || groqKey.trim().isEmpty()) && 
            Files.exists(CONFIG_DIR.resolve("config.json"))) {
            try {
                JsonNode config = mapper.readTree(CONFIG_DIR.resolve("config.json").toFile());
                JsonNode appSettings = config.get("app_settings");
                
                if (appSettings != null) {
                    JsonNode tokens = appSettings.get("tokens");
                    if (tokens != null) {
                        if ((openaiKey == null || openaiKey.trim().isEmpty()) && tokens.has("openai")) {
                            openaiKey = tokens.get("openai").asText();
                            if (openaiKey != null && !openaiKey.trim().isEmpty()) {
                                logger.info("Loaded OpenAI API key from config.json");
                            }
                        }
                        if ((groqKey == null || groqKey.trim().isEmpty()) && tokens.has("groq")) {
                            groqKey = tokens.get("groq").asText();
                            if (groqKey != null && !groqKey.trim().isEmpty()) {
                                logger.info("Loaded Groq API key from config.json");
                            }
                        }
                    }
                }
            } catch (IOException e) {
                logger.error("Error reading config.json: " + e.getMessage(), e);
            }
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new JobOps()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        // Check dependencies first
        checkDependencies();
        
        // Validate backend
        if (!BACKEND_CONFIGS.containsKey(backend)) {
            System.err.println("Invalid backend: " + backend + ". Available: " + BACKEND_CONFIGS.keySet());
            return 1;
        }
        
        // Validate watch directory
        if (!Files.exists(watchDir)) {
            Files.createDirectories(watchDir);
        }
        if (!Files.isDirectory(watchDir)) {
            System.err.println("Watch path must be a directory: " + watchDir);
            return 1;
        }
        
        // Validate export directory
        if (!Files.exists(exportDir)) {
            Files.createDirectories(exportDir);
        }
        if (!Files.isDirectory(exportDir)) {
            System.err.println("Export path must be a directory: " + exportDir);
            return 1;
        }
        
        // Validate resume file if provided
        if (resumePath != null) {
            if (!Files.exists(resumePath)) {
                System.err.println("Resume file not found: " + resumePath);
                return 1;
            }
            String fileName = resumePath.getFileName().toString().toLowerCase();
            if (!fileName.endsWith(".pdf") && !fileName.endsWith(".docx") && !fileName.endsWith(".md")) {
                System.err.println("Resume file must be PDF, DOCX, or MD format: " + resumePath);
                return 1;
            }
            // Load resume content immediately
            String content = readFileContent(resumePath);
            if (content != null && !content.trim().isEmpty()) {
                state.setLastResume(content, resumePath, Files.getLastModifiedTime(resumePath));
                logger.info("Resume loaded successfully from: " + resumePath);
            } else {
                System.err.println("Could not read resume content from: " + resumePath);
                return 1;
            }
        }
        
        // Load API keys from .env file first
        loadEnvFile();
        
        currentBackend = BACKEND_CONFIGS.get(backend);
        System.out.println("Starting JobOps with backend: " + backend + " (model: " + currentBackend.model + ")");
        System.out.println("Watching directory: " + watchDir);
        
        // Validate API keys for commercial backends
        if ("openai".equals(backend)) {
            if (openaiKey == null || openaiKey.trim().isEmpty()) {
                System.err.println("OpenAI API key required. Use --openai-key parameter or set OPENAI_API_KEY in .env file");
            return 1;
        }
            System.out.println("OpenAI API key found");
        }
        if ("groq".equals(backend)) {
            if (groqKey == null || groqKey.trim().isEmpty()) {
                System.err.println("Groq API key required. Use --groq-key parameter or set GROQ_API_KEY in .env file");
            return 1;
            }
            System.out.println("Groq API key found");
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                start();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
        
        // Keep main thread alive
        while (running) {
            Thread.sleep(1000);
        }
        
        return 0;
    }

    public void start() throws Exception {
        setupDirectories();
        setupSystemTray();
        startDaemonService();
        
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    private void setupDirectories() throws IOException {
        Files.createDirectories(watchDir);
    }

    private void setupSystemTray() {
        if (!SystemTray.isSupported()) {
            System.err.println("System tray not supported!");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();
        Image image = createTrayIcon();

        PopupMenu popup = new PopupMenu();
        
        MenuItem statusItem = new MenuItem("JobOps AI (" + backend + ") - Running");
        statusItem.setEnabled(false);
        popup.add(statusItem);
        popup.addSeparator();
        
        MenuItem watchItem = new MenuItem("Stop Watching");
        watchItem.addActionListener(e -> toggleWatchMode(watchItem));
        popup.add(watchItem);
        
        MenuItem resumesItem = new MenuItem("View Resumes");
        resumesItem.addActionListener(e -> openDirectory(watchDir));
        popup.add(resumesItem);

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(e -> {
            try {
                shutdown();
            } catch (Exception ex) {
                System.err.println("Error during shutdown: " + ex.getMessage());
            } finally {
                System.exit(0);
            }
        });
        popup.add(exitItem);

        trayIcon = new TrayIcon(image, "JobOps AI Assistant", popup);
        trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
            showNotification("JobOps Started", 
                "AI Assistant running with " + backend + " (" + currentBackend.model + ")", 
                TrayIcon.MessageType.INFO);
        } catch (AWTException e) {
            System.err.println("TrayIcon could not be added: " + e.getMessage());
        }
    }

    private void toggleWatchMode(MenuItem watchItem) {
        watching = !watching;
        if (watching) {
            watchItem.setLabel("Stop Watching");
            showNotification("Watch Mode Enabled", 
                "Resume watching for job applications", 
                TrayIcon.MessageType.INFO);
        } else {
            watchItem.setLabel("Start Watching");
            showNotification("Watch Mode Disabled", 
                "Stopped watching for job applications", 
                TrayIcon.MessageType.INFO);
        }
        updateTrayIcon();
    }

    private void updateTrayIcon() {
        if (trayIcon != null) {
            Image newImage = createTrayIcon();
            trayIcon.setImage(newImage);
        }
    }

    private Image createTrayIcon() {
        int size = 16;
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Different colors based on watching state and backend
        Color bgColor;
        if (!watching) {
            bgColor = new Color(220, 53, 69);  // Red when not watching
        } else {
            bgColor = switch (backend) {
            case "openai" -> new Color(16, 163, 127);  // OpenAI green
            case "groq" -> new Color(255, 102, 0);     // Groq orange
            default -> new Color(0, 120, 212);         // Default blue for Ollama
        };
        }
        
        g2d.setColor(bgColor);
        g2d.fillOval(0, 0, size, size);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (size - fm.stringWidth("J")) / 2;
        int y = (size - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString("J", x, y);
        g2d.dispose();
        
        return image;
    }

    private void startDaemonService() {
        // Use virtual threads for the scheduler
        scheduler = Executors.newScheduledThreadPool(2, Thread.ofVirtual().factory());
        
        // Monitor resumes directory only if no resume path is provided
        scheduler.scheduleAtFixedRate(() -> {
                try {
                if (resumePath == null) {
                    checkForNewResumes();
                } else {
                    checkResumeFile();
                }
                state.cleanupOldEntries();
                } catch (Exception e) {
                    logger.error("Error checking resumes: " + e.getMessage(), e);
                }
        }, 0, 30, TimeUnit.SECONDS);

        // Start clipboard monitoring
        startClipboardMonitoring();
    }

    private void startClipboardMonitoring() {
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        
        Thread monitorThread = new Thread(() -> {
            while (running) {
                try {
                    String content = getClipboardContent();
                    if (content != null && !content.equals(lastClipboardContent)) {
                        lastClipboardContent = content;
                        processClipboardContent(content);
                    }
                    Thread.sleep(1000); // Check every second
                } catch (IllegalStateException e) {
                    // Clipboard access error - wait longer before retrying
                    logger.warn("Clipboard access error, waiting before retry: " + e.getMessage());
                    try {
                        Thread.sleep(5000); // Wait 5 seconds before retrying
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } catch (Exception e) {
                    logger.error("Error monitoring clipboard: " + e.getMessage(), e);
                    try {
                        Thread.sleep(1000); // Wait 1 second before retrying
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }, CLIPBOARD_MONITOR_THREAD);
        
        monitorThread.setDaemon(true);
        monitorThread.start();
    }

    private String getClipboardContent() {
        try {
            Transferable contents = clipboard.getContents(null);
            if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String) contents.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (IllegalStateException e) {
            // Clipboard is busy or not accessible
            logger.warn("Clipboard not accessible: " + e.getMessage());
            throw e; // Rethrow to handle in the monitoring thread
        } catch (Exception e) {
            logger.error("Error getting clipboard content: " + e.getMessage(), e);
        }
        return null;
    }

    private void processClipboardContent(String content) {
        // Only process if watching is enabled
        if (!watching) {
            return;
        }
        
        // Check if content is a URL
        if (content != null && content.matches("https?://\\S+")) {
            // Show confirmation dialog
            SwingUtilities.invokeLater(() -> {
                // Check if URL was already processed
                boolean alreadyProcessed = state.isUrlProcessed(content);
                String message = alreadyProcessed ? 
                    "This job posting has already been processed.\nDo you want to process it again?" :
                    "Process job posting from URL?\n" + content;
                String title = alreadyProcessed ? 
                    "JobOps - Override Previous Processing" :
                    "JobOps - Process Job URL";
                
                int result = JOptionPane.showConfirmDialog(
                    null,
                    message,
                    title,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (result == JOptionPane.YES_OPTION) {
                    // Start processing in a new thread
                    Thread.startVirtualThread(() -> {
                        try {
                            // Scrape the URL content
                            JobData jobData = scrapeJobDescription(content, "", "");
                            
                            // Create timestamp for this application
                            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                            String baseDirName = String.format("%s_%s_%s",
                                sanitizeFilename(jobData.company),
                                sanitizeFilename(jobData.title),
                                timestamp);
                            
                            // Create temporary directory for files
                            Path tempDir = Files.createTempDirectory("jobops_");
                            try {
                                // Ensure directory exists
                                Files.createDirectories(tempDir);
                                
                                // Create job description file
                                String jobDescContent = String.format("""
                                    # %s - %s
                                    
                                    ## Job Description
                                    
                                    %s
                                    
                                    ## Requirements
                                    
                                    %s
                                    
                                    ## Source
                                    
                                    [Original Job Posting](%s)
                                    """,
                                    jobData.company,
                                    jobData.title,
                                    jobData.description,
                                    jobData.requirements,
                                    content);
                                
                                Path jobDescFile = tempDir.resolve("job_description.md");
                                Files.writeString(jobDescFile, jobDescContent);
                                
                                // Get latest resume
                                String resumeContent = getLatestResumeContent();
                                if (resumeContent != null) {
                                    // Save resume as markdown
                                    Path resumeFile = tempDir.resolve("resume.md");
                                    Files.writeString(resumeFile, resumeContent);
                                    
                                    // Generate motivation letter
                                    String letterContent = generateLetterWithLLM(jobData, resumeContent, "en");
                                    Path letterFile = tempDir.resolve("motivation_letter.md");
                                    Files.writeString(letterFile, letterContent);
                                    
                                    // Create zip file
                                    Path zipFile = watchDir.resolve(baseDirName + ".zip");
                                    createApplicationZip(tempDir, zipFile);
                                    
                                    // Mark URL as processed
                                    state.markUrlProcessed(content);
                                    
                                    // Clear clipboard to prevent duplicate processing
                                    try {
                                        StringSelection emptySelection = new StringSelection("");
                                        clipboard.setContents(emptySelection, null);
                                        lastClipboardContent = "";
                                    } catch (Exception e) {
                                        logger.error("Error clearing clipboard: " + e.getMessage(), e);
                                    }
                                    
                                    // Show notification
                                    showNotification("Application Package Created", 
                                        "Application package for " + jobData.company + " has been created.", 
                                        TrayIcon.MessageType.INFO);
                                } else {
                                    showNotification("Warning", 
                                        "No resume found. Please add a resume to continue.", 
                                        TrayIcon.MessageType.WARNING);
                                }
                            } finally {
                                // Clean up temporary directory
                                deleteDirectory(tempDir);
                            }
                            
                        } catch (Exception e) {
                            logger.error("Error processing clipboard URL: " + e.getMessage(), e);
                            showNotification("Error", 
                                "Failed to process job posting URL: " + e.getMessage(), 
                                TrayIcon.MessageType.ERROR);
                        }
                    });
                }
            });
        }
    }

    private void convertMarkdownToPdf(Path markdownFile, Path pdfFile) throws Exception {
        // Use pandoc if available, otherwise use a simple HTML conversion
        try {
            ProcessBuilder pb = new ProcessBuilder("pandoc", 
                markdownFile.toString(),
                "-o", pdfFile.toString(),
                "--pdf-engine=wkhtmltopdf");
            Process p = pb.start();
            int exitCode = p.waitFor();
            if (exitCode != 0) {
                throw new IOException("Pandoc conversion failed with exit code: " + exitCode);
            }
        } catch (IOException e) {
            // Fallback to simple HTML conversion
            String markdown = Files.readString(markdownFile);
            String html = String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; margin: 40px; }
                        h1 { color: #2c3e50; }
                        h2 { color: #34495e; margin-top: 20px; }
                        a { color: #3498db; }
                    </style>
                </head>
                <body>
                    %s
                </body>
                </html>
                """, markdown);
            
            Path htmlFile = markdownFile.resolveSibling(markdownFile.getFileName().toString() + ".html");
            Files.writeString(htmlFile, html);
            
            // Use wkhtmltopdf if available
            try {
                ProcessBuilder pb = new ProcessBuilder("wkhtmltopdf", 
                    htmlFile.toString(),
                    pdfFile.toString());
                Process p = pb.start();
                int exitCode = p.waitFor();
                if (exitCode != 0) {
                    throw new IOException("wkhtmltopdf conversion failed with exit code: " + exitCode);
                }
            } catch (IOException ex) {
                // If all conversions fail, just copy the markdown file
                Files.copy(markdownFile, pdfFile, StandardCopyOption.REPLACE_EXISTING);
                logger.warn("PDF conversion failed, using markdown file instead: " + ex.getMessage());
            }
        }
    }

    private void createApplicationZip(Path sourceDir, Path zipFile) throws IOException {
        // Ensure the export directory exists
        Files.createDirectories(exportDir);
        
        // Create the zip file in the export directory
        Path targetZipFile = exportDir.resolve(zipFile.getFileName());
        
        try (var fs = FileSystems.newFileSystem(targetZipFile, Map.of("create", "true"))) {
            Files.walk(sourceDir)
                .filter(path -> !Files.isDirectory(path))
                .forEach(path -> {
                    try {
                        Path targetPath = fs.getPath("/" + sourceDir.relativize(path).toString());
                        Files.copy(path, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        logger.error("Error adding file to zip: " + e.getMessage(), e);
                    }
                });
        }
    }

    private void deleteDirectory(Path dir) throws IOException {
        Files.walk(dir)
            .sorted(Comparator.reverseOrder())
            .forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    logger.error("Error deleting file: " + e.getMessage(), e);
                }
            });
    }

    private void checkForNewResumes() throws IOException {
        if (!Files.exists(watchDir)) return;
        
        try (var stream = Files.list(watchDir)) {
            var files = stream.filter(Files::isRegularFile)
                  .filter(path -> path.toString().toLowerCase().matches(".*\\.(txt|md|pdf|docx)$"))
                  .toList();
            
            for (Path file : files) {
                processResumeFile(file);
            }
        }
    }

    private void processResumeFile(Path resumePath) {
        // Use virtual thread for resume processing
        Thread.startVirtualThread(() -> {
            try {
                if (!Files.exists(resumePath) || Files.size(resumePath) == 0) {
                    logger.info("Skipping empty or non-existent file: " + resumePath);
                    return;
                }

                FileTime currentModifiedTime = Files.getLastModifiedTime(resumePath);
                FileTime lastModifiedTime = state.lastResumeModifiedTime.get();
                
                // Only process if file is new or has been modified
                if (lastModifiedTime == null || !currentModifiedTime.equals(lastModifiedTime)) {
                String content = readFileContent(resumePath);
                    if (content != null && !content.trim().isEmpty()) {
                        state.setLastResume(content, resumePath, currentModifiedTime);
                        logger.info("Resume content updated successfully");
                    }
                } else {
                    logger.info("Resume file unchanged, using cached content");
                }
                
            } catch (IOException e) {
                logger.error("Error reading resume file " + resumePath + ": " + e.getMessage(), e);
            } catch (Exception e) {
                logger.error("Unexpected error processing resume " + resumePath + ": " + e.getMessage(), e);
            }
        });
    }

    private void processApplicationFile(Path filePath) {
        // Use virtual thread for file processing
        Thread.startVirtualThread(() -> {
            try {
                if (!Files.exists(filePath) || Files.size(filePath) == 0) {
                    logger.info("Skipping empty or non-existent file: " + filePath);
                    return;
                }

                String content = readFileContent(filePath);
                if (content == null || content.trim().isEmpty()) {
                    logger.info("Skipping empty content file: " + filePath);
                    return;
                }

                // Check if this is a URL-only file
                if (content.trim().matches("https?://\\S+")) {
                    String url = content.trim();
                    ProgressDialog progressDialog = ProgressDialog.getInstance("Processing Job URL", 
                        "Please wait while we process the job posting...");
                    progressDialog.setVisible(true);

                    try {
                        progressDialog.updateProgress("Connecting to job posting...", 10);
                        JobData jobData = scrapeJobDescription(url, "", "");
                        
                        progressDialog.updateProgress("Processing job content...", 60);
                        try {
                        Thread.sleep(500);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            logger.warn("Job processing interrupted");
                        }
                        
                        // Create temporary directory for files
                        Path tempDir = Files.createTempDirectory("jobops_");
                        try {
                            // Ensure directory exists
                            Files.createDirectories(tempDir);
                            
                            // Create job description file
                            String jobDescContent = String.format("""
                            # %s - %s
                            
                            ## Job Description
                            
                            %s
                            
                            ## Requirements
                            
                            %s
                            
                            ## Source
                            
                            [Original Job Posting](%s)
                            """,
                            jobData.company,
                            jobData.title,
                            jobData.description,
                            jobData.requirements,
                            url);
                        
                            Path jobDescFile = tempDir.resolve("job_description.md");
                            Files.writeString(jobDescFile, jobDescContent);
                            
                            // Convert to PDF
                            convertMarkdownToPdf(jobDescFile, tempDir.resolve("job_description.pdf"));
                            
                            // Get latest resume
                            String resumeContent = getLatestResumeContent();
                            if (resumeContent != null) {
                                // Save resume as PDF
                                Path resumeFile = tempDir.resolve("resume.md");
                                Files.writeString(resumeFile, resumeContent);
                                convertMarkdownToPdf(resumeFile, tempDir.resolve("resume.pdf"));
                                
                                // Generate motivation letter
                                String letterContent = generateLetterWithLLM(jobData, resumeContent, "en");
                                Path letterFile = tempDir.resolve("motivation_letter.md");
                                Files.writeString(letterFile, letterContent);
                                convertMarkdownToPdf(letterFile, tempDir.resolve("motivation_letter.pdf"));
                                
                                // Create zip file
                                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                                String baseDirName = String.format("%s_%s_%s",
                                    sanitizeFilename(jobData.company),
                                    sanitizeFilename(jobData.title),
                                    timestamp);
                                Path zipFile = watchDir.resolve(baseDirName + ".zip");
                                createApplicationZip(tempDir, zipFile);
                                
                                // Show notification
                                showNotification("Application Package Created", 
                                    "Application package for " + jobData.company + " has been created.", 
                                    TrayIcon.MessageType.INFO);
                            } else {
                                showNotification("Warning", 
                                    "No resume found. Please add a resume to continue.", 
                                    TrayIcon.MessageType.WARNING);
                            }
                        } finally {
                            // Clean up temporary directory
                            deleteDirectory(tempDir);
                        }
                        
                        progressDialog.updateProgress("Job description saved!", 100);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            logger.warn("Job processing interrupted");
                        }
                        progressDialog.dispose();

                } catch (Exception e) {
                    progressDialog.dispose();
                        logger.error("Error processing URL: " + e.getMessage(), e);
                    throw e;
                }
                }
            } catch (IOException e) {
                logger.error("Error reading application file " + filePath + ": " + e.getMessage(), e);
            } catch (Exception e) {
                logger.error("Unexpected error processing application " + filePath + ": " + e.getMessage(), e);
            }
        });
    }

    private void generateMotivationLetterFromContent(Path filePath, String content, ProgressDialog progressDialog) throws Exception {
        String resumeContent = state.getLastResumeContent();
        if (resumeContent == null) {
            throw new RuntimeException("No resume found. Please add a resume to " + watchDir);
        }

        try {
            // Extract company and job title from the filename
            String filename = filePath.getFileName().toString();
            String[] parts = filename.split("_");
            String company = parts.length > 0 ? parts[0] : "Unknown Company";
            String jobTitle = parts.length > 1 ? parts[1] : "Unknown Position";

            progressDialog.updateProgress("Analyzing job requirements...", 70);
            Thread.sleep(500);

            progressDialog.updateProgress("Generating letter content...", 80);
            String letterContent = generateLetterWithLLM(new JobData(company, jobTitle, content), resumeContent, "en");
            
            progressDialog.updateProgress("Checking for duplicates...", 85);
            Thread.sleep(500);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy"));
            String outputFilename = String.format("%s_%s_%s.md",
                sanitizeFilename(company),
                sanitizeFilename(jobTitle),
                timestamp);
            
            Path outputPath = watchDir.resolve(outputFilename);
            
            // Check if a similar letter already exists
            if (isDuplicateLetter(company, jobTitle)) {
                progressDialog.updateProgress("Similar letter already exists!", 100);
                Thread.sleep(1000);
                progressDialog.dispose();
                showNotification("Duplicate Letter", 
                    "A similar letter for " + company + " already exists.", 
                    TrayIcon.MessageType.WARNING);
                return;
            }
            
            progressDialog.updateProgress("Saving letter...", 90);
            Files.writeString(outputPath, letterContent);
            
            progressDialog.updateProgress("Letter generated successfully!", 100);
            Thread.sleep(1000);
            progressDialog.dispose();
            
            showNotification("Letter Generated", 
                "Motivation letter created for " + company, 
                TrayIcon.MessageType.INFO);
                
        } catch (Exception e) {
            progressDialog.dispose();
            logger.error("Error generating letter: " + e.getMessage(), e);
            throw e;
        }
    }

    private String readFileContent(Path filePath) throws IOException {
        String fileName = filePath.getFileName().toString().toLowerCase();
        
        if (fileName.endsWith(".pdf")) {
            try (PDDocument document = PDDocument.load(filePath.toFile())) {
                PDFTextStripper stripper = new PDFTextStripper();
                return stripper.getText(document);
            }
        } else if (fileName.endsWith(".docx")) {
            try (XWPFDocument document = new XWPFDocument(Files.newInputStream(filePath))) {
                XWPFWordExtractor extractor = new XWPFWordExtractor(document);
                return extractor.getText();
            }
        } else if (fileName.endsWith(".md")) {
            String content = Files.readString(filePath, java.nio.charset.StandardCharsets.UTF_8);
            Parser parser = Parser.builder().build();
            TextContentRenderer renderer = TextContentRenderer.builder().build();
            return renderer.render(parser.parse(content));
        } else {
            // Default to plain text
            return Files.readString(filePath, java.nio.charset.StandardCharsets.UTF_8);
        }
    }

    private void showGenerateDialog() {
        SwingUtilities.invokeLater(() -> {
            JDialog dialog = new JDialog((Frame) null, "Generate Motivation Letter", true);
            dialog.setSize(500, 350);
            dialog.setLocationRelativeTo(null);
            dialog.setLayout(new BorderLayout());

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.WEST;

            // Show current backend info
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            JLabel backendLabel = new JLabel("Backend: " + backend + " (" + currentBackend.model + ")");
            backendLabel.setFont(backendLabel.getFont().deriveFont(Font.BOLD));
            panel.add(backendLabel, gbc);

            // Job URL
            gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
            panel.add(new JLabel("Job URL:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            JTextField urlField = new JTextField(30);
            panel.add(urlField, gbc);

            // Company
            gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
            panel.add(new JLabel("Company:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            JTextField companyField = new JTextField(30);
            panel.add(companyField, gbc);

            // Job Title
            gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
            panel.add(new JLabel("Job Title:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            JTextField titleField = new JTextField(30);
            panel.add(titleField, gbc);

            // Language
            gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
            panel.add(new JLabel("Language:"), gbc);
            gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            JComboBox<String> languageBox = new JComboBox<>(new String[]{"en", "nl"});
            panel.add(languageBox, gbc);

            // Buttons
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton generateBtn = new JButton("Generate");
            JButton cancelBtn = new JButton("Cancel");

            generateBtn.addActionListener(e -> {
                String url = urlField.getText().trim();
                if (url.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please enter a job URL", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                dialog.dispose();
                
                new Thread(() -> {
                    try {
                        generateMotivationLetter(url, companyField.getText().trim(), 
                                               titleField.getText().trim(), 
                                               (String) languageBox.getSelectedItem());
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() -> 
                            showNotification("Generation Failed", ex.getMessage(), TrayIcon.MessageType.ERROR)
                        );
                    }
                }).start();
            });

            cancelBtn.addActionListener(e -> dialog.dispose());

            buttonPanel.add(generateBtn);
            buttonPanel.add(cancelBtn);

            dialog.add(panel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            dialog.setVisible(true);
        });
    }

    private void showCurrentConfig() {
        SwingUtilities.invokeLater(() -> {
            String config = String.format("""
                JobOps Configuration:
                
                Backend: %s
                Model: %s
                Base URL: %s
                
                Directories:
                Watch Directory: %s
                
                API Keys:
                OpenAI: %s
                Groq: %s
                """, 
                backend, 
                currentBackend.model, 
                currentBackend.baseUrl,
                watchDir,
                openaiKey != null ? "Set" : "Not set",
                groqKey != null ? "Set" : "Not set"
            );
            
            JOptionPane.showMessageDialog(null, config, "JobOps Configuration", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void generateMotivationLetter(String jobUrl, String company, String jobTitle, String language) throws Exception {
        String resumeContent = getLatestResumeContent();
        if (resumeContent == null) {
            throw new RuntimeException("No resume found. Please add a resume to " + watchDir);
        }

        // Show progress dialog
        ProgressDialog progressDialog = new ProgressDialog("Generating Motivation Letter", 
            "Please wait while we generate your motivation letter...");
        progressDialog.setVisible(true);

        try {
            progressDialog.updateProgress("Scraping job description...", 20);
            JobData jobData = scrapeJobDescription(jobUrl, company, jobTitle);
            
            progressDialog.updateProgress("Generating letter content...", 50);
            String letterContent = generateLetterWithLLM(jobData, resumeContent, language);
            
            progressDialog.updateProgress("Saving letter...", 80);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String baseDirName = String.format("%s_%s_%s",
                sanitizeFilename(jobData.company),
                sanitizeFilename(jobData.title),
                timestamp);
            
            // Create temporary directory for files
            Path tempDir = Files.createTempDirectory("jobops_");
            try {
                // Ensure directory exists
                Files.createDirectories(tempDir);
                
                // Create job description file
                String jobDescContent = String.format("""
                    # %s - %s
                    
                    ## Job Description
                    
                    %s
                    
                    ## Requirements
                    
                    %s
                    
                    ## Source
                    
                    [Original Job Posting](%s)
                    """,
                    jobData.company,
                    jobData.title,
                    jobData.description,
                    jobData.requirements,
                    jobUrl);
                
                Path jobDescFile = tempDir.resolve("job_description.md");
                Files.writeString(jobDescFile, jobDescContent);
                
                // Convert to PDF
                convertMarkdownToPdf(jobDescFile, tempDir.resolve("job_description.pdf"));
                
                // Save resume as PDF
                Path resumeFile = tempDir.resolve("resume.md");
                Files.writeString(resumeFile, resumeContent);
                convertMarkdownToPdf(resumeFile, tempDir.resolve("resume.pdf"));
                
                // Save motivation letter
                Path letterFile = tempDir.resolve("motivation_letter.md");
                Files.writeString(letterFile, letterContent);
                convertMarkdownToPdf(letterFile, tempDir.resolve("motivation_letter.pdf"));
                
                // Create zip file
                Path zipFile = watchDir.resolve(baseDirName + ".zip");
                createApplicationZip(tempDir, zipFile);
            
            progressDialog.updateProgress("Letter generated successfully!", 100);
            progressDialog.dispose();
            
            showNotification("Letter Generated", 
                "Motivation letter created for " + jobData.company, 
                TrayIcon.MessageType.INFO);
            } finally {
                // Clean up temporary directory
                deleteDirectory(tempDir);
            }
                
        } catch (Exception e) {
            progressDialog.dispose();
            logger.error("Error generating letter: " + e.getMessage(), e);
            throw e;
        }
    }

    private boolean isDuplicateLetter(String company, String jobTitle) {
        // Since we're no longer using a database, we'll check for existing zip files
        try (var stream = Files.list(watchDir)) {
            return stream
                .filter(Files::isRegularFile)
                .filter(path -> path.toString().endsWith(".zip"))
                .anyMatch(path -> {
                    String filename = path.getFileName().toString();
                    return filename.startsWith(sanitizeFilename(company) + "_" + sanitizeFilename(jobTitle));
                });
        } catch (IOException e) {
            logger.error("Error checking for duplicate letters: " + e.getMessage(), e);
            return false;
        }
    }

    private JobData scrapeJobDescription(String url, String company, String jobTitle) throws Exception {
        try {
            // Use Playwright for headless browser automation without external Chrome install
            String html;
            try (Playwright playwright = Playwright.create()) {
                Browser browser = playwright.chromium()
                                      .launch(new BrowserType.LaunchOptions().setHeadless(true));
                Page page = browser.newPage();
                page.navigate(url);
                html = page.content();
                browser.close();
            }
            // Parse the loaded HTML with Jsoup for further processing
            Document doc = Jsoup.parse(html);

            // Extract company and title if not provided
            if (company == null || company.isEmpty()) {
                company = extractCompanyFromHtml(doc);
            }
            if (jobTitle == null || jobTitle.isEmpty()) {
                jobTitle = extractJobTitleFromHtml(doc);
            }

            // Get all text content and format as markdown
            StringBuilder content = new StringBuilder();
            
            // Add title
            content.append("# ").append(jobTitle).append("\n\n");
            
            // Add company
            content.append("## ").append(company).append("\n\n");
            
            // Get main content
            String mainContent = doc.body().text();
            
            // Clean up the content
            String cleanedContent = mainContent
                .replaceAll("\\s+", " ")  // Replace multiple spaces with single space
                .replaceAll("(?m)^\\s*$[\n\r]{1,}", "\n\n")  // Replace multiple empty lines with double newline
                .trim();
                
            content.append(cleanedContent);

            return new JobData(company, jobTitle, content.toString(), "");
        } catch (Exception e) {
            logger.error("Error scraping job description from " + url + ": " + e.getMessage(), e);
            throw new IOException("Failed to scrape job description: " + e.getMessage(), e);
        }
    }

    private String extractCompanyFromHtml(Document doc) {
        // Try to find company name in common locations
        String company = doc.select("meta[property=og:site_name]").attr("content");
        if (company.isEmpty()) {
            company = doc.select("meta[name=author]").attr("content");
        }
        if (company.isEmpty()) {
            var element = doc.select("a[href*=about], a[href*=company]").first();
            if (element != null) {
                company = element.text();
            }
        }
        return company.isEmpty() ? "Unknown Company" : company;
    }

    private String extractJobTitleFromHtml(Document doc) {
        // Try to find job title in common locations
        String title = doc.select("meta[property=og:title]").attr("content");
        if (title.isEmpty()) {
            var element = doc.select("h1").first();
            if (element != null) {
                title = element.text();
            }
        }
        if (title.isEmpty()) {
            title = doc.select("title").text();
        }
        return title.isEmpty() ? "Unknown Position" : title;
    }

    private static class JobData {
        final String company;
        final String title;
        final String description;
        final String requirements;

        JobData(String company, String title, String content) {
            this(company, title, content, "");
        }

        JobData(String company, String title, String description, String requirements) {
            this.company = company;
            this.title = title;
            this.description = description;
            this.requirements = requirements;
        }
    }

    private static class ProgressDialog extends JDialog {
        private JProgressBar progressBar;
        private JLabel statusLabel;
        private static ProgressDialog instance;
        private volatile boolean isDisposing = false;
        
        public static synchronized ProgressDialog getInstance(String title, String initialMessage) {
            if (instance == null || !instance.isVisible()) {
                instance = new ProgressDialog(title, initialMessage);
            }
            return instance;
        }
        
        private ProgressDialog(String title, String initialMessage) {
            super((Frame) null, title, false);
            setSize(400, 150);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
            setAlwaysOnTop(true);
            
            // Create progress bar
            progressBar = new JProgressBar(0, 100);
            progressBar.setStringPainted(true);
            
            // Create status label
            statusLabel = new JLabel(initialMessage);
            
            // Add components
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(progressBar, BorderLayout.CENTER);
            panel.add(statusLabel, BorderLayout.SOUTH);
            add(panel);
        }
        
        public void updateProgress(String message, int progress) {
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText(message);
                progressBar.setValue(progress);
            });
        }
    }

    private void openDirectory(Path dir) {
        try {
            Desktop.getDesktop().open(dir.toFile());
        } catch (IOException e) {
            logger.error("Error opening directory: " + e.getMessage(), e);
            showNotification("Error", "Could not open directory: " + dir, TrayIcon.MessageType.ERROR);
        }
    }

    private void showNotification(String title, String message, TrayIcon.MessageType type) {
        if (trayIcon != null) {
            // Add click listener to open the export directory
            trayIcon.addActionListener(e -> {
                try {
                    Desktop.getDesktop().open(exportDir.toFile());
                } catch (IOException ex) {
                    logger.error("Error opening export directory: " + ex.getMessage(), ex);
                }
            });
            trayIcon.displayMessage(title, message, type);
        }
    }

    private String sanitizeFilename(String input) {
        return input.replaceAll("[^a-zA-Z0-9.-]", "_");
    }

    private static class BackendConfig {
        final String model;
        final String baseUrl;

        BackendConfig(String model, String baseUrl) {
            this.model = model;
            this.baseUrl = baseUrl;
        }
    }

    private void shutdown() {
        running = false;
        if (scheduler != null) {
                scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        if (trayIcon != null) {
            SystemTray.getSystemTray().remove(trayIcon);
        }
    }

    private String getLatestResumeContent() {
        // If resume path was provided via command line, use that
        if (resumePath != null && Files.exists(resumePath)) {
            try {
                FileTime currentModifiedTime = Files.getLastModifiedTime(resumePath);
                FileTime lastModifiedTime = state.lastResumeModifiedTime.get();
                
                // Only read the file if it's new or has been modified
                if (lastModifiedTime == null || !currentModifiedTime.equals(lastModifiedTime)) {
                    String content = readFileContent(resumePath);
                    if (content != null && !content.trim().isEmpty()) {
                        state.setLastResume(content, resumePath, currentModifiedTime);
                        return content;
                    }
                } else {
                    // Return cached content if file hasn't changed
                    String cachedContent = state.getLastResumeContent();
                    if (cachedContent != null && !cachedContent.trim().isEmpty()) {
                        return cachedContent;
                    }
                }
            } catch (IOException e) {
                logger.error("Error reading resume file: " + e.getMessage(), e);
            }
        } else if (resumePath == null) {
            // Only search in watch directory if no resume path was provided
            try (var stream = Files.list(watchDir)) {
                var latestResume = stream
                    .filter(Files::isRegularFile)
                    .filter(path -> {
                        String name = path.getFileName().toString().toLowerCase();
                        return name.endsWith(".md") || name.endsWith(".docx") || name.endsWith(".pdf");
                    })
                    .max((path1, path2) -> {
                        try {
                            return Files.getLastModifiedTime(path1).compareTo(Files.getLastModifiedTime(path2));
                        } catch (IOException e) {
                            return 0;
                        }
                    });
                
                if (latestResume.isPresent()) {
                    Path path = latestResume.get();
                    FileTime currentModifiedTime = Files.getLastModifiedTime(path);
                    FileTime lastModifiedTime = state.lastResumeModifiedTime.get();
                    
                    // Only read the file if it's new or has been modified
                    if (lastModifiedTime == null || !currentModifiedTime.equals(lastModifiedTime)) {
                        String content = readFileContent(path);
                        if (content != null && !content.trim().isEmpty()) {
                            state.setLastResume(content, path, currentModifiedTime);
                            return content;
                        }
                    } else {
                        // Return cached content if file hasn't changed
                        String cachedContent = state.getLastResumeContent();
                        if (cachedContent != null && !cachedContent.trim().isEmpty()) {
                            return cachedContent;
                        }
                    }
                }
                
                showNotification("Warning", 
                    "No resume found. Please add a resume file (.md, .docx, or .pdf) to " + watchDir + 
                    " or specify one with --resume", 
                    TrayIcon.MessageType.WARNING);
            } catch (IOException e) {
                logger.error("Error searching for resume: " + e.getMessage(), e);
            }
        }
        return null;
    }

    private String generateLetterWithLLM(JobData jobData, String resumeContent, String language) throws Exception {
        // Prepare the prompt for the LLM
        String prompt = String.format("""
            Generate a motivation letter for the following job:
            
            Company: %s
            Position: %s
            
            Job Description:
            %s
            
            Requirements:
            %s
            
            My Resume:
            %s
            
            Please write a professional motivation letter in %s language.
            Focus on matching my experience with the job requirements.
            Keep it concise and impactful.
            """,
            jobData.company,
            jobData.title,
            jobData.description,
            jobData.requirements,
            resumeContent,
            language
        );

        // Call the appropriate LLM backend
        String response;
        switch (backend) {
            case "ollama":
                response = callOllama(prompt);
                break;
            case "openai":
                response = callOpenAI(prompt);
                break;
            case "groq":
                response = callGroq(prompt);
                break;
            default:
                throw new IllegalStateException("Unsupported backend: " + backend);
        }

        return response;
    }

    private String callOllama(String prompt) throws Exception {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(currentBackend.baseUrl + "/api/generate"))
                .timeout(Duration.ofSeconds(60))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(String.format("""
                    {
                        "model": "%s",
                        "prompt": %s,
                        "stream": false
                    }
                    """, currentBackend.model, mapper.writeValueAsString(prompt))))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("Ollama API error: " + response.statusCode());
            }

            JsonNode json = mapper.readTree(response.body());
            return json.get("response").asText();
        } catch (Exception e) {
            logger.error("Error calling Ollama API: " + e.getMessage(), e);
            throw new IOException("Failed to generate response from Ollama: " + e.getMessage(), e);
        }
    }

    private String callOpenAI(String prompt) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(currentBackend.baseUrl + "/chat/completions"))
            .timeout(Duration.ofSeconds(30))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + openaiKey)
            .POST(HttpRequest.BodyPublishers.ofString(String.format("""
                {
                    "model": "%s",
                    "messages": [
                        {"role": "system", "content": "You are a professional career coach helping to write motivation letters."},
                        {"role": "user", "content": %s}
                    ],
                    "temperature": 0.7
                }
                """, currentBackend.model, mapper.writeValueAsString(prompt))))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("OpenAI API error: " + response.statusCode());
        }

        JsonNode json = mapper.readTree(response.body());
        return json.get("choices").get(0).get("message").get("content").asText();
    }

    private String callGroq(String prompt) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(currentBackend.baseUrl + "/chat/completions"))
            .timeout(Duration.ofSeconds(30))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + groqKey)
            .POST(HttpRequest.BodyPublishers.ofString(String.format("""
                {
                    "model": "%s",
                    "messages": [
                        {"role": "system", "content": "You are a professional career coach helping to write motivation letters."},
                        {"role": "user", "content": %s}
                    ],
                    "temperature": 0.7
                }
                """, currentBackend.model, mapper.writeValueAsString(prompt))))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Groq API error: " + response.statusCode());
        }

        JsonNode json = mapper.readTree(response.body());
        return json.get("choices").get(0).get("message").get("content").asText();
    }

    private void processResumeContent(String content) {
        // For now, we just store the resume content in memory
        // This could be extended to perform additional processing if needed
        if (content != null && !content.trim().isEmpty()) {
            // The content will be used when generating motivation letters
            logger.info("Resume content processed successfully");
        }
    }

    private void checkResumeFile() {
        try {
            if (Files.exists(resumePath)) {
                FileTime currentModifiedTime = Files.getLastModifiedTime(resumePath);
                FileTime lastModifiedTime = state.lastResumeModifiedTime.get();
                
                if (lastModifiedTime == null || !currentModifiedTime.equals(lastModifiedTime)) {
                    String content = readFileContent(resumePath);
                    if (content != null && !content.trim().isEmpty()) {
                        state.setLastResume(content, resumePath, currentModifiedTime);
                        logger.info("Resume content updated from watched file: " + resumePath);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error checking resume file: " + e.getMessage(), e);
        }
    }

    private void checkDependencies() {
        try {
            // Test each dependency by creating an instance
            new ObjectMapper();
            Jsoup.parse("<html><body>Test</body></html>");
            new PDDocument();
            new XWPFDocument();
            Parser.builder().build();
            TextContentRenderer.builder().build();
            LogManager.getLogger(JobOps.class);
            
            logger.info("All dependencies loaded successfully");
        } catch (Exception e) {
            logger.error("Error loading dependencies: " + e.getMessage(), e);
            System.err.println("Error loading dependencies. Please ensure you have an internet connection for the first run.");
            System.exit(1);
        }
    }
}
