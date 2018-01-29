package com.sane.router.networks.datagramFields;

import com.sane.router.networks.Constants;
import com.sane.router.networks.datagram.Datagram;
import com.sane.router.networks.headerFields.HeaderField;
import com.sane.router.support.ParentActivity;
import com.sane.router.support.Utilities;

/**
 * The datagram payload field, holds a datagram object
 *
 * @author Joshua Johnston
 */

public class DatagramPayloadField implements HeaderField
{
    //Fields
    private Datagram packet;

    //Methods
    /**
     * A typical constructor, with a twist: this datagram field takes another datagram
     *
     * @param pkt - the datagram payload
     */
    DatagramPayloadField(Datagram pkt)
    {
        packet = pkt;
    }
    /**
     * returns the datagram object
     *
     * @return payload - the datagram payload to returned
     */
    public Datagram getPayload()
    {
        return packet;
    }

    //Interface Implementation (see interface documentation)
    @Override
    public String toString()//necessary to interface, not useful here
    {
        return packet.toString();
    }
    public String toTransmissionString()//what the address field looks like in transit
    {
        return packet.toTransmissionString();
    }
    public String toHexString()//the hex address field, pre-padding
    {
        return packet.toHexString();
    }
    public String explainSelf()//the LL2P Address Field explains itself
    {
        return packet.toProtocolExplanationString();
    }
    public String toASCIIString()//necessary interface function, not useful here
    {
        return packet.toSummaryString();
    }
}

