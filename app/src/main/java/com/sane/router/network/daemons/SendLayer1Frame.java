package com.sane.router.network.daemons;

import android.os.AsyncTask;
import android.util.Log;

import com.sane.router.network.Constants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Class to create asynchronous threads that send Layer 1 Frames
 *
 * @author Joshua Johnston
 */
public class SendLayer1Frame extends AsyncTask<DatagramPacket,Void,Void>
{
    //Fields
    private static DatagramSocket sendSocket;//socket for sending UPD frames

    //Methods
    /**
     * The background activity of the SendLayer1Frame class
     *
     * @param arg0 - The Datagram object to transmit
     */
    @Override protected Void doInBackground(DatagramPacket...arg0)
    {
        if (sendSocket == null)//If sendSocked is not yet initialized...
        {
            try {sendSocket = new DatagramSocket();}//open sendSocket
            catch (SocketException e) {e.printStackTrace();}
        }

        DatagramPacket packet = arg0[0];
        try {
            sendSocket.send(packet); //Send the frame
            Log.i(Constants.LOG_TAG, " \n \n>>>>>>>>>Sent frame: "
                    + new String(packet.getData()) + " \n \n");
            }
        catch (IOException e) {e.printStackTrace();}

        return null;
    }
}

