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
    private String CRCValue; //CRCField stored as ASCII

    //Methods
    /**
     * A typical constructor
     *
     * @param typeValueString - a string representing the constructed CRCField field
     */
    public CRCField(String typeValueString)
    {
        CRCValue = typeValueString.substring(0, Constants.LL2P_CRC_FIELD_LENGTH);
    }

    //Interface Implementation (see interface documentation)
    @Override public String toString()//returns CRCField value as is
    {
        return CRCValue;
    }
    public String toTransmissionString()//what the CRCField field looks like in transit
    {
        return Utilities.padHexString(CRCValue, Constants.LL2P_CRC_FIELD_LENGTH);
    }
    public String toHexString()//the hex CRCField field, pre-padding
    {
        String returnString = "";
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < CRCValue.length(); index++)
        {
            builder.append(Integer.toHexString(CRCValue.charAt(index)));
        }
        return builder.toString();
    }
    public String explainSelf()//the CRCField Field explains itself
    {
        return "CRCField Value: " + CRCValue;
    }
    public String toASCIIString()//necessary interface function, not very useful here
    {
        return CRCValue;
    }
}
