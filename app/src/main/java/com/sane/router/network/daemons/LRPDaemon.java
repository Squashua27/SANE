package com.sane.router.network.daemons;

import android.util.Log;

import com.sane.router.UI.UIManager;
import com.sane.router.network.Constants;
import com.sane.router.network.datagram.LRPPacket;
import com.sane.router.network.datagramFields.NetworkDistancePair;
import com.sane.router.network.table.RoutingTable;
import com.sane.router.network.table.TimedTable;
import com.sane.router.network.tableRecords.ARPRecord;
import com.sane.router.network.tableRecords.Record;
import com.sane.router.network.tableRecords.RoutingRecord;
import com.sane.router.support.BootLoader;
import com.sane.router.support.ParentActivity;
import com.sane.router.support.Utilities;

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
        Log.i(Constants.LOG_TAG," \n \n LRP Daemon receiving new packet... \n \n");
        String packetData = new String(lrpPacket);
        LRPPacket packet = new LRPPacket(packetData);

        processLRPPacket(packet, ll2pSource);
    }
    /**
     * Processes a received LRP Packet, making updates to Routing and Forwarding tables
     *
     * @param packet - The packet to process
     * @param ll2pSource - The source of the packet to process
     */
    public void processLRPPacket(LRPPacket packet, int ll2pSource)
    {
        Log.d(Constants.LOG_TAG," \n \n LRP Daemon processing packet... \n \n");

        if (!((TimedTable)arpDaemon.getARPTable()).touch(ll2pSource))
            arpDaemon.getARPTable().addItem(new ARPRecord(ll2pSource, packet.getSourceLL3P().getLL3PAddress()));

        List<RoutingRecord> routes = new ArrayList<>();

        for(NetworkDistancePair pair: packet.getRoutes())
            routes.add(new RoutingRecord(pair.getNetwork(), pair.getDistance()+1, packet.getSourceLL3P().getLL3PAddress()));

        routingTable.addRoutes(routes);
        forwardingTable.addRoutes(routingTable.getBestRoutes());
    }
    /**
     * Expires old records, adds self and adjacent nodes to Routing Table, updates forwarding table,
     * and sends the best routes for each network to all neighbor nodes
     */
    private void updateRoutes()
    {
        Log.i(Constants.LOG_TAG," \n \n LRP Daemon updating routes... \n \n");

        routingTable.expireRecords(Constants.LRP_RECORD_TTL);     //\
        forwardingTable.expireRecords(Constants.LRP_RECORD_TTL); //Expire old records

        RoutingRecord myRoute = new RoutingRecord(Constants.LL3P_NETWORK,0,Constants.LL3P_ADDRESS_HEX);

        routingTable.addNewRoute(myRoute);
        forwardingTable.addNewRoute(myRoute);

        String lrpUpdate;
        for( Integer ll3p: arpDaemon.getAttachedNodes())
        {   //prepare to get unique list /adjacency:
            lrpUpdate = Constants.LL3P_ADDRESS
                    + Integer.toHexString(sequenceNumber)
                    + Integer.toHexString(forwardingTable.getRoutesExcluding(ll3p).size());
            Log.d(Constants.LOG_TAG, " \n1) Building LRP Packet: " + lrpUpdate + " \n");
            for (RoutingRecord route : forwardingTable.getRoutesExcluding(ll3p))
            {   //add each network-distance pair as a string
                lrpUpdate += Utilities.padHexString(Integer.toHexString(route.getNetworkNumber()), Constants.LL3P_NETWORK_LENGTH);
                lrpUpdate += Utilities.padHexString(Integer.toHexString(route.getDistance()), Constants.LL3P_DISTANCE_LENGTH);
            }
            Log.d(Constants.LOG_TAG, " \n2) Building LRP Packet: " + lrpUpdate + " \n");
            ll2Daemon.sendLRPUpdate(new LRPPacket(lrpUpdate), arpDaemon.getMacAddress(ll3p)); //send an LRP update
            sequenceNumber++;
            if (sequenceNumber>15)
                sequenceNumber = 0;
        }
    }
    //Interface Implementation
    /**
     * Definitive method of the Observer Interface, boots self at the BootLoader's signal,
     * or removes records to maintain current tables
     */
    @Override public void update(Observable observable, Object object)
    {
        if (observable instanceof BootLoader)//Get references on clear signal from BootLoader
        {
            ll2Daemon = LL2Daemon.getInstance();
            arpDaemon = ARPDaemon.getInstance();
            arpDaemon.addObserver(this); //adds self as
        }
        else if (observable instanceof ARPDaemon)//Removes outdated routes when triggered by ARPDaemon
        {
            if (object != null)
                for(Record record : (List<Record>) object)
                {
                    routingTable.removeRoutesFrom(((RoutingRecord)record).getNetworkNumber());
                    forwardingTable.removeRoutesFrom(((RoutingRecord)record).getNetworkNumber());
                }
        }
    }
    /**
     * Definitive method of the Runnable Interface, removes expired records at set interval,
     * also periodically communicates routing information with adjacent nodes,
     * this accomplished through the calling of updateRoutes
     */
    @Override public void run()
    {
        ParentActivity.getParentActivity().runOnUiThread(new Runnable() {
            @Override public void run()
            {
                updateRoutes();
            }
        });
    }
}