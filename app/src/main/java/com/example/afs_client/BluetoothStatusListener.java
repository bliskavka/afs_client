package com.example.afs_client;

public interface BluetoothStatusListener {
    void onBluetoothDisabled();
    void onConnected();
    void onConnectionFailed();
    void onConnectionLost();
}
