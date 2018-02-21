package com.sane.router.networks.datagramFields;

import com.sane.router.networks.Constants;
import com.sane.router.support.Utilities;

/**
 * The Address field of the LL2P packet
 *
 * @author Joshua Johnston
 */
public class LL2PAddressField implements HeaderField
{
    //Fields
    private int address; //the address as an integer
    private boolean isSourceAddress; //true if contained address is a source address
    private String explanation; //explains field contents

    //Methods
    /**
     * A typical constructor
     *
     * @param newAddress - integer address of constructed LL2PAddressField
     * @param isSource - true if the given address is a source address
     */
    public LL2PAddressField(int newAddress, boolean isSource)//constructor
    {
        address = newAddress;
        isSourceAddress = isSource;
    }
    /**
     * A typical constructor
     *
     * @param newAddress - string address of constructed LL2PAddressField
     * @param isSource - true if the given address is a source address
     */
    public LL2PAddressField(String newAddress, boolean isSource)//constructor
    {
        address = Integer.parseInt(newAddress,16);
        isSourceAddress = isSource;
    }

    /**
     * Typical getter method
     * @return int - the address as an integer
     */
    public int getAddress() {
        return address;
    }

    /**
     * Returns the contents of the isSourceAddress field
     *
     * @return boolean, true if contained address is a source address
     */
    public boolean isSourceAddressField(){return isSourceAddress;}
    /**
     * Sets the explanation field
     */
    private void setExplanation()
    {
        explanation = "";
        if(isSourceAddress)
            explanation = "Source ";
        else
            explanation = "Destination ";
        explanation += "LL2P Address: 0x" + toTransmissionString().toUpperCase();
    }

    //Interface Implementation (see interface documentation)
    @Override public String toString()//necessary to interface, not useful here
    {
        return Integer.toHexString(address);
    }
    public String toTransmissionString()//what the address field looks like in transit
    {
        return Utilities.padHexString(Integer.toHexString(address), Constants.LL2P_ADDRESS_LENGTH);
    }
    public String toHexString()//the hex address field, pre-padding
    {
        return Integer.toHexString(address);

        //char[] bytes = string.toCharArray();
        //StringBuilder sb = new StringBuilder();
        //for (int i = 0; i+2 < string.length(); i+=2)
        //{
        //    sb.append(String.format("%02X ", bytes[i]));
        //}

        //return sb.toString();
    }
    public String explainSelf()//the LL2P Address Field explains itself
    {
        setExplanation();
        return explanation;
    }
    public String toASCIIString()//necessary interface function, not useful here
    {
        return Integer.toString(address).toUpperCase();
    }

}
