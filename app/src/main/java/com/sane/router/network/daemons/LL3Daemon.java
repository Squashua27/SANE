package com.sane.router.network.daemons;

import android.util.Log;

import com.sane.router.UI.Messenger;
import com.sane.router.UI.UIManager;
import com.sane.router.network.Constants;
import com.sane.router.network.datagram.LL3PDatagram;
import com.sane.router.network.table.TimedTable;

import java.util.Observable;
import java.util.Observer;

/**
 * The Daemon of the routing layer (Lab Layer 3), responsible for the
 * creation, forwarding and processing of LL3P Datagrams
 *
 * @author Joshua Johnston
 */

public class LL3Daemon implements Observer
{
    //Fields
    private ARPDaemon arpDemon; //reference to interact with Address Resolution Protocol
    private LL2Daemon ll2Demon; //reference to Layer 2 for transmission of packets
    private LRPDaemon lrpDemon; //reference to obtain next hop for forwarding
    private Messenger messenger;//reference to the Messenger application object

    //Singleton Implementation
    private static final LL3Daemon ourInstance = new LL3Daemon();
    public static LL3Daemon getInstance() {
        return ourInstance;
    }
    private LL3Daemon() {} //(empty constructor)

    //Methods
    /**
     * Sends an LL3P Datagram to the Layer 2 daemon for transmission
     *
     * @param packet - the packet to be sent
     */
    public void sendToNextHop(LL3PDatagram packet)
    {
        try
        {
            int ll3Destination = packet.getDestinationNetwork();
            int nextHop = lrpDemon.getForwardingTable().getBestRoute(ll3Destination).getNextHop();
            int ll2Destination = arpDemon.getMacAddress(nextHop);
            ll2Demon.sendLL3PDatagram(packet, ll2Destination);
        }
        catch (Exception e)
        {
            Log.e(Constants.LOG_TAG," \nFailed to send LL3P packet to next hop... \n");
            e.printStackTrace();
        }
    }

    /**
     * Sends a given message to the given Layer 3 destination
     *
     * @param message - The message to send
     * @param dest - the LL3 destination address
     */
    public void sendMessage(String message, int dest)
    {
        LL3PDatagram messagePacket = new LL3PDatagram(message, dest);
        sendToNextHop(messagePacket);
    }

    /**
     * Processes a LL3 packet
     *
     * @param packet - the packet to process
     * @param ll2Source - the layer 2 address of the packet
     */
    public void processLL3Packet(LL3PDatagram packet, int ll2Source)
    {
        if (packet.isExpired())
            return;
        ((TimedTable)arpDemon.getARPTable()).touch(ll2Source);
            //arpDemon.getARPTable().addItem(new ARPRecord(ll2Source, packet.getSource()));

        if (packet.getDestination() == Constants.LL3P_ADDRESS_HEX)
            messenger.receiveMessage(packet.getSource(), packet.getPayload().toString());//TODO:Does this work?
            //UIManager.getInstance().displayMessage(packet.getPayload().toString());
        else
        {
            packet.decrementTTL();
            sendToNextHop(packet);
        }
    }

    //Interface Implementation
    /**
     * Methods of the Observer Interface, connects to cooperating daemons upon bootLoader signal
     *
     * @param observable - the observed object
     * @param object - the object optionally passed by the triggering observable
     */
    @Override public void update(Observable observable, Object object)
    {
        arpDemon = ARPDaemon.getInstance();
        ll2Demon = LL2Daemon.getInstance();
        lrpDemon = LRPDaemon.getInstance();
        messenger= UIManager.getInstance().getMessenger();
    }
}
