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
        
        Menu modelsMenu = new Menu("Models");
        populateModelsMenu(modelsMenu);
        
        popup.add(toggleItem);
        popup.add(modelsMenu);
        popup.addSeparator();
        popup.add(createExitItem());
        
        Image icon = createTrayIconImage();
        trayIcon = new TrayIcon(icon, APP_NAME, popup);
        trayIcon.setImageAutoSize(true);
        SystemTray.getSystemTray().add(trayIcon);
    }
    
    static void initializeSpeechRecognition() throws IOException {
        Path modelPath = resolveModelPath(currentModel);
        if(!Files.exists(modelPath)) downloadModel(currentModel);
        
        Model model;
        try {
            model = new Model(modelPath.toString());
        } catch (IOException e) {
            System.err.println("Failed to load model at " + modelPath + ": " + e.getMessage());
            if (!DEFAULT_MODEL.equals(currentModel)) {
                System.err.println("Falling back to default model " + DEFAULT_MODEL);
                currentModel = DEFAULT_MODEL;
                prefs.put("model", currentModel);
                modelPath = resolveModelPath(currentModel);
                if (!Files.exists(modelPath)) downloadModel(currentModel);
                model = new Model(modelPath.toString());
            } else {
                throw e;
            }
        }
        recognizer = new Recognizer(model, AUDIO_RATE);
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
        modelOptions.forEach((name, model) -> {
            MenuItem item = new MenuItem(name);
            item.addActionListener(e -> setModel(model));
            item.setEnabled(!model.equals(currentModel));
            menu.add(item);
        });
    }
    
    static void setModel(String model) {
        prefs.put("model", model);
        currentModel = model;
        try {
            initializeSpeechRecognition();
        } catch (IOException e) {
            showError("Model Error", e.getMessage());
        }
    }
    
    static void loadModelOptions() {
        try {
            JSONObject config = new JSONObject(Files.readString(USER_DIR.resolve("models.json")));
            JSONArray models = config.getJSONArray("models");
            for(int i=0; i<models.length(); i++) {
                JSONObject model = models.getJSONObject(i);
                if(model.getString("backend").equals("vosk")) {
                    modelOptions.put(model.getString("name"), model.getString("model"));
                }
            }
        } catch (IOException e) {
            // Default models
            modelOptions.put("English Small", "vosk-model-small-en-us-0.15");
            modelOptions.put("German", "vosk-model-small-de-zamia-0.3");
        }
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
            SystemTray.getSystemTray().remove(trayIcon);
            System.exit(0);
        });
        return exit;
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
}
