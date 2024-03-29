package com.sane.router.network.daemons;

import android.os.AsyncTask;
import android.util.Log;

import com.sane.router.UI.UIManager;
import com.sane.router.network.Constants;
import com.sane.router.network.datagram.LL2PFrame;
import com.sane.router.network.datagramFields.LL2PAddressField;
import com.sane.router.network.table.Table;
import com.sane.router.network.tableRecords.AdjacencyRecord;
import com.sane.router.support.BootLoader;
import com.sane.router.support.FrameLogger;
import com.sane.router.support.IPAddressGetter;
import com.sane.router.support.factories.TableRecordFactory;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Observable;
import java.util.Observer;

/**
 * Handles data transmission at the highest level, Interfaces with IP and UDP protocols,
 * maintains the adjacency table, keeps FrameLogger and Sniffer UI updated of changes
 *
 * @author Joshua Johnston
 */
public class LL1Daemon extends Observable implements Observer
{
    //Fields
    private Table adjacencyTable;//records of adjacency between LL2P and IP addresses
    private IPAddressGetter ipAddressGetter;//private reference to a singleton
    private UIManager uiManager;//private reference to a singleton
    private TableRecordFactory factory;//private singleton factory reference
    private LL2Daemon ll2Demon;//private reference to upper layer's daemon singleton

    //Singleton Implementation
    private static final LL1Daemon ourInstance = new LL1Daemon();
    public static LL1Daemon getInstance() {
        return ourInstance;
    }
    private LL1Daemon()
    {
        adjacencyTable = new Table();
    }

    //Methods
    /**
     * Basic getter method, returns the adjacency table
     *
     * @return Table - the adjacency table
     */
    public Table getAdjacencyTable()
    {
        return adjacencyTable;
    }
    /**
     * Creates an AdjacencyRecord given an LL2P and an IP address,
     * adds the Record to the adjacency Table
     *
     * @param ll2p - LL2P address of the record to create
     * @param ipAddress - IP address of the record to create
     */
    public void addAdjacency(String ll2p, String ipAddress)
    {
        AdjacencyRecord newRecord = factory.getItem //create an Adjacency Record
                (Constants.ADJACENCY_RECORD,ll2p+ipAddress);

        adjacencyTable.addItem(newRecord); //add record to adjacency table

        ll2Demon.sendARPRequest(Integer.valueOf(ll2p,16));

        setChanged();//notify observers of change to the adjacency list
        notifyObservers(newRecord);
    }
    /**
     * Removes an entry from the AdjacencyRecord table given the item to remove
     *
     * @param recordToRemove - the record to be removed from the table
     * @return Table - adjacencyTable after the remove operation
     */
    public Table removeAdjacency(AdjacencyRecord recordToRemove)
    {
        adjacencyTable.removeItem(recordToRemove);

        setChanged();
        notifyObservers();//notify observers of change to the adjacency list

        return adjacencyTable;
    }
    public Table removeAdjacency(String ll2pAddress) //^overload
    {
        adjacencyTable.removeItem(Integer.valueOf(ll2pAddress,16));

        setChanged();
        notifyObservers();//notify observers of change to the adjacency list

        return adjacencyTable;
    }
    /**
     * Given a LL2P frame to transmit, passes the frame
     * to and spins a new thread with the SendLayer1Frame class
     *
     * @param ll2pFrame - the frame to be transmitted
     */
    public void sendFrame(LL2PFrame ll2pFrame)
    {
        Log.i(Constants.LOG_TAG, " \n \nSending Frame: "+ ll2pFrame.toTransmissionString()+" \n \n");
        //Construct the DatagramPacket Type object to be sent by argument
        byte[] packet = ll2pFrame.toTransmissionString().getBytes();
        int packetLength = ll2pFrame.toTransmissionString().length();
        LL2PAddressField dest = ll2pFrame.getDestinationAddress();

        InetAddress IPAddress = null;
        try
        {
            IPAddress=((AdjacencyRecord)adjacencyTable.getItem(dest.getAddress())).getIPAddress();

            DatagramPacket sendPacket = new DatagramPacket
                    (packet, packetLength, IPAddress, Constants.UDP_PORT);

            new SendLayer1Frame().execute(sendPacket);//spin off thread using SendLayer1Frame

            Log.i(Constants.LOG_TAG, " \n \n...Frame sent. \n \n");
        }
        catch (Exception e)
        {
            Log.e(Constants.LOG_TAG, " \n \n...Frame dropped. \n \n");
            e.printStackTrace();
        }



        setChanged();//notify observers of change to frame data
        notifyObservers(ll2pFrame);//send the frame so that observers know what changed
    }
    /**
     * Receives a frame from the receiveLayer1Frame class,
     * creates a L2 frame from the array and passes the frame to L2Daemon
     *
     * @param frame - the received frame in the form of a Byte array from UDP
     */
    public void processL1FrameBytes(byte[] frame)
    {
        LL2PFrame ll2pFrame = new LL2PFrame(new String(frame));

        setChanged();
        notifyObservers(ll2pFrame);

        try {
            Log.i(Constants.LOG_TAG, " \n \n<<<<<<<<<Received frame: \n"
                    + ll2pFrame.toProtocolExplanationString() + " \n \n");
        }catch (Exception e)
        {
            Log.e(Constants.LOG_TAG, " \n \n...Failed to process L1 Frame. \n \n");
            e.printStackTrace();
        }
        ll2Demon.processLL2PFrame(ll2pFrame);
    }

    //Observer Implementation
    /**
     * Required method of the Observer class,
     * stalls operation until BootLoader has finished loading,
     * keeps records (and objects that use them) updated
     *
     * @param observable - the observable object triggering an update
     * @param object - an object passed in by the calling method
     */
    public void update(Observable observable, Object object)
    {
        if (observable instanceof BootLoader)
        {
            ipAddressGetter = IPAddressGetter.getInstance();
            factory = TableRecordFactory.getInstance();
            uiManager = UIManager.getInstance();
            ll2Demon = LL2Daemon.getInstance();

            new ReceiveLayer1Frame().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            addObserver(FrameLogger.getInstance());
            addObserver(UIManager.getInstance());
        }
    }
}
