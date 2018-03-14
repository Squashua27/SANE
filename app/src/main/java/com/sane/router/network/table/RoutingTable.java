package com.sane.router.network.table;

import android.util.Log;

import com.sane.router.network.Constants;
import com.sane.router.network.tableRecords.Record;
import com.sane.router.network.tableRecords.RoutingRecord;
import com.sane.router.support.LabException;

import java.util.ArrayList;
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
        synchronized (table)
        {
            Log.i(Constants.LOG_TAG, " \n \nAdding RoutingTable Record, checking for key... \n \n");
            //if (((TimedTable) table.).touch(newRoute.getKey()))
            //    Log.i(Constants.LOG_TAG, "Matching key found: "
            //            + ((((RoutingTable) table)).getItem(newRoute.getKey())).getKey() + " \n \n");
            //else
                {
                Log.i(Constants.LOG_TAG, "Matching key not found, creating record... \n \n");
                table.add(newRoute); //add new record to ARP table
                Log.i(Constants.LOG_TAG, "Record added to table: "
                        + newRoute.toString() + " \n \n");
                }
            setChanged();//notify observers of change to the adjacency list
            notifyObservers();
        }
    }
    /**
     * Removes a Routing Record from the Routing Table
     *
     * @param recordToRemove - the Routing Record to remove
     */
    public void removeItem(Record recordToRemove)
    {
//        Log.i(Constants.LOG_TAG, "\n\nRemoving RoutingTable Record, checking for key...\n\n");
//        if (((TimedTable) table).touch(recordToRemove.getKey()))
//        {
//            Log.i(Constants.LOG_TAG, "Matching key found, deleting record: "
//                    + ((RoutingRecord) ((TimedTable) table).getItem(recordToRemove.getKey())).toString() + "\n\n");
//
//            ((TimedTable) table).removeItem(recordToRemove.getKey());
//            Log.i(Constants.LOG_TAG, " \n ...Record deleted.");
//        }
        removeItem(recordToRemove.getKey());
    }
    /**
     * Returns the LL3P address of the next hop given a network number
     *
     * @param network - the network to retrieve the next hop of
     */
    public Integer getNextHop(Integer network)
    {
        Log.i(Constants.LOG_TAG, "\n\nGetting next hop, checking for key...\n\n");

        for (Record record : table)
        {
            if (network.equals(((RoutingRecord)record).getNetworkNumber()))
            {
                Log.i(Constants.LOG_TAG, "Matching network found," +
                        " returning next hop of record: "
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
    public List<RoutingRecord> getRoutesExcluding(Integer ll3p)//
    {
        synchronized (table)
        {
            List<RoutingRecord> routes = new ArrayList<RoutingRecord>();
            Log.i(Constants.LOG_TAG, "\n \nGetting routes excluding: " + ll3p + "...\n \n");

            for (Record record : table)
                if (ll3p.equals(((RoutingRecord) record).getNetworkNumber()))
                    routes.add((RoutingRecord) record);
            return routes;
        }
    }

    /**
     * Removes all routes through a particular node after the node's death
     *
     * @param ll3p - the LL3P address of the dead none
     */
    public void removeRoutesFrom(Integer ll3p)
    {
        synchronized (table)
        {
            List<RoutingRecord> routesToRemove = new ArrayList<RoutingRecord>();
            Log.i(Constants.LOG_TAG, "\n \nGetting routes excluding: " + ll3p + "...\n \n");

            for (Record record : table)
                if (!(((int) ll3p) == ((int) (((RoutingRecord) record).getNextHop()))))
                    ((Table) routesToRemove).addItem((RoutingRecord) record);

            for (Record route : routesToRemove)
                ((Table) table).removeItem(route.getKey());
        }
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

        return bestRoutes;
    }

    /**
     * Gets all network numbers from the table, ignoring duplicates
     *
     * @return networks - the list of network numbers
     */
    private List<Integer> getAllNetworks()
    {
        List<Integer> networks = new ArrayList<Integer>();
        synchronized (table)
        {
            for (Record record : table)
                if (!networks.contains(((RoutingRecord) record).getNetworkNumber()))
                    networks.add(((RoutingRecord) record).getNetworkNumber());
        }
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

        synchronized (table)
        {
            for (Record record : table)
                if (((RoutingRecord) record).getNetworkNumber().equals(network))
                    if (((RoutingRecord) record).getDistance() < bestRouteDistance)
                    {
                        bestRoute = (RoutingRecord) record;
                        bestRouteDistance = ((RoutingRecord) record).getDistance();
                    }
        }
        if (bestRouteDistance == 999)
            new LabException("Failed to perform RoutingTable.getBestRoute(network)");
        return bestRoute;
    }

    /**
     * Adds each Route from a set to the table if that Route is shorter than any
     * existing route of the same key
     *
     * @param newRoutes - The List of new Routes with which to update table
     */
    public void addRoutes(List<RoutingRecord> newRoutes)
    {
        boolean duplicate;
        boolean betterRoute;
        for(RoutingRecord route : newRoutes)//Iterate New Routes
        {
            duplicate = false;
            betterRoute = false;

            synchronized (table)
            {
                for( Record record : table )//Iterate Old Routes
                    if (route.getKey() == record.getKey())//Check if new route already known
                    {
                        duplicate = true;
                        if (route.getDistance() < ((RoutingRecord) record).getDistance())//check if new route is better
                            betterRoute = true;
                    }

                if (duplicate)
                {
                    if (betterRoute)// If a route was already known but the new one is shorter
                    {
                        removeItem(route.getKey());
                        addNewRoute(route);
                    }
                    else// if an old route is shorter than the new one
                        touch(route.getKey());
                }
                else// if the route is new
                    addNewRoute(route);
            }
        }
    }

    /**
     * Adds a new Route if none exists, replacing an old record if the new one is shorter
     *
     * @param newRoute - The new Routing Record to be added
     */
    public void addOrReplace(RoutingRecord newRoute)
    {
        Boolean duplicate = false;
        Boolean betterRoute = false;
        synchronized (table)
        {
            for( Record record : table )//Iterate Old Routes
                if (newRoute.getKey() == record.getKey())//Check if new route already known
                {
                    duplicate = true;
                    if (newRoute.getDistance() < ((RoutingRecord) record).getDistance())//check if new route is better
                        betterRoute = true;
                }

            if (duplicate)
            {
                if (betterRoute)// If a route was already known but the new one is shorter
                {
                    removeItem(newRoute.getKey());
                    addNewRoute(newRoute);
                }
                else// if an old route is shorter than the new one
                    touch(newRoute.getKey());
            }
            else// if the route is new
                addNewRoute(newRoute);
        }
    }

    /**
     * Expires routes, removing the ones that are past their expiration date
     *
     * @param routeTTL - The Time To Live of Routing Records
     */
    public List<Record> expireRoutes(int routeTTL)
    {
        return ((TimedTable)table).expireRecords(routeTTL);
    }
}