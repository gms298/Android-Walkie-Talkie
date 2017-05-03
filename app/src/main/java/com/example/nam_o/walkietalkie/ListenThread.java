package com.example.nam_o.walkietalkie;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ListenThread {

    private BluetoothSocket listenSocket;
    private byte[] buffer;
    // Accept connection and create socket object
    public boolean acceptConnect(BluetoothAdapter adapter, UUID mUUID) {
        BluetoothServerSocket temp = null;
        try {
            temp = adapter.listenUsingRfcommWithServiceRecord("BTService", mUUID);
        } catch(IOException e) {
            Log.d("LISTEN", "Error at listen using RFCOMM");
        }

        try {
            listenSocket = temp.accept();
        } catch (IOException e) {
            Log.d("LISTEN", "Error at accept connection");
        }
        if (listenSocket != null) {
            try {
                temp.close();
            } catch (IOException e) {
                Log.d("LISTEN", "Error at socket close");
            }
            return true;
        }
        return false;
    }

    // Close connection
    public boolean closeConnect() {
        try {
            listenSocket.close();
        } catch(IOException e) {
            Log.d("LISTEN", "Failed at socket close");
            return false;
        }
        return true;
    }

    // Return socket object
    public BluetoothSocket getSocket() {
        return listenSocket;
    }

}
