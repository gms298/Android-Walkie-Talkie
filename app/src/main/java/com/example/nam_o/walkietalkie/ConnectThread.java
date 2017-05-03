package com.example.nam_o.walkietalkie;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread
{
    private BluetoothDevice bDevice;
    private BluetoothSocket bSocket;
    // Establish connection
    public boolean connect(BluetoothDevice device, UUID UUID) {

        // Get the MAC address
        bDevice = device;

        try {
            // Create a RFCOMM socket with the UUID
            bSocket = bDevice.createRfcommSocketToServiceRecord(UUID);
        }
        catch (IOException e) {
            Log.d("CONNECT", "Failed at create RFCOMM");
            return false;
        }

        if (bSocket == null) {
            return false;
        }

        try {
            // Try to connect
            bSocket.connect();
        } catch(IOException e) {
            Log.d("CONNECT", "Failed at socket connect");
            try {
                bSocket.close();
            } catch(IOException close) {
                Log.d("CONNECT", "Failed at socket close");
            }
            // Moved return false out from inner catch, making it return false when connect is unsuccessful.
            // Return value used to determine if intent switch to next screen.
            return false;
        }
        return true;
    }
    // Close connection
    public boolean closeConnect() {
        try {
            bSocket.close();
        } catch(IOException e) {
            Log.d("CONNECT", "Failed at socket close");
            return false;
        }
        return true;
    }

    // Returns the bluetooth socket object
    public BluetoothSocket getSocket() {
        return bSocket;
    }
}
