package com.sane.router.network.datagramFields;

import com.sane.router.network.Constants;
import com.sane.router.support.Utilities;

/**
 * Header Field Containing the sequence number of an LRP packet,
 * which determines packet order in a series of LRP packets
 *
 * @author Joshua Johnston
 */
public class LRPSequenceNumber implements HeaderField
{
    //Fields
    private Integer sequenceNumber;//The definitive class field

    //Methods
    public LRPSequenceNumber(String string)
    {
        sequenceNumber = Integer.valueOf(string,16);
    }

    //Interface Implementation
    @Override public String toTransmissionString()
    {
        return Integer.toHexString(sequenceNumber);
    }
    @Override public String toHexString()
    {
        return Integer.toHexString(sequenceNumber);
    }
    @Override public String explainSelf()
    {
        return "Sequence Number: 0x" + toTransmissionString().toUpperCase();
    }
    @Override public String toASCIIString()
    {
        return Integer.toString(sequenceNumber).toUpperCase();
    }
}
