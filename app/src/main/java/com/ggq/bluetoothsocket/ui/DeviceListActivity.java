package com.ggq.bluetoothsocket.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ggq.bluetoothsocket.ui.adapter.DeviceAdapter;
import com.ggq.bluetoothsocket.R;
import com.ggq.bluetoothsocket.util.UtilPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DYK on 2015/11/1.
 */
public class DeviceListActivity extends Activity {
    private ListView lv_Device_List;
    private List<BluetoothDevice> DeviceList = new ArrayList<BluetoothDevice>();
    private DeviceAdapter deviceAdapter;


    protected static final int SUCCESS_CONNECT = 0;
    protected static final int SUCCESS_ACCEPT = 1;
    protected static final int SUCCESS_EXCHANGE = 2;
    private UtilPool utilPool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_devicelist);
        registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        init();
    }

    private void init() {
        //做些初始化的工作，并且设置adapter，这个adapter非常简单就是两个TextView，主要是为了以后方便扩展（单聊啦，讨论组啦。。。。）
        deviceAdapter = new DeviceAdapter(this, DeviceList);
        lv_Device_List = (ListView) findViewById(R.id.lv_devicelist);
        lv_Device_List.setAdapter(deviceAdapter);
        utilPool = new UtilPool(BluetoothAdapter.getDefaultAdapter(), mHandler);
        utilPool.Accept();

        lv_Device_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                utilPool.Connect(DeviceList.get(position));

            }
        });

    }


    BroadcastReceiver discoveryResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice remotDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            DeviceList.add(remotDevice);
            deviceAdapter.notifyDataSetChanged();
        }
    };


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SUCCESS_CONNECT:
                    Toast.makeText(getApplicationContext(), "链接成功", Toast.LENGTH_SHORT).show();

                    break;
                case SUCCESS_ACCEPT:
                    Toast.makeText(getApplicationContext(), "收到请求", Toast.LENGTH_LONG).show();

                    break;
                case SUCCESS_EXCHANGE:
                    byte[] buffer = (byte[]) msg.obj;
                    System.out.println(new String(buffer));
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(discoveryResult);
    }
}
