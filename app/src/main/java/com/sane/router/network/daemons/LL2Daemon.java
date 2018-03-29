package com.sane.router.network.daemons;

import android.util.Log;

import com.sane.router.UI.UIManager;
import com.sane.router.network.Constants;
import com.sane.router.network.datagram.ARPDatagram;
import com.sane.router.network.datagram.LL2PFrame;
import com.sane.router.network.datagram.LRPPacket;
import com.sane.router.network.datagramFields.LL2PAddressField;
import com.sane.router.network.datagramFields.LL2PTypeField;

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
    private LL1Daemon lesserDemon; //the less experienced daemon
    private ARPDaemon arpDemon; //reference to help manage ARP frames
    private LRPDaemon lrpDaemon; //reference to the manager of routing records and methods

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
        Log.i(Constants.LOG_TAG, " \n \nProcessing LL2P frame... \n \n");
        LL2PTypeField type = frame.getType();
        LL2PAddressField dest = frame.getDestinationAddress();
        LL2PAddressField source = frame.getSourceAddress();

        if (dest.toString().equalsIgnoreCase(Constants.LL2P_ADDRESS)) //Is this frame for me?
        {
            Log.i(Constants.LOG_TAG, " \n \n... It's for me! \n \n");
            //TODO: check CRCField
            if (type.toHexString().equalsIgnoreCase(Constants.LL2P_TYPE_ECHO_REQUEST_HEX))
            {
                Log.i(Constants.LOG_TAG, " \n \nProcessing LL2P Echo Request... \n \n");
                LL2PFrame echoReply = new LL2PFrame
                        (source.toTransmissionString()
                        + Constants.LL2P_ADDRESS
                        + Constants.LL2P_TYPE_ECHO_REPLY_HEX
                        + "CCCC");

                lesserDemon.sendFrame(echoReply);
            }
            if (type.toHexString().equalsIgnoreCase(Constants.LL2P_TYPE_ARP_REQUEST_HEX))
            {
                Log.i(Constants.LOG_TAG, " \n \nProcessing LL2P ARP Request... \n \n");
                arpDemon.processARPRequest(source.getAddress(),
                        ((ARPDatagram) (frame.getPayloadField().getPayload())));
                //sendARPReply(source.getAddress());
            }
            if (type.toHexString().equalsIgnoreCase(Constants.LL2P_TYPE_ARP_REPLY_HEX))
            {
                Log.i(Constants.LOG_TAG, " \n \nProcessing LL2P ARP Reply... \n \n");
                arpDemon.processARPReply(source.getAddress(),
                        ((ARPDatagram) (frame.getPayloadField().getPayload())));
            }
            if (type.toHexString().equalsIgnoreCase(Constants.LL2P_TYPE_LRP_HEX))
            {
                Log.i(Constants.LOG_TAG, " \n \nProcessing LL2P LRP Update... \n \n");
                lrpDaemon.processLRPPacket((LRPPacket) frame.getPayloadField().getPayload());
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

        lesserDemon.sendFrame(echoRequest);
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

        lesserDemon.sendFrame(frame);
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

        lesserDemon.sendFrame(frame);
    }

    /**
     * Frames and sends an LRP update
     *
     * @param lrpPacket - the packet to send
     * @param ll2p - the Layer 2 Address of the packet to send
     */
    public void sendLRPUpdate(LRPPacket lrpPacket, int ll2p)
    {
        Log.i(Constants.LOG_TAG, " \n \nSending LRP Update frame to address: 0x"+Integer.toHexString(ll2p)+"... \n \n");
        LL2PFrame frame = new LL2PFrame
                (Integer.toHexString(ll2p)
                        + Constants.LL2P_ADDRESS
                        + Constants.LL2P_TYPE_LRP_HEX
                        + Constants.LL3P_ADDRESS
                        + "CCCC",
                        lrpPacket);

        lesserDemon.sendFrame(frame);
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
        lesserDemon = LL1Daemon.getInstance();
        arpDemon = ARPDaemon.getInstance();
    }
}
