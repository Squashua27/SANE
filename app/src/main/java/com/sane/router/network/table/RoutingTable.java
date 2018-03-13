package com.sane.router.network.table;

import android.util.Log;

import com.sane.router.network.Constants;
import com.sane.router.network.tableRecords.ARPRecord;
import com.sane.router.network.tableRecords.Record;
import com.sane.router.network.tableRecords.RoutingRecord;
import com.sane.router.network.tableRecords.TableRecord;
import com.sane.router.support.Utilities;

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

            table.add(newRoute); //add new record to ARP table
            Log.i(Constants.LOG_TAG, "Record added to table: "
                    + newRoute.toString() + "\n\n\n");
        }
        setChanged();//notify observers of change to the adjacency list
        notifyObservers();
    }

    public void removeItem(Record toRemove)
    {
        Log.i(Constants.LOG_TAG, "\n\nRemoving RoutingTable Record, checking for key...\n\n");
        if (((TimedTable)table).touch(toRemove.getKey()))
            Log.i(Constants.LOG_TAG, "Matching key found, deleting record: "
                    + ((RoutingRecord)((TimedTable)table).getItem(toRemove.getKey())).toString() + "\n\n");
    }
}