package com.sane.router.network.table;

import android.util.Log;

import com.sane.router.network.Constants;
import com.sane.router.network.tableRecords.Record;
import com.sane.router.network.tableRecords.RoutingRecord;
import com.sane.router.support.LabException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RoutingTable extends TimedTable
{
    //Methods
    public RoutingTable() { super(); } //Constructor
    /**
     * Adds a Routing Record to the table, if the record already exists: touch() it
     *
     * @param newRoute - new route to be added (or touched)
     */
    public void addNewRoute(RoutingRecord newRoute)
    {
        Log.i(Constants.LOG_TAG, " \n \nAdding RoutingTable Record, checking for key... \n \n");

        Iterator iterator = getTableAsList().iterator();
        RoutingRecord newRouteRecord = newRoute;

        while(iterator.hasNext())
        {
            RoutingRecord routingRecord = (RoutingRecord)iterator.next();
            if (routingRecord.getNetworkNumber() == newRoute.getNetworkNumber())
            {
                if (routingRecord.getKey() == newRoute.getKey())
                {
                    if (routingRecord.getDistance() == newRoute.getDistance())
                    {
                        touch(routingRecord.getKey());
                    }
                    else
                    {
                        getTableAsList().set(getTableAsList().indexOf(routingRecord),newRoute);
                    }
                    return;
                }
            }
        }
        addItem(newRoute);
        Log.i(Constants.LOG_TAG, "Record added to table: "
                + newRoute.toString() + " \n \n");
    }
    /**
     * Removes a Routing Record from the Routing Table
     *
     * @param recordToRemove - the Routing Record to remove
     */
    public void removeItem(Record recordToRemove)
    {
        removeItem(recordToRemove.getKey());
    }
    /**
     * Returns the LL3P address of the next hop given a network number
     *
     * @param network - the network to retrieve the next hop of
     */
    public Integer getNextHop(Integer network)
    {
        Log.i(Constants.LOG_TAG, " \n \nGetting next hop, checking for key... \n \n");
        for (Record record : table)
        {
            if (network == ((RoutingRecord)record).getNetworkNumber())
            {
                Log.i(Constants.LOG_TAG, "Matching network found,"
                        + " returning next hop of record: "
                        + record.toString() + "\n\n");
                return ((RoutingRecord) record).getNextHop();
            }
        }
        return -1;
    }
    /**
     * Returns a list of all Table Records EXCEPT those from the passed LL3P address
     *
     * @param ll3p - the address not to return records from
     * @return List - the returned list of RoutingRecords
     */
    public List<RoutingRecord> getRoutesExcluding(int ll3p)//
    {
        List<RoutingRecord> routes = new ArrayList<RoutingRecord>();
        Log.i(Constants.LOG_TAG, "\n \nGetting routes excluding: " + ll3p + "...\n \n");

        for (Record record : table)
        {
            Log.i(Constants.LOG_TAG, " \n \n compared LL3P: " + Integer.toHexString(ll3p));
            Log.i(Constants.LOG_TAG, " \n Next hop: " + Integer.toHexString(((RoutingRecord) record).getNextHop()));
            if (ll3p != ((RoutingRecord) record).getNextHop())
                routes.add((RoutingRecord) record);
        }
            return routes;
    }

    /**
     * Removes all routes through a particular node after the node's death
     *
     * @param ll3p - the LL3P address of the dead none
     */
    public void removeRoutesFrom(Integer ll3p)
    {
        List<RoutingRecord> routesToRemove = new ArrayList<RoutingRecord>();
        Log.i(Constants.LOG_TAG, " \n \nRemoving routes from LL3P Address: " + ll3p + "... \n \n");

        for (Record record : table)
            if (((int) ll3p) == ((int) (((RoutingRecord) record).getNetworkNumber())))
                routesToRemove.add((RoutingRecord) record);

        for (RoutingRecord route : routesToRemove)
            removeItem(route);
    }

    /**
     * Gets a best route for each unique Network number in table
     *
     * @return bestRoutes - the set of best routes (least distance)
     */
    public List<RoutingRecord> getBestRoutes()
    {
        Log.i(Constants.LOG_TAG, "\n \nGetting best routes... \n \n");
        List<RoutingRecord> bestRoutes = new ArrayList<RoutingRecord>();
        List<Integer> networks = getAllNetworks();

        for (Integer network : networks)
            bestRoutes.add(getBestRoute(network));

        updateDisplay();

        return bestRoutes;
    }

    /**
     * Gets all network numbers from the table, ignoring duplicates
     *
     * @return networks - the list of network numbers
     */
    private List<Integer> getAllNetworks()
    {
        Log.i(Constants.LOG_TAG, "\n \nGetting all networks... \n \n");

        List<Integer> networks = new ArrayList<Integer>();
        for (Record record : table)
            if (!networks.contains(((RoutingRecord) record).getNetworkNumber()))
                networks.add(((RoutingRecord) record).getNetworkNumber());

        return networks;
    }
    /**
     * Gets the best route to take to a given network
     *
     * @param network - The network to find the best route to
     * @return bestRoute - A record holding the best route to the given network
     */
    public RoutingRecord getBestRoute(Integer network)
    {
        int bestRouteDistance = 999;
        RoutingRecord bestRoute = new RoutingRecord(0,0,0);

        for (Record record : table)
            if (((RoutingRecord) record).getNetworkNumber() == network )
                if (((RoutingRecord) record).getDistance() < bestRouteDistance)
                {
                    bestRoute = (RoutingRecord) record;
                    bestRouteDistance = ((RoutingRecord) record).getDistance();
                }

        if (bestRouteDistance == 999)
            Log.i(Constants.LOG_TAG, "Failed to get best route...");
        return bestRoute;
    }

    /**
     * Adds each Route from a set to the table if that Route is newer than any
     * existing route of the same key
     *
     * @param newRoutes - The List of new Routes with which to update table
     */
    public void addRoutes(List<RoutingRecord> newRoutes)
    {
        for(RoutingRecord route : newRoutes)//Iterate New Routes
        {
            addNewRoute(route);
        }
    }
    /**
     * Adds each new Route if none exists, replacing an old record if it exists,
     * used by Forwarding Table
     *
     * @param newRoutes - The new Routing Records to be added
     */
    public void addOrReplaceRoutes(List<RoutingRecord> newRoutes)
    {
        for(RoutingRecord newRoute : newRoutes)//Iterate New Routes
            addOrReplace(newRoute);
    }
    /**
     * Adds a new Route if none exists, replacing an old record if it exists,
     * used by Forwarding Table
     *
     * @param newRoute - The new Routing Record to be added
     */
    public void addOrReplace(RoutingRecord newRoute)
    {
        for( Record route : table )//Iterate Old Routes
            if (newRoute.getNetworkNumber() == ((RoutingRecord)route).getNetworkNumber())//Check if already have route from network
                if (newRoute.getNextHop() == ((RoutingRecord)route).getNetworkNumber())
                    removeItem(route.getKey());
    }
}