import java.io.*;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class PresetManager {
  private final Map<String, Preset> presets = new LinkedHashMap<>();

  // creates and stores presets
  public PresetManager() {
    this.initialize();
  }

  public Preset getPreset(String name) {
    return presets.get(name);
  }

  public void initialize() {
    if (!this.loadFromFile()) {
      loadDefaults();
    }
  }

  public void loadDefaults() {
    addPreset("Classic", new Preset("Classic", "Traditional Pomodoro technique", 1, 1, 2, 4));
    addPreset(
        "Quick Focus",
        new Preset("Quick Focus", "Short sessions for low-energy days", 15, 3, 15, 4));
    addPreset(
        "Deep Work",
        new Preset("Deep Work", "Long focus sessions for demanding tasks", 50, 10, 30, 3));
    addPreset("Study", new Preset("Study", "Balanced sessions for studying", 30, 5, 20, 4));
    addPreset(
        "Monk Mode",
        new Preset(
            "Monk Mode", "Extended focus sessions for extreme concentration", 90, 10, 45, 2));
  }

  public void addPreset(String name, Preset preset) {
    if (presets.containsKey(name)) {
      return;
    }
    presets.put(name, preset);
    this.saveToFile();
  }

  public void removePreset(String name) {
    if (presets.containsKey(name)) {
      presets.remove(name);
      this.saveToFile();
    }
  }

  public Collection<Preset> getPresets() {
    return presets.values();
  }

  public void saveToFile() {
    try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("presets.dat"))) {
      output.writeObject(presets); // write the whole map at once
    } catch (IOException e) {
      System.err.println("Error saving to presets.dat: " + e.getMessage());
    }
  }

  public boolean loadFromFile() {
    try (ObjectInputStream input = new ObjectInputStream(new FileInputStream("presets.dat"))) {
      Map<String, Preset> loaded = (Map<String, Preset>) input.readObject();
      presets.clear();
      presets.putAll(loaded);
      return true;
    } catch (IOException e) {
      System.err.println("Error loading presets.dat: " + e.getMessage());
      return false;
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
