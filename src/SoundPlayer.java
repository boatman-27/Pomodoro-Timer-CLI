import java.io.InputStream;
import javax.sound.sampled.*;

public class SoundPlayer {

  public static void play(String resourcePath) {
    try {
      InputStream audioSrc =
              SoundPlayer.class.getResourceAsStream(resourcePath);

      if (audioSrc == null) {
        System.err.println("Sound not found: " + resourcePath);
        return;
      }

      AudioInputStream audioStream =
              AudioSystem.getAudioInputStream(audioSrc);

      Clip clip = AudioSystem.getClip();
      clip.open(audioStream);
      clip.start();

    } catch (Exception e) {
      System.err.println("Failed to play sound: " + e.getMessage());
    }
  }
}
