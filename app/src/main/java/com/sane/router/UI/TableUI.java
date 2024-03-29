package com.sane.router.UI;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.sane.router.R;
import com.sane.router.network.Constants;
import com.sane.router.network.daemons.ARPDaemon;
import com.sane.router.network.daemons.LL1Daemon;
import com.sane.router.network.daemons.LRPDaemon;
import com.sane.router.support.BootLoader;
import com.sane.router.support.ParentActivity;

import java.util.Observable;
import java.util.Observer;

/**
 * The class of a Table UI object to create and manage four singleTableUI objects
 *
 * @author Joshua Johnston
 */
public class TableUI implements Runnable, Observer
{
    //Fields
    private AdjacencyTableUI adjacencyTableUI;
    private SingleTableUI arpTableUI;
    private SingleTableUI routingTableUI;
    private SingleTableUI forwardingTableUI;

    //Methods
    /**
     * Empty constructor, UI objects are built after the router is booted
     */
    public void TableUI(){}

    //Interface Implementation
    /**
     * Runs once per second to keep tables current
     */
    @Override public void run()
    {
        //adjacencyTableUI.updateView();
        arpTableUI.updateView();
        routingTableUI.updateView();
        forwardingTableUI.updateView();
    }
    /**
     * Suspends operation until the update call is received from the bootLoader
     *
     * @param observable - the observed object
     * @param o - an additional object optionally passed with the call to update
     */
    @Override public void update(Observable observable, Object o)
    {
        Log.d(Constants.LOG_TAG," \n \n Booting TableUI... \n \n");
        if (observable instanceof BootLoader)
        {
            Activity activity = ParentActivity.getParentActivity();//get instance of parent
            Context context = activity.getBaseContext(); //get context from parent activity

            adjacencyTableUI = new AdjacencyTableUI(activity, //instantiate the Adjacency Table
                    R.id.adjacencyTable,
                    LL1Daemon.getInstance().getAdjacencyTable(),
                    LL1Daemon.getInstance());

            arpTableUI = new SingleTableUI(activity, //instantiate the ARP Table
                    R.id.ARPTable,
                    ARPDaemon.getInstance().getARPTable());

            routingTableUI = new SingleTableUI(activity, //instantiate the Routing Table
                    R.id.routingTable,
                    LRPDaemon.getInstance().getRoutingTable());

            forwardingTableUI = new SingleTableUI(activity, //instantiate the Forwarding Table
                    R.id.forwardingTable,
                    LRPDaemon.getInstance().getForwardingTable());
        }
    }
}