package com.sane.router.network.daemons;

import com.sane.router.UI.UIManager;
import com.sane.router.network.Constants;
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
        }
    }

    public void sendEchoRequest(String LL2PAddress)
    {
        LL2PFrame echoRequest = new LL2PFrame
                (LL2PAddress//Integer.parseInt(LL2PAddress,16)
                        + Constants.LL2P_ADDRESS
                        + Constants.LL2P_TYPE_TEXT_HEX
                        + "This sentence is a text payload."
                        + "CC");

        lesserDemon.sendFrame(echoRequest);
    }

    //TODO: method - sendARPRequest
    //TODO: method - sendArpReply

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
    }
}