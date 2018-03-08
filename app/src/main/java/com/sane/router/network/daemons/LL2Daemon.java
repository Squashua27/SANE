package com.sane.router.network.daemons;

import com.sane.router.UI.UIManager;
import com.sane.router.network.Constants;
import com.sane.router.network.datagram.ARPDatagram;
import com.sane.router.network.datagram.LL2PFrame;
import com.sane.router.network.datagramFields.LL2PAddressField;
import com.sane.router.network.datagramFields.LL2PTypeField;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Joshua Johnston on 2/20/2018.
 */

public class LL2Daemon implements Observer
{
    //Fields
    private UIManager uiManager; //reference used to interface manager
    private LL1Daemon lesserDemon; //the less experienced daemon
    private ARPDaemon arpDemon; //reference to help manage ARP frames

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
        LL2PTypeField type = frame.getType();
        LL2PAddressField dest = frame.getDestinationAddress();
        LL2PAddressField source = frame.getSourceAddress();

        if (dest.toString().equalsIgnoreCase(Constants.LL2P_ADDRESS)) //Is this frame for me?
        {
            //TODO: check CRCField

            if (type.toHexString().equalsIgnoreCase(Constants.LL2P_TYPE_ECHO_REQUEST_HEX) ||
                    type.toHexString().equalsIgnoreCase(Constants.LL2P_TYPE_TEXT_HEX) )
            {
                LL2PFrame echoReply = new LL2PFrame
                        (source.toTransmissionString()
                        + Constants.LL2P_ADDRESS
                        + Constants.LL2P_TYPE_ECHO_REPLY_HEX
                        + "CC");

                lesserDemon.sendFrame(echoReply);
            }
            if (type.toHexString().equalsIgnoreCase(Constants.LL2P_TYPE_ARP_REQUEST_HEX))
            {
                arpDemon.processARPRequest(source.getAddress(),
                        ((ARPDatagram) frame.getPayloadField().getPayload()));
                //sendARPReply(source.getAddress());
            }
            if (type.toHexString().equalsIgnoreCase(Constants.LL2P_TYPE_ARP_REPLY_HEX))
            {
                arpDemon.processARPReply(source.getAddress(),
                        ((ARPDatagram) frame.getPayloadField().getPayload()));
            }
        }
    }

    public void sendEchoRequest(String LL2PAddress)
    {
        LL2PFrame echoRequest = new LL2PFrame
                (LL2PAddress
                + Constants.LL2P_ADDRESS
                + Constants.LL2P_TYPE_TEXT_HEX
                + "This sentence is a text payload."
                + "CC");

        lesserDemon.sendFrame(echoRequest);
    }

    public void sendARPRequest(int ll2p)
    {
        LL2PFrame frame = new LL2PFrame
                (Integer.toHexString(ll2p)
                + Constants.LL2P_ADDRESS
                + Constants.LL2P_TYPE_ARP_REQUEST_HEX
                + Constants.LL3P_ADDRESS
                + "CC",
                new ARPDatagram(Constants.LL3P_ADDRESS, true));

        lesserDemon.sendFrame(frame);
    }

    public void sendARPReply(int ll2p)
    {
        LL2PFrame frame = new LL2PFrame
                (Integer.toHexString(ll2p)
                + Constants.LL2P_ADDRESS
                + Constants.LL2P_TYPE_ARP_REPLY_HEX
                + Constants.LL3P_ADDRESS
                + "CC",
                new ARPDatagram(Constants.LL3P_ADDRESS, true));

        lesserDemon.sendFrame(frame);
    }

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
