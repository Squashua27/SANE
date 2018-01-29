package com.sane.router.networks.datagramFields;

import com.sane.router.networks.Constants;
import com.sane.router.networks.headerFields.HeaderField;
import com.sane.router.support.Utilities;

/**
 * The CRC field, placeholder for full Cyclical Redundancy Check Class
 *
 * @author Joshua Johnston
 */

public class CRC implements HeaderField
{
    //Fields
    private String CRCValue; //fake string value of the address

    //Methods
    public CRC(String typeValueString)
    {
        CRCValue = typeValueString.substring(0, 2* Constants.LL2P_CRC_FIELD_LENGTH);
    }

    //Interface Implementation (see interface documentation)
    @Override
    public String toString()//returns CRC value as is
    {
        return CRCValue;
    }
    public String toTransmissionString()//what the CRC field looks like in transit
    {
        return Utilities.padHexString(CRCValue, Constants.LL2P_CRC_FIELD_LENGTH);
    }
    public String toHexString()//the hex CRC field, pre-padding
    {
        return CRCValue;
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
