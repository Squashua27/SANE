package com.sane.router.network.datagramFields;

/**
 * Header Field Containing the count number of an LRP packet,
 * tracks number of hops from source
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
    }//Constructor
    public int getCount(){return count;}

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
        return Integer.toString(count,16).toUpperCase();
    }
}
