package com.ggq.bluetoothsocket.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;


/**
 * Created by DYK on 2015/11/26.
 */
public class ImageResizer {

    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fileDescriptor,int reqWidth,int reqHeight)
    {
        final BitmapFactory.Options options= new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor,null,options);
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return  BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
    }


    public  Bitmap decodeSampleBitmapFromResource(Resources resources,int resID, int reqWidth,int reqHeight)
    {
        final BitmapFactory.Options options= new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(resources,resID,options);
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeResource(resources,resID,options);
    }

    public  int calculateInSampleSize(BitmapFactory.Options options,int reqWith,int reqHeight)
    {
        final int height=options.outHeight;
        final int widht=options.outWidth;
        int inSampleSize=1;
        if(height>reqHeight||widht>reqWith)
        {
            final int halfHeight=height/2;
            final int halfWidth=widht/2;
            while((halfHeight/inSampleSize)>=reqHeight&&(halfWidth/inSampleSize)>=reqWith)
                inSampleSize*=2;
        }
        return inSampleSize;
    }
}
