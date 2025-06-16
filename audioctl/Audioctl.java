///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS dev.langchain4j:langchain4j:1.0.1
//DEPS dev.langchain4j:langchain4j-open-ai:1.0.1
//DEPS dev.langchain4j:langchain4j-groq:1.0.1
//DEPS dev.langchain4j:langchain4j-ollama:1.0.1
//DEPS dev.langchain4j:langchain4j-perplexity:1.0.1
//FILES ../common/LLMClient.java

import common.LLMClient;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.time.Instant;

public class Audioctl {
    public static void main(String... args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: audioctl <question or command>");
            System.exit(1);
        }
        String prompt = String.join(" ", args);
        LLMClient llm = LLMClient.create();
        String response = llm.generate("Audio Control Expert: " + prompt);
        System.out.println(response);
    }
}

// Abstract designs for the audioctl module following SOLID and secure coding practices

// Generic asynchronous use-case interface
interface UseCase<I, O> {
    CompletableFuture<O> execute(I input);
}

// Value object representing an audio device
final class AudioDevice {
    private final String id;
    private final String name;
    private final AudioDeviceType type;

    public AudioDevice(String id, String name, AudioDeviceType type) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.type = Objects.requireNonNull(type, "type cannot be null");
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public AudioDeviceType getType() { return type; }
}

enum AudioDeviceType {
    INPUT,
    OUTPUT
}

// Command and result abstractions
final class ConnectDeviceCommand {
    private final String deviceId;

    public ConnectDeviceCommand(String deviceId) {
        this.deviceId = Objects.requireNonNull(deviceId, "deviceId cannot be null");
    }

    public String getDeviceId() { return deviceId; }
}

final class DeviceConnectedResult {
    private final Instant timestamp;

    public DeviceConnectedResult(Instant timestamp) {
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }

    public Instant getTimestamp() { return timestamp; }
}

// Use-case interface for connecting an audio device
interface ConnectDeviceUseCase extends UseCase<ConnectDeviceCommand, DeviceConnectedResult> {}

// Domain-specific exceptions
class AudioException extends Exception {
    public AudioException(String message) { super(message); }
    public AudioException(String message, Throwable cause) { super(message, cause); }
}

class DeviceNotFoundException extends AudioException {
    public DeviceNotFoundException(String deviceId) {
        super("Audio device not found: " + deviceId);
    }
}

// Command and result abstractions for device disconnection
final class DisconnectDeviceCommand {
    private final String deviceId;

    public DisconnectDeviceCommand(String deviceId) {
        this.deviceId = Objects.requireNonNull(deviceId, "deviceId cannot be null");
    }

    public String getDeviceId() { return deviceId; }
}

final class DeviceDisconnectedResult {
    private final Instant timestamp;

    public DeviceDisconnectedResult(Instant timestamp) {
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }

    public Instant getTimestamp() { return timestamp; }
}

// Use-case interface for disconnecting an audio device
interface DisconnectDeviceUseCase extends UseCase<DisconnectDeviceCommand, DeviceDisconnectedResult> {}

// Value object representing the current audio state
final class AudioState {
    private final boolean playing;
    private final int volumeLevel;
    private final Instant timestamp;

    public AudioState(boolean playing, int volumeLevel, Instant timestamp) {
        this.playing = playing;
        this.volumeLevel = volumeLevel;
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }

    public boolean isPlaying() { return playing; }
    public int getVolumeLevel() { return volumeLevel; }
    public Instant getTimestamp() { return timestamp; }
}

// Commands for playback and volume
final class PlaybackCommand {
    private final String deviceId;

    public PlaybackCommand(String deviceId) {
        this.deviceId = Objects.requireNonNull(deviceId, "deviceId cannot be null");
    }

    public String getDeviceId() { return deviceId; }
}

final class VolumeCommand {
    private final String deviceId;
    private final int volumeLevel;

    public VolumeCommand(String deviceId, int volumeLevel) {
        this.deviceId = Objects.requireNonNull(deviceId, "deviceId cannot be null");
        this.volumeLevel = volumeLevel;
    }

    public String getDeviceId() { return deviceId; }
    public int getVolumeLevel() { return volumeLevel; }
}

// Result objects for playback and volume
final class PlaybackResult {
    private final AudioState state;

    public PlaybackResult(AudioState state) {
        this.state = Objects.requireNonNull(state, "state cannot be null");
    }

    public AudioState getState() { return state; }
}

final class VolumeSetResult {
    private final AudioState state;

    public VolumeSetResult(AudioState state) {
        this.state = Objects.requireNonNull(state, "state cannot be null");
    }

    public AudioState getState() { return state; }
}

// Use-case interfaces for playback and volume
interface StartPlaybackUseCase extends UseCase<PlaybackCommand, PlaybackResult> {}
interface StopPlaybackUseCase extends UseCase<PlaybackCommand, PlaybackResult> {}
interface SetVolumeUseCase extends UseCase<VolumeCommand, VolumeSetResult> {}

// Domain-specific exceptions for other operations
class DeviceDisconnectionException extends AudioException {
    public DeviceDisconnectionException(String deviceId) {
        super("Failed to disconnect audio device: " + deviceId);
    }
}

class PlaybackFailedException extends AudioException {
    public PlaybackFailedException(String deviceId) {
        super("Playback failed on device: " + deviceId);
    }
}

class VolumeSettingException extends AudioException {
    public VolumeSettingException(String deviceId, int volumeLevel) {
        super("Failed to set volume to " + volumeLevel + " on device: " + deviceId);
    }
} 