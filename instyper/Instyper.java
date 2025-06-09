///usr/bin/env jbang "$0" "$@" ; exit $?
//REPO sonatypeSnapshots::https://oss.sonatype.org/content/repositories/snapshots/
//DEPS com.alphacephei:vosk:0.3.32
//DEPS net.java.dev.jna:jna:5.7.0
//DEPS org.slf4j:slf4j-simple:1.7.30
//DEPS org.json:json:20230227
//DEPS com.formdev:flatlaf:3.1.1
//DEPS org.apache.commons:commons-compress:1.21
//JAVA 24

import org.vosk.Model;
import org.vosk.Recognizer;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.*;
import java.util.prefs.Preferences;
import javax.swing.*;
import java.net.URL;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.image.BufferedImage;

public class Instyper {

    static final String APP_NAME = "Instant Typer";
    static final String USER_HOME = System.getProperty("user.home");
    static final Path USER_DIR = Paths.get(USER_HOME, ".instyper");
    static final Path MODELS_DIR = USER_DIR.resolve("models");
    static final String DEFAULT_MODEL = "vosk-model-small-en-us-0.15";
    static final Path CONFIG_PATH = USER_DIR.resolve("config.json");
    static final Path LOG_PATH = USER_DIR.resolve("instyper.log");
    static final Path FIRST_RUN_FLAG = USER_DIR.resolve(".first_run_complete");
    
    static AtomicBoolean isListening = new AtomicBoolean(false);
    static Recognizer recognizer;
    static TargetDataLine microphone;
    static Robot robot;
    static TrayIcon trayIcon;
    static Preferences prefs = Preferences.userRoot().node("/com/instyper");
    static String currentModel;
    static Map<String,String> modelOptions = new LinkedHashMap<>();
    static JDialog loadingDialog;
    static JProgressBar progressBar;
    
    static final int AUDIO_RATE = 16000;
    static final int AUDIO_CHUNK = 4000;
    static final int AUDIO_CHANNELS = 1;
    static final int FRAMES_PER_BUFFER = 8000;

    static Model currentModelInstance;
    static JDialog modelLoadingDialog;
    static JProgressBar modelProgressBar;
    static JLabel modelStatusLabel;

    static WatchService watchService;
    static WatchKey watchKey;
    static Set<String> currentModels = new HashSet<>();

    public static void main(String... args) throws Exception {
        System.setProperty("flatlaf.uiScale", "1.0");
        UIManager.setLookAndFeel(new FlatLightLaf());
        
        initializeDirectories();
        loadConfiguration();
        initializeAudioSystem();
        createSystemTray();
        initializeSpeechRecognition();
        registerGlobalHotkey();
        showFirstRunTutorial();
        startModelsWatcher();
        
        while(true) Thread.sleep(1000);
    }

    static void initializeDirectories() throws IOException {
        Files.createDirectories(MODELS_DIR);
        if(!Files.exists(CONFIG_PATH)) Files.writeString(CONFIG_PATH, "{}");
        if(!Files.exists(LOG_PATH)) Files.createFile(LOG_PATH);
    }
    
    static void loadConfiguration() {
        currentModel = prefs.get("model", DEFAULT_MODEL);
        loadModelOptions();
    }
    
