package com.sane.router.network.daemons;

import android.util.Log;

import com.sane.router.UI.UIManager;
import com.sane.router.network.Constants;
import com.sane.router.network.datagram.ARPDatagram;
import com.sane.router.network.datagram.LL2PFrame;
import com.sane.router.network.datagram.LL3PDatagram;
import com.sane.router.network.datagram.LRPPacket;
import com.sane.router.network.datagramFields.LL2PAddressField;
import com.sane.router.network.datagramFields.LL2PTypeField;
import com.sane.router.support.Utilities;

import java.util.Observable;
import java.util.Observer;

/**
 * The Layer 2 Daemon, responsible for framing and node-to-node frame transmission
 *
 * @author Joshua Johnston
 */
public class LL2Daemon implements Observer
{
    //Fields
    private UIManager uiManager; //reference used to interface manager
    private LL1Daemon ll1Demon; //the less experienced daemon
    private LL3Daemon ll3Demon; //the higher leveled demon
    private ARPDaemon arpDemon; //reference to help manage ARP frames
    private LRPDaemon lrpDemon; //reference to the manager of routing records and methods

    //Singleton Implementation
    private static final LL2Daemon ourInstance = new LL2Daemon();//empty constructor
    public static LL2Daemon getInstance() {
        return ourInstance;
    }
    private LL2Daemon(){}

    //Methods
    /**
     * Receives an LL2P Frame and responds in accordance with the frame type
     *
     * @param frame - the received frame
     */
    public void processLL2PFrame(LL2PFrame frame)
    {
        Log.i(Constants.LOG_TAG, " \n \nReceived LL2P frame... \n \n");
        LL2PTypeField type = frame.getType();
        LL2PAddressField dest = frame.getDestinationAddress();
        LL2PAddressField source = frame.getSourceAddress();

        if (dest.toString().equalsIgnoreCase(Constants.LL2P_ADDRESS)) //Is this frame for me?
        {
            Log.i(Constants.LOG_TAG, " \n \n... It's for me! \n \n");

            if (type.toHexString().equalsIgnoreCase(Constants.LL2P_TYPE_ECHO_REQUEST_HEX))
            {
                Log.i(Constants.LOG_TAG, " \n \nProcessing Echo Request frame... \n \n");
                LL2PFrame echoReply = new LL2PFrame
                        (source.toTransmissionString()
                        + Constants.LL2P_ADDRESS
                        + Constants.LL2P_TYPE_ECHO_REPLY_HEX
                        + "CCCC");

                ll1Demon.sendFrame(echoReply);
            }
            if (type.toHexString().equalsIgnoreCase(Constants.LL2P_TYPE_ARP_REQUEST_HEX))
            {
                Log.i(Constants.LOG_TAG, " \n \nProcessing ARP Request frame... \n \n");
                arpDemon.processARPRequest(source.getAddress(),
                        ((ARPDatagram) (frame.getPayloadField().getPayload())));
                //sendARPReply(source.getAddress());
            }
            if (type.toHexString().equalsIgnoreCase(Constants.LL2P_TYPE_ARP_REPLY_HEX))
            {
                Log.i(Constants.LOG_TAG, " \n \nProcessing ARP Reply frame... \n \n");
                arpDemon.processARPReply(source.getAddress(),
                        ((ARPDatagram) (frame.getPayloadField().getPayload())));
            }
            if (type.toHexString().equalsIgnoreCase(Constants.LL2P_TYPE_LRP_HEX))
            {
                Log.i(Constants.LOG_TAG, " \n \nProcessing LRP Update frame... \n \n");
                lrpDemon.processLRPPacket((LRPPacket) frame.getPayloadField().getPayload(),source.getAddress());
            }
            if (type.toHexString().equalsIgnoreCase(Constants.LL2P_TYPE_LL3P_HEX))
            {
                Log.i(Constants.LOG_TAG, " \n \nProcessing LL3P frame... \n \n");
                ll3Demon.processLL3Packet((LL3PDatagram) frame.getPayloadField().getPayload(), source.getAddress());
            }
        }
    }
    /**
     * Builds and sends an Echo Request Text Frame
     *
     * @param ll2p - The destination address
     */
    public void sendEchoRequest(String ll2p)
    {
        Log.i(Constants.LOG_TAG, " \n \nSending Echo Request frame to address: 0x"+ll2p+"... \n \n");
        LL2PFrame echoRequest = new LL2PFrame
                (ll2p
                + Constants.LL2P_ADDRESS
                + Constants.LL2P_TYPE_ECHO_REQUEST_HEX
                + "This sentence is a text payload."
                + "CCCC");

        ll1Demon.sendFrame(echoRequest);
    }
    /**
     * Builds an ARP request frame around an ARP Datagram and sends it
     *
     * @param ll2p - The destination address
     */
    public void sendARPRequest(int ll2p)
    {
        Log.i(Constants.LOG_TAG, " \n \nSending ARP Request frame to address: 0x"+Integer.toHexString(ll2p)+"... \n \n");
        LL2PFrame frame = new LL2PFrame
                (Integer.toHexString(ll2p)
                + Constants.LL2P_ADDRESS
                + Constants.LL2P_TYPE_ARP_REQUEST_HEX
                + Constants.LL3P_ADDRESS
                + "CCCC",
                new ARPDatagram(Constants.LL3P_ADDRESS, true));

        ll1Demon.sendFrame(frame);
    }
    /**
     * Builds an ARP Reply frame around an ARP Datagram and sends it
     *
     * @param ll2p - The destination address
     */
    public void sendARPReply(int ll2p)
    {
        Log.i(Constants.LOG_TAG, " \n \nSending ARP Reply frame to address: 0x"+Integer.toHexString(ll2p)+"... \n \n");
        LL2PFrame frame = new LL2PFrame
                (Integer.toHexString(ll2p)
                + Constants.LL2P_ADDRESS
                + Constants.LL2P_TYPE_ARP_REPLY_HEX
                + Constants.LL3P_ADDRESS
                + "CCCC",
                new ARPDatagram(Constants.LL3P_ADDRESS, true));

        ll1Demon.sendFrame(frame);
    }

