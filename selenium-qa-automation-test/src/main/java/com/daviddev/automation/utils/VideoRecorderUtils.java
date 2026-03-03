package com.daviddev.automation.utils;

import org.monte.media.Format;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.monte.media.AudioFormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

/**
 * Utility class for recording video using Monte Screen Recorder.
 */
public class VideoRecorderUtils {
    
    private static final String RECORDINGS_DIR = "./recordings/";
    private static final int FPS = 15;
    private static final float QUALITY = 0.8f;
    
    private ScreenRecorder screenRecorder;
    private File currentRecordingFile;
    
    /**
     * Starts recording the screen.
     *
     * @param testClassName the name of the test class
     * @param testMethodName the name of the test method
     * @throws IOException if recording cannot be started
     * @throws AWTException if the graphics environment cannot be accessed
     */
    public void startRecording(String testClassName, String testMethodName) 
            throws IOException, AWTException {
        
        // Create recordings directory if it doesn't exist
        File recordingsDir = new File(RECORDINGS_DIR);
        if (!recordingsDir.exists()) {
            recordingsDir.mkdirs();
        }
        
        // Get the graphics configuration
        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();
        
        // Create the screen recorder
        screenRecorder = new ScreenRecorder(gc,
                new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                        CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                        DepthKey, 24, FrameRateKey, Rational.valueOf(FPS),
                        QualityKey, QUALITY, KeyFrameIntervalKey, 15 * FPS),
                new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black",
                        FrameRateKey, Rational.valueOf(FPS)),
                null);
        
        // Start recording
        screenRecorder.start();
        System.out.println("Video recording started for: " + testClassName + "." + testMethodName);
        
        // Generate expected filename for tracking
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testClassName + "_" + testMethodName + "_" + timestamp + ".avi";
        currentRecordingFile = new File(recordingsDir, fileName);
    }
    
    /**
     * Stops the recording and saves the video file.
     *
     * @return the File object of the saved video
     * @throws IOException if recording cannot be stopped
     */
    public File stopRecording() throws IOException {
        if (screenRecorder != null) {
            screenRecorder.stop();
            System.out.println("Video recording stopped");
            
            // Move the recorded file to the correct location with the correct name
            java.util.List<File> createdFiles = screenRecorder.getCreatedMovieFiles();
            if (createdFiles != null && !createdFiles.isEmpty()) {
                File recordedFile = createdFiles.get(0);
                if (currentRecordingFile != null && !recordedFile.equals(currentRecordingFile)) {
                    if (recordedFile.renameTo(currentRecordingFile)) {
                        System.out.println("Video saved to: " + currentRecordingFile.getAbsolutePath());
                        return currentRecordingFile;
                    }
                }
                System.out.println("Video saved to: " + recordedFile.getAbsolutePath());
                return recordedFile;
            }
        }
        return null;
    }
    
    /**
     * Gets the recordings directory path.
     *
     * @return the recordings directory path
     */
    public static String getRecordingsDirectory() {
        return RECORDINGS_DIR;
    }
    
    /**
     * Checks if a recording is currently in progress.
     *
     * @return true if recording, false otherwise
     */
    public boolean isRecording() {
        return screenRecorder != null && screenRecorder.getState() == ScreenRecorder.State.RECORDING;
    }
}
