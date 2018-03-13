package com.sane.router.network.table;

import android.util.Log;

import com.sane.router.network.Constants;
import com.sane.router.network.tableRecords.Record;
import com.sane.router.network.tableRecords.RoutingRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class RoutingTable extends TimedTable implements Observable
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
        Log.i(Constants.LOG_TAG, "\n\nAdding RoutingTable Record, checking for key...\n\n");
        if (((TimedTable)table).touch(newRoute.getKey()))
            Log.i(Constants.LOG_TAG, "Matching key found, record updated: "
                    + ((RoutingRecord)((TimedTable)table).getItem(newRoute.getKey())).toString() + "\n\n");
        else
        {
            Log.i(Constants.LOG_TAG, "Matching key not found, creating record...\n\n");

            ((Table)table).addItem(newRoute); //add new record to ARP table
            Log.i(Constants.LOG_TAG, "Record added to table: "
                    + newRoute.toString() + "\n\n\n");
        }
        setChanged();//notify observers of change to the adjacency list
        notifyObservers();
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
        synchronized (table) {
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
        synchronized (table) {
            List<RoutingRecord> routesToRemove = new ArrayList<RoutingRecord>();
            Log.i(Constants.LOG_TAG, "\n \nGetting routes excluding: " + ll3p + "...\n \n");

            for (Record record : table)
                if (!(((int) ll3p) == ((int) (((RoutingRecord) record).getNextHop()))))
                    ((Table) routesToRemove).addItem((RoutingRecord) record);

            for (Record route : routesToRemove)
                ((Table) table).removeItem(route.getKey());
        }
    }
}