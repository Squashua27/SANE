package com.sane.router.network.daemons;

import android.util.Log;

import com.sane.router.network.Constants;
import com.sane.router.network.datagram.ARPDatagram;
import com.sane.router.network.table.TimedTable;
import com.sane.router.network.table.tableRecords.ARPRecord;
import com.sane.router.network.table.tableRecords.Record;
import com.sane.router.support.BootLoader;
import com.sane.router.support.Utilities;
import com.sane.router.support.factories.TableRecordFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    public int getMacAddress(int ll3p)
    {
        for (Record arpRecord : arpTable.getTableAsList())
        {
            if (((ARPRecord)arpRecord).getLL3PAddress()==ll3p)
            {
                arpTable.touch(arpRecord.getKey());
                return ((ARPRecord)arpRecord).getLL3PAddress();
            }
        }
        return -1;
    }

    public List<Record> getArpTable(){return arpTable.getTableAsList();}//standard getter

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
                    + newRecord.explainSelf() + "\n\n\n");
        }
        setChanged();//notify observers of change to the adjacency list
        notifyObservers();
    }

    public List<Integer> getAttachedNodes()
    {
        List<Integer> nodeList = Collections.synchronizedList(new ArrayList<Integer>());
        for (Record arpRecord : arpTable.getTableAsList())
            nodeList.add(((ARPRecord) arpRecord).getLL3PAddress());

        return nodeList;
    }

    public void processARPRequest(int ll2pAddress, ARPDatagram arpDatagram)
    {
        addARPRecord(ll2pAddress,arpDatagram.getLL3PAddress());
        //ll2Demon.processLL2PFrame();

    }

    /**
     * Processes an ARP Reply
     *
     * @param ll2pAddress - the LL2P Address to touch/add
     * @param arpDatagram - contains the related LL3P address
     */
    public void processARPReply(int ll2pAddress, ARPDatagram arpDatagram)
    {
        addARPRecord(ll2pAddress,arpDatagram.getLL3PAddress());
    }

    /**
     * A function to test the fields and methods of classes pertaining to the
     * Address Resolution Protocol, called from the BootLoader while debugging
     */
    public void testARP()
    {
        Log.i(Constants.LOG_TAG, "\n\n\nBeginning ARP test...\n\n");
        Log.i(Constants.LOG_TAG, "Creating Arp Table Entry...\n\n");
        addARPRecord(27,11);
        Log.i(Constants.LOG_TAG, "Table content:\n\n"+arpTable.toString()+"\n\n");

        Log.i(Constants.LOG_TAG, "Creating two more Table Entries...\n\n");
        addARPRecord(1,1);
        addARPRecord(13,13);


        Log.i(Constants.LOG_TAG, "\nTable content:\n\n"+arpTable.toString()+"\n\n");

        Log.i(Constants.LOG_TAG, "\nCreating two more Table Entries...\n\n");
        Log.i(Constants.LOG_TAG, "\nTable content:\n\n"+arpTable.toString()+"\n\n");

        Log.i(Constants.LOG_TAG, "\nTouching some records just for fun...\n\n");
        arpTable.touch(1);
        arpTable.touch(27);

        arpTable.expireRecords(10);
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
    @Override public void run()
    {
        List<Record> removedRecords = arpTable.expireRecords(Constants.ARP_RECORD_TTL);
        if (!removedRecords.isEmpty())
            notifyObservers(removedRecords);
    }
}