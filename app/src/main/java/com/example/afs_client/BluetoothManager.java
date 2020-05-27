package com.example.afs_client;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.afs_client.serial.SerialListener;
import com.example.afs_client.serial.SerialService;
import com.example.afs_client.serial.SerialSocket;

import androidx.lifecycle.MutableLiveData;


public class BluetoothManager implements ServiceConnection, SerialListener {

    private static BluetoothManager instance;
    private BluetoothStatusListener bluetoothStatusListener;

    private enum Connected {False, Pending, True}

    private static final String deviceAddress = "94:E3:6D:9C:67:D7";
    private SerialSocket socket;
    private Connected connected = Connected.False;
    private Activity activity;
    private SerialService service;

    private MutableLiveData<Measurement> measuredData = new MutableLiveData<>();


    private BluetoothManager(Activity activity) throws Exception{
        this.activity = activity;
        //if(!checkBluetooth()) throw new Exception("Bluetooth isn`t supported on this device"); TODO uncomment later
        activity.bindService(new Intent(activity, SerialService.class), this, Context.BIND_AUTO_CREATE);
    }

    private boolean checkBluetooth(){
        if(!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) return false;
        if(BluetoothAdapter.getDefaultAdapter() == null) return false;
        return true;
    }


    //********************************Public Methods**************************************


    public static BluetoothManager getInstance(Activity activity) throws Exception {
        if(instance == null) instance = new BluetoothManager(activity);
        return instance;
    }

    public void setBluetoothStatusListener(BluetoothStatusListener bluetoothStatusListener){
        this.bluetoothStatusListener = bluetoothStatusListener;
    }

    public MutableLiveData<Measurement> getMeasuredData() {
        return measuredData;
    }

    public void connect() {
        activity.runOnUiThread(() -> {
            try {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if(!bluetoothAdapter.isEnabled()) {
                    bluetoothStatusListener.onBluetoothDisabled();
                    return;
                }

                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
                String deviceName = device.getName() != null ? device.getName() : device.getAddress();

                connected = Connected.Pending;
                socket = new SerialSocket();
                service.connect(this, "Connected to " + deviceName);
                socket.connect(activity, service, device);
            } catch (Exception e) {
                onSerialConnectError(e);
            }
        });
    }


    public void attach(){
        if (service != null) service.attach(this);
    }

    public void detach(){
        if(service != null && !activity.isChangingConfigurations())
            service.detach();
    }

    public void disconnect() {
        if (connected != Connected.False) {
            connected = Connected.False;
            service.disconnect();
            socket.disconnect();
            socket = null;
            measuredData.setValue(new Measurement());
            bluetoothStatusListener.onConnectionLost();
        }
    }

    public void send(String str) {
        if (connected != Connected.True) {
            Toast.makeText(activity, "not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            byte[] data = (str + "\r\n").getBytes();
            socket.write(data);
        } catch (Exception e) {
            onSerialIoError(e);
        }
    }


    //*********************************Service Callbacks**************************************


    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        service = ((SerialService.SerialBinder) binder).getService();
        attach();
        connect();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
        disconnect();
    }


    //*********************************Serial Socket Callbacks**********************************


    @Override
    public void onSerialConnect() {
        Log.d("t", "Connected");
        connected = Connected.True;
        bluetoothStatusListener.onConnected();
    }

    @Override
    public void onSerialConnectError(Exception e) {
        Log.d("t", "Connection failed");
        measuredData.setValue(new Measurement());
        bluetoothStatusListener.onConnectionFailed();
        disconnect();
    }

    @Override
    public void onSerialRead(byte[] data) {
        measuredData.setValue(new Measurement(data));
    }

    @Override
    public void onSerialIoError(Exception e) {
        Log.d("t", "Connection lost");
        measuredData.setValue(new Measurement());
        bluetoothStatusListener.onConnectionLost();
        disconnect();
    }


}
