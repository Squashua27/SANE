package com.sane.router.network.datagram;

import com.sane.router.network.Constants;
import com.sane.router.network.datagramFields.LL3PAddressField;
import com.sane.router.support.factories.HeaderFieldFactory;

/**
 * The Address Resolution Protocol Datagram Class,
 * used in transmission of data to resolve Layer 3 addresses
 * to Layer 2 addresses
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
        int type;
        if (isSource)
            type = Constants.LL3P_SOURCE_ADDRESS_FIELD;
        else
            type = Constants.LL3P_DEST_ADDRESS_FIELD;
        ll3PAddressField = HeaderFieldFactory.getInstance().getItem(type,ll3p);
    }

    public int getLL3PAddress(){return ll3PAddressField.getLL3PAddress();}//typical getter

    //Datagram Interface Implementation
    @Override public String toHexString() {return ll3PAddressField.toHexString();}
    @Override public String toProtocolExplanationString() {return ll3PAddressField.explainSelf();}
    @Override public String toSummaryString() {return ll3PAddressField.explainSelf();}
    @Override public String toTransmissionString(){return ll3PAddressField.toTransmissionString();}
}
