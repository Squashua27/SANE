package com.sane.router.network.datagramFields;

import com.sane.router.network.Constants;
import com.sane.router.support.Utilities;

/**
 * The CRCField field, placeholder for full Cyclical Redundancy Check Class
 *
 * @author Joshua Johnston
 */

public class CRCField implements HeaderField
{
    //Fields
    private String crcValue; //CRCField stored as HEX

    //Methods
    /**
     * A typical constructor
     *
     * @param typeValueString - a string representing the constructed CRCField field
     */
    public CRCField(String typeValueString)
    {
        crcValue = typeValueString.substring(0, Constants.LL2P_CRC_FIELD_LENGTH*2);
    }
    //Interface Implementation (see interface documentation)
    @Override public String toString()//returns CRCField value as is
    {
        return crcValue;
    }
    public String toTransmissionString()//what the CRCField field looks like in transit
    {
        return toHexString();
    }
    public String toHexString()//the hex CRCField field, pre-padding
    {
        return Utilities.padHexString(crcValue, Constants.LL2P_CRC_FIELD_LENGTH);
    }
    public String explainSelf()//the CRCField Field explains itself
    {
        return "CRCField Value: " + crcValue;
    }
    public String toASCIIString()//necessary interface function, not very useful here
    {
        String hex = crcValue;
        StringBuilder ASCII = new StringBuilder();
        //formatting per ASCII char
        for (int i = 0; i < hex.length(); i+=2)
        {
            String str = hex.substring(i, i+2);
            if (32<=Integer.parseInt(str,16) && Integer.parseInt(str,16)<=126)
                ASCII.append((char)Integer.parseInt(str, 16));
            else
                ASCII.append(".");
        }
        return ASCII.toString();
    }
}
