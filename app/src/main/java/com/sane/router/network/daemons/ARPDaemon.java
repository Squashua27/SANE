package com.sane.router.network.daemons;

import android.util.Log;

import com.sane.router.network.Constants;
import com.sane.router.network.table.TimedTable;
import com.sane.router.network.table.tableRecords.ARPRecord;
import com.sane.router.support.BootLoader;
import com.sane.router.support.Utilities;
import com.sane.router.support.factories.TableRecordFactory;

import java.util.Observable;
import java.util.Observer;

/**
 * @author Joshua Johnston
 */
public class ARPDaemon extends Observable implements Observer, Runnable
{
    //Fields
    private TimedTable arpTable; //THE ARP table
    private LL2Daemon ll2Demon;//private instance used to send ARP frames
    private TableRecordFactory factory; //

    //Singleton Implementation
    private static final ARPDaemon ourInstance = new ARPDaemon();

    public static ARPDaemon getInstance() {
        return ourInstance;
    }

    private ARPDaemon() {
        arpTable = new TimedTable();
    }

    //Methods
    private void addARPRecord(int ll2p, int ll3p)
    {
        Log.i(Constants.LOG_TAG, "\n\nAdding ARP Record, checking for matching key...\n\n");
        if (arpTable.touch(ll2p))
            Log.i(Constants.LOG_TAG, "Matching key found, record updated: "
                    + ((ARPRecord) arpTable.getItem(ll2p)).explainSelf() + "\n\n");
        else
        {
            Log.i(Constants.LOG_TAG, "Matching key not found, creating record...\n\n");
            ARPRecord newRecord = factory.getItem(Constants.ARP_RECORD,//Create a new ARP Record
                    Utilities.padHexString(Integer.toHexString(ll2p),//LL2P of new record...
                    Constants.LL2P_ADDRESS_LENGTH)                       //...padded to length
                    + Utilities.padHexString(Integer.toHexString(ll3p), //LL3P of new record...
                    Constants.LL3P_ADDRESS_LENGTH));                   //..padded to length

            arpTable.addItem(newRecord); //add new record to ARP table
            Log.i(Constants.LOG_TAG, "Record added to table: "
                    + newRecord.explainSelf() + "\n\n");
        }
        //setChanged();//notify observers of change to the adjacency list
        //notifyObservers();
    }

    /**
     * A function to test the fields and methods of classes pertaining to the
     * Address Resolution Protocol, called from the BootLoader while debugging
     */
    public void testARP()
    {

    }

    /**
     * Required method of the Observer Class, Ensures non-operation until bootLoader prompt
     *
     * @param observable - The observed object
     * @param object     - An object passed in by the triggering observable
     */
    @Override public void update(Observable observable, Object object)
    {
        if (observable instanceof BootLoader)
        {
            ll2Demon = LL2Daemon.getInstance();
            factory = TableRecordFactory.getInstance();
        }

    }
    @Override public void run(){} //TODO: write this method
}