package com.sane.router.network.daemons;

import com.sane.router.UI.UIManager;
import com.sane.router.network.Constants;
import com.sane.router.network.datagram.LRPPacket;
import com.sane.router.network.datagramFields.NetworkDistancePair;
import com.sane.router.network.table.RoutingTable;
import com.sane.router.network.tableRecords.ARPRecord;
import com.sane.router.network.tableRecords.Record;
import com.sane.router.network.tableRecords.RoutingRecord;
import com.sane.router.support.BootLoader;
import com.sane.router.support.factories.TableRecordFactory;

import java.util.ArrayList;
import java.util.Collections;
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

        addObserver(UIManager.getInstance());
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
     * Called when an LRP Packet is received by the L2 Daemon, touches ARP entry to keep
     * Layer 2 addresses current, Processes LRP packet to update Routing&Forwarding tables
     *
     * @param lrpPacket - the received packet, data with which to make the packet object
     * @param ll2pSource - the source ll2p address or next hop
     */
    public void receiveNewLRP(byte[] lrpPacket, Integer ll2pSource)
    {
        //arpDaemon.getARPTable()?
        //TODO: touch ARP Entry with matching LL2P
        String packetData = new String(lrpPacket);
        LRPPacket packet = new LRPPacket(packetData);
        List<RoutingRecord> routes = Collections.synchronizedList(new ArrayList<RoutingRecord>());

        for(NetworkDistancePair pair: packet.getRoutes())
            routes.add(new RoutingRecord(pair.getNetwork(), pair.getDistance(), ll2pSource))

        routingTable.addRoutes(routes);
        forwardingTable.addOrReplaceRoutes(routes);
        setChanged();
        notifyObservers();
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

    private void updateRoutes()
    {
        routingTable.expireRecords(Constants.LRP_RECORD_TTL);
        forwardingTable.expireRecords(Constants.LRP_RECORD_TTL);

        routingTable.addNewRoute(new RoutingRecord(Integer.parseInt(Constants.LL2P_ADDRESS,16),0,Integer.parseInt(Constants.LL2P_ADDRESS,16)));

        List<RoutingRecord> routes = Collections.synchronizedList(new ArrayList<RoutingRecord>());
        for( Record adjacency: arpDaemon.getARPTable().getTableAsList())
            routes.add(new RoutingRecord(((ARPRecord) adjacency).getLL2PAddress(), 1, ((ARPRecord) adjacency).getLL2PAddress()));


        routingTable.addRoutes(routes);
    }

    /**
     * Definitive method of the Runnable Interface, removes expired records at set interval
     */
    @Override public void run()
    {
        //TODO: Force work onto the UI thread?
        updateRoutes();
    }
}