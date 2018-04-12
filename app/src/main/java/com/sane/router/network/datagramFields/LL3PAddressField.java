package com.sane.router.network.datagramFields;

import com.sane.router.network.Constants;
import com.sane.router.support.Utilities;

/**
 * The LL3P Address Class of the HeaderField Interface,
 * holds an LL3P address, knows whether it is a source address,
 * In transmission, looks like this:
 *
 *        _______________________________________
 * offset|_________0_________|_________1_________|
 *  0x00 |__Network_Address__|___Host_Address____|
 *
 * @author Joshua Johnston
 */
public class LL3PAddressField implements HeaderField
{
    //Fields
    private Integer address;    //The LL3P address
    private Integer networkAddress; //LL3P address network component
    private Integer hostAddress;   //LL3P address host component
    private Boolean isSource;//Whether LL3P is a source address

    //Methods
    public LL3PAddressField(String ll3p, Boolean source)//Constructor
    {
        ll3p = Utilities.padHexString(ll3p, Constants.LL3P_ADDRESS_LENGTH);

        address = Integer.parseInt(ll3p,16);

        networkAddress = Integer.parseInt(ll3p.substring
                (2*Constants.LL3P_ADDRESS_NETWORK_OFFSET,
                        2*Constants.LL3P_ADDRESS_NETWORK_LENGTH),16);

        hostAddress = Integer.parseInt(ll3p.substring
                (2*Constants.LL3P_ADDRESS_HOST_OFFSET),16);

        isSource = source;
    }
    public int getLL3PAddress(){return address;}//Standard getter
    public int getNetworkAddress(){
        return networkAddress;
    }

    //Interface Implementation
    @Override public String toTransmissionString()
    {
        return toHexString();
    }
    public String networkToHex()
    {
        return Utilities.padHexString(Integer.toHexString(hostAddress), Constants.LL3P_ADDRESS_HOST_LENGTH);
    }
    public String hostToHex()
    {
        return Utilities.padHexString(Integer.toHexString(networkAddress), Constants.LL3P_ADDRESS_NETWORK_LENGTH);
    }
    @Override public String toHexString()
    {
        return Utilities.padHexString(Integer.toHexString(address), Constants.LL3P_ADDRESS_LENGTH);
    }
    @Override public String explainSelf()
    {
        return "LL3P Address: 0x" + toTransmissionString()
                + "\nSource Address: " + isSource.toString();
    }
    @Override public String toASCIIString()
    {
        return Integer.toString(address);
    }
}
