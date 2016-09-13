package com.ggq.bluetoothsocket.ui.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ggq.bluetoothsocket.R;

import java.util.List;

/**
 * Created by DYK on 2015/11/1.
 */
public class DeviceAdapter extends BaseAdapter{

    private Context context;
    private List<BluetoothDevice> DeviceList;
    private LayoutInflater inflater;

    public DeviceAdapter(Context context,List<BluetoothDevice> list)
    {
        this.context=context;
        this.DeviceList=list;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return DeviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return DeviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_devicelist_item, null);
            holder = new ViewHolder();
            holder.tv_DeviceName = (TextView) convertView.findViewById(R.id.tv_device_name);
            holder.tv_DeviceAddress = (TextView) convertView.findViewById(R.id.tv_device_address);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_DeviceName.setText(DeviceList.get(position).getName());
        holder.tv_DeviceAddress.setText(DeviceList.get(position).getAddress());
        return convertView;

    }

    static class ViewHolder {
        TextView tv_DeviceName;
        TextView tv_DeviceAddress;
    }


}
