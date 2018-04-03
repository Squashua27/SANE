package com.sane.router.network.datagramFields;

import android.util.Log;

import com.sane.router.network.Constants;
import com.sane.router.support.Utilities;

/**
 * The Type Field of a LL2P packet
 *
 * @author Joshua Johnston
 */
public class LL2PTypeField implements HeaderField
{
    //Fields
    private int type; //the integer value of a LL2P Type field
    private String explanation; //what this class has to say for itself

    //Methods
    /**
     * A typical constructor
     *
     * @param typeValue - integer value of constructed LL2PTypeField
     */
    public LL2PTypeField(int typeValue)
    {
        type = typeValue;
        Log.i(Constants.LOG_TAG, "\n\n"+ explainSelf() +"\n\n");
    }
    /**
     * A typical constructor
     *
     * @param string - a string representing constructed LL2PTypeField
     */
    public LL2PTypeField(String string)
    {
        type = Integer.parseInt(string, 16);
    }
    /**
     * Sets an explanation descriptive of the Type Field content and implications
     */
    private void setExplanation()
    {
        explanation = "";
        explanation += "LL2P Type: 0x" + toTransmissionString();

        if(type == Constants.LL2P_TYPE_LL3P)
            explanation += " - the LL3P type";
        else if(type == Constants.LL2P_TYPE_RESERVED)
            explanation += " - the Reserved type";
        else if(type == Constants.LL2P_TYPE_LRP)
            explanation += " - the LRP type";
        else if(type == Constants.LL2P_TYPE_ECHO_REQUEST)
            explanation += " - the Echo Request type";
        else if(type == Constants.LL2P_TYPE_ECHO_REPLY)
            explanation += " - the Echo Reply type";
        else if(type == Constants.LL2P_TYPE_ARP_REQUEST)
            explanation += " - the ARP Request type";
        else if(type == Constants.LL2P_TYPE_ARP_REPLY)
            explanation += " - the ARP Reply type";
        else if(type == Constants.LL2P_TYPE_TEXT)
            explanation += " - the Text type";
        else
            explanation += " - NOT a valid LL2P Type";
    }

    public int getType() {
        return type;
    }

    //Interface Implementation (see interface documentation)
    @Override public String toString()//necessary to interface, not very useful here
    {
        return Integer.toString(type);
    }
    public String toTransmissionString()//what the Type field looks like in transit
    {
        return Utilities.padHexString(Integer.toHexString(type), Constants.LL2P_TYPE_FIELD_LENGTH);
    }
    public String toHexString()//the hex LL2P Type field, pre-padding
    {
        return Integer.toHexString(type);
    }
    public String explainSelf()//the LL2P Type Field explains itself
    {
        setExplanation();
        return explanation;
    }
    public String toASCIIString()//necessary interface function, not very useful here
    {
        return Integer.toString(type);
    }
}
