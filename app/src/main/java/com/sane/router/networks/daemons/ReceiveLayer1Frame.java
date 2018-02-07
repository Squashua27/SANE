package com.sane.router.networks.daemons;

import android.os.AsyncTask;
import android.util.Log;

import com.sane.router.networks.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Asynchronous class for packet receipt
 */
public class ReceiveLayer1Frame extends AsyncTask<Void, Void, byte[]>
{
    private static DatagramSocket receiveSocket;

    @Override
    protected byte[] doInBackground(Void... nothingToSeeHere)
    {
        byte[] receiveData = new byte[1024];
        if (receiveSocket==null) //if receiveSocket not yet initialized...
        {
            try {receiveSocket = new DatagramSocket(Constants.UDP_PORT);}//set receiveSocket
            catch (SocketException e) {e.printStackTrace();}
        }

        // create a datagram packet to receive the UPD data.
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        Log.d(Constants.LOG_TAG, "Inside rx unicast Thread");

        try {receiveSocket.receive(receivePacket);}//receive the packet
        catch (IOException e) {e.printStackTrace();}

        int bytesReceived = receivePacket.getLength ();
        byte[] frameBytes = new String(receivePacket.getData()).substring
                (0,bytesReceived).getBytes();
        Log.d(Constants.LOG_TAG, "Received bytes: "+ new String(frameBytes));

        return frameBytes;
    }

    @Override
    protected void onPostExecute(byte[] frameBytes) {
        LL1Daemon.getInstance().processL1FrameBytes(frameBytes);
        new ReceiveLayer1Frame().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
