package com.sane.router.network.datagramFields;

import com.sane.router.network.Constants;

/**
 * The LL3P Address Class of the HeaderField Interface,
 * holds an LL3P address, knows whether it is a source address
 *
 * @author Joshua Johnston
 */
public class LL3PAddressField implements HeaderField
{
    //Fields
    private Integer address;    //The LL3P address
    private Integer networkID; //LL3P address network component
    private Integer hostID;   //LL3P address host component
    private Boolean isSource;//Whether LL3P is a source address

    //Methods
    public LL3PAddressField(String ll3p, Boolean source)//Constructor
    {
        address = Integer.parseInt(ll3p,16);

        networkID = Integer.parseInt(ll3p.substring
                (2*Constants.LL3P_ADDRESS_NETWORK_OFFSET,
                        2*Constants.LL3P_ADDRESS_NETWORK_LENGTH),16);

        hostID = Integer.parseInt(ll3p.substring
                (2*Constants.LL3P_ADDRESS_HOST_OFFSET),16);

        isSource = source;
    }
    public int getLL3PAddress(){return address;}//Standard getter

    //HeaderField Interface Implementation
    @Override public String toTransmissionString()
    {
        return Integer.toHexString(address);
    }
    @Override public String toHexString()
    {
        return Integer.toHexString(address);
    }
    @Override public String explainSelf()
    {
        return "LL3P Address: 0x" + Integer.toHexString(address)
                + "\nSource Address: " + isSource.toString();
    }
    @Override public String toASCIIString()
    {
        return Integer.toString(address);
    }
}
