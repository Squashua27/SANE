package com.sane.router.network.table.tableRecords;

import com.sane.router.network.Constants;

/**
 * An Address Resolution Protocol Record,
 * a record linking layer 2 to layer 3 address
 *
 * @author Joshua Johnston
 */
public class ARPRecord extends Record
{
    //Fields
    private Integer ll2pAddress; //record key & neighbor LL2P address
    private Integer ll3pAddress; //neighbor LL3P address

    //Methods
    public ARPRecord(String addresses)//primary constructor
    {
        super();
        ll2pAddress = Integer.parseInt(addresses.substring(0,2*Constants.LL2P_ADDRESS_LENGTH),16);
        ll3pAddress = Integer.parseInt(addresses.substring(2*Constants.LL2P_ADDRESS_LENGTH),16);
    }
    public ARPRecord(int ll2p, int ll3p)//constructor for use in testing
    {
        super();
        ll2pAddress = ll2p; //construct entry with passed-in values
        ll3pAddress = ll3p;//
    }
    public ARPRecord(){ super(); }//null constructor

    public int getLL2PAddress(){return ll2pAddress;}//standard getter
    public int getLL3PAddress(){return ll3pAddress;}//standard getter
    public void setLL2PAddress(int ll2p){ll2pAddress=ll2p;}//standard setter
    public void setLL3PAddress(int ll3p){ll3pAddress=ll3p;}//standard setter

    public String toString()
    {
        return "LL2P Address:  " + getLL2PAsString()
            + "   LL3P Address:  " + getLL3PAsString();
        //TODO: Add age in seconds to string
    }
    public String getLL2PAsString()
    {
        return Integer.toHexString(ll2pAddress);
    }
    public String getLL3PAsString()
    {
        return Integer.toHexString(ll3pAddress);
    }
    public String explainSelf()
    {
        return "\nLL2P Address:  " + getLL2PAsString()
                + "\nLL3P Address:  " + getLL3PAsString()+"\n";
    }

    public int getKey(){return ll2pAddress;}//typical getter
}
