package org.xent.mctalk.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

//http://www.google.com/search?sourceid=chrome&ie=UTF-8&q=java+microphone
//http://stackoverflow.com/questions/3705581/java-sound-api-capturing-microphone
//http://download.oracle.com/javase/tutorial/sound/capturing.html
//http://www.developer.com/java/other/article.php/2105421/Java-Sound-Capturing-Microphone-Data-into-an-Audio-File.htm

/**
 *
 * @author Chance Snow
 */
public class Microphone {

    private AudioFormat format;
    private TargetDataLine microphone;
    private DataLine.Info info;
    private boolean stopped = false;
    private boolean dirty = false;
    private byte[] data = null;

    public Microphone() {
    }

    /**
     * Initialize the microphone.
     *
     * @return Whether or not the microphone was initialized successfully.
     */
    public boolean init() {
        format = new AudioFormat(8000.0f,16,2,true,true);
        info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info))
            return false;

        try {
            microphone = AudioSystem.getTargetDataLine(format);
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    /**
     * Open the line to prepare to receive data.
     *
     * @return Whether or not the line was opened successfully.
     */
    public boolean openLine() {
        if (microphone != null) {
            try {
                microphone.open(format);
            } catch (Exception ex) {
                return false;
            }
            return true;
        } else
            return false;
    }

    /**
     * Close the line after you are no longer receiving data.
     *
     * @return Whether or not the line was closed successfully.
     */
    public boolean closeLine() {
        if (microphone != null) {
            try {
                microphone.close();
            } catch (Exception ex) {
                return false;
            }
            return true;
        } else
            return false;
    }

    /**
     * Start capturing audio data.
     */
    public void start() {
        data = new byte[0];
        microphone.start();
        Thread captureThread = new Thread() {
            @Override
            public void run() {
                while(!stopped) {
                    data = new byte[microphone.available()];
                    microphone.read(data, 0, data.length);
                    dirty = true;
                }
            }
        };
        captureThread.start();
    }

    /**
     * Stop capturing audio data.
     */
    public void stop() {
        stopped = true;
        data = new byte[microphone.available()];
        microphone.read(data, 0, data.length);
        dirty = true;
        microphone.stop();
    }

    public byte[] getBuffer() {
        dirty = false;
        return data;
    }

    public boolean isBufferDirty() {
        return dirty;
    }
}