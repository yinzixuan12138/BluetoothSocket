package com.ggq.bluetoothsocket.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ggq.bluetoothsocket.ui.adapter.DeviceAdapter;
import com.ggq.bluetoothsocket.R;
import com.ggq.bluetoothsocket.util.UtilPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DYK on 2015/11/14.
 */
public class FragmentDeviceList extends Fragment implements AdapterView.OnItemClickListener{

    private ListView lv_Device_List;
    private List<BluetoothDevice> DeviceList = new ArrayList<BluetoothDevice>();
    private DeviceAdapter deviceAdapter;
    private View contentView;
    private UtilPool utilPool;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView=inflater.inflate(R.layout.fragment_devicelist,null);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        init();

    }

    private void init() {
        utilPool=new UtilPool(BluetoothAdapter.getDefaultAdapter(),new Handler());
        deviceAdapter = new DeviceAdapter(getActivity(), DeviceList);
        lv_Device_List = (ListView) contentView.findViewById(R.id.lv_devicelist);
        lv_Device_List.setAdapter(deviceAdapter);
        lv_Device_List.setOnItemClickListener(this);
    }

    BroadcastReceiver discoveryResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice remotDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            DeviceList.add(remotDevice);
            deviceAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(discoveryResult);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        utilPool.Connect(DeviceList.get(position));
    }
}