    static void initializeAudioSystem() throws AWTException, LineUnavailableException {
        robot = new Robot();
        AudioFormat format = new AudioFormat(AUDIO_RATE, 16, AUDIO_CHANNELS, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        microphone = (TargetDataLine) AudioSystem.getLine(info);
    }
    
    static void createSystemTray() throws IOException, UnsupportedLookAndFeelException, AWTException {
        if(!SystemTray.isSupported()) return;
        
        PopupMenu popup = new PopupMenu();
        MenuItem toggleItem = new MenuItem("Start Listening");
        toggleItem.addActionListener(e -> toggleListening(toggleItem));
        
        Menu modelsMenu = new Menu("Select Model");
        populateModelsMenu(modelsMenu);
        
        MenuItem editModelsItem = new MenuItem("Edit Models");
        editModelsItem.addActionListener(e -> openModelsConfig());
        
        popup.add(toggleItem);
        popup.add(modelsMenu);
        popup.add(editModelsItem);
        popup.addSeparator();
        popup.add(createExitItem());
        
        Image icon = createTrayIconImage();
        trayIcon = new TrayIcon(icon, APP_NAME, popup);
        trayIcon.setImageAutoSize(true);
        SystemTray.getSystemTray().add(trayIcon);
    }
    
    static void initializeSpeechRecognition() throws IOException {
        Path modelPath = resolveModelPath(currentModel);
        if(!Files.exists(modelPath)) {
            updateModelLoadingStatus("Downloading model...");
            downloadModel(currentModel);
        }
        
        updateModelLoadingStatus("Loading model...");
        try {
            // Get the backend from models.json
            String backend = getModelBackend(currentModel);
            
            // Initialize based on backend
            switch (backend.toLowerCase()) {
                case "vosk":
                    currentModelInstance = new Model(modelPath.toString());
                    recognizer = new Recognizer(currentModelInstance, AUDIO_RATE);
                    break;
                case "whisper":
                case "coqui":
                    // Add support for other backends here
                    throw new IOException("Backend " + backend + " is not yet supported");
                default:
                    throw new IOException("Unknown backend: " + backend);
            }
        } catch (IOException e) {
            System.err.println("Failed to load model at " + modelPath + ": " + e.getMessage());
            if (!DEFAULT_MODEL.equals(currentModel)) {
                System.err.println("Falling back to default model " + DEFAULT_MODEL);
                currentModel = DEFAULT_MODEL;
                prefs.put("model", currentModel);
                modelPath = resolveModelPath(currentModel);
                if (!Files.exists(modelPath)) {
                    updateModelLoadingStatus("Downloading default model...");
                    downloadModel(currentModel);
                }
                updateModelLoadingStatus("Loading default model...");
                currentModelInstance = new Model(modelPath.toString());
                recognizer = new Recognizer(currentModelInstance, AUDIO_RATE);
            } else {
                throw e;
            }
        }
    }
    
    static void registerGlobalHotkey() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if(e.getID() == KeyEvent.KEY_PRESSED && e.isControlDown() && e.isAltDown() && e.getKeyCode() == KeyEvent.VK_SPACE) {
                toggleListening(null);
                return true;
            }
            return false;
        });
    }
    
    static void toggleListening(MenuItem item) {
        if(isListening.get()) stopListening();
        else startListening();
        
        if(item != null) item.setLabel(isListening.get() ? "Stop Listening" : "Start Listening");
    }
    
    static void startListening() {
        isListening.set(true);
        Executors.newVirtualThreadPerTaskExecutor().submit(() -> {
            try {
                microphone.open(new AudioFormat(AUDIO_RATE, 16, 1, true, false));
                microphone.start();
                
                byte[] buffer = new byte[4096];
                while(isListening.get()) {
                    int bytesRead = microphone.read(buffer, 0, buffer.length);
                    if(recognizer.acceptWaveForm(buffer, bytesRead)) {
                        String result = recognizer.getResult();
                        String text = parseResult(result);
                        if(!text.isEmpty()) typeText(text);
                    }
                }
            } catch (LineUnavailableException e) {
                showError("Audio Error", e.getMessage());
            }
        });
    }
    
    static void stopListening() {
        isListening.set(false);
        microphone.stop();
        microphone.close();
    }
    
    static String parseResult(String json) {
        JSONObject obj = new JSONObject(json);
        return obj.optString("text", "");
    }
    
    static void typeText(String text) {
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.delay(50);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }
    
    static Path resolveModelPath(String modelName) {
        Path voskModels = MODELS_DIR.resolve("vosk");
        Path modelPath = voskModels.resolve(modelName);
        if(!Files.exists(modelPath)) modelPath = MODELS_DIR.resolve(modelName);
        return modelPath;
    }
    
    static void downloadModel(String modelName) throws IOException {
        showLoadingDialog();
        
        String zipUrl = "https://alphacephei.com/vosk/models/" + modelName + ".zip";
        Path zipPath = MODELS_DIR.resolve(modelName + ".zip");
        
        try(InputStream in = new URL(zipUrl).openStream()) {
            Files.copy(in, zipPath, StandardCopyOption.REPLACE_EXISTING);
        }
        
        try(ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipPath))) {
            ZipEntry entry;
            while((entry = zis.getNextEntry()) != null) {
                Path outputPath = MODELS_DIR.resolve(entry.getName());
                if(entry.isDirectory()) Files.createDirectories(outputPath);
                else Files.copy(zis, outputPath);
            }
        }
        
        Files.delete(zipPath);
        prefs.put("model", modelName);
        hideLoadingDialog();
    }
    
    static void populateModelsMenu(Menu menu) {
        menu.removeAll(); // Clear existing items
        modelOptions.forEach((displayName, modelId) -> {
            MenuItem item = new MenuItem(displayName);
            item.addActionListener(e -> setModel(modelId));
            // Add check mark to currently selected model
            if (modelId.equals(currentModel)) {
                item.setLabel("✓ " + displayName);
                item.setEnabled(false); // Optionally disable the selected item
            }
            menu.add(item);
        });
    }
    
    static void setModel(String model) {
        prefs.put("model", model);
        currentModel = model;
        
        // Show loading dialog
        showModelLoadingDialog("Loading new model...");
        
        // Load model in virtual thread
        Thread.startVirtualThread(() -> {
            try {
                // Cleanup previous model
                cleanupModel();
                
                // Initialize new model
                initializeSpeechRecognition();
                
                // Update UI on EDT
                SwingUtilities.invokeLater(() -> {
                    hideModelLoadingDialog();
                    trayIcon.displayMessage(APP_NAME, "Model switched successfully", TrayIcon.MessageType.INFO);
                    // Update the models menu to show new selection
                    PopupMenu popup = trayIcon.getPopupMenu();
                    for (int i = 0; i < popup.getItemCount(); i++) {
                        if (popup.getItem(i) instanceof Menu && popup.getItem(i).getLabel().equals("Select Model")) {
                            populateModelsMenu((Menu) popup.getItem(i));
                            break;
                        }
                    }
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    hideModelLoadingDialog();
                    showError("Model Error", e.getMessage());
                });
            }
        });
    }
    
    static void cleanupModel() {
        if (recognizer != null) {
            recognizer.close();
            recognizer = null;
        }
        if (currentModelInstance != null) {
            currentModelInstance.close();
            currentModelInstance = null;
        }
        if (watchService != null) {
            try {
                watchService.close();
            } catch (IOException e) {
                System.err.println("Error closing watch service: " + e.getMessage());
            }
        }
        // Force garbage collection
        System.gc();
    }
    
    static void showModelLoadingDialog(String message) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            modelLoadingDialog = new JDialog(frame, "Loading Model", true);
            modelProgressBar = new JProgressBar();
            modelProgressBar.setIndeterminate(true);
            modelStatusLabel = new JLabel(message);
            
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(modelStatusLabel, BorderLayout.NORTH);
            panel.add(modelProgressBar, BorderLayout.CENTER);
            
            modelLoadingDialog.add(panel);
            modelLoadingDialog.setSize(300, 100);
            modelLoadingDialog.setLocationRelativeTo(null);
            modelLoadingDialog.setVisible(true);
        });
    }
    
    static void hideModelLoadingDialog() {
        if (modelLoadingDialog != null) {
            modelLoadingDialog.dispose();
            modelLoadingDialog = null;
        }
    }
    
    static void updateModelLoadingStatus(String message) {
        if (modelStatusLabel != null) {
            SwingUtilities.invokeLater(() -> modelStatusLabel.setText(message));
        }
    }
    
    static void loadModelOptions() {
        modelOptions.clear();
        
        try {
            JSONObject config = new JSONObject(Files.readString(USER_DIR.resolve("models.json")));
            JSONArray models = config.getJSONArray("models");
            
            for(int i=0; i<models.length(); i++) {
                JSONObject model = models.getJSONObject(i);
                String backend = model.getString("backend");
                String name = model.getString("name");
                String modelId = model.getString("model");
                
                // Simple format: backend + name
                String displayName = backend + ": " + name;
                modelOptions.put(displayName, modelId);
            }
            
            // If no models were loaded, add default Vosk model
            if (modelOptions.isEmpty()) {
                modelOptions.put("vosk: English", "vosk-model-small-en-us-0.15");
            }
        } catch (IOException e) {
            // Only add default model if file doesn't exist or is invalid
            modelOptions.put("vosk: English", "vosk-model-small-en-us-0.15");
        }
    }
    
    static String getModelBackend(String modelId) throws IOException {
        try {
            JSONObject config = new JSONObject(Files.readString(USER_DIR.resolve("models.json")));
            JSONArray models = config.getJSONArray("models");
            for(int i=0; i<models.length(); i++) {
                JSONObject model = models.getJSONObject(i);
                if(model.getString("model").equals(modelId)) {
                    return model.getString("backend");
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading models.json: " + e.getMessage());
        }
        return "vosk"; // Default to vosk if not found
    }
    
    static Image createTrayIconImage() {
        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.GREEN);
        g.fillOval(16, 16, 32, 32);
        g.dispose();
        return image;
    }
    
    static MenuItem createExitItem() {
        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(e -> {
            try {
                // Stop listening if active
                if (isListening.get()) {
                    stopListening();
                }
                
                // Cleanup model resources
                cleanupModel();
                
                // Remove tray icon
                SystemTray.getSystemTray().remove(trayIcon);
                
                // Exit the application
                System.exit(0);
            } catch (Exception ex) {
                System.err.println("Error during shutdown: " + ex.getMessage());
                System.exit(1);
            }
        });
        return exit;
    }
    
    // Add shutdown hook to ensure cleanup even if process is terminated
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (isListening.get()) {
                    stopListening();
                }
                cleanupModel();
            } catch (Exception e) {
                System.err.println("Error during shutdown hook: " + e.getMessage());
            }
        }));
    }
    
    static void showLoadingDialog() {
        JFrame frame = new JFrame();
        loadingDialog = new JDialog(frame, "Downloading Model", true);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        
        loadingDialog.add(progressBar);
        loadingDialog.setSize(300, 75);
        loadingDialog.setLocationRelativeTo(null);
        loadingDialog.setVisible(true);
    }
    
    static void hideLoadingDialog() {
        if(loadingDialog != null) {
            loadingDialog.dispose();
            loadingDialog = null;
        }
    }
    
    static void showError(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    static void showFirstRunTutorial() {
        if(Files.exists(FIRST_RUN_FLAG)) return;
        
        JOptionPane.showMessageDialog(null, """
                                            Welcome to Instant Typer!
                                            
                                            Use Ctrl+Alt+Space to start/stop voice typing.
                                            Right-click the tray icon to change models.""", 
            "First Run Tutorial", JOptionPane.INFORMATION_MESSAGE);
        
        try { Files.createFile(FIRST_RUN_FLAG); } 
        catch (IOException e) { /* Ignore */ }
    }
    
    static void openModelsConfig() {
        try {
            Path modelsConfig = USER_DIR.resolve("models.json");
            if (!Files.exists(modelsConfig)) {
                // Create default models.json if it doesn't exist
                JSONObject config = new JSONObject();
                JSONArray models = new JSONArray();
                
                // Add only the default Vosk model
                JSONObject defaultModel = new JSONObject();
                defaultModel.put("name", "English");
                defaultModel.put("model", "vosk-model-small-en-us-0.15");
                defaultModel.put("backend", "vosk");
                models.put(defaultModel);
                
                config.put("models", models);
                Files.writeString(modelsConfig, config.toString(2));
            }
            
            // Open file with OS-specific default text editor
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder pb;
            
            if (os.contains("win")) {
                pb = new ProcessBuilder("notepad", modelsConfig.toString());
            } else if (os.contains("mac")) {
                pb = new ProcessBuilder("open", modelsConfig.toString());
            } else {
                // Linux and other Unix-like systems
                pb = new ProcessBuilder("xdg-open", modelsConfig.toString());
            }
            
            pb.start();
            
            // Show message about reloading
            trayIcon.displayMessage(APP_NAME, 
                "After editing models.json, restart the application to apply changes.", 
                TrayIcon.MessageType.INFO);
                
        } catch (Exception e) {
            showError("Error", "Failed to open models configuration: " + e.getMessage());
        }
    }
    
    static void startModelsWatcher() throws IOException {
        watchService = FileSystems.getDefault().newWatchService();
        Path modelsConfig = USER_DIR.resolve("models.json");
        watchKey = modelsConfig.getParent().register(watchService, 
            StandardWatchEventKinds.ENTRY_MODIFY);
            
        // Store initial models
        updateCurrentModels();
        
        // Start watching in a virtual thread
        Thread.startVirtualThread(() -> {
            try {
                while (true) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.context().toString().equals("models.json")) {
                            handleModelsConfigChange();
                        }
                    }
                    key.reset();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    static void updateCurrentModels() {
        try {
            JSONObject config = new JSONObject(Files.readString(USER_DIR.resolve("models.json")));
            JSONArray models = config.getJSONArray("models");
            currentModels.clear();
            for (int i = 0; i < models.length(); i++) {
                JSONObject model = models.getJSONObject(i);
                if (model.getString("backend").equals("vosk")) {
                    currentModels.add(model.getString("model"));
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading models.json: " + e.getMessage());
        }
    }
    
    static void handleModelsConfigChange() {
        try {
            // Wait a bit to ensure file is completely written
            Thread.sleep(100);
            
            JSONObject config = new JSONObject(Files.readString(USER_DIR.resolve("models.json")));
            JSONArray models = config.getJSONArray("models");
            Set<String> newModels = new HashSet<>();
            
            // Get new model list
            for (int i = 0; i < models.length(); i++) {
                JSONObject model = models.getJSONObject(i);
                if (model.getString("backend").equals("vosk")) {
                    newModels.add(model.getString("model"));
                }
            }
            
            // Find removed models
            Set<String> removedModels = new HashSet<>(currentModels);
            removedModels.removeAll(newModels);
            
            // Handle removed models
            for (String model : removedModels) {
                handleRemovedModel(model);
            }
            
            // Update current models
            currentModels = newModels;
            
            // Reload model options
            loadModelOptions();
            
        } catch (Exception e) {
            System.err.println("Error handling models.json change: " + e.getMessage());
        }
    }
    
    static void handleRemovedModel(String modelName) {
        SwingUtilities.invokeLater(() -> {
            int response = JOptionPane.showConfirmDialog(null,
                "Model '" + modelName + "' has been removed from the configuration.\n" +
                "Would you like to delete the model files as well?",
                "Remove Model Files",
                JOptionPane.YES_NO_OPTION);
                
            if (response == JOptionPane.YES_OPTION) {
                try {
                    Path modelPath = resolveModelPath(modelName);
                    if (Files.exists(modelPath)) {
                        deleteDirectory(modelPath);
                        trayIcon.displayMessage(APP_NAME,
                            "Model files for '" + modelName + "' have been removed.",
                            TrayIcon.MessageType.INFO);
                    }
                } catch (IOException e) {
                    showError("Error", "Failed to remove model files: " + e.getMessage());
                }
            }
        });
    }
    
    static void deleteDirectory(Path path) throws IOException {
        Files.walk(path)
            .sorted(Comparator.reverseOrder())
            .forEach(p -> {
                try {
                    Files.delete(p);
                } catch (IOException e) {
                    System.err.println("Error deleting " + p + ": " + e.getMessage());
                }
            });
    }
}
