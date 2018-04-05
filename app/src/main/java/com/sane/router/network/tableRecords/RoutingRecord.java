package com.sane.router.network.tableRecords;

import com.sane.router.network.Constants;
import com.sane.router.network.datagramFields.NetworkDistancePair;
import com.sane.router.support.Utilities;
import com.sane.router.support.factories.HeaderFieldFactory;

/**
 * An extension of the Record Class with the addition of the necessary components
 * for linking a NetworkDistancePair and next hop for use in routing protocols
 *
 * @author Joshua Johnston
 */
public class RoutingRecord extends Record
{
    //Fields
    private NetworkDistancePair networkDistancePair;//a remote network and distance to it
    private Integer nextHop;//the source of the route, and the next hop on the route to it
    private Integer key; //key used to locate records (= network*256*256 + nextHop)

    //Methods
    public RoutingRecord(int netNum, int dist, int nxtHop)//Constructor
    {
        super();

        HeaderFieldFactory factory = HeaderFieldFactory.getInstance();

        networkDistancePair = factory.getItem
                (Constants.NETWORK_DISTANCE_PAIR,
                Utilities.padHexString
                (Integer.toHexString(netNum),
                2*Constants.LL3P_ADDRESS_NETWORK_LENGTH)
                +Utilities.padHexString
                (Integer.toHexString(dist),
                2*Constants.LL3P_DISTANCE_LENGTH));

        nextHop = nxtHop;

        key = netNum*256*256 + nextHop;
    }
    public Integer getNetworkNumber(){return networkDistancePair.getNetwork();}
    public Integer getDistance(){return networkDistancePair.getDistance();}
    public Integer getNextHop(){return nextHop;}
    @Override public String toString()
    {
        return "Network: " + Integer.toHexString(networkDistancePair.getNetwork())
                + " |  Distance: " + Integer.toHexString(networkDistancePair.getDistance())
                + " |  Next hop: " + Integer.toHexString(nextHop)
                + " |  Age: " + getAgeInSeconds();
    }

    //Interface Implementation
    public int getKey(){return key;}
}
