package com.ggq.bluetoothsocket.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by DYK on 2015/11/1.
 */
public class UtilPool {


    private final UUID Util_UUID;
    private final String Util_NAME;

    protected static final int SUCCESS_CONNECT = 0;
    protected static final int SUCCESS_ACCEPT = 1;
    protected static final int SUCCESS_EXCHANGE = 2;


    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public UtilPool(BluetoothAdapter mAdapter, Handler mHandler) {
        this.mAdapter = mAdapter;
        this.mHandler = mHandler;
        Util_UUID = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB");
        Util_NAME = mAdapter.getName();
    }

    //和一个socket建立连接
    public synchronized void Connect(BluetoothDevice ClientDevice) {
        final BluetoothDevice knownDevice = mAdapter.getRemoteDevice(ClientDevice.getAddress());
        Set<BluetoothDevice> bondDevices = mAdapter.getBondedDevices();
        if (bondDevices.contains(knownDevice)) {
            mAdapter.cancelDiscovery();
            BluetoothSocket ClientScoket = null;
            try {
                ClientScoket = ClientDevice.createRfcommSocketToServiceRecord(Util_UUID);
            } catch (IOException e) {

            }
            final BluetoothSocket finalClientScoket = ClientScoket;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (finalClientScoket != null) {
                            finalClientScoket.connect();
                            Constact.NowSocket=finalClientScoket;
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        try {
                            finalClientScoket.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    mHandler.obtainMessage(SUCCESS_CONNECT, finalClientScoket).sendToTarget();
                }


            });
        } else {
            try {
                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                createBondMethod.invoke(knownDevice);
            } catch (Exception e) {

            }


        }
    }

    //创天socket服务
    public synchronized void Accept() {
        BluetoothServerSocket SocketServer = null;
        try {
            //开启监听
            SocketServer = mAdapter.listenUsingRfcommWithServiceRecord(Util_NAME, Util_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final BluetoothServerSocket finalSocketServer = SocketServer;

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                BluetoothSocket socket = null;

                while (true) {
                    try {
                        socket = finalSocketServer.accept();
                        Constact.NowSocket=socket;
                        Exchange(Constact.NowSocket,"连接");
                        mHandler.obtainMessage(SUCCESS_ACCEPT, socket).sendToTarget();
                    } catch (IOException e) {
                        break;
                    }
                }
            }
        });
    }


    //开始聊天
    public synchronized void Exchange(BluetoothSocket socket,String meassge) {
        final BluetoothSocket mmSocket;
        final InputStream mmInStream;
        final OutputStream mmOutStream;
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
        try {
            String str = meassge;
            byte[] srtbyte = str.getBytes();
            mmOutStream.write(srtbyte);
        } catch (IOException e) {
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[1024];  // buffer store for the stream
                int bytes; // bytes returned from read()


                while (true) {
                    try {
                        bytes = mmInStream.read(buffer);

                        mHandler.obtainMessage(SUCCESS_EXCHANGE, bytes, -1, buffer).sendToTarget();
                    } catch (IOException e) {
                        break;
                    }
                }
            }
        });
    }
}
