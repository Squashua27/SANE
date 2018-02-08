package com.sane.router.networks.table.tableRecords;

import com.sane.router.networks.Constants;
import com.sane.router.support.IPAddressGetter;
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
    private Integer LL2PAddress; //the Lab Layer 2 Protocol address, and the key
    private InetAddress IPAddress; //the IP address

    //Methods
    /**
     * Adjacency Record Constructor
     *
     * @param LL2PandIPAddress - the LL2P Address of the Adjacency Record to construct
     *                           AND the IP Address of the Adjacency Record to construct
     */
    public AdjacencyRecord(String LL2PandIPAddress)
    {
        super();  //a call to the superclass' constructor

        LL2PAddress = Integer.parseInt(LL2PandIPAddress.substring
                (0, 2*Constants.LL2P_ADDRESS_LENGTH),16);

        IPAddress = IPAddressGetter.getInstance().getInetAddress
                (LL2PandIPAddress.substring(2*Constants.LL2P_ADDRESS_LENGTH));
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
        return "\nLL2P Address: 0x" + Utilities.padHexString(Integer.toHexString(LL2PAddress),
                Constants.LL2P_ADDRESS_LENGTH)
                + "\nIP Address: " + IPAddress.toString();
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
