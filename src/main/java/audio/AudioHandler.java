package audio;

import helpers.Constants;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javax.sound.sampled.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

/**
 * Abstract class which provides audio output and input capabilities. Subclass of SynthesizerV2,
 * so the pitch and speed of the MP3 can be edited through children of this class.
 */
public abstract class AudioHandler extends SynthesizerV2 implements Runnable {

    /**
     * Constructor
     */
    public AudioHandler() {
        super(Constants.googleAPIKey);
    }

    /**
     * Will need to be overriden in subclass
     */
    @Override
    public abstract void run();

    /**
     * Converts a String message into an InputStream of MP3 data, which is returned in a Runnable that's
     * run method can be called to play the MP3 using JLayer.
     *
     * @param text a String message
     * @return a Runnable
     */
    protected Runnable textToSpeech(String text) {
        return () -> {
            try {

                // Using JLayer to play MP3 data
                AdvancedPlayer player = new AdvancedPlayer(getMP3Data(text));
                player.play();

            } catch (IOException | JavaLayerException e) {
                e.printStackTrace();
            }
        };
    }

    /**
     * Calls Python script to convert speech to text. Returns a String of the spoken words,
     * or what the API interprets was said.
     *
     * @return a String of the words spoken by the user
     * @throws IOException
     * @throws InterruptedException
     */
    protected String speechToText() throws IOException, InterruptedException {

        // Start the Python script and wait for it to complete before continuing
        Process process = new ProcessBuilder("python", "src/main/python/speech_to_text.py")
                .redirectErrorStream(true)
                .start();
        System.out.println("Listening");
        process.waitFor();

        // Will read values from script
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String s;
        String userResponse = null;
        while ((s = input.readLine()) != null) {
            System.out.println("Response: " + s);
            userResponse = s;
        }
        return userResponse;
    }

    /**
     * Plays a song from a .wav file. User can end song by saying "stop".
     *
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     * @throws InterruptedException
     */
    protected void playNiceSong() throws UnsupportedAudioFileException, IOException,
            LineUnavailableException, InterruptedException {

        InputStream song = AudioHandler.class.getClassLoader().getResourceAsStream("nicesong.wav");
        CountDownLatch latch = new CountDownLatch(1); // will need to call countDown 1 time to continue main thread

        try (AudioInputStream ais = AudioSystem.getAudioInputStream(song)) {
            Clip clip = AudioSystem.getClip();

            // if the clip is stopped resume the main thread
            clip.addLineListener(l -> {
                if (l.getType() == LineEvent.Type.STOP) {
                    latch.countDown();
                }
            });
            clip.open(ais);
            clip.start();

            // check if user says to stop the song
            while (clip.isOpen()){
                String s = speechToText();
                if (s.equalsIgnoreCase("stop") || s.contains("stop")) {
                    clip.close();
                }
            }
        }
        latch.await(); // pauses current thread until countDown is called
    }
}
