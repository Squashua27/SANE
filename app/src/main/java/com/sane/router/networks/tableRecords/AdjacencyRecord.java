package com.sane.router.networks.tableRecords;

import com.sane.router.networks.Constants;
import com.sane.router.networks.datagramFields.LL2PTypeField;
import com.sane.router.support.Utilities;

import java.net.InetAddress;

/**
 * Adjacency Record, a subclass of the the Record Class,
 *
 * @author Joshua Johnston
 */
public class AdjacencyRecord extends Record
{
    //Fields
    private int LL2PAddress; //the Lab Layer 2 Protocol address, and the key
    private InetAddress IPAddress; //the IP address

    //Methods
    /**
     * Adjacency Record Constructor
     *
     * @param ll2paddress - the LL2P Address of the Adjacency Record to construct
     * @param ipaddress - the IP Address of the Adjacency Record to construct
     */
    public AdjacencyRecord(int ll2paddress, InetAddress ipaddress)
    {
        super();  //a call to the superclass' constructor
        LL2PAddress = ll2paddress; //initialize with given LL2P Address
        IPAddress = ipaddress;   //initialize with given IP Address
    }
    public int getLL2PAddress() //Standard getter
    {
        return LL2PAddress;
    }
    public void setLL2PAddress(int ll2paddress) //Standard setter
    {
        LL2PAddress = ll2paddress;
    }
    public InetAddress getIPAddress() //Standard getter
    {
        return IPAddress;
    }
    public void setIPAddress(InetAddress ipaddress)
    {
        IPAddress = ipaddress;
    }

    /**
     * Overridden method, outputs a single-line summary of
     */
    @Override public String toString()
    {
        return "LL2P Address: 0x" + Utilities.padHexString(Integer.toHexString(LL2PAddress),
                + 2*Constants.LL2P_ADDRESS_LENGTH)
                + "; IP Address: " + IPAddress.toString();
    }

    //Interface Implementation
    /**
     * Overridden Interface method,
     * The LL2P Address is the key to the Adjacency Record Class
     *
     * @return int - the key: the Lab Layer 2 Protocol Address
     */
    @Override public int getKey()
    {
        return LL2PAddress;
    }
    /**
     * Overridden Interface method,
     * Age not applicable to Adjacency Records, return 0
     *
     * @return int - 0 (not applicable)
     */
    @Override public int getAgeInSeconds()
    {
        return 0;
    }


}
