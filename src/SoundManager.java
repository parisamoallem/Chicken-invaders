import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundManager {

    private static Clip themeClip;
    private static boolean soundEnabled = true;


    public static void playTheme() {
        try {
            if (themeClip == null) {
                File file = new File("resources/theme.wav");
                if (!file.exists()) {
                    System.out.println("فایل موزیک پیدا نشد: " + file.getAbsolutePath());
                    return;
                }
                AudioInputStream audio = AudioSystem.getAudioInputStream(file);
                themeClip = AudioSystem.getClip();
                themeClip.open(audio);
            }
            themeClip.setFramePosition(0);
            themeClip.loop(Clip.LOOP_CONTINUOUSLY);
            themeClip.start();
        } catch (Exception e) {
            System.out.println("خطا در پخش موزیک اصلی: " + e.getMessage());
        }
    }


    public static void stopTheme() {
        if (themeClip != null && themeClip.isRunning()) {
            themeClip.stop();
        }
    }

    public static void setThemeOn(boolean on) {
        if (on) {
            playTheme();
        } else {
            stopTheme();
        }
    }

    public static void playShot() {
        playSFX("resources/mixkit-short-laser-gun-shot-1670.wav");
    }

    public static void playExplosion() {
        playSFX("resources/mixkit-epic-impact-afar-explosion-2782.wav");
    }

    public static void playLifeLost() {
        playSFX("resources/lifelost.wav");
    }

    public static void playGameOver() {
        stopTheme();
        playSFX("resources/mixkit-retro-arcade-game-over-470.wav");
    }

    private static void playSFX(String filePath) {
        if (!soundEnabled) return;

        new Thread(() -> {
            try {
                File soundFile = new File(filePath);
                if (!soundFile.exists()) {
                    return;
                }
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            } catch (Exception e) {
                System.out.println("خطا در پخش افکت صوتی (" + filePath + "): " + e.getMessage());
            }
        }).start();
    }
}