package com.sane.router.network.datagramFields;

/**
 * Header Field Containing the count number of an LRP packet,
 * which determines packet count in a series of LRP packets
 *
 * @author Joshua Johnston
 */
public class LRPCount implements HeaderField
{
    //Fields
    private Integer count;//The definitive class field

    //Methods
    public LRPCount(String string)
    {
        count = Integer.valueOf(string,16);
    }

    //Interface Implementation
    @Override public String toTransmissionString()
    {
        return Integer.toHexString(count);
    }
    @Override public String toHexString()
    {
        return Integer.toHexString(count);
    }
    @Override public String explainSelf()
    {
        return "Count: 0x" + toTransmissionString().toUpperCase();
    }
    @Override public String toASCIIString()
    {
        return Integer.toString(count).toUpperCase();
    }
}