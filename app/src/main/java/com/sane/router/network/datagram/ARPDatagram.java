package com.sane.router.network.datagram;

import android.util.Log;

import com.sane.router.network.Constants;
import com.sane.router.network.datagramFields.LL2PAddressField;
import com.sane.router.network.datagramFields.LL3PAddressField;
import com.sane.router.support.factories.HeaderFieldFactory;

/**
 * The Address Resolution Protocol Datagram Class,
 * used in transmission of data to resolve Layer 3 addresses
 * to Layer 2 addresses, contains only an LL3P Address Field object
 *
 * In transmission, the ARP Datagram looks like:       ...or like this:
 *        _______________________________________          _______________________________________
 * offset|_________0_________|_________1_________|  offset|_________0_________|_________1_________|
 *  0x00 |__Network_Address__|___Host_Address____|   0x00 |______________LL3_Address______________|
 *
 * @author Joshua Johnston
 */
public class ARPDatagram implements Datagram
{
    //Field
    private LL3PAddressField ll3PAddressField;//the only field of an ARP Datagram

    //Methods
    public ARPDatagram(String ll3p, boolean isSource)//Constructor
    {
        Log.i(Constants.LOG_TAG, " \n \nConstructing an ARP Datagram... \n \n");

        ll3PAddressField = new LL3PAddressField(ll3p, isSource);
    }
    public int getLL3PAddress(){return ll3PAddressField.getLL3PAddress();}//typical getter

    //Datagram Interface Implementation
    @Override public String toHexString() {return ll3PAddressField.toHexString();}
    @Override public String toProtocolExplanationString() {return ll3PAddressField.explainSelf();}
    @Override public String toSummaryString() {return ll3PAddressField.explainSelf();}
    @Override public String toTransmissionString(){return ll3PAddressField.toTransmissionString();}
}