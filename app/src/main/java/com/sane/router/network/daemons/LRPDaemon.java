package com.sane.router.network.daemons;

import com.sane.router.network.Constants;
import com.sane.router.network.table.RoutingTable;
import com.sane.router.network.tableRecords.Record;
import com.sane.router.support.BootLoader;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Routing Protocol Daemon, handles the methods and updating of Routing Records
 *
 * @author Joshua Johnston
 */
public class LRPDaemon extends Observable implements Observer, Runnable
{
    //Fields
    private RoutingTable routingTable;//The table containing ALL known routes
    private RoutingTable forwardingTable;//The table containing the BEST known route /network
    private int sequenceNumber;//Used in sequencing LRP packets, number 0-15
    private LL2Daemon ll2Daemon;//Reference for the sending of LRP packets
    public ARPDaemon arpDaemon;//Reference to the ARP daemon, which knows about adjacent networks

    //Singleton Implementation
    private static final LRPDaemon ourInstance = new LRPDaemon();
    public static LRPDaemon getInstance() {
        return ourInstance;
    }
    private LRPDaemon()
    {
        routingTable = new RoutingTable();
        forwardingTable = new RoutingTable();
        sequenceNumber = 0;
    }

    //Interface Implementation
    @Override public void update(Observable observable, Object object)
    {
        if (observable instanceof BootLoader)
        {
            ll2Daemon = LL2Daemon.getInstance();
            arpDaemon = ARPDaemon.getInstance();
            arpDaemon.addObserver(this); //adds self as ARPDaemon observer (to watch ARP table)
        }
    }
    /**
     * Definitive method of the Runnable Interface, removes expired records at set interval
     */
    @Override public void run()
    {
        routingTable.expireRecords(Constants.LRP_RECORD_TTL);
    }
}