package com.sane.router.network.daemons;

import com.sane.router.network.Constants;
import com.sane.router.network.table.RoutingTable;
import com.sane.router.network.tableRecords.Record;
import com.sane.router.network.tableRecords.RoutingRecord;
import com.sane.router.support.BootLoader;
import com.sane.router.support.factories.TableRecordFactory;

import java.util.ArrayList;
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

    //Methods

    public RoutingTable getRoutingTable() {return routingTable;}
    public ArrayList<Record> getRoutingTableAsList()
    {
        return (ArrayList<Record>) routingTable.getTableAsList();
    }
    public RoutingTable getForwardingTable() {return forwardingTable;}
    public ArrayList<Record> getForwardingTableAsList()
    {
        return (ArrayList<Record>) forwardingTable.getTableAsList();
    }

    /**
     * A Lab Routing Protocol Datagram:
     *        _______________________________________________
     * offset|_____0_____|_____1_____|_____2_____|_____3_____|
     *  0x00 |______Source_LL3P______|Seq#_|Count|/\/\/\/\/\/|
     *  0x03 |___Net_#1__|__Dist_#1__|___Net_#2__|__Dist_#2__|
     *  0x07 |___Net_#3__|__Dist_#3__|___Net_#4__|__Dist_#4__|
     *  0x0B |___Net_#5__|__Dist_#5__|___Net_#6__|__Dist_#6__|
     *  0X0F |___Net_#7__|__Dist_#7__|...max_15_NetDistPairs_|
     */
    public void receiveNewLRP(byte[] lrpPacket, Integer ll2pSource)
    {
        //TODO: touch packet with matching LL2P
        String packet = new String(lrpPacket);


    }

    //Interface Implementation
    /**
     * Definitive method of the Observer Interface
     */
    @Override public void update(Observable observable, Object object)
    {
        if (observable instanceof BootLoader)//Get references on clear signal from BootLoader
        {
            ll2Daemon = LL2Daemon.getInstance();
            arpDaemon = ARPDaemon.getInstance();
            arpDaemon.addObserver(this); //adds self as ARPDaemon observer (to watch ARP table)
        }
        else if (object instanceof List)//Removes outdated routes when triggered by ARPDaemon
        {
            for(Record record : (List<Record>) object)
            {
                routingTable.removeRoutesFrom(((RoutingRecord)record).getNetworkNumber());
                forwardingTable.removeRoutesFrom(((RoutingRecord)record).getNetworkNumber());
            }
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