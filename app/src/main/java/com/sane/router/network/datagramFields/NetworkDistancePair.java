package com.sane.router.network.datagramFields;

import com.sane.router.network.Constants;
import com.sane.router.support.Utilities;

/**
 * Header Field Class containing an LL3P network and distance pair
 *
 * @author Joshua Johnston
 */
public class NetworkDistancePair implements HeaderField
{
    //Fields
    private Integer network; //identifier of a route's available network
    private Integer distance; //distance to the remote network

    //Methods
    public NetworkDistancePair(String pair)
    {
        network = Integer.valueOf(pair.substring(0, 2*Constants.LL3P_ADDRESS_NETWORK_LENGTH));
        distance = Integer.valueOf(pair.substring(2*Constants.LL3P_ADDRESS_NETWORK_LENGTH));
    }

    //Private to-string methods to simplify interface methods
    private String networkToString()
    {
        return Utilities.padHexString(Integer.toHexString(network),Constants.LL3P_ADDRESS_NETWORK_LENGTH);
    }
    private String distanceToString()
    {
        return Utilities.padHexString(Integer.toHexString(distance),Constants.LL3P_DISTANCE_LENGTH);
    }

    //Interface Implementation
    @Override public String toTransmissionString()
    {
        return networkToString() +  distanceToString();
    }
    @Override public String toHexString()
    {
        return networkToString() + distanceToString();
    }
    @Override public String explainSelf()
    {
        return "Network: 0x"+networkToString()+"   Distance: 0x"+distanceToString();
    }
    @Override public String toASCIIString()
    {
        return Integer.toString(network,16).toUpperCase()
                + Integer.toString(distance,16).toUpperCase();
    }
}