    /**
     * Frames and sends an LRP update
     *
     * @param lrpPacket - the packet to send
     * @param dest - the Layer 2 destination address of the packet to send
     */
    public void sendLRPUpdate(LRPPacket lrpPacket, int dest)
    {
        Log.i(Constants.LOG_TAG, " \n \nSending LRP Update frame to address: 0x"+Utilities.padHexString(Integer.toHexString(dest), Constants.LL2P_ADDRESS_LENGTH)+"... \n \n");
        LL2PFrame frame = new LL2PFrame
                (Utilities.padHexString
                (Integer.toHexString(dest),Constants.LL2P_ADDRESS_LENGTH)
                + Constants.LL2P_ADDRESS
                + Constants.LL2P_TYPE_LRP_HEX
                + Constants.LL3P_ADDRESS
                + "CCCC",
                lrpPacket);

        ll1Demon.sendFrame(frame);
    }
    /**
     * Frames and sends an LL3P packet
     *
     * @param packet - the packet to send
     * @param dest - the Layer 2 destination address of the packet to send
     */
    public void sendLL3PDatagram(LL3PDatagram packet, int dest)
    {
        Log.i(Constants.LOG_TAG, " \n \nSending framed LL3P packet to address: 0x"+Utilities.padHexString(Integer.toHexString(dest), Constants.LL2P_ADDRESS_LENGTH)+"... \n \n");
        LL2PFrame frame = new LL2PFrame
                (Utilities.padHexString
                (Integer.toHexString(dest),Constants.LL2P_ADDRESS_LENGTH)
                + Constants.LL2P_ADDRESS
                + Constants.LL2P_TYPE_LL3P_HEX
                + Constants.LL3P_ADDRESS
                + "CCCC",
                packet);

        ll1Demon.sendFrame(frame);
    }

    //Interface Implementation
    /**
     * Required method of Observer classes, triggered by bootLoader, constructs self
     *
     * @param observer - content irrelevant - only one caller (bootLoader)
     * @param object - content irrelevant - only one caller (bootLoader)
     */
    public void update(Observable observer, Object object)
    {
        uiManager = UIManager.getInstance();

        ll1Demon = LL1Daemon.getInstance();
        ll3Demon = LL3Daemon.getInstance();
        arpDemon = ARPDaemon.getInstance();
        lrpDemon = LRPDaemon.getInstance();
    }
}
