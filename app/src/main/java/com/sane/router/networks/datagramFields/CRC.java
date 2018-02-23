package com.sane.router.networks.datagramFields;

import com.sane.router.networks.Constants;
import com.sane.router.support.Utilities;

/**
 * The CRC field, placeholder for full Cyclical Redundancy Check Class
 *
 * @author Joshua Johnston
 */

public class CRC implements HeaderField
{
    //Fields
    private String CRCValue; //CRC stored as ASCII

    //Methods
    /**
     * A typical constructor
     *
     * @param typeValueString - a string representing the constructed CRC field
     */
    public CRC(String typeValueString)
    {
        CRCValue = typeValueString.substring(0, Constants.LL2P_CRC_FIELD_LENGTH);
    }

    //Interface Implementation (see interface documentation)
    @Override public String toString()//returns CRC value as is
    {
        return CRCValue;
    }
    public String toTransmissionString()//what the CRC field looks like in transit
    {
        return Utilities.padHexString(CRCValue, Constants.LL2P_CRC_FIELD_LENGTH);
    }
    public String toHexString()//the hex CRC field, pre-padding
    {
        String returnString = "";
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < CRCValue.length(); index++)
        {
            builder.append(Integer.toHexString(CRCValue.charAt(index)));
        }
        return builder.toString();
    }
    public String explainSelf()//the CRC Field explains itself
    {
        return "CRC Value: " + CRCValue;
    }
    public String toASCIIString()//necessary interface function, not very useful here
    {
        return CRCValue;
    }
}
