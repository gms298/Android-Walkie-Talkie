package com.example.nam_o.walkietalkie;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.*;
import android.content.Context;

import at.markushi.ui.CircleButton;


public class MainConversation extends AppCompatActivity {

    private CircleButton audioBtn;
    private Context context;
    private BluetoothSocket bSocket = null;
    private Thread recordingThread = null;
    private Thread playThread = null;
    private AudioRecord recorder = null;
    private AudioTrack track = null;
    private AudioManager am = null;
    private InputStream inStream;
    private OutputStream outStream;
    private byte buffer[] = null;
    private byte playBuffer[] = null;
    int minSize = AudioTrack.getMinBufferSize(16000, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
    private int bufferSize = minSize;
    private boolean isRecording = false;

    // Based on previous project and
    // http://stackoverflow.com/questions/8499042/android-audiorecord-example
    // Record Audio
    public void startRecording() {
        Log.d("AUDIO", "Assigning recorder");
        buffer = new byte[bufferSize];

        // Start Recording
        recorder.startRecording();
        isRecording = true;
        // Start a thread
        recordingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                sendRecording();
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }
    // Method for sending Audio
    public void sendRecording() {
        // Infinite loop until microphone button is released
        while (isRecording) {
            try {
                recorder.read(buffer, 0, bufferSize);
                outStream.write(buffer);
            } catch (IOException e) {
                Log.d("AUDIO", "Error when sending recording");
            }

        }
    }

    // Set input & output streams
    public void setupStreams() {
        try {
            inStream = bSocket.getInputStream();
        } catch (IOException e) {
            Log.e("SOCKET", "Error when creating input stream", e);
        }
        try {
            outStream = bSocket.getOutputStream();
        } catch (IOException e) {
            Log.e("SOCKET", "Error when creating output stream", e);
        }
    }

    // Stop Recording and free up resources
    public void stopRecording() {
        if (recorder != null) {
            isRecording = false;
            recorder.stop();
        }
    }

    public void audioCreate() {
        // Audio track object
        track = new AudioTrack(AudioManager.STREAM_MUSIC,
                16000, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, minSize, AudioTrack.MODE_STREAM);
        // Audio record object
        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 16000,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);
    }

    // Playback received audio
    public void startPlaying() {
        Log.d("AUDIO", "Assigning player");
        // Receive Buffer
        playBuffer = new byte[minSize];

        track.play();
        // Receive and play audio
        playThread = new Thread(new Runnable() {
            @Override
            public void run() {
                receiveRecording();
            }
        }, "AudioTrack Thread");
        playThread.start();
    }

    // Receive audio and write into audio track object for playback
    public void receiveRecording() {
        int i = 0;
        while (!isRecording) {
            try {
                if (inStream.available() == 0) {
                    //Do nothing
                } else {
                    inStream.read(playBuffer);
                    track.write(playBuffer, 0, playBuffer.length);
                }
            } catch (IOException e) {
                Log.d("AUDIO", "Error when receiving recording");
            }
        }
    }

    // Stop playing and free up resources
    public void stopPlaying() {
        if (track != null) {
            isRecording = true;
            track.stop();
        }
    }

    public void destroyProcesses() {
        //Release resources for audio objects
        track.release();
        recorder.release();
    }

    // Setter for socket object
    public void setSocket(BluetoothSocket bSocket) {
        this.bSocket = bSocket;
    }

}