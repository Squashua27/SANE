package com.sane.router.networks.daemons;

import com.sane.router.UI.UIManager;
import com.sane.router.networks.Constants;
import com.sane.router.networks.datagram.LL2PFrame;
import com.sane.router.networks.table.Table;
import com.sane.router.networks.tableRecords.AdjacencyRecord;
import com.sane.router.support.BootLoader;
import com.sane.router.support.FrameLogger;
import com.sane.router.support.IPAddressGetter;
import com.sane.router.support.TableRecordFactory;

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
    //private LL2PDaemon ll2pDaemon;//private reference to another layer's daemon singleton

    //Singleton Implementation
    private static final LL1Daemon ourInstance = new LL1Daemon();
    public static LL1Daemon getInstance() {
        return ourInstance;
    }
    private LL1Daemon()
    {
        adjacencyTable = new Table();
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
            //ll2pDaemon = LL2PDaemon.getInstance();
            //create a frameTransmitter object?

            addObserver(FrameLogger.getInstance());
            addObserver(UIManager.getInstance());
        }
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

    public void addAdjacency(String LL2PAddress, String ipAddress)
    {
        adjacencyTable.addItem((AdjacencyRecord)factory.getItem(Constants.ADJACENCY_RECORD, LL2PAddress+ipAddress));

        //setChanged();???????????????????????????
        //notifyObservers();//notify observers of change to the adjacency list

        return;
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

    public void sendFrame(LL2PFrame ll2pFrame)
    {

    }
    public void processL1FrameBytes(Byte[] frame)
    {
        LL2PFrame ll2pFrame = new LL2PFrame(frame.toString());

        setChanged();
        notifyObservers(ll2pFrame);

        //pass the frame to LL2Daemon

        return;
    }
}
