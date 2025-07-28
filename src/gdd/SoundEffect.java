package gdd;

import java.io.File;
import javax.sound.sampled.*;

public class SoundEffect {
    public static void play(String filePath, float volume) {
        new Thread(() -> {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);

                // ✅ ปรับ volume ด้วย FloatControl
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float dB = Math.max(-80, Math.min(6, volume)); // clamp ให้อยู่ในช่วงที่ลำโพงรับได้
                gainControl.setValue(dB);

                clip.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // 🔄 Shortcut: default volume = 0 dB
    public static void play(String filePath) {
        play(filePath, 0f);
    }
}